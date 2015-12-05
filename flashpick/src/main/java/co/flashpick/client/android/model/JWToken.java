package co.flashpick.client.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Miko on 2015-12-04.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class JWToken {

    public JWToken(){};
    public JWToken(String accessToken) {
        this.token = token;
    }


    public void setAccessToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String token;
}
