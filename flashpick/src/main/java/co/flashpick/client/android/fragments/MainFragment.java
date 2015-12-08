package co.flashpick.client.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import co.flashpick.client.android.R;
import co.flashpick.client.android.model.AuthenticationManager;

/**
 * Created by Miko on 2015-12-05.
 */

public class MainFragment extends Fragment {

    public View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        setupGreetingButton();
        return view;
    }

    private void setupGreetingButton() {
        Button button = (Button) view.findViewById(R.id.buttonGreeting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) view.findViewById(R.id.textViewGreeting);
                textView.setText(AuthenticationManager.greeting());
            }
        });
    }
}