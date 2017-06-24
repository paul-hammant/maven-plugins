package org.apache.maven.plugins.changes.schema;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;

/**
 * @author Olivier Lamy
 * @since 28 juil. 2008
 * @version $Id: ChangesSchemaValidator.java 1784411 2017-02-25 22:36:31Z gboue $
 */
public interface ChangesSchemaValidator
{
    /**
     * @param file File to validate.
     * @param schemaVersion Schema version.
     * @param failOnValidationError if false SchemaValidatorException will not be throw
     * @return {@link XmlValidationHandler}
     * @throws SchemaValidatorException if validation failed.
     */
    XmlValidationHandler validateXmlWithSchema( File file, String schemaVersion, boolean failOnValidationError )
        throws SchemaValidatorException;

}
