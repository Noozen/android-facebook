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
import co.flashpick.client.android.model.AuthenticationManager;
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

    public View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        setupGreetingButton();
        return view;
    }

    private void setupGreetingButton() {
        Button button = (Button) view.findViewById(R.id.buttonGreeting);
        final Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("name", "Android");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationManager.request(Request.Method.GET, "greeting", null, null, queryMap, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        TextView textView = (TextView) view.findViewById(R.id.textViewGreeting);
                        textView.setText(response.toString());
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Failed authorization at \"greeting\"!");
                    }
                });
            }
        });
    }
}