package com.fitpass.libfitpass.base.utilities;

import static java.text.DateFormat.getDateInstance;

import android.util.Log;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FitpassHealthManager {
    /*public static DataReadRequest queryFitnessData() {
        //[START build_read_data_request]
        //Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        Calendar startCalendar = Calendar.getInstance(Locale.getDefault());
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        long startTime2 = startCalendar.getTimeInMillis();
        DateFormat dateFormat = getDateInstance();
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime2, endTime, TimeUnit.MILLISECONDS)
                .build();
        // [END build_read_data_request]
        return readRequest;
    }

    public static DataReadRequest queryFitnessCaloriesData() {
        //[START build_read_data_request]
        //Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();

        Calendar startCalendar = Calendar.getInstance(Locale.getDefault());
        startCalendar.setTime(now);
        startCalendar.set(Calendar.DATE,-6);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        long startTime2 = startCalendar.getTimeInMillis();
        DateFormat dateFormat = getDateInstance();

        SimpleDateFormat existingFomrat = new SimpleDateFormat("yyyy-MM-dd");
        Log.i("Range Start:",  existingFomrat.format(startTime2));
        Log.i("Range End:",  existingFomrat.format(endTime));
        Log.e("start time for request",startCalendar.get(Calendar.DAY_OF_MONTH) + ":" + startCalendar.get(Calendar.MONTH) + ":" + startCalendar.get(Calendar.YEAR));
        Log.e("endtime time for request",cal.get(Calendar.DAY_OF_MONTH) + ":" + cal.get(Calendar.MONTH) + ":" + cal.get(Calendar.YEAR));
        DataReadRequest readRequest = new DataReadRequest.Builder()
                //The data request can specify multiple data types to return, effectively
                //combining multiple data queries into one call.
                //In this example, it's very unlikely that the request is for several hundred
                //datapoints each consisting of a few steps and a timestamp.  The more likely
                //scenario is wanting to see how many steps were walked per day, for 7 days.
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                //Analogous to a "Group By" in SQL, defines how data should be aggregated.
                //bucketByTime allows for a time span, whereas bucketBySession would allow
                //bucketing by "sessions", which would need to be defined in code.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime2, endTime, TimeUnit.MILLISECONDS)
                .build();
        // [END build_read_data_request]
        return readRequest;
    }*/

}
