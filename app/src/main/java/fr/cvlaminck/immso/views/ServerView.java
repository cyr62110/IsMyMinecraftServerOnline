package fr.cvlaminck.immso.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Observable;
import java.util.Observer;

import fr.cvlaminck.immso.R;
import fr.cvlaminck.immso.data.entities.MinecraftServerEntity;
import fr.cvlaminck.immso.minecraft.MinecraftServer;

@EViewGroup(R.layout.serverview)
public class ServerView
    extends LinearLayout
    implements Observer {

    @ViewById
    protected TextView txtName;

    @ViewById
    protected TextView txtVersion;

    @ViewById
    protected TextView txtAddress;

    @ViewById
    protected TextView txtStatus;

    private MinecraftServerEntity server;

    public ServerView(Context context) {
        super(context);
    }

    public MinecraftServerEntity getServer() {
        return server;
    }

    public void setServer(MinecraftServerEntity server) {
        //Remove the previous object observed by this view
        if(this.server != null)
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
        setStatus(server.getStatus(), animated);
        if(server.getStatus() != MinecraftServer.Status.UNKNOWN) {
            setVersion(server.getVersion());
            //If the server is online, then we display the number of player
            if(server.getStatus() == MinecraftServer.Status.ONLINE)
                setNumberOfPlayer(server.getNumberOfPlayer(), server.getMaxNumberOfPlayer(), animated);
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
        final String sVersion = String.format("Minecraft %s", version);
        txtVersion.setText(sVersion);
    }

    private void setStatus(MinecraftServer.Status status, boolean animated) {
        //TODO add animation and cool stuff here
        txtStatus.setText(status.name());
    }

    private void setNumberOfPlayer(int numberOfPlayer, int maxNumberOfPlayer, boolean animated) {
        //TODO not supported for now
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
        if(server != null)
            server.deleteObserver(this);
    }
}
