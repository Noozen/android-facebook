package co.flashpick.client.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import co.flashpick.client.android.R;
import co.flashpick.client.android.callbacks.FacebookLoginCallback;
import co.flashpick.client.android.callbacks.FlashpickLoginCallback;
import co.flashpick.client.android.model.AuthenticationManager;
import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miko on 2015-12-05.
 */

public class FacebookLoginFragment extends Fragment {

    public View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(co.flashpick.client.android.R.layout.facebook_login_fragment, container, false);

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.loginButton);
        List<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_friends");

        loginButton.setReadPermissions(permissions);

        loginButton.registerCallback(AuthenticationManager.facebookCallbackManager, new FacebookLoginCallback());

        setupDebugButton();
        return view;
    }

    private void setupDebugButton() {
        Button button = (Button) view.findViewById(R.id.buttonJWToken);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationManager.authenticateThroughFacebook(new FlashpickLoginCallback() {
                    @Override
                    public void callback() {
                        TextView textView = (TextView) view.findViewById(R.id.textViewJWToken);
                        textView.setText(AuthenticationManager.jwtToken);
                    }
                });
            }
        });
    }
}