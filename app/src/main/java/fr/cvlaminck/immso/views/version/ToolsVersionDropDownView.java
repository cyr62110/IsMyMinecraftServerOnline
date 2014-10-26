package fr.cvlaminck.immso.views.version;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import fr.cvlaminck.immso.R;
import fr.cvlaminck.immso.minecraft.MinecraftToolsVersion;

@EViewGroup(R.layout.toolsversionadapter_dropdownview)
public class ToolsVersionDropDownView
    extends LinearLayout {

    @ViewById
    protected TextView txtVersion;

    @ViewById
    protected View llSpace;

    @ViewById
    protected TextView txtPunchLine;

    private MinecraftToolsVersion toolsVersion = null;

    public ToolsVersionDropDownView(Context context) {
        super(context);
    }

    public ToolsVersionDropDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolsVersionDropDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setToolsVersion(MinecraftToolsVersion toolsVersion) {
        this.toolsVersion = toolsVersion;
        updateViews();
    }

    private void updateViews() {
        if(toolsVersion == null) {
            txtVersion.setText("Unknown");
            llSpace.setVisibility(GONE);
            txtPunchLine.setVisibility(GONE);
        } else {
            txtVersion.setText(toolsVersion.getSupportedMinecraftVersions());
            if(toolsVersion.getPunchLine() == null) {
                llSpace.setVisibility(GONE);
                txtPunchLine.setVisibility(GONE);
            } else {
                llSpace.setVisibility(VISIBLE);
                txtPunchLine.setVisibility(VISIBLE);
                txtPunchLine.setText(toolsVersion.getPunchLine());
            }
        }
    }
}
