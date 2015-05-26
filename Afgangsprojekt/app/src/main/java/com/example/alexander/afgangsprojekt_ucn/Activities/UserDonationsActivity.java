package com.example.alexander.afgangsprojekt_ucn.Activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.alexander.afgangsprojekt_ucn.R;
import com.example.alexander.afgangsprojekt_ucn.Utility.UtilityMethods;


public class UserDonationsActivity extends Activity {

    TextView twTotalDonatedAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_donations);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        twTotalDonatedAmount = (TextView)findViewById(R.id.totalDonatedMoney);

        twTotalDonatedAmount.setText(String.valueOf(UtilityMethods.GetTotalDonationsForCurrentUser()));

    }

}
