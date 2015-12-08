package co.flashpick.client.android.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import co.flashpick.client.android.callbacks.FlashpickLoginCallback;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Miko on 2015-12-05.
 */
public class AuthenticationManager {

    final private static String TAG = "AuthenticationManager";
    final private static String FLASHPICK_SERVLET_ADDRESS = "http://192.168.0.10:8080/flashpick-server-services-ws/";

    public static Activity activityContext;
    public static String jwtToken;
    public static CallbackManager facebookCallbackManager;
    public static RequestQueue requestQueue;

    private static String getFacebookAuthUrlWithParam() {
        return FLASHPICK_SERVLET_ADDRESS + "mobileFBAuth?accessToken=" + AccessToken.getCurrentAccessToken().getToken();
    }

    private static String getAuthenticatedRequestUrl(String url) {
        return FLASHPICK_SERVLET_ADDRESS + url + "?accessToken=" + AccessToken.getCurrentAccessToken().getToken();
    }

    public static String authenticateThroughFacebook() {
        return authenticateThroughFacebook(new FlashpickLoginCallback() {
            @Override
            public void callback() {

            }
        });
    }

    public static String authenticateThroughFacebook(final FlashpickLoginCallback callback) {
        JsonObjectRequest facebookAuthRequest = new JsonObjectRequest(Request.Method.GET, getFacebookAuthUrlWithParam(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    jwtToken = response.getString("token");
                    callback.callback();
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing successful mobileFBAuth response!");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Failed authorization at mobileFBAuth!");
            }
        });
        requestQueue.add(facebookAuthRequest);
        saveFlashpickToken();
        return jwtToken;
    }

    private static void saveFlashpickToken () {
        SharedPreferences sharedPref = activityContext.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("FLASHPICK_TOKEN", jwtToken);
        editor.commit();
    }

    public static void request(int method, String url, JSONObject jsonObject, Map<String, String> headers, Map<String, String> queryParams, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        if(headers == null) {
            headers = new HashMap<String, String>();
            
        headers.put("Authorization", "Bearer " + jwtToken);
        final Map<String, String> finalHeaders = headers;

        if(queryParams == null)
            queryParams = new HashMap<String, String>();

        String requestBuilder = getAuthenticatedRequestUrl(url);
        for (Map.Entry<String, String> entry : queryParams.entrySet())
        {
            requestBuilder = requestBuilder + "&" + entry.getKey() + "=" + entry.getValue();
        }
        JsonObjectRequest anyAuthenticatedRequest = new JsonObjectRequest(method, requestBuilder, jsonObject, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return finalHeaders;
            }
        };
        requestQueue.add(anyAuthenticatedRequest);
    }
}
