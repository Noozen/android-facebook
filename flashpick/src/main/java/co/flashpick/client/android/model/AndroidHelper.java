package co.flashpick.client.android.model;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by Miko on 2015-12-10.
 */
public class AndroidHelper {
    public static Locale locale;
    public static Activity activityContext;

    public static void changeLanguage(String lang, Fragment fragment) {
        Configuration config = activityContext.getResources().getConfiguration();

        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
        {
            AndroidHelper.locale = new Locale(lang);
            Locale.setDefault(AndroidHelper.locale);
            config.locale = AndroidHelper.locale;
            activityContext.getResources().updateConfiguration(config, activityContext.getResources().getDisplayMetrics());

            if(fragment!=null) {
                FragmentTransaction fragTransaction = activityContext.getFragmentManager().beginTransaction();
                fragTransaction.detach(fragment);
                fragTransaction.attach(fragment);
                fragTransaction.commit();
            }
        }

        UserSettings.language = lang;
    }
}
