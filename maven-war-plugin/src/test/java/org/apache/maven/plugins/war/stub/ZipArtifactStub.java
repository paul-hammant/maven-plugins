package org.apache.maven.plugins.war.stub;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;

import org.apache.maven.artifact.handler.ArtifactHandler;

/**
 * @author Olivier Lamy
 * @since 8 juin 07
 * @version $Id: ZipArtifactStub.java 1734135 2016-03-08 19:58:34Z khmarbaise $
 */
public class ZipArtifactStub
    extends AbstractArtifactStub
{
    private File zip;

    public ZipArtifactStub( String basedir, ArtifactHandler artifactHandler, File zipFile )
    {
        super( basedir );
        super.setArtifactHandler( artifactHandler );
        this.zip = zipFile;
    }

    
    public String getId()
    {
        return null;
    }

    public ArtifactHandler getArtifactHandler()
    {
        return super.getArtifactHandler();
    }

    public String getScope()
    {
        return super.getScope();
    }

    public String getVersion()
    {
        return "1.0";
    }

    public boolean isOptional()
    {
        return super.isOptional();
    }

    public File getFile()
    {
        return this.zip;
    }

    public String getType()
    {
        return "zip";
    }

   
    public String getArtifactId()
    {
        return "zipId";
    }

    public String getGroupId()
    {
        return "zipGroupId";
    }

}
