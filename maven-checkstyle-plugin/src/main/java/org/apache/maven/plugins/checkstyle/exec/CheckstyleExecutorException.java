package org.apache.maven.plugins.checkstyle.exec;

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

/**
 * @author Olivier Lamy
 * @since 2.5
 * @version $Id: CheckstyleExecutorException.java 1797601 2017-06-04 19:01:48Z gboue $
 */
public class CheckstyleExecutorException
    extends Exception
{

    private static final long serialVersionUID = 3239899539232498912L;

    /**
     * 
     */
    public CheckstyleExecutorException()
    {
        // nothing
    }

    /**
     * @param message Message of the exception.
     */
    public CheckstyleExecutorException( String message )
    {
        super( message );
    }

    /**
     * @param cause Cause of the exception.
     */
    public CheckstyleExecutorException( Throwable cause )
    {
        super( cause );
    }

    /**
     * @param message Message of the exception.
     * @param cause Cause of the exception.
     */
    public CheckstyleExecutorException( String message, Throwable cause )
    {
        super( message, cause );
    }

}
