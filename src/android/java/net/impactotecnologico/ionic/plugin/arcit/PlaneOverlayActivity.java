package net.impactotecnologico.ionic.plugin.arcit;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.res.AssetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import android.view.Gravity;
import android.view.MotionEvent;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class PlaneOverlayActivity extends AppCompatActivity {
    private static final String TAG = "ARCITPlugin-TAG";
    private static final double MIN_OPENGL_VERSION = 3.0;

    private int layoutId;
    private int fragmentId;
    private int objRawId;

    private ArFragment arFragment;
    private ModelRenderable relojRenderable;
    

    private void loadLocalResources(){
        this.layoutId = getResources().getIdentifier("activity_plane", "layout", getPackageName());
        this.fragmentId = getResources().getIdentifier("plane_fragment", "id", getPackageName());
        this.objRawId = getResources().getIdentifier("reloj", "raw", getPackageName());

        Log.d(TAG, "Id Obtenido: " + this.objRawId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        this.loadLocalResources();

        setContentView(this.layoutId);
        this.arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(this.fragmentId);

        ModelRenderable.builder()
                .setSource(this, this.objRawId)
                .build()
                .thenAccept(renderable -> relojRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load reloj renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (relojRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode reloj = new TransformableNode(arFragment.getTransformationSystem());
                    reloj.setParent(anchorNode);
                    reloj.setRenderable(relojRenderable);
                    reloj.select();
                });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        //firstTime = false;// There's a result, allow to exit the activity !
        
        // Do something with the result of the Intent data
 
        // Send parameters to retrieve in cordova.
        Intent intent = new Intent();
        intent.putExtra("data", "This is the sent information from the 2 activity :) ");
        setResult(Activity.RESULT_OK, intent);
        finish();// Exit of this activity !
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}
