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
package jakarta.faces.application;

import jakarta.faces.FacesWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.flow.Flow;

import java.util.Map;
import java.util.Set;

/**
 *
 * @since 2.2
 */
public abstract class NavigationHandlerWrapper extends NavigationHandler
    implements FacesWrapper<NavigationHandler>
{

    private NavigationHandler delegate;

    @Deprecated
    public NavigationHandlerWrapper()
    {
    }

    public NavigationHandlerWrapper(NavigationHandler delegate)
    {
        this.delegate = delegate;
    }
    
    @Override
    public void handleNavigation(FacesContext context, String fromAction, String outcome)
    {
        getWrapped().handleNavigation(context, fromAction, outcome);
    }
    
    @Override
    public void handleNavigation(FacesContext context,
                                 String fromAction,
                                 String outcome,
                                 String toFlowDocumentId)
    {
        getWrapped().handleNavigation(context, fromAction, outcome, toFlowDocumentId);
    }


    @Override
    public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome)
    {
        return getWrapped().getNavigationCase(context, fromAction, outcome);
    }

    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases()
    {
        return getWrapped().getNavigationCases();
    }

    @Override
    public void performNavigation(String outcome)
    {
        getWrapped().performNavigation(outcome);
    }

    @Override
    public void inspectFlow(FacesContext context, Flow flow)
    {
        getWrapped().inspectFlow(context, flow);
    }

    @Override
    public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome,
                                            String toFlowDocumentId)
    {
        return getWrapped().getNavigationCase(context, fromAction, outcome, toFlowDocumentId);
    }

    @Override
    public NavigationHandler getWrapped()
    {
        return delegate;
    }
}
