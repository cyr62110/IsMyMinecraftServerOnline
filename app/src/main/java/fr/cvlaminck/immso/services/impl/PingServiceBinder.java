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
