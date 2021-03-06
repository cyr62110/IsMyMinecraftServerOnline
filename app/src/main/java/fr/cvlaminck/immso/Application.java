/**
 * Copyright 2014 Cyril Vlaminck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.cvlaminck.immso;

import org.androidannotations.annotations.EApplication;

@EApplication
public class Application
        extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //We need to ensure that all tools versions are loaded and added to the register.
        try {
            Class.forName("fr.cvlaminck.immso.minecraft.v1_7.tools.Minecraft1_7Tools");
            Class.forName("fr.cvlaminck.immso.minecraft.v1_8.tools.Minecraft1_8Tools");
        } catch (ClassNotFoundException e) {
        }
    }
}
