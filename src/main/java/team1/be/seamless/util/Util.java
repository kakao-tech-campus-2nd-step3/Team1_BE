package team1.be.seamless.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    public Util() {
    }

    public static boolean isNull(String str) {
        return str == null || str.isBlank();
    }

    public static LocalDateTime parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        return dateTime;
    }
}
