package fr.cvlaminck.aboutmyapp.configs.builder;

import android.content.Context;

import fr.cvlaminck.aboutmyapp.R;
import fr.cvlaminck.aboutmyapp.configs.AboutMyAppDialogConfig;

public class AboutMyAppDialogConfigBuilder {

    private Context context = null;

    private int titleResId = 0;

    private String title = null;

    private int layoutResId = 0;

    public AboutMyAppDialogConfigBuilder(Context context) {
        this.context = context;
    }

    public AboutMyAppDialogConfigBuilder title(int titleResId) {
        this.titleResId = titleResId;
        return this;
    }

    public AboutMyAppDialogConfigBuilder layout(int layoutResId) {
        this.layoutResId = layoutResId;
        return this;
    }

    public AboutMyAppDialogConfig build() {
        final AboutMyAppDialogConfig dialogConfig = new AboutMyAppDialogConfig();
        //Layout
        dialogConfig.setLayout((layoutResId ==0) ? R.layout.default_aboutmyappdialogfragment : layoutResId);
        //Title
        if(title == null) {
            final String sTitle = context.getString((titleResId == 0) ? R.string.default_aboutmyappdialogfragment_title : titleResId);
            dialogConfig.setTitle(sTitle);
        } else
            dialogConfig.setTitle(title);

        return dialogConfig;
    }

}
