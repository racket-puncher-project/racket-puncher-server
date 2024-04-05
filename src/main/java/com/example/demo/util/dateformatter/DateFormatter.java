package com.example.demo.util.dateformatter;

import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static final DateTimeFormatter formForDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter formForChatSentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter formForDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter formForTime = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter formForWeather = DateTimeFormatter.ofPattern("yyyyMMdd");
}
