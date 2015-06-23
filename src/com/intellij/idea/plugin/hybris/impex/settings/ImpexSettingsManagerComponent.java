/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.impex.settings;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Created 19:39 29 March 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexSettingsManagerComponent implements ApplicationComponent, ImpexSettingsManager {

    protected final ImpexSettingsData settingsData = new ImpexSettingsData();

    @Override
    public void initComponent() {
        PropertiesComponent.getInstance().loadFields(this.settingsData);
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return this.getClass().getName();
    }

    @NotNull
    @Override
    public ImpexSettingsData getImpexSettingsData() {
        return this.settingsData;
    }

    @Override
    public void saveImpexSettingsData() {
        PropertiesComponent.getInstance().saveFields(this.settingsData);
    }
}
