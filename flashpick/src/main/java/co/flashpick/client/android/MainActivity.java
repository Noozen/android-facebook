package co.flashpick.client.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends Activity {

    private static String FLASHPICK_SERVER_ADDRESS = System.getenv("FLASHPICK_SERVER_ADDRESS");

    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;
    String userId = null;
    String jwtToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };

        LoginButton loginButton = (LoginButton) findViewById(R.id.loginButton1);
        List<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_friends");

        loginButton.setReadPermissions(permissions);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult result) {
                System.out.println("success");
                sendLoginRequest();
            }

            @Override
            public void onError(FacebookException error) {
                // TODO Auto-generated method stub
                System.out.println("error");
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                System.out.println("cancel");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void sendLoginRequest() {
        if(userId == null && AccessToken.getCurrentAccessToken() != null)
            userId = AccessToken.getCurrentAccessToken().getUserId();

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //TODO: sparametryzować adres. Tu trzeba podać adres ip maszyny, na której uruchomiony jest serwer
        String url = FLASHPICK_SERVER_ADDRESS + "flashpick-server-services-ws/mobileFBAuth?accessToken=" + AccessToken.getCurrentAccessToken().getToken();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                EditText editText = (EditText) findViewById(R.id.editText1);
                editText.setText("Response from the server: " + response.toString());
                try {
                    jwtToken = response.getString("token");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Button button = (Button) findViewById(R.id.button1);
                button.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EditText editText = (EditText) findViewById(R.id.editText1);
                editText.setText("Error.\n" + error.getMessage());
            }
        });
		/*
		{
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Authorization",  "Bearer " + "lol");
				return headers;
			}
		};
		*/

        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }

    public void sendGreetingRequest(View v) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = FLASHPICK_SERVER_ADDRESS + "flashpick-server-services-ws/greeting";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                EditText editText = (EditText) findViewById(R.id.editText1);
                editText.setText("Response from the server: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EditText editText = (EditText) findViewById(R.id.editText1);
                editText.setText("Error.\n" + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization",  "Bearer " + jwtToken);
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }
}