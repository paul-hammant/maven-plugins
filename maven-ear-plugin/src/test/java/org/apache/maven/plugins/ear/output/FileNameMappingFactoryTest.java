package org.apache.maven.plugins.ear.output;

import org.apache.maven.plugins.ear.output.FileNameMapping;
import org.apache.maven.plugins.ear.output.FileNameMappingFactory;
import org.apache.maven.plugins.ear.output.FullFileNameMapping;
import org.apache.maven.plugins.ear.output.NoVersionFileNameMapping;
import org.apache.maven.plugins.ear.output.NoVersionForEjbFileNameMapping;
import org.apache.maven.plugins.ear.output.StandardFileNameMapping;

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

import junit.framework.TestCase;

/**
 * @author <a href="snicoll@apache.org">Stephane Nicoll</a>
 * @version $Id: FileNameMappingFactoryTest.java 1368659 2012-08-02 19:28:23Z snicoll $
 */
public class FileNameMappingFactoryTest
    extends TestCase
{

    public void testDefaultFileNameMapping()
    {
        final FileNameMapping actual = FileNameMappingFactory.getDefaultFileNameMapping();
        assertNotNull( actual );
        assertEquals( StandardFileNameMapping.class, actual.getClass() );
    }

    public void testGetFileNameMappingByName()
    {
        final FileNameMapping actual =
            FileNameMappingFactory.getFileNameMapping( FileNameMappingFactory.STANDARD_FILE_NAME_MAPPING );
        assertNotNull( actual );
        assertEquals( StandardFileNameMapping.class, actual.getClass() );
    }

    public void testGetFileNameMappingByName2()
    {
        final FileNameMapping actual =
            FileNameMappingFactory.getFileNameMapping( FileNameMappingFactory.FULL_FILE_NAME_MAPPING );
        assertNotNull( actual );
        assertEquals( FullFileNameMapping.class, actual.getClass() );
    }

    public void testGetFileNameMappingByName3()
    {
        final FileNameMapping actual =
            FileNameMappingFactory.getFileNameMapping( FileNameMappingFactory.NO_VERSION_FILE_NAME_MAPPING );
        assertNotNull( actual );
        assertEquals( NoVersionFileNameMapping.class, actual.getClass() );
    }

    public void testGetFileNameMappingByName4()
    {
        final FileNameMapping actual =
            FileNameMappingFactory.getFileNameMapping( FileNameMappingFactory.NO_VERSION_FOR_EJB_FILE_NAME_MAPPING );
        assertNotNull( actual );
        assertEquals( NoVersionForEjbFileNameMapping.class, actual.getClass() );
    }

    public void testGetFileNameMappingByClass()
    {
        final FileNameMapping actual =
            FileNameMappingFactory.getFileNameMapping( StandardFileNameMapping.class.getName() );
        assertNotNull( actual );
        assertEquals( StandardFileNameMapping.class, actual.getClass() );
    }

    public void testGetFileNameMappingByClass2()
    {
        final FileNameMapping actual = FileNameMappingFactory.getFileNameMapping( FullFileNameMapping.class.getName() );
        assertNotNull( actual );
        assertEquals( FullFileNameMapping.class, actual.getClass() );
    }

    public void testGetFileNameMappingByUnknownClass()
    {
        try
        {
            FileNameMappingFactory.getFileNameMapping( "com.foo.bar" );
            fail( "Should have failed" );
        }
        catch ( IllegalStateException e )
        {
            // OK
        }
    }
}
