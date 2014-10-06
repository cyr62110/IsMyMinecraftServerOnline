package fr.cvlaminck.immso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import fr.cvlaminck.immso.minecraft.MinecraftToolsVersion;
import fr.cvlaminck.immso.minecraft.SupportedToolsVersions;

public class ToolsVersionAdapter
        extends ArrayAdapter<MinecraftToolsVersion> {

    private LayoutInflater layoutInflater = null;

    public ToolsVersionAdapter(Context context) {
        super(context, 0);
        layoutInflater = LayoutInflater.from(context);
        addAll(SupportedToolsVersions.getInstance().getAvailableVersions());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup rootView) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(android.R.layout.simple_spinner_item, null);
        }
        final MinecraftToolsVersion toolsVersion = getItem(position);
        final TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);

        text1.setText(toolsVersion.getSupportedMinecraftVersions());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(android.R.layout.simple_spinner_item, null);
        }
        final MinecraftToolsVersion toolsVersion = getItem(position);
        final TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);

        text1.setText(toolsVersion.getSupportedMinecraftVersions());

        return convertView;
    }
}
