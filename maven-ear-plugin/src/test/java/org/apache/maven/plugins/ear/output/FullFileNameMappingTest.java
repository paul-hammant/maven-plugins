package org.apache.maven.plugins.ear.output;

import static org.junit.Assert.assertEquals;

import org.apache.maven.plugins.ear.output.FullFileNameMapping;
import org.junit.Test;

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

/**
 * @author <a href="snicoll@apache.org">Stephane Nicoll</a>
 */
public class FullFileNameMappingTest
    extends AbstractFileNameMappingTestBase
{

    private final FullFileNameMapping instance = new FullFileNameMapping();

    @Test
    public void testSimpleArtifact()
    {
        assertEquals( "org-apache-foo-1.0-SNAPSHOT.jar",
                      instance.mapFileName( createArtifactWithGroupId( "org.apache", "foo", "1.0-SNAPSHOT", "jar" ) ) );
    }

    @Test
    public void testArtifactWithClassifier()
    {
        assertEquals( "org-apache-foo-1.0-SNAPSHOT-sources.jar",
                      instance.mapFileName( createArtifactWithGroupId( "org.apache", "foo", "1.0-SNAPSHOT", "jar",
                                                                       "sources" ) ) );
    }
}
