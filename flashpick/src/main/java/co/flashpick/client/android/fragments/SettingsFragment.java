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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Miko on 2015-12-05.
 */

public class SettingsFragment extends Fragment {

    final private String TAG = "MainFragment";
    final private SettingsFragment thisFragment = this;

    public View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_fragment, container, false);
        return view;
    }
}