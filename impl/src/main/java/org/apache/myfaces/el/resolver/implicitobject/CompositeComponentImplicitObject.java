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
package org.apache.myfaces.el.resolver.implicitobject;

import jakarta.el.ELContext;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.apache.myfaces.view.facelets.el.CompositeComponentELUtils;

import java.util.List;

/**
 * Encapsulates information needed by the ImplicitObjectResolver
 * 
 * @author Leonardo Uribe (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class CompositeComponentImplicitObject extends ImplicitObject
{

    private static final String NAME = "cc";

    /** Creates a new instance of CompositeComponentImplicitObjectImplicitObject */
    public CompositeComponentImplicitObject()
    {
    }

    @Override
    public Object getValue(ELContext context)
    {
        FacesContext facesContext = facesContext(context);
        
        // Look for the attribute set by LocationValueExpression
        // or LocationMethodExpression on the FacesContext
        List<UIComponent> list = (List<UIComponent>) facesContext.getAttributes()
                .get(CompositeComponentELUtils.CURRENT_COMPOSITE_COMPONENT_KEY);
        
        UIComponent cc = null;
        
        if (list != null && !list.isEmpty())
        {
            cc = list.get(list.size()-1);
        }
        if (cc == null)
        {
            // take the composite component from the stack
            cc = UIComponent.getCurrentCompositeComponent(facesContext(context));
        }
        return cc;
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public Class<?> getType()
    {
        return null;
    }

}
