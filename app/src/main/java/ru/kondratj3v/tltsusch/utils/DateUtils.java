package ru.kondratj3v.tltsusch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
        }
        return 0;
    }

    public static String getTabName(int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        SimpleDateFormat sdf = new SimpleDateFormat("EE\ndd.MM", Locale.getDefault());
        cal.add(Calendar.DAY_OF_WEEK, day - 1);
        return sdf.format(cal.getTime());
    }

    public static int getCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());

        try {
            Date date1 = simpleDateFormat.parse("1/9/2016");
            Date date2 = simpleDateFormat.parse(simpleDateFormat.format(cal.getTime()));

            if (date1 != null & date2 != null) {
                long different = date2.getTime() - date1.getTime();
                return (int) Math.ceil(TimeUnit.MILLISECONDS.toDays(different) / 7 + 2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}