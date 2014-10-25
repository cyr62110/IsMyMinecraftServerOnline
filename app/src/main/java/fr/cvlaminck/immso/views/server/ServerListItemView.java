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
package fr.cvlaminck.immso.views.server;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import fr.cvlaminck.immso.R;
import fr.cvlaminck.immso.data.entities.MinecraftServerEntity;
import fr.cvlaminck.immso.minecraft.MinecraftServer;
import fr.cvlaminck.immso.utils.TimeFormatter;

@EViewGroup(R.layout.serverlistitemview)
public class ServerListItemView
        extends LinearLayout
        implements Observer {

    @ViewById
    protected TextView txtName;

    @ViewById
    protected TextView txtVersion;

    @ViewById
    protected TextView txtAddress;

    @ViewById
    protected StatusView cvStatus;

    @ViewById
    protected View llOfflineSince;

    @ViewById
    protected TextView txtOfflineSince;

    @ViewById
    protected NumberOfPlayersView cvNumberOfPlayers;

    @Bean
    protected TimeFormatter timeFormatter;

    private MinecraftServerEntity server;

    public ServerListItemView(Context context) {
        super(context);
    }

    public MinecraftServerEntity getServer() {
        return server;
    }

    public void setServer(MinecraftServerEntity server) {
        //Remove the previous object observed by this view
        if (this.server != null)
            this.server.deleteObserver(this);
        //Then, we store the new object and register as an observer
        this.server = server;
        this.server.addObserver(this);
        //Finally, we update our view
        updateViews(false);
    }

    /**
     * Update the views to match the value stored in the entity.
     */
    private void updateViews(boolean animated) {
        setName(server.getName());
        setAddress(server.getHost(), server.getPort());
        //If we have a status, we display more information on the server
        setStatus(server.getDetailedStatus(), animated);
        if (server.getDetailedStatus() != MinecraftServer.DetailedStatus.UNKNOWN) {
            setVersion(server.getVersion());
            //If the server is online, we update the view displaying the number of players.
            if (server.getDetailedStatus() == MinecraftServer.DetailedStatus.ONLINE)
                setNumberOfPlayer(server.getNumberOfPlayer(), server.getMaxNumberOfPlayer());
            //If the server is offline, we display the offline since
            if (server.getDetailedStatus() == MinecraftServer.DetailedStatus.OFFLINE)
                setOfflineSince(server.getOfflineSince());
        }
    }

    private void setName(String name) {
        txtName.setText(name);
    }

    private void setAddress(String host, int port) {
        final String address = String.format("%s:%d", host, port);
        txtAddress.setText(address);
    }

    private void setVersion(String version) {
        //TODO Display unknown if no information about the server
        //TODO Do not add Minecraft if version is not starting with a number. ex. Epicube
        if (version == null || version.isEmpty()) {
            txtVersion.setVisibility(View.INVISIBLE);
        } else {
            final String sVersion;
            if (Character.isDigit(version.charAt(0)))
                sVersion = "Minecraft " + version;
            else
                sVersion = version;
            txtVersion.setVisibility(View.VISIBLE);
            txtVersion.setText(sVersion);
        }
    }

    private void setStatus(MinecraftServer.DetailedStatus detailedStatus, boolean animated) {
        //TODO add animation and cool stuff here
        cvStatus.setDetailedStatus(detailedStatus);
        //We hide the number of players if the server is not online
        switch (detailedStatus.equivalentStatus()) {
            case UNKNOWN:
                //We are probably pinging the server, so we display the progress view
                llOfflineSince.setVisibility(View.GONE);
                cvNumberOfPlayers.setVisibility(View.GONE);
                //TODO : display the progress view
                break;
            case OFFLINE:
                //The server is offline, we hide everything.
                //TODO : maybe show an offline since view
                llOfflineSince.setVisibility(View.VISIBLE);
                cvNumberOfPlayers.setVisibility(View.GONE);
                break;
            case ONLINE:
                //The server is online, we display the number of players view
                //TODO : hide other view when implemented
                llOfflineSince.setVisibility(View.GONE);
                cvNumberOfPlayers.setVisibility(VISIBLE);
                break;
        }
    }

    private void setNumberOfPlayer(int numberOfPlayer, int maxNumberOfPlayer) {
        cvNumberOfPlayers.setNumberOfPlayers(numberOfPlayer);
        cvNumberOfPlayers.setMaxNumberOfPlayers(maxNumberOfPlayer);
    }

    private void setOfflineSince(long offlineSince) {
        offlineSince = new Date().getTime() - offlineSince;
        txtOfflineSince.setText(timeFormatter.format(offlineSince, true));
    }

    @Override
    @UiThread
    public void update(Observable observable, Object o) {
        updateViews(true);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        //Do not forget to remove the reference in the observable
        if (server != null)
            server.deleteObserver(this);
    }
}
