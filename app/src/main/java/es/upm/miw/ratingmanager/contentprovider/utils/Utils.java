package es.upm.miw.ratingmanager.contentprovider.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.upm.miw.ratingmanager.contentprovider.provider.ApplicationProvider;

/**
 * Created by franlopez on 13/11/2016.
 */

public class Utils {
    private final static String FORMAT_FOR_DATES = "yyyy-MM-dd";

    public Utils() {
    }

    public String addMonthToADate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_FOR_DATES,
                Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(this.stringToDate(date));
        calendar.add(Calendar.MONTH, 1);

        return simpleDateFormat.format(calendar.getTime());
    }

    public Date stringToDate(String date) {
        Date generatedDate = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_FOR_DATES,
                Locale.getDefault());

        try {
            generatedDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            Log.i(ApplicationProvider.FILTER_FOR_LOGS, e.getMessage());
        }

        return generatedDate;
    }

    public Date timeStampToDate(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(timeStamp);
        return calendar.getTime();
    }
}
