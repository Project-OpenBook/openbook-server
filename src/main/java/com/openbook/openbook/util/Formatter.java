package com.openbook.openbook.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {

    public static String getFormattingDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateTime.format(formatter);
    }

    public static String getFormattingTime(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        return dateTime.format(formatter);
    }

    public static LocalDateTime getDateTime(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

}
