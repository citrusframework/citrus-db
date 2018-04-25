/*
 * Copyright 2006-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.db.agent;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @author Christoph Deppisch
 */
public class JdbcDriverInterceptor {

    @RuntimeType
    public static Object intercept(@Origin Method method,
                                       @AllArguments Object[] args,
                                       @SuperCall Callable<?> callable) throws Exception {
        try {
            // The JdbcDriver class will not be available in the context of the instrumented method,
            // so we must use our agent class loader to load the driver class and do the call via reflection.
            Class<?> delegator = ClassLoaderUtils.getInstance().currentClassLoader().loadClass("com.consol.citrus.db.driver.JdbcDriver");
            Method delegateMethod = delegator.getMethod(method.getName(), Arrays.stream(args).map(Object::getClass).collect(Collectors.toList()).toArray(new Class[] {}));
            return delegateMethod.invoke(delegator.getField("driverInstance").get(null), args);
        } catch (Exception e) {
            System.err.println("Error executing Citrus JDBC driver on " + method.getDeclaringClass().getName() + "#" + method.getName());
            throw e;
        }
    }
}
