package fr.cvlaminck.aboutmyapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import fr.cvlaminck.aboutmyapp.configs.AboutMyAppConfig;
import fr.cvlaminck.aboutmyapp.configs.AboutMyAppDialogConfig;
import fr.cvlaminck.aboutmyapp.configs.builder.AboutMyAppConfigBuilder;

/**
 * Dialog displaying information about the application.
 */
public class AboutMyAppDialogFragment
        extends DialogFragment {

    private final static String CONFIG_KEY = "cfg";

    /**
     * Create a new configured instance of the dialog using the provided configuration.
     * /!\ You must use one of the newInstance method to create a new instance, using the constructor will
     * surely lead you to NullPointerException.
     *
     * @param config Configuration of the library. Use the AboutMyAppConfigBuilder to create an instance.
     * @return A new configured AboutMyAppDialogFragment instance.
     */
    public static AboutMyAppDialogFragment newInstance(AboutMyAppConfig config) {
        //If no configuration is provided, we throw an exception
        if(config == null)
            throw new IllegalArgumentException("No configuration provided to the AboutMyAppDialogFragment");
        final AboutMyAppDialogFragment dialogFragment = new AboutMyAppDialogFragment();

        //We put the configuration in the argument
        final Bundle arguments = new Bundle();
        arguments.putParcelable(CONFIG_KEY, config);
        dialogFragment.setArguments(arguments);

        return dialogFragment;
    }

    /**
     * Create a new configured instance of the dialog using the default configuration.
     * /!\ You must use one of the newInstance method to create a new instance, using the constructor will
     * surely lead you to NullPointerException.
     *
     * @return A new configured AboutMyAppDialogFragment instance.
     */
    public static AboutMyAppDialogFragment newInstance(Context context) {
        return newInstance(new AboutMyAppConfigBuilder(context).build());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //We retrieve the configuration
        final AboutMyAppConfig config = getArguments().getParcelable(CONFIG_KEY);
        final AboutMyAppDialogConfig dialogConfig = config.getDialogConfig();

        //We inflate the view using the configured layout
        final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View contentView = layoutInflater.inflate(dialogConfig.getLayout(), null);
        //And we start the population of all fields
        //TODO

        //We use an AlertDialog.Builder to create the library dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialogConfig.getTitle())
                .setView(contentView);
        //TODO add the 2 buttons

        final Dialog dialog = builder.create();
        return dialog;
    }



}
