package org.apache.maven.plugin.ant;

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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Clean all Ant build files.
 *
 * @author <a href="mailto:vincent.siveton@gmail.com">Vincent Siveton</a>
 * @version $Id: AntCleanMojo.java 1640228 2014-11-17 21:20:42Z hboutemy $
 */
@Mojo( name = "clean" )
public class AntCleanMojo
    extends AbstractMojo
{
    // ----------------------------------------------------------------------
    // Mojo components
    // ----------------------------------------------------------------------

    // ----------------------------------------------------------------------
    // Mojo parameters
    // ----------------------------------------------------------------------

    /**
     * The working project.
     */
    @Parameter( defaultValue = "${project}", readonly = true, required = true )
    private MavenProject project;

    /**
     * Forcing the deletion of the custom <code>build.xml</code>.
     *
     * @since 2.2
     */
    @Parameter( property = "deleteCustomFiles", defaultValue = "false" )
    private boolean deleteCustomFiles;

    /**
     * {@inheritDoc}
     */
    public void execute()
        throws MojoExecutionException
    {
        File buildXml = new File( project.getBasedir(), AntBuildWriter.DEFAULT_BUILD_FILENAME );
        if ( buildXml.exists() )
        {
            if ( deleteCustomFiles )
            {
                if ( !buildXml.delete() )
                {
                    throw new MojoExecutionException( "Cannot delete " + buildXml.getAbsolutePath() );
                }
            }
            else if ( getLog().isInfoEnabled() )
            {
                getLog().info( "Not deleting custom " + buildXml.getName()
                                   + ", use -DdeleteCustomFiles=true to force its deletion" );
            }
        }

        File mavenBuildXml = new File( project.getBasedir(), AntBuildWriter.DEFAULT_MAVEN_BUILD_FILENAME );
        if ( mavenBuildXml.exists() && !mavenBuildXml.delete() )
        {
            throw new MojoExecutionException( "Cannot delete " + mavenBuildXml.getAbsolutePath() );
        }

        File mavenBuildProperties = new File( project.getBasedir(), AntBuildWriter.DEFAULT_MAVEN_PROPERTIES_FILENAME );
        if ( mavenBuildProperties.exists() && !mavenBuildProperties.delete() )
        {
            throw new MojoExecutionException( "Cannot delete " + mavenBuildProperties.getAbsolutePath() );
        }

        getLog().info( "Deleted Ant build files for project " + project.getArtifactId() + " in "
                           + project.getBasedir().getAbsolutePath() );
    }

}
