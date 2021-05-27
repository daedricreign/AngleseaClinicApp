package com.angleseahospital.nurse.firestore;

import java.util.Calendar;

public class Util {

    public static String getTodayLogPath() {
        return "/logs/" + getToday();
    }

    public static String getToday() {
        Calendar cal = Calendar.getInstance();

        String today = cal.get(Calendar.YEAR) + "-";

        String monOfYear = cal.get(Calendar.MONTH) + "";
        today += (monOfYear.length() == 1 ? "0" + monOfYear : monOfYear) + "-";

        String dayOfMonth = cal.get(Calendar.DAY_OF_MONTH) + "";
        return today + (dayOfMonth.length() == 1 ? "0" + dayOfMonth : dayOfMonth);
    }
}