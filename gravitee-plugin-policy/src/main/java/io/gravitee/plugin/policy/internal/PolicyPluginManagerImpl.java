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
package io.gravitee.plugin.policy.internal;

import io.gravitee.plugin.policy.PolicyPlugin;
import io.gravitee.plugin.policy.PolicyPluginManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
public class PolicyPluginManagerImpl implements PolicyPluginManager {

    private final static String SCHEMAS_DIRECTORY = "schemas";

    private final Map<String, PolicyPlugin> definitions = new HashMap<>();

    @Override
    public void register(PolicyPlugin policyPlugin) {
        definitions.putIfAbsent(policyPlugin.id(), policyPlugin);
    }

    @Override
    public Collection<PolicyPlugin> findAll() {
        return definitions.values();
    }

    @Override
    public PolicyPlugin get(String policy) {
        return definitions.get(policy);
    }

    @Override
    public String getPolicyConfiguration(String policy) throws IOException {
        Path policyWorkspace = get(policy).path();

        File[] schemas = policyWorkspace.toFile().listFiles(
                pathname -> pathname.isDirectory() && pathname.getName().equals(SCHEMAS_DIRECTORY));

        if (schemas.length == 1) {
            File schemaDir = schemas[0];

            if (schemaDir.listFiles().length > 0) {
                return new String(Files.readAllBytes(schemaDir.listFiles()[0].toPath()));
            }
        }

        return null;
    }
}
