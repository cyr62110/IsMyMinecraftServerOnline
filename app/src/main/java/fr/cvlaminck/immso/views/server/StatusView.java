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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import fr.cvlaminck.immso.R;
import fr.cvlaminck.immso.minecraft.MinecraftServer;

/**
 * Custom view for showing server status
 */
public class StatusView
        extends View {

    private MinecraftServer.DetailedStatus status = MinecraftServer.DetailedStatus.UNKNOWN;

    private int onlineStatusColor = Color.GREEN;

    private int offlineStatusColor = Color.RED;

    private int fullStatusColor = Color.YELLOW;

    public StatusView(Context context) {
        super(context);
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray customAttrs = context.obtainStyledAttributes(attrs, R.styleable.StatusView);
        parseCustomAttributes(customAttrs);
        customAttrs.recycle();
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray customAttrs = context.obtainStyledAttributes(attrs, R.styleable.StatusView, defStyleAttr, 0);
        parseCustomAttributes(customAttrs);
        customAttrs.recycle();
    }

    private void parseCustomAttributes(TypedArray attrs) {
        offlineStatusColor = attrs.getColor(R.styleable.StatusView_offlineStatusColor, Color.RED);
        onlineStatusColor = attrs.getColor(R.styleable.StatusView_onlineStatusColor, Color.GREEN);
        fullStatusColor = attrs.getColor(R.styleable.StatusView_fullStatusColor, Color.YELLOW);
    }

    private int getColorForStatus(MinecraftServer.DetailedStatus status) {
        switch (status.equivalentStatus()) {
            case UNKNOWN:
                return 0;
            case OFFLINE:
                return offlineStatusColor;
            case ONLINE:
                if (status == MinecraftServer.DetailedStatus.FULL)
                    return fullStatusColor;
                else
                    return onlineStatusColor;
        }
        return 0;
    }

    public MinecraftServer.DetailedStatus getDetailedStatus() {
        return status;
    }

    public void setDetailedStatus(MinecraftServer.DetailedStatus status) {
        //TODO work on animating this view
        this.status = status;
        setBackgroundColor(getColorForStatus(status));
    }
}
