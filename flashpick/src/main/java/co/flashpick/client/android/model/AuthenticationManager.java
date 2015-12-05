package co.flashpick.client.android.model;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by Miko on 2015-12-05.
 */
public class AuthenticationManager {

    public static String FLASHPICK_SERVER_ADDRESS = "http://192.168.0.10:8080/";

    public static String facebookUserId;
    public static String jwtToken;
    public static CallbackManager facebookCallbackManager;
    public static AccessToken facebookAccessToken;
    public static RestTemplate restTemplate = new RestTemplate();

    private static String responseHelper;

    public static String getFacebookAuthUrlWithParam() {
        return FLASHPICK_SERVER_ADDRESS + "flashpick-server-services-ws/mobileFBAuth?accessToken=" + facebookAccessToken.getToken();
    }

    public static String authenticateThroughFacebook() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ResponseEntity<JWToken> answer = restTemplate.getForEntity(getFacebookAuthUrlWithParam(), JWToken.class, new HashMap<String, String>());
                jwtToken = answer.getBody().getToken();
            }
        }).start();
        return jwtToken;
    }

    public static String greeting() {
        final HashMap<String, String> requestHeaders = new HashMap<String, String>();

        requestHeaders.put("Authorization",  "Bearer " + jwtToken);
        requestHeaders.put("name", "Android");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(FLASHPICK_SERVER_ADDRESS + "flashpick-server-services-ws/greeting", String.class, requestHeaders);
                responseHelper = responseEntity.getBody();
            }
        }).start();

        return responseHelper;
    }
}
