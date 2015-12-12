package co.flashpick.client.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import co.flashpick.client.android.R;
import co.flashpick.client.android.model.AndroidHelper;
import co.flashpick.client.android.model.UserData;

/**
 * Created by Miko on 2015-12-05.
 */

public class SettingsFragment extends Fragment {

    final private String TAG = "MainFragment";
    final private SettingsFragment thisFragment = this;

    public View view;

    private Spinner spinner;

    //Hack to avoid calling spinner listener on creation
    private int listenerPrepared = -1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_fragment, container, false);
        loadDataSetAdapter();
        setRememberedLanguageSelection();
        setOnLanguageSelectedListener();
        return view;
    }

    private void setOnLanguageSelectedListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(listenerPrepared >= 0) {
                    String language = (String) parentView.getItemAtPosition(position);
                    switch(language) {
                        case "English":
                            AndroidHelper.changeLanguage("en", thisFragment);
                            break;
                        case "Polski":
                            AndroidHelper.changeLanguage("pl", thisFragment);
                            break;
                        case "Italiano":
                            AndroidHelper.changeLanguage("it", thisFragment);
                            break;
                    }
                }
                listenerPrepared++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    private void setRememberedLanguageSelection() {
        switch(UserData.language) {
            case "en":
                spinner.setSelection(0);
                break;
            case "pl":
                spinner.setSelection(1);
                break;
            case "it":
                spinner.setSelection(2);
                break;
        }
    }

    private void loadDataSetAdapter() {
        spinner = (Spinner) view.findViewById(R.id.languageSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AndroidHelper.activityContext, R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
}