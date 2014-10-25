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
package fr.cvlaminck.immso.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

import fr.cvlaminck.immso.R;
import fr.cvlaminck.immso.adapters.ToolsVersionAdapter;
import fr.cvlaminck.immso.data.entities.MinecraftServerEntity;
import fr.cvlaminck.immso.minecraft.MinecraftToolsVersion;
import fr.cvlaminck.immso.services.api.PingService;

@EActivity(R.layout.addserveractivity)
public class AddServerActivity
        extends BaseActivity {

    @ViewById
    protected Spinner spVersion;

    @ViewById
    protected EditText txtName;

    @ViewById
    protected EditText txtHost;

    @ViewById
    protected EditText txtPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate a "Done/Discard" custom action bar view.
        LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done_discard, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        done();
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_discard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        discard();
                    }
                });

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @AfterViews
    protected void afterViews() {
        spVersion.setAdapter(new ToolsVersionAdapter(this));
    }

    @Override
    protected void onPingServiceConnected(PingService pingService) {

    }

    @EditorAction(R.id.txtPort)
    protected void txtPortEditorAction(TextView textView, int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            done();
        }
    }

    private void done() {
        //First, we need to retrieve the value entered by the end-user
        boolean bValue = true;
        final MinecraftServerEntity server = new MinecraftServerEntity();
        bValue &= getVersion(server);
        bValue &= getName(server);
        bValue &= getHost(server);
        bValue &= getPort(server);
        //If one of the value entered by the user does not match the requirement we stop here
        if (!bValue)
            return;
        //Otherwise, we save the value in our service and finish this activity
        pingService.addServer(server);
        finish();
    }

    private void discard() {
        finish();
    }

    private boolean getVersion(MinecraftServerEntity server) {
        final MinecraftToolsVersion version = (MinecraftToolsVersion) spVersion.getSelectedItem();
        server.setToolsVersion(version.getToolsVersion());
        return true;
    }

    private boolean getName(MinecraftServerEntity server) {
        final String name = txtName.getText().toString().trim();
        if (name.isEmpty()) {
            txtName.setError(getString(R.string.addserveractivity_txtName_error));
            return false;
        } else {
            txtName.setError(null);
            server.setName(name);
            return true;
        }
    }

    private boolean getHost(MinecraftServerEntity server) {
        final String host = txtHost.getText().toString().trim();
        if (host.isEmpty()) {
            txtHost.setError(getString(R.string.addserveractivity_txtHost_errorEmpty));
            return false;
            //TODO : check host pattern
        } else {
            txtHost.setError(null);
            server.setHost(host);
            return true;
        }
    }

    private boolean getPort(MinecraftServerEntity server) {
        try {
            final int port = Integer.parseInt(txtPort.getText().toString());
            if (port >= 1 || port <= 65535) {
                txtPort.setError(null);
                server.setPort(port);
                return true;
            }
        } catch (NumberFormatException e) {
        }
        txtPort.setError(getString(R.string.addserveractivity_txtPort_error));
        return false;
    }
}
