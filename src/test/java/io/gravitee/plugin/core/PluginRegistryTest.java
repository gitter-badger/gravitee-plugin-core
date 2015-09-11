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
package io.gravitee.plugin.core;

import io.gravitee.common.event.EventManager;
import io.gravitee.plugin.api.ClassLoaderFactory;
import io.gravitee.plugin.api.Plugin;
import io.gravitee.plugin.internal.ClassLoaderFactoryImpl;
import io.gravitee.plugin.internal.PluginRegistryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URL;

import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
public class PluginRegistryTest {

    private ClassLoaderFactory classLoaderFactory;

    @Before
    public void setUp() {
        classLoaderFactory = new ClassLoaderFactoryImpl();
    }

    @Test(expected = RuntimeException.class)
    public void startWithInvalidWorkspace() throws Exception {
        PluginRegistryImpl pluginRegistry = new PluginRegistryImpl();
        pluginRegistry.start();
    }

    @Test(expected = RuntimeException.class)
    public void startWithInexistantWorkspace() throws Exception {
        PluginRegistryImpl pluginRegistry = new PluginRegistryImpl(
                "/io/gravitee/plugin/invalid/");
        pluginRegistry.start();
    }

    @Test
    public void startWithEmptyWorkspace() throws Exception {
        URL dir = PluginRegistryTest.class.getResource("/io/gravitee/plugin/empty-workspace/");
        PluginRegistryImpl pluginRegistry = new PluginRegistryImpl(dir.getPath());
        pluginRegistry.start();

        Assert.assertTrue(pluginRegistry.plugins().isEmpty());
    }

    @Test
    public void startTwiceWorkspace() throws Exception {
        URL dir = PluginRegistryTest.class.getResource("/io/gravitee/plugin/workspace/");
        PluginRegistryImpl pluginRegistry = Mockito.spy(new PluginRegistryImpl(dir.getPath()));
        pluginRegistry.setClassLoaderFactory(classLoaderFactory);
        pluginRegistry.setEventManager(mock(EventManager.class));
        pluginRegistry.start();
        verify(pluginRegistry, atMost(1)).init();

        pluginRegistry.start();
        verify(pluginRegistry, atMost(1)).init();
    }

    @Test
    public void startWithWorkspace_noJar() throws Exception {
        URL dir = PluginRegistryTest.class.getResource("/io/gravitee/plugin/invalid-workspace-nojar/");
        PluginRegistryImpl pluginRegistry = new PluginRegistryImpl(dir.getPath());
        pluginRegistry.start();

        Assert.assertTrue(pluginRegistry.plugins().isEmpty());
    }

    @Test
    public void startWithValidWorkspace_onePolicyDefstartion() throws Exception {
        URL dir = PluginRegistryTest.class.getResource("/io/gravitee/plugin/workspace/");
        PluginRegistryImpl pluginRegistry = new PluginRegistryImpl(dir.getPath());
        pluginRegistry.setClassLoaderFactory(classLoaderFactory);
        pluginRegistry.setEventManager(mock(EventManager.class));
        pluginRegistry.start();

        Assert.assertEquals(1, pluginRegistry.plugins().size());
    }

    @Test
    public void startWithValidWorkspace_checkPluginDescriptor() throws Exception {
        URL dir = PluginRegistryTest.class.getResource("/io/gravitee/plugin/workspace/");
        PluginRegistryImpl pluginRegistry = new PluginRegistryImpl(dir.getPath());
        pluginRegistry.setClassLoaderFactory(classLoaderFactory);
        pluginRegistry.setEventManager(mock(EventManager.class));
        pluginRegistry.start();

        Assert.assertEquals(1, pluginRegistry.plugins().size());

        Plugin plugin = pluginRegistry.plugins().iterator().next();
        Assert.assertEquals("my-policy", plugin.id());
        Assert.assertEquals("my.project.gravitee.policies.MyPolicy", plugin.clazz().getName());
    }
}