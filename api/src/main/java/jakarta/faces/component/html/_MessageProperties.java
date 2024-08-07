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
package jakarta.faces.component.html;

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFProperty;

interface _MessageProperties
{
    /**
     * CSS class to be used for messages with severity "ERROR".
     * 
     */
    @JSFProperty
    public abstract String getErrorClass();

    /**
     * CSS style to be used for messages with severity "ERROR".
     * 
     */
    @JSFProperty
    public abstract String getErrorStyle();

    /**
     * CSS class to be used for messages with severity "FATAL".
     * 
     */
    @JSFProperty
    public abstract String getFatalClass();

    /**
     * CSS style to be used for messages with severity "FATAL".
     * 
     */
    @JSFProperty
    public abstract String getFatalStyle();

    /**
     * CSS class to be used for messages with severity "INFO".
     * 
     */
    @JSFProperty
    public abstract String getInfoClass();

    /**
     * CSS style to be used for messages with severity "INFO".
     * 
     */
    @JSFProperty
    public abstract String getInfoStyle();

    /**
     * CSS class to be used for messages with severity "SUCCESS".
     *
     */
    @JSFProperty
    public abstract String getSuccessClass();

    /**
     * CSS style to be used for messages with severity "SUCCESS".
     *
     */
    @JSFProperty
    public abstract String getSuccessStyle();

    /**
     * If true, the message summary will be rendered as a tooltip (i.e. HTML title attribute).
     * 
     */
    @JSFProperty(defaultValue="false")
    public abstract boolean isTooltip();
    
    /**
     * CSS class to be used for messages with severity "WARN".
     * 
     */
    @JSFProperty
    public abstract String getWarnClass();

    /**
     * CSS style to be used for messages with severity "WARN".
     * 
     */
    @JSFProperty
    public abstract String getWarnStyle();

}
