package co.flashpick.client.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import co.flashpick.client.android.callbacks.FacebookLoginCallback;
import co.flashpick.client.android.fragments.FacebookLoginFragment;
import co.flashpick.client.android.fragments.MainFragment;
import co.flashpick.client.android.model.AuthenticationManager;
import com.android.volley.toolbox.Volley;
import com.facebook.*;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;

import java.util.Arrays;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAuthenticationManager();
        setupFacebook();

        setContentView(R.layout.activity_main);
        setupMainMenuListeners();

        authenticateUser();
    }

    private void setupAuthenticationManager() {
        AuthenticationManager.activityContext = this;
        AuthenticationManager.requestQueue = Volley.newRequestQueue(this);
    }

    private void authenticateUser() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        AuthenticationManager.jwtToken = sharedPref.getString("FLASHPICK_TOKEN","NULL");
        if(AccessToken.getCurrentAccessToken() != null) {
            AuthenticationManager.authenticateThroughFacebook();
        }
        if(AccessToken.getCurrentAccessToken() == null) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_friends"));
        }
    }

    private void setupFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        AuthenticationManager.facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(AuthenticationManager.facebookCallbackManager, new FacebookLoginCallback());
    }

    private void setupMainMenuListeners() {
        ImageButton mainFragmentButton = (ImageButton) findViewById(R.id.mainScreenButton);
        mainFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragment fragment = new MainFragment();
                getFragmentManager().beginTransaction().replace(R.id.mainLayout, fragment).commit();
            }
        });

        ImageButton facebookFragmentButton = (ImageButton) findViewById(R.id.facebookScreenButton);
        facebookFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookLoginFragment fragment = new FacebookLoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.mainLayout, fragment).commit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AuthenticationManager.facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
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
    }
}