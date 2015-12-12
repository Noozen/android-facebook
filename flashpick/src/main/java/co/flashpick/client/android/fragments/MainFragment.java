package co.flashpick.client.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import co.flashpick.client.android.R;
import co.flashpick.client.android.model.AndroidHelper;
import co.flashpick.client.android.model.AuthenticationManager;
import co.flashpick.client.android.model.UserData;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Miko on 2015-12-05.
 */

public class MainFragment extends Fragment {

    final private String TAG = "MainFragment";
    final private MainFragment thisFragment = this;

    public View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        getEmailAddress();
        setupLocaleButtons();
        return view;
    }

    private void setupLocaleButtons() {
        Button button = (Button) view.findViewById(R.id.localeEN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.changeLanguage("en", thisFragment);
            }
        });

        Button button2 = (Button) view.findViewById(R.id.localePL);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.changeLanguage("pl", thisFragment);
            }
        });
    }

    private void getEmailAddress() {
        Button button = (Button) view.findViewById(R.id.buttonGreeting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationManager.request(Request.Method.GET, "profiles/" + UserData.userId, null, null, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        TextView textView = (TextView) view.findViewById(R.id.textViewGreeting);
                        try {
                            textView.setText(response.getJSONObject("user").getString("email"));
                        } catch (JSONException e) {
                            Log.e(TAG,"Failed while parsing JSONObject response.", e);
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO:Badac zwracany status http
                        Log.e(TAG, "Failed request at profiles/{userid}",error);
                    }
                });
            }
        });
    }
}