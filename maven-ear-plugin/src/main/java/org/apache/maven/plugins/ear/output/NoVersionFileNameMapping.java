package org.apache.maven.plugins.ear.output;

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

import org.apache.maven.artifact.Artifact;

/**
 * A simplified version of the standard file name mapping which does not retain the version in the generated file name.
 * 
 * @author <a href="snicoll@apache.org">Stephane Nicoll</a>
 */
public class NoVersionFileNameMapping
    extends AbstractFileNameMapping
{

    /**
     * {@inheritDoc}
     */
    public String mapFileName( Artifact a )
    {
        return generateFileName( a, false );
    }

}
