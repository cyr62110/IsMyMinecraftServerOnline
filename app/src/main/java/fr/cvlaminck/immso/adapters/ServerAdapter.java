package fr.cvlaminck.immso.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collection;
import java.util.List;

import fr.cvlaminck.immso.data.entities.MinecraftServerEntity;
import fr.cvlaminck.immso.views.ServerView;
import fr.cvlaminck.immso.views.ServerView_;

/**
 */
public class ServerAdapter
    extends BaseAdapter {

    private Context context = null;

    private List<MinecraftServerEntity> servers;

    public ServerAdapter(Context context, List<MinecraftServerEntity> servers) {
        this.context = context;
        this.servers = servers;
    }

    @Override
    public int getCount() {
        return (servers != null) ? servers.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return servers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup root) {
        if(convertView == null) {
            convertView = ServerView_.build(context);
        }

        final ServerView serverView = (ServerView) convertView;
        final MinecraftServerEntity server = (MinecraftServerEntity) getItem(position);
        serverView.setServer(server);

        return serverView;
    }
}
