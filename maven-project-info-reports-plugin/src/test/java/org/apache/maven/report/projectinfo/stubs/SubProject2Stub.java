package org.apache.maven.report.projectinfo.stubs;

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

import java.util.Collections;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;

/**
 * @author <a href="mailto:vincent.siveton@gmail.com">Vincent Siveton</a>
 * @version $Id: SubProject2Stub.java 1038048 2010-11-23 10:52:14Z vsiveton $
 */
public class SubProject2Stub
    extends ProjectInfoProjectStub
{
    @Override
    protected String getPOM()
    {
        return "subproject2/pom.xml";
    }

    @Override
    public List<Dependency> getDependencies()
    {
        Dependency d = new Dependency();
        d.setGroupId( "junit" );
        d.setArtifactId( "junit" );
        d.setVersion( "3.8.1" );
        d.setScope( Artifact.SCOPE_COMPILE );

        return Collections.singletonList( d );
    }
}
