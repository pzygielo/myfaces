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
package org.apache.myfaces.util.lang;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import javax.el.ELException;

public class MethodHandleUtils
{
    private static Method privateLookupIn;

    static
    {
        try
        {
            privateLookupIn = MethodHandles.class.getMethod("privateLookupIn", Class.class,
                    MethodHandles.Lookup.class);
        }
        catch (Exception e)
        {
        }
    }

    public static boolean isSupported()
    {
        return privateLookupIn != null;
    }
    
    public static class LambdaPropertyDescriptor
    {
        private Class<?> type;
        private Function getter;
        private BiConsumer setter;

        public Class<?> getType()
        {
            return type;
        }

        public Function getGetter()
        {
            return getter;
        }

        public BiConsumer getSetter()
        {
            return setter;
        }
    }

    public static HashMap<String, LambdaPropertyDescriptor> getLambdaPropertyDescriptors(Class<?> target)
    {
        try
        {            
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(target).getPropertyDescriptors();
            HashMap<String, LambdaPropertyDescriptor> properties = new HashMap<>(propertyDescriptors.length);

            for (PropertyDescriptor pd : Introspector.getBeanInfo(target).getPropertyDescriptors())
            {
                LambdaPropertyDescriptor lpd = new LambdaPropertyDescriptor();
                lpd.type = pd.getPropertyType();
                
                MethodHandles.Lookup lookup = (MethodHandles.Lookup) privateLookupIn.invoke(null, target,
                        MethodHandles.lookup());

                Method getter = pd.getReadMethod();
                if (getter != null)
                {
                    MethodHandle getterHandle = lookup.unreflect(getter);
                    CallSite getterCallSite = LambdaMetafactory.metafactory(lookup,
                            "apply",
                            MethodType.methodType(Function.class),
                            MethodType.methodType(Object.class, Object.class),
                            getterHandle,
                            getterHandle.type());
                    lpd.getter = (Function) getterCallSite.getTarget().invokeExact();
                }

                Method setter = pd.getWriteMethod();
                if (setter != null)
                {
                    MethodHandle setterHandle = lookup.unreflect(setter);
                    lpd.setter = createSetter(lookup, lpd, setterHandle);
                }
                
                properties.put(pd.getName(), lpd);
            }
            
            return properties;
        }
        catch (Throwable e)
        {
            throw new ELException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected static BiConsumer createSetter(MethodHandles.Lookup lookup, LambdaPropertyDescriptor propertyInfo,
            MethodHandle setterHandle)
            throws LambdaConversionException, Throwable
    {
        // special handling for primitives required, see https://dzone.com/articles/setters-method-handles-and-java-11
        if (propertyInfo.type.isPrimitive())
        {
            if (propertyInfo.type == double.class)
            {
                ObjDoubleConsumer consumer = (ObjDoubleConsumer) createSetterCallSite(
                        lookup, setterHandle, ObjDoubleConsumer.class, double.class).getTarget().invokeExact();
                return (a, b) -> consumer.accept(a, (double) b);
            }
            else if (propertyInfo.type == int.class)
            {
                ObjIntConsumer consumer = (ObjIntConsumer) createSetterCallSite(
                        lookup, setterHandle, ObjIntConsumer.class, int.class).getTarget().invokeExact();
                return (a, b) -> consumer.accept(a, (int) b);
            }
            else if (propertyInfo.type == long.class)
            {
                ObjLongConsumer consumer = (ObjLongConsumer) createSetterCallSite(
                        lookup, setterHandle, ObjLongConsumer.class, long.class).getTarget().invokeExact();
                return (a, b) -> consumer.accept(a, (long) b);
            }
            else if (propertyInfo.type == float.class)
            {
                ObjFloatConsumer consumer = (ObjFloatConsumer) createSetterCallSite(
                        lookup, setterHandle, ObjFloatConsumer.class, float.class).getTarget().invokeExact();
                return (a, b) -> consumer.accept(a, (float) b);
            }
            else if (propertyInfo.type == byte.class)
            {
                ObjByteConsumer consumer = (ObjByteConsumer) createSetterCallSite(
                        lookup, setterHandle, ObjByteConsumer.class, byte.class).getTarget().invokeExact();
                return (a, b) -> consumer.accept(a, (byte) b);
            }
            else if (propertyInfo.type == char.class)
            {
                ObjCharConsumer consumer = (ObjCharConsumer) createSetterCallSite(
                        lookup, setterHandle, ObjCharConsumer.class, char.class).getTarget().invokeExact();
                return (a, b) -> consumer.accept(a, (char) b);
            }
            else if (propertyInfo.type == short.class)
            {
                ObjShortConsumer consumer = (ObjShortConsumer) createSetterCallSite(
                        lookup, setterHandle, ObjShortConsumer.class, short.class).getTarget().invokeExact();
                return (a, b) -> consumer.accept(a, (short) b);
            }
            else if (propertyInfo.type == boolean.class)
            {
                ObjBooleanConsumer consumer = (ObjBooleanConsumer) createSetterCallSite(
                        lookup, setterHandle, ObjBooleanConsumer.class, boolean.class).getTarget().invokeExact();
                return (a, b) -> consumer.accept(a, (boolean) b);
            }
            else
            {
                throw new RuntimeException("Type is not supported yet: " + propertyInfo.type.getName());
            }
        }
        else
        {
            return (BiConsumer) createSetterCallSite(lookup, setterHandle, BiConsumer.class, Object.class).getTarget()
                    .invokeExact();
        }
    }

    protected static CallSite createSetterCallSite(MethodHandles.Lookup lookup, MethodHandle setter,
            Class<?> interfaceType, Class<?> valueType)
            throws LambdaConversionException
    {
        return LambdaMetafactory.metafactory(lookup,
                "accept",
                MethodType.methodType(interfaceType),
                MethodType.methodType(void.class, Object.class, valueType),
                setter,
                setter.type());
    }

    @FunctionalInterface
    public interface ObjFloatConsumer<T extends Object>
    {
        public void accept(T t, float i);
    }

    @FunctionalInterface
    public interface ObjByteConsumer<T extends Object>
    {
        public void accept(T t, byte i);
    }

    @FunctionalInterface
    public interface ObjCharConsumer<T extends Object>
    {
        public void accept(T t, char i);
    }

    @FunctionalInterface
    public interface ObjShortConsumer<T extends Object>
    {
        public void accept(T t, short i);
    }

    @FunctionalInterface
    public interface ObjBooleanConsumer<T extends Object>
    {
        public void accept(T t, boolean i);
    }
}
