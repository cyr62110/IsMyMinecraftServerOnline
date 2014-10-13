package fr.cvlaminck.immso.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.cvlaminck.immso.data.entities.MinecraftServerEntity;
import fr.cvlaminck.immso.minecraft.MinecraftServer;
import fr.cvlaminck.immso.views.server.ServerListHeaderView;
import fr.cvlaminck.immso.views.server.ServerListHeaderView_;
import fr.cvlaminck.immso.views.server.ServerListItemView;
import fr.cvlaminck.immso.views.server.ServerListItemView_;

/**
 */
public class ServerAdapter
        extends BaseAdapter {

    private Context context = null;

    private List<MinecraftServerEntity> servers;

    private List<MinecraftServerEntity> sortedServers = null;

    private int numberOfOnlineServer = 0;

    private int numberOfOfflineServer = 0;

    public ServerAdapter(Context context, List<MinecraftServerEntity> servers) {
        this.context = context;
        this.servers = servers;
        sortServersAndUpdateStats();
    }

    @Override
    public int getCount() {
        if(sortedServers == null)
            return 0;
        //We display only the header if we have at least one server in the category
        int count = sortedServers.size();
        if(numberOfOnlineServer > 0)
            count ++;
        if(numberOfOfflineServer > 0)
            count ++;
        return count;
    }

    @Override
    public Object getItem(int position) {
        //There is at least always one header
        if(position == 0) {
            //If we are on this header
            if(numberOfOfflineServer > 0)
                return null;
            else
                return null;
        }
        position--;
        //Then we take care of the second header
        if(numberOfOfflineServer > 0) {
            //If we are on the second header
            if(position == numberOfOfflineServer)
                return null;
            if(position > numberOfOfflineServer)
                position--;
        }
        return sortedServers.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        //Headers are not enabled
        if(position == 0)
            return false;
        if(numberOfOfflineServer > 0 && position == numberOfOfflineServer + 1)
            return false;
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup root) {
        //We display the header for the offline servers or the online servers if no offline server in the list
        if(position == 0) {
            if(numberOfOfflineServer > 0)
                return getOfflineHeaderView(convertView, root);
            else
                return getOnlineHeaderView(convertView, root);
        }
        //We display the header for the online server if not displayed at the top of the list
        if(numberOfOfflineServer > 0 && position == numberOfOfflineServer + 1)
            return getOnlineHeaderView(convertView, root);
        return getServerListItemView(position, convertView, root);
    }

    @Override
    public void notifyDataSetChanged() {
        //If the list of server is updated, we need to sort the list again and update the stats
        sortServersAndUpdateStats();
        //Then we notify that the data set has changed
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        //If the list of server is updated, we need to sort the list again and update the stats
        sortServersAndUpdateStats();
        //Then we notify that the data set has changed
        super.notifyDataSetInvalidated();
    }

    private View getOnlineHeaderView(View convertView, ViewGroup root) {
        //TODO : handle convert view
        convertView = ServerListHeaderView_.build(context);

        final ServerListHeaderView headerView = (ServerListHeaderView) convertView;
        headerView.setStatus(MinecraftServer.Status.ONLINE);
        headerView.setNumberOfServerWithStatus(numberOfOnlineServer);

        return headerView;
    }

    private View getOfflineHeaderView(View convertView, ViewGroup root) {
        //TODO : handle convert view
        convertView = ServerListHeaderView_.build(context);

        final ServerListHeaderView headerView = (ServerListHeaderView) convertView;
        headerView.setStatus(MinecraftServer.Status.OFFLINE);
        headerView.setNumberOfServerWithStatus(numberOfOfflineServer);

        return headerView;
    }

    private View getServerListItemView(int position, View convertView, ViewGroup root) {
        //TODO : handle convert view
        //if (convertView == null) {
            convertView = ServerListItemView_.build(context);
        //}

        final ServerListItemView serverView = (ServerListItemView) convertView;
        final MinecraftServerEntity server = (MinecraftServerEntity) getItem(position);
        serverView.setServer(server);

        return serverView;
    }

    private void sortServersAndUpdateStats() {
        if(servers == null) {
            sortedServers = null;
            numberOfOfflineServer = 0;
            numberOfOnlineServer = 0;
            return;
        }
        //We sort the servers in the list
        sortedServers = new ArrayList<>(servers);
        Collections.sort(sortedServers, new Comparator<MinecraftServerEntity>() {
            @Override
            public int compare(MinecraftServerEntity s1, MinecraftServerEntity s2) {
                if (s1.getStatus() == s2.getStatus()) {
                    //If both servers have the same status, we have different rules to sort them
                    if (s1.getStatus() == MinecraftServer.Status.OFFLINE)
                        return -new Long(s1.getOfflineSince()).compareTo(s2.getOfflineSince()); //Offline servers are ordered so the one with the longest offline time is the first.
                    else
                        return 1; //The ordering of online/unknown servers does not matter
                } else {
                    //Offline servers are the first in the list
                    if (s1.getStatus() == MinecraftServer.Status.OFFLINE)
                        return -1;
                    else
                        return 1;
                }
            }
        });
        //And we update the stats
        numberOfOfflineServer = 0;
        for (int i = 0; i < sortedServers.size(); i++) {
            if (sortedServers.get(i).getStatus() == MinecraftServer.Status.OFFLINE)
                numberOfOfflineServer++;
        }
        numberOfOnlineServer = sortedServers.size() - numberOfOfflineServer;
    }
}
