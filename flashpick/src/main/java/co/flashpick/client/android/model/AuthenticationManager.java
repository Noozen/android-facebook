package co.flashpick.client.android.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Miko on 2015-12-05.
 */
public class AuthenticationManager {

    public static String FLASHPICK_SERVLET_ADDRESS = "http://192.168.0.10:8080/flashpick-server-services-ws/";

    public static Activity activityContext;
    public static String facebookUserId;
    public static String jwtToken;
    public static CallbackManager facebookCallbackManager;
    public static RestTemplate restTemplate = new RestTemplate();

    private static String responseHelper;

    public static String getFacebookAuthUrlWithParam() {
        return FLASHPICK_SERVLET_ADDRESS + "mobileFBAuth?accessToken=" + AccessToken.getCurrentAccessToken().getToken();
    }

    public static String authenticateThroughFacebook() {
        if(AuthenticationManager.facebookUserId == null && AccessToken.getCurrentAccessToken() != null) {
            AuthenticationManager.facebookUserId = AccessToken.getCurrentAccessToken().getUserId();
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResponseEntity<JWToken> answer = restTemplate.getForEntity(getFacebookAuthUrlWithParam(), JWToken.class);
                jwtToken = answer.getBody().getToken();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("FacebookAuthentication","Server response was not successfull!");
        }
        SharedPreferences sharedPref = activityContext.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("FLASHPICK_TOKEN", jwtToken);
        editor.commit();

        return jwtToken;
    }

    public static String greeting() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + jwtToken);
                HttpEntity entity = new HttpEntity(headers);

                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(FLASHPICK_SERVLET_ADDRESS + "greeting")
                        .queryParam("name", "Android");

                ResponseEntity<String> responseEntity = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
                responseHelper = responseEntity.getBody();
                if(responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    authenticateThroughFacebook();
                    responseEntity = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
                    responseHelper = responseEntity.getBody();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Greeting Request","Server response was not successfull!");
        }
        return responseHelper;
    }
}
