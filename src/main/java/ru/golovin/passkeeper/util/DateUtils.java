package ru.golovin.passkeeper.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class DateUtils {

    public Date getExpirationDate(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        return calendar.getTime();
    }

    public LocalDateTime convertDateToLocalDateTime(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
