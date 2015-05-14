package com.example.alexander.afgangsprojekt_ucn.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.alexander.afgangsprojekt_ucn.Models.Cause;
import com.example.alexander.afgangsprojekt_ucn.Models.NGO;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.URL;
import java.util.List;
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

}
