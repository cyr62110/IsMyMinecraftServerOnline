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
