package com.example.alexander.afgangsprojekt_ucn.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alexander.afgangsprojekt_ucn.Models.Cause;
import com.example.alexander.afgangsprojekt_ucn.Models.NGO;
import com.example.alexander.afgangsprojekt_ucn.Models.Steps;
import com.google.gson.Gson;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;



import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class UtilityMethods
{

    // Converts a url to a bitmap
    public static Bitmap ConvertUrlToBitmap(String url)
    {
        try
        {
            URL newUrl = new URL(url);
            return BitmapFactory.decodeStream(newUrl.openConnection().getInputStream());
        }
        catch(Exception f)
        {
            f.printStackTrace();
        }

        return null;
    }

    // Gets the amount of supporters for the cause
    public static int GetSupportersCountForCause(Cause cause)
    {

        ParseObject compareCause = ParseObject.createWithoutData("Cause", cause.getObjectId());

        try
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("PublicUserProfile");
            query.whereEqualTo("cause",
                               compareCause);
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.setMaxCacheAge(TimeUnit.HOURS.toMillis(1 ));
            return query.find().size();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    // Gets the total amount of money donated to the selected cause
    public static Number GetDonatedMoneyForCause(Cause cause)
    {
        ParseObject compareCause = ParseObject.createWithoutData("Cause", cause.getObjectId());
        double donatedAmount = 0;
        try
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Donation");
            query.whereEqualTo("cause", compareCause);
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.setMaxCacheAge(TimeUnit.HOURS.toMillis(1));
            List<ParseObject> donationList = query.find();


            for (ParseObject item : donationList)
            {
                Number temp = item.getNumber("donationAmount");
                donatedAmount += temp.doubleValue();
            }

            return donatedAmount;

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return donatedAmount;
    }
    // Gets the current users donation for a selected cause
    public static List<ParseObject> GetCurrentUserDonationsForCause(String causeObjectId)
    {
        ParseObject cause = ParseObject.createWithoutData("Cause",
                                                          causeObjectId);
        try
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Donation");
            query.whereEqualTo("cause",
                    cause);
            query.whereEqualTo("user",
                    ParseUser.getCurrentUser());
            List<ParseObject> donationList = query.find();

            return donationList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    // Gets the total amount of steps for a selected cause
    public static Number GetTotalStepsForCause(String causeObjectId)
    {
        ParseObject cause = ParseObject.createWithoutData("Cause", causeObjectId);
        double steps = 0;
        try
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Donation");
            query.whereEqualTo("cause", cause);

            List<ParseObject> donationList = query.find();

            for (ParseObject item : donationList)
            {
                steps += item.getNumber("stepAmount").doubleValue();
            }

            return steps;
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        return steps;
    }

    public static Number GetTotalDonationsForNgo(String ngoObjectId)
    {
        double donatedAmount = 0;
        ParseObject currentNgo = ParseObject.createWithoutData("NGO", ngoObjectId);

        try
        {
            
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Donation");
            query.whereEqualTo("ngo", currentNgo);
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.setMaxCacheAge(TimeUnit.HOURS.toMillis(1));
            List<ParseObject> donationList = query.find();

            for (ParseObject item : donationList)
            {
                donatedAmount += item.getNumber("donationAmount").doubleValue();
            }

            return donatedAmount;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return donatedAmount;
    }

    public static Number GetTotalDonationsForCurrentUser(){
        double donatedAmount = 0;
        ParseObject currentUser = ParseUser.getCurrentUser();

        try
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Donation");
            query.whereEqualTo("user", currentUser);
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.setMaxCacheAge(TimeUnit.HOURS.toMillis(1));
            List<ParseObject> donationList = query.find();

            for (ParseObject item : donationList)
            {
                donatedAmount += item.getNumber("donationAmount").doubleValue();
            }
            return donatedAmount;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return donatedAmount;
    }
    public static Number MoneyPerStep (double steps){
        ParseObject currentUser = ParseUser.getCurrentUser();
        double moneyPerStep = 0;
        try
        {
            ParseQuery<ParseObject> PublicUserTable = ParseQuery.getQuery("PublicUserProfile");
            PublicUserTable.whereEqualTo("nonPublicUser", currentUser);
            ParseObject pubUser = PublicUserTable.getFirst();
            ParseObject companyTeam = pubUser.getParseObject("companyTeam").fetch();
            ParseObject company = companyTeam.getParseObject("company").fetch();
            double maxSteps = company.getNumber("maxSteps").intValue();
            double maxDonation = company.getNumber("maxDonation").intValue();
            moneyPerStep = (maxDonation / maxSteps) * steps;
            return moneyPerStep;

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return moneyPerStep;
    }


    public static Object ConvertJsonToObject(){
        Gson gson = new Gson();


        try
        {
            SimpleDateFormat isoFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserActivity");
            Calendar cal = Calendar.getInstance();
            Date now = new Date();
            cal.setTime(now);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            String dateString = isoFormat.format(cal.getTime());

            Date date = isoFormat.parse(dateString);

            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.whereGreaterThanOrEqualTo("activityDate", date);
            ParseObject usrActivity = query.getFirst();

            Object stepsObj = usrActivity.get("steps");


            System.out.println("Test");

            return stepsObj;
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        catch (java.text.ParseException f) {
            f.printStackTrace();
        }


        return 0;
    }

}
