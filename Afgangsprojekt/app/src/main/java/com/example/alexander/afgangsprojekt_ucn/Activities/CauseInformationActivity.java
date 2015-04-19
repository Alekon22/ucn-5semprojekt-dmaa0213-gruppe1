package com.example.alexander.afgangsprojekt_ucn.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maaaarckyo.donagoandroid.Models.Cause;
import com.example.maaaarckyo.donagoandroid.R;
import com.example.maaaarckyo.donagoandroid.Utility.UtilityMethods;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.FileInputStream;
import java.util.List;


public class CauseInformationActivity extends Activity
{
    TextView causeDescription;
    TextView donatedMoney;
    TextView yourContribution;
    TextView yourSteps;
    TextView otherContribution;
    TextView otherSteps;

    ImageView causeHeader;

    Button supportCauseBtn;

    Cause clickedCause;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cause_information);


        clickedCause = getIntent().getParcelableExtra("causeObject");

        causeDescription = (TextView)findViewById(R.id.causeDescription);
        donatedMoney = (TextView)findViewById(R.id.donatedMoney);
        yourContribution = (TextView)findViewById(R.id.twContributedMoney);
        yourSteps = (TextView)findViewById(R.id.twContributedSteps);
        otherSteps = (TextView)findViewById(R.id.twOtherContributedSteps);
        otherContribution = (TextView)findViewById(R.id.twOtherContributions);

        causeHeader = (ImageView)findViewById(R.id.causeImage);

        supportCauseBtn = (Button)findViewById(R.id.supportCauseBtn);

        causeDescription.setText(clickedCause.getDescription());
        donatedMoney.setText(clickedCause.getDonatedAmount().toString() + " DKK");

        List<ParseObject> donationList = UtilityMethods.GetCurrentUserDonationsForCause(clickedCause.getObjectId());

        double donatedAmount = 0;
        int steps = 0;

        for (ParseObject item : donationList)
        {

            donatedAmount += item.getNumber("donationAmount").doubleValue();
            steps += item.getNumber("stepAmount").intValue();
        }

        double totalSteps = UtilityMethods.GetTotalStepsForCause(clickedCause.getObjectId()).doubleValue();

        yourContribution.setText(String.valueOf(donatedAmount) + " DKK");
        yourSteps.setText(String.valueOf(steps) + " skridt");
        otherContribution.setText(String.valueOf(clickedCause.getDonatedAmount().doubleValue() - donatedAmount) + " DKK");
        otherSteps.setText(String.valueOf(totalSteps - steps) + " skridt");

        final ParseObject pubUsr = (ParseObject)ParseUser.getCurrentUser().get("publicUserProfile");

        checkCauseSupported(pubUsr);

        supportCauseBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ParseObject parseCause = ParseObject.createWithoutData("Cause", clickedCause.getObjectId());

                pubUsr.put("currentCause", parseCause);
                pubUsr.saveInBackground(new SaveCallback()
                {
                    @Override
                    public void done(ParseException e)
                    {
                        Toast.makeText(CauseInformationActivity.this,
                                       "Du støtter nu sagen!",
                                       Toast.LENGTH_SHORT).show();
                        supportCauseBtn.setEnabled(false);
                        supportCauseBtn.setBackgroundColor(Color.GRAY);
                    }
                });
            }
        });

        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try
        {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
            causeHeader.setImageBitmap(bmp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setCurrentTabByTag("aboutTab");
        tabHost.setup();

        TabHost.TabSpec aboutTab = tabHost.newTabSpec("aboutTab");
        aboutTab.setIndicator("Om");
        aboutTab.setContent(R.id.aboutTab);
        tabHost.addTab(aboutTab);

        TabHost.TabSpec donationTab = tabHost.newTabSpec("donationsTab");
        donationTab.setIndicator("Donationer");
        donationTab.setContent(R.id.donationsTab);
        tabHost.addTab(donationTab);

        tabHost.setCurrentTab(0);



    }

    private void checkCauseSupported(ParseObject pubUsr)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PublicUserProfile");
        query.getInBackground(pubUsr.getObjectId(), new GetCallback<ParseObject>()
        {
            @Override
            public void done(ParseObject parseObject, ParseException e)
            {
                ParseObject parseCause2 = parseObject.getParseObject("currentCause");
                try
                {
                    parseCause2.fetchIfNeeded();
                }
                catch(ParseException g)
                {
                    g.printStackTrace();
                }

                if (parseCause2.getObjectId().equals(clickedCause.getObjectId()))
                {
                    supportCauseBtn.setEnabled(false);
                    supportCauseBtn.setBackgroundColor(Color.GRAY);
                }
                else
                {
                    supportCauseBtn.setEnabled(true);
                }
            }
        });
    }


}
