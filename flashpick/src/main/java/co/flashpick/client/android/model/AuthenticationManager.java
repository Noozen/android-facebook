package co.flashpick.client.android.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import co.flashpick.client.android.callbacks.FlashpickLoginCallback;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Miko on 2015-12-05.
 */
public class AuthenticationManager {

    final private static String TAG = "AuthenticationManager";
    final private static String FLASHPICK_SERVLET_ADDRESS = "http://192.168.0.10:8080/flashpick-server-services-ws/";

    public static String jwtToken;
    public static CallbackManager facebookCallbackManager;
    public static RequestQueue requestQueue;

    private static String getFacebookAuthUrlWithParam() {
        return FLASHPICK_SERVLET_ADDRESS + "profiles?accessToken=" + AccessToken.getCurrentAccessToken().getToken();
    }

    private static String getRequestUrl(String url) {
        return FLASHPICK_SERVLET_ADDRESS + url + "?";
    }

    public static void authenticateThroughFacebook() {
        authenticateThroughFacebook(new FlashpickLoginCallback() {
            @Override
            public void callback() {

            }
        });
    }

    public static void authenticateThroughFacebook(final FlashpickLoginCallback callback) {
        JsonObjectRequest facebookAuthRequest = new JsonObjectRequest(Request.Method.POST, getFacebookAuthUrlWithParam(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    jwtToken = response.getString("token");
                    saveFlashpickToken();
                    saveUsername();
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
    }

    private static void saveUsername() {
        try {
            JWT jwt = JWTParser.parse(jwtToken);
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            UserData.userId = claimsSet.getSubject();
        } catch (ParseException e) {
            Log.e(TAG, "Parsing JWT failed.");
        }
    }

    private static void saveFlashpickToken() {
        SharedPreferences sharedPref = AndroidHelper.activityContext.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("FLASHPICK_TOKEN", jwtToken);
        editor.commit();
    }

    public static void request(int method, String url, JSONObject jsonObject, Map<String, String> headers, Map<String, String> queryParams, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        if (headers == null)
            headers = new HashMap<String, String>();

        headers.put("Authorization", "Bearer " + jwtToken);
        final Map<String, String> finalHeaders = headers;

        if (queryParams == null)
            queryParams = new HashMap<String, String>();

        String requestBuilder = getRequestUrl(url);
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
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
