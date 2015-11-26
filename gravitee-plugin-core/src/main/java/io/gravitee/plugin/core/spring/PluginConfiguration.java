/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.plugin.core.spring;

import io.gravitee.plugin.core.api.ClassLoaderFactory;
import io.gravitee.plugin.core.api.PluginContextFactory;
import io.gravitee.plugin.core.api.PluginRegistry;
import io.gravitee.plugin.core.internal.ClassLoaderFactoryImpl;
import io.gravitee.plugin.core.internal.PluginContextFactoryImpl;
import io.gravitee.plugin.core.internal.PluginEventListener;
import io.gravitee.plugin.core.internal.PluginRegistryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
@Configuration
public class PluginConfiguration {

    @Bean
    public PluginRegistry pluginRegistry() {
        return new PluginRegistryImpl();
    }

    @Bean
    public ClassLoaderFactory classLoaderFactory() {
        return new ClassLoaderFactoryImpl();
    }

    @Bean
    public PluginContextFactory pluginContextFactory() {
        return new PluginContextFactoryImpl();
    }

    @Bean
    public PluginEventListener pluginEventListener() {
        return new PluginEventListener();
    }
}