package it.smasini.utility.library.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import it.smasini.utility.library.OtherUtility;

/**
 * Created by Simone on 20/09/16.
 */
public class PortraitPhoneLandscapeTabletActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        OtherUtility.setOrientationPortraitPhoneLandscapeTablet(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OtherUtility.setOrientationPortraitPhoneLandscapeTablet(this);
    }
}
