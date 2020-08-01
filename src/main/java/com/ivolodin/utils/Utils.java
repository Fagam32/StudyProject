package com.ivolodin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Utils {
    public static LocalDateTime createDateTimeFromString(String strTime) {
        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        try {
            return LocalDateTime.ofInstant(sdt.parse(strTime).toInstant(), ZoneId.systemDefault());
        } catch (ParseException e) {
            return null;
        }
    }

    public static LocalDate createDateFromString(String birthDateString) {
        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return LocalDate.ofInstant(sdt.parse(birthDateString).toInstant(), ZoneId.systemDefault());
        } catch (ParseException e) {
            return null;
        }
    }
}
