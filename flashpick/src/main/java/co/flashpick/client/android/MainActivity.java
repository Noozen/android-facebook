package co.flashpick.client.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import co.flashpick.client.android.fragments.FacebookLoginFragment;
import co.flashpick.client.android.fragments.MainFragment;
import co.flashpick.client.android.model.AuthenticationManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFacebook();
        setContentView(R.layout.activity_main);
        setupMainMenuListeners();
    }

    private void setupFacebook() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        AuthenticationManager.facebookCallbackManager = CallbackManager.Factory.create();
        AuthenticationManager.facebookAccessToken = AccessToken.getCurrentAccessToken();

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