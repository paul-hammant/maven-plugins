package org.apache.maven.plugins.help;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecution.Source;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.PrettyPrintXMLWriter;
import org.codehaus.plexus.util.xml.XMLWriter;
import org.codehaus.plexus.util.xml.XmlWriterUtil;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

/**
 * Displays the effective POM as an XML for this build, with the active profiles factored in, or a specified artifact.
 *
 * @version $Id: EffectivePomMojo.java 1769446 2016-11-12 23:16:20Z gboue $
 * @since 2.0
 */
@Mojo( name = "effective-pom", aggregator = true )
public class EffectivePomMojo
    extends AbstractEffectiveMojo
{
    // ----------------------------------------------------------------------
    // Mojo parameters
    // ----------------------------------------------------------------------

    /**
     * The Maven project.
     *
     * @since 2.0.2
     */
    @Parameter( defaultValue = "${project}", readonly = true, required = true )
    private MavenProject project;

    /**
     * The projects in the current build. The effective-POM for
     * each of these projects will written.
     */
    @Parameter( defaultValue = "${reactorProjects}", required = true, readonly = true )
    private List<MavenProject> projects;

    /**
     * This mojo execution, used to determine if it was launched from the lifecycle or the command-line.
     */
    @Parameter( defaultValue = "${mojo}", required = true, readonly = true )
    private MojoExecution mojoExecution;

    /**
     * The artifact for which to display the effective POM.
     * <br>
     * <b>Note</b>: Should respect the Maven format, i.e. <code>groupId:artifactId[:version]</code>. The
     * latest version of the artifact will be used when no version is specified.
     * 
     * @since 3.0.0
     */
    @Parameter( property = "artifact" )
    private String artifact;

    // ----------------------------------------------------------------------
    // Public methods
    // ----------------------------------------------------------------------

    /** {@inheritDoc} */
    public void execute()
        throws MojoExecutionException
    {
        if ( StringUtils.isNotEmpty( artifact ) )
        {
            project = getMavenProject( artifact );
        }

        StringWriter w = new StringWriter();
        XMLWriter writer =
            new PrettyPrintXMLWriter( w, StringUtils.repeat( " ", XmlWriterUtil.DEFAULT_INDENTATION_SIZE ),
                                      project.getModel().getModelEncoding(), null );

        writeHeader( writer );

        String effectivePom;
        if ( shouldWriteAllEffectivePOMsInReactor() )
        {
            // outer root element
            writer.startElement( "projects" );
            for ( MavenProject subProject : projects )
            {
                writeEffectivePom( subProject, writer );
            }
            writer.endElement();

            effectivePom = w.toString();
            effectivePom = prettyFormat( effectivePom );
        }
        else
        {
            writeEffectivePom( project, writer );

            effectivePom = w.toString();
        }

        if ( output != null )
        {
            try
            {
                writeXmlFile( output, effectivePom, project.getModel().getModelEncoding() );
            }
            catch ( IOException e )
            {
                throw new MojoExecutionException( "Cannot write effective-POM to output: " + output, e );
            }

            getLog().info( "Effective-POM written to: " + output );
        }
        else
        {
            StringBuilder message = new StringBuilder();

            message.append( LS );
            message.append( "Effective POMs, after inheritance, interpolation, and profiles are applied:" );
            message.append( LS ).append( LS );
            message.append( effectivePom );
            message.append( LS );

            getLog().info( message.toString() );
        }
    }

    /**
     * Determines if all effective POMs of all the projects in the reactor should be written. When this goal is started
     * on the command-line, it is always the case. However, when it is bound to a phase in the lifecycle, it is only the
     * case when the current project being built is the head project in the reactor.
     * 
     * @return <code>true</code> if all effective POMs should be written, <code>false</code> otherwise.
     */
    private boolean shouldWriteAllEffectivePOMsInReactor()
    {
        Source source = mojoExecution.getSource();
        // [MNG-5550] For Maven < 3.2.1, the source is null, instead of LIFECYCLE: only rely on comparisons with CLI
        return projects.size() > 1
            && ( source == Source.CLI || source != Source.CLI && projects.get( 0 ).equals( project ) );
    }

    // ----------------------------------------------------------------------
    // Private methods
    // ----------------------------------------------------------------------

    /**
     * Method for writing the effective pom informations of the current build.
     *
     * @param project the project of the current build, not null.
     * @param writer the XML writer , not null, not null.
     * @throws MojoExecutionException if any
     */
    private static void writeEffectivePom( MavenProject project, XMLWriter writer )
        throws MojoExecutionException
    {
        Model pom = project.getModel();
        cleanModel( pom );

        String effectivePom;

        StringWriter sWriter = new StringWriter();
        MavenXpp3Writer pomWriter = new MavenXpp3Writer();
        try
        {
            pomWriter.write( sWriter, pom );
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Cannot serialize POM to XML.", e );
        }

        effectivePom = addMavenNamespace( sWriter.toString(), true );

        writeComment( writer, "Effective POM for project \'" + project.getId() + "\'" );

        writer.writeMarkup( effectivePom );
    }

    /**
     * Apply some logic to clean the model before writing it.
     *
     * @param pom not null
     */
    private static void cleanModel( Model pom )
    {
        Properties properties = new SortedProperties();
        properties.putAll( pom.getProperties() );
        pom.setProperties( properties );
    }

    /**
     * @param effectivePom not null
     * @return pretty format of the xml  or the original <code>effectivePom</code> if an error occurred.
     */
    private static String prettyFormat( String effectivePom )
    {
        SAXBuilder builder = new SAXBuilder();

        try
        {
            Document effectiveDocument = builder.build( new StringReader( effectivePom ) );

            StringWriter w = new StringWriter();
            Format format = Format.getPrettyFormat();
            XMLOutputter out = new XMLOutputter( format );
            out.output( effectiveDocument, w );

            return w.toString();
        }
        catch ( JDOMException e )
        {
            return effectivePom;
        }
        catch ( IOException e )
        {
            return effectivePom;
        }
    }
}
