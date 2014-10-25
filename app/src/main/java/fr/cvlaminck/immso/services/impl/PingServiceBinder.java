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
package fr.cvlaminck.immso.services.impl;

import android.os.Binder;

import java.util.List;

import fr.cvlaminck.immso.data.entities.MinecraftServerEntity;
import fr.cvlaminck.immso.services.api.PingService;

public class PingServiceBinder
        extends Binder
        implements PingService {

    private fr.cvlaminck.immso.services.impl.PingService service;

    public PingServiceBinder(fr.cvlaminck.immso.services.impl.PingService service) {
        this.service = service;
    }

    @Override
    public List<MinecraftServerEntity> getServers() {
        return service.getServers();
    }

    @Override
    public void addServer(MinecraftServerEntity minecraftServer) {
        service.addServer(minecraftServer);
    }

    @Override
    public void removeServer(MinecraftServerEntity minecraftServer) {
        service.removeServer(minecraftServer);
    }

    @Override
    public void refreshServerStatus() {
        service.refreshServerStatus();
    }

    @Override
    public void registerServerStatusChangedListener(ServerStatusChangedListener listener) {
        service.registerServerStatusChangedListener(listener);
    }

    @Override
    public void unregisterServerStatusChangedListener(ServerStatusChangedListener listener) {
        service.unregisterServerStatusChangedListener(listener);
    }
}
