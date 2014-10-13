package fr.cvlaminck.immso.views.server;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import fr.cvlaminck.immso.R;
import fr.cvlaminck.immso.minecraft.MinecraftServer;

/**
 *
 */
@EViewGroup(R.layout.serverlistheaderview)
public class ServerListHeaderView extends LinearLayout {

    private MinecraftServer.Status status;

    private int numberOfServer;

    @ViewById
    protected TextView txtHeader;

    public ServerListHeaderView(Context context) {
        super(context);
    }

    public ServerListHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ServerListHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setStatus(MinecraftServer.Status status) {
        this.status = status;
        updateText();
    }

    public void setNumberOfServerWithStatus(int numberOfServer) {
        this.numberOfServer = numberOfServer;
        updateText();
    }

    private void updateText() {
        String statusText = null;
        switch (status) {
            case ONLINE:
                statusText = getContext().getString(R.string.serverviewheaderview_status_online);
                break;
            case OFFLINE:
                statusText = getContext().getString(R.string.serverlistheaderview_status_offline);
                break;
        }
        txtHeader.setText(String.format("%s (%d)", statusText, numberOfServer));
    }
}
