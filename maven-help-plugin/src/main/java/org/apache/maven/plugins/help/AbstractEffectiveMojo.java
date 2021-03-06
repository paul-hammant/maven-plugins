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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.WriterFactory;
import org.codehaus.plexus.util.xml.XMLWriter;
import org.codehaus.plexus.util.xml.XmlWriterUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Base class with common utilities to write effective Pom/settings.
 *
 * @author <a href="mailto:vincent.siveton@gmail.com">Vincent Siveton</a>
 * @version $Id: AbstractEffectiveMojo.java 1742353 2016-05-05 03:22:53Z schulte $
 * @since 2.1
 */
public abstract class AbstractEffectiveMojo
    extends AbstractHelpMojo
{
    /** The POM XSD URL */
    private static final String POM_XSD_URL = "http://maven.apache.org/maven-v4_0_0.xsd";

    /** The Settings XSD URL */
    private static final String SETTINGS_XSD_URL = "http://maven.apache.org/xsd/settings-1.0.0.xsd";

    /**
     * Utility method to write an XML content in a given file.
     *
     * @param output is the wanted output file.
     * @param content contains the XML content to be written to the file.
     * @param encoding is the wanted encoding to use when writing file.
     * @throws IOException if any
     * @see AbstractHelpMojo#writeFile(File, String) if encoding is null.
     */
    protected static void writeXmlFile( File output, String content, String encoding )
        throws IOException
    {
        if ( output == null )
        {
            return;
        }

        if ( StringUtils.isEmpty( encoding ) )
        {
            writeFile( output, content );
            return;
        }

        Writer out = null;
        try
        {
            output.getParentFile().mkdirs();

            out = WriterFactory.newXmlWriter( output );

            out.write( content );

            out.close();
            out = null;
        }
        finally
        {
            IOUtil.close( out );
        }
    }

    /**
     * Write comments in the Effective POM/settings header.
     *
     * @param writer not null
     */
    protected static void writeHeader( XMLWriter writer )
    {
        XmlWriterUtil.writeCommentLineBreak( writer );
        XmlWriterUtil.writeComment( writer, " " );
        // Use ISO 8601 format for date and time
        String formattedDateTime = DateFormatUtils.ISO_DATETIME_FORMAT.format( System.currentTimeMillis() );
        XmlWriterUtil.writeComment( writer, "Generated by Maven Help Plugin on " + formattedDateTime );
        XmlWriterUtil.writeComment( writer, "See: http://maven.apache.org/plugins/maven-help-plugin/" );
        XmlWriterUtil.writeComment( writer, " " );
        XmlWriterUtil.writeCommentLineBreak( writer );

        XmlWriterUtil.writeLineBreak( writer );
    }

    /**
     * Write comments in a normalize way.
     *
     * @param writer not null
     * @param comment not null
     */
    protected static void writeComment( XMLWriter writer, String comment )
    {
        XmlWriterUtil.writeCommentLineBreak( writer );
        XmlWriterUtil.writeComment( writer, " " );
        XmlWriterUtil.writeComment( writer, comment );
        XmlWriterUtil.writeComment( writer, " " );
        XmlWriterUtil.writeCommentLineBreak( writer );

        XmlWriterUtil.writeLineBreak( writer );
    }

    /**
     * Add a Pom/Settings namespaces to the effective XML content.
     *
     * @param effectiveXml not null the effective POM or Settings
     * @param isPom if <code>true</code> add the Pom xsd url, otherwise add the settings xsd url.
     * @return the content of the root element, i.e. &lt;project/&gt; or &lt;settings/&gt; with the Maven namespace or
     *         the original <code>effective</code> if an error occurred.
     * @see #POM_XSD_URL
     * @see #SETTINGS_XSD_URL
     */
    protected static String addMavenNamespace( String effectiveXml, boolean isPom )
    {
        SAXBuilder builder = new SAXBuilder();

        try
        {
            Document document = builder.build( new StringReader( effectiveXml ) );
            Element rootElement = document.getRootElement();

            // added namespaces
            Namespace pomNamespace = Namespace.getNamespace( "", "http://maven.apache.org/POM/4.0.0" );
            rootElement.setNamespace( pomNamespace );

            Namespace xsiNamespace = Namespace.getNamespace( "xsi", "http://www.w3.org/2001/XMLSchema-instance" );
            rootElement.addNamespaceDeclaration( xsiNamespace );
            if ( rootElement.getAttribute( "schemaLocation", xsiNamespace ) == null )
            {
                rootElement.setAttribute( "schemaLocation", "http://maven.apache.org/POM/4.0.0 "
                    + ( isPom ? POM_XSD_URL : SETTINGS_XSD_URL ), xsiNamespace );
            }

            ElementFilter elementFilter = new ElementFilter( Namespace.getNamespace( "" ) );
            for ( Iterator<?> i = rootElement.getDescendants( elementFilter ); i.hasNext(); )
            {
                Element e = (Element) i.next();
                e.setNamespace( pomNamespace );
            }

            StringWriter w = new StringWriter();
            Format format = Format.getPrettyFormat();
            XMLOutputter out = new XMLOutputter( format );
            out.output( document.getRootElement(), w );

            return w.toString();
        }
        catch ( JDOMException e )
        {
            return effectiveXml;
        }
        catch ( IOException e )
        {
            return effectiveXml;
        }
    }

    /**
     * Properties which provides a sorted keySet().
     */
    protected static class SortedProperties
        extends Properties
    {
        /** serialVersionUID */
        static final long serialVersionUID = -8985316072702233744L;

        /** {@inheritDoc} */
        @SuppressWarnings( { "rawtypes", "unchecked" } )
        public Set<Object> keySet()
        {
            Set<Object> keynames = super.keySet();
            List list = new ArrayList( keynames );
            Collections.sort( list );

            return new LinkedHashSet<Object>( list );
        }
    }
}
