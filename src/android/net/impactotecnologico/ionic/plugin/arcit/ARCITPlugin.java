/**
 */
package net.impactotecnologico.ionic.plugin.arcit;

import android.content.Context;
import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import java.util.Date;

public class ARCITPlugin extends CordovaPlugin {
  private static final String TAG = "ARCITPlugin";

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Log.d(TAG, "Inicializando ARCITPlugin");
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if(action.equals("bienvenida")) {
      // An example of returning data back to the web layer
       String phrase = args.getString(0);
      // Echo back the first argument      
      final PluginResult result = new PluginResult(PluginResult.Status.OK, "Hola todo el... "+phrase);
      callbackContext.sendPluginResult(result);
    }
    return true;
  }

  private void openNewActivity(Context context, String action) {
    Intent intent = new Intent(context, AugmentedActivity.class);

    if (action.equals("bienvenida")) {
    //  intent.putExtra(AugmentedActivity.ACCION, Acciones.BIENVENIDA);
    }
    if (action.equals("menu")) {
    //  intent.putExtra(AugmentedActivity.ACCION, Acciones.MENU);
    }
    System.out.println("Entra en vista");
    this.cordova.getActivity().startActivity(intent);
  }

}
