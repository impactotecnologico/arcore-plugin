/**
 */
package net.impactotecnologico.ionic.plugin.arcit;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.ArCoreApk.InstallStatus;

import android.util.Log;

import java.util.Date;

public class ARCITPlugin extends CordovaPlugin {
  private static final String TAG = "ARCITPlugin-TAG";
  private Activity activity;
  private Context context;
  private CallbackContext PUBLIC_CALLBACKS = null;
  public static String HIDE_HAND = "hideHand";
  private boolean userRequestedInstall = true;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Log.d(TAG, "Inicializando ARCITPlugin");
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    this.context = cordova.getActivity().getApplicationContext();
    this.activity = cordova.getActivity();
    PUBLIC_CALLBACKS = callbackContext;
   
    String param = args.getString(0);
    Boolean showOrHide = Boolean.valueOf(param);

    validateARCoreInstallation();
    if(userRequestedInstall){
      this.openNewActivity(action, showOrHide);

      final PluginResult pluginResult = new  PluginResult(PluginResult.Status.NO_RESULT);
      pluginResult.setKeepCallback(true);
        
      return true;
    }
    return false;
    
  }

  private void validateARCoreInstallation(){
    try {
      switch (ArCoreApk.getInstance().requestInstall(this.activity, userRequestedInstall)) {
        case INSTALLED:
          // Success, create the AR session.
          break;
        case INSTALL_REQUESTED:
          userRequestedInstall = false;
      }

    } catch (UnavailableUserDeclinedInstallationException | UnavailableDeviceNotCompatibleException e){
      e.printStackTrace();
      userRequestedInstall = false;
      Toast.makeText(this.context, "Necesitas instalar la aplicación ARCore", Toast.LENGTH_LONG).show();
    }
  }

  private void openNewActivity(String action, boolean showOrHide) {
    
    Log.d(TAG, "Entra en vista AR... para " + action);

    if (action.equals("bienvenida")) {

      Intent intent = new Intent("net.impactotecnologico.ionic.plugin.arcit.ImageRecognitionActivity");
      intent.putExtra(HIDE_HAND, showOrHide);
      // Now, cordova will expect for a result using startActivityForResult and will be handle by the onActivityResult.
      cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
        
    }
    if (action.equals("plano")) {
      Intent intent = new Intent("net.impactotecnologico.ionic.plugin.arcit.PlaneOverlayActivity");
      intent.putExtra(HIDE_HAND, showOrHide);
      // Now, cordova will expect for a result using startActivityForResult and will be handle by the onActivityResult.
      cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
    }

    if (action.equals("chroma")) {
      Intent intent = new Intent("net.impactotecnologico.ionic.plugin.arcit.ChromaKeyVideoActivity");
      intent.putExtra(HIDE_HAND, showOrHide);
      // Now, cordova will expect for a result using startActivityForResult and will be handle by the onActivityResult.
      cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
    }
    
    
  }

  @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if(resultCode == cordova.getActivity().RESULT_OK){
            Bundle extras = data.getExtras();// Get data sent by the Intent
            String information = extras.getString("data");
            
            System.out.println("INFO: " + information);

            PluginResult resultado = new PluginResult(PluginResult.Status.OK, "this value will be sent to cordova");
            resultado.setKeepCallback(true);
            PUBLIC_CALLBACKS.sendPluginResult(resultado);
            return;
        }else if(resultCode == cordova.getActivity().RESULT_CANCELED){
            PluginResult resultado = new PluginResult(PluginResult.Status.OK, "canceled action, process this in javascript");
            resultado.setKeepCallback(true);
            PUBLIC_CALLBACKS.sendPluginResult(resultado);
            return;
        }
        // Handle other results if exists.
        super.onActivityResult(requestCode, resultCode, data);
    }

}
