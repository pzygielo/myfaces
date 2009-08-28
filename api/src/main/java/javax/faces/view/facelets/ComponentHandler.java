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
package javax.faces.view.facelets;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;

/**
 * Implementation of the tag logic used in the JSF specification. This is your golden hammer for wiring UIComponents to
 * Facelets.
 * 
 * @author Jacob Hookom
 * @version $Id: ComponentHandler.java,v 1.19 2008/07/13 19:01:47 rlubke Exp $
 */
public class ComponentHandler extends DelegatingMetaTagHandler
{
    private ComponentConfig config;
    private TagHandlerDelegate helper;
    
    public ComponentHandler(ComponentConfig config)
    {
        super(config);
        
        this.config = config;
        
        // Spec seems to indicate that the helper is created here, as opposed to other Handler
        // instances, where it's presumably a new instance for every getter call.
        
        this.helper = delegateFactory.createComponentHandlerDelegate (this);
    }

    public ComponentConfig getComponentConfig()
    {
        return config;
    }

    public static final boolean isNew(UIComponent component)
    {
        // -= Leonardo Uribe =- It seems org.apache.myfaces.view.facelets.tag.jsf.ComponentSupport.isNew(UIComponent)
        // has been moved to this location.
        // Originally this method was called from all tags that generate any kind of listeners
        // (f:actionListener, f:phaseListener, f:setPropertyActionListener, f:valueChangeListener).
        // This method prevent add listener when a facelet is applied twice. But at this moment we don't have
        // a valid use case that reproduce this behavior, and tracking down the original code from facelets
        // cvs log, there is no information or bug related to.
        // TODO: Only partially done... how do we determine that the component is "new to the tree"?
        // pending update it when composite components are created. (Aparently it is not necessary)
        return component != null && component.getParent() == null;
    }

    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent)
    {
        // no-op.
    }

    public void onComponentPopulated(FaceletContext ctx, UIComponent c, UIComponent parent)
    {
        // no-op.
    }

    protected TagHandlerDelegate getTagHandlerDelegate()
    {
        return helper;
    }
}
