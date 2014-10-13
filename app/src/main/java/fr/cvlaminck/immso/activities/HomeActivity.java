package fr.cvlaminck.immso.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import fr.cvlaminck.immso.R;
import fr.cvlaminck.immso.adapters.ServerAdapter;
import fr.cvlaminck.immso.data.entities.MinecraftServerEntity;
import fr.cvlaminck.immso.preferences.UserPreferences_;
import fr.cvlaminck.immso.services.api.PingService;
import fr.cvlaminck.immso.views.swipedismiss.SwipeDismissListViewTouchListener;

@EActivity(R.layout.homeactivity)
@OptionsMenu(R.menu.homeactivity)
public class HomeActivity
        extends BaseActivity
        implements PingService.ServerStatusChangedListener {

    @ViewById
    protected ListView lvServers;

    @ViewById
    protected View llEmpty;

    @ViewById
    protected SwipeRefreshLayout srlRefresh;

    @AfterViews
    protected void afterViews() {
        lvServers.setEmptyView(llEmpty);
        srlRefresh.setOnRefreshListener(onRefreshListener);
        //We configure the swipe to dismiss using Roman Nurik's code
        final SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(lvServers, dismissCallbacks);
        lvServers.setOnTouchListener(touchListener);
        lvServers.setOnScrollListener(touchListener.makeScrollListener());
    }

    @Override
    protected void onPingServiceConnected(PingService pingService) {
        pingService.registerServerStatusChangedListener(this);
        //When we have retrieved a reference to the service, we ask for the
        //list of servers and we display them in the ListView
        final List<MinecraftServerEntity> servers = pingService.getServers();
        final ServerAdapter adapter = new ServerAdapter(this, servers);
        lvServers.setAdapter(adapter);
        //If the list is empty, we disable the refresh layout
        if(servers.isEmpty())
            srlRefresh.setEnabled(false);
    }

    @Override
    protected void onPingServerDisconnected(PingService pingService) {
        pingService.unregisterServerStatusChangedListener(this);
    }

    /**
     * Refresh the status of all server in the list
     */
    private void refreshServerStatus() {
        //If the list is empty, we do not need to refresh
        if(lvServers.getAdapter() == null || lvServers.getAdapter().isEmpty())
            return;
        pingService.refreshServerStatus();
    }

    @OptionsItem(R.id.miAdd)
    protected void addServerToList() {
        AddServerActivity_.intent(this).start();
    }

    @OptionsItem(R.id.miRefresh)
    protected void miRefresh() {
        srlRefresh.setRefreshing(true);
        refreshServerStatus();
    }

    @OptionsItem(R.id.miPreferences)
    protected void showPreferences() {
        UserPreferenceActivity_.intent(this)
                .start();
    }

    @Override
    public void onServerListChanged(List<MinecraftServerEntity> servers) {
        if(lvServers != null && lvServers.getAdapter() != null)
            ((ServerAdapter)lvServers.getAdapter()).notifyDataSetChanged();
        //If the list is empty, we disable the refresh layout
        srlRefresh.setEnabled(!servers.isEmpty());
    }

    @Override
    public void onAllServerStatusUpdated(List<MinecraftServerEntity> servers) {
        //We notify the UI that the refresh is finished
        srlRefresh.setRefreshing(false);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshServerStatus();
        }
    };

    private SwipeDismissListViewTouchListener.DismissCallbacks dismissCallbacks = new SwipeDismissListViewTouchListener.DismissCallbacks() {
        @Override
        public boolean canDismiss(int position) {
            return true;
        }

        @Override
        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
            for(int position : reverseSortedPositions) {
                final MinecraftServerEntity server = (MinecraftServerEntity) listView.getAdapter().getItem(position);
                pingService.removeServer(server);
            }
        }
    };
}
