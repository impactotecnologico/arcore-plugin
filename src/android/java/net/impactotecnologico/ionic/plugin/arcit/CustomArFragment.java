package net.impactotecnologico.ionic.plugin.arcit;

import android.util.Log;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

public class CustomArFragment extends ArFragment {

    private static final String TAG = "ARCITPlugin-TAG";

    @Override
    protected Config getSessionConfiguration(Session session) {
        getPlaneDiscoveryController().setInstructionView(null);
        Config config = new Config(session);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        session.configure(config);
        getArSceneView().setupSession(session);


        if ((((ImageRecognitionActivity) getActivity()).setupAugmentedImagesDb(config, session))) {
            Log.d(TAG, "Imagen cargada en imagesDB");
        } else {
            Log.e(TAG, "Faliure setting up db");
        }


        return config;
    }

}
