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
