package fr.cvlaminck.immso;

import org.androidannotations.annotations.EApplication;

/**
 *
 */
@EApplication
public class Application
    extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //We need to ensure that all tools versions are loaded and added to the register.
        try {
            Class.forName("fr.cvlaminck.immso.minecraft.v1_7.tools.Minecraft1_7Tools");
        } catch (ClassNotFoundException e) {}
    }
}
