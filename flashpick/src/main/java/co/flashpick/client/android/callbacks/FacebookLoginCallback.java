package co.flashpick.client.android.callbacks;

import co.flashpick.client.android.model.AuthenticationManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

/**
 * Created by Miko on 2015-12-05.
 */
public class FacebookLoginCallback implements FacebookCallback<LoginResult> {
    @Override
    public void onSuccess(LoginResult result) {
        System.out.println("success");
        AuthenticationManager.authenticateThroughFacebook();
    }

    @Override
    public void onError(FacebookException error) {
        System.out.println("error");
    }

    @Override
    public void onCancel() {
        System.out.println("cancel");
    }
}
