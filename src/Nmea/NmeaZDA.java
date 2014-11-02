package Nmea;

import java.sql.Timestamp;

public class NmeaZDA extends NmeaMessage {

    public Integer _xx, _yy;

    public NmeaZDA(String line) {
        super(line);

        String[] time = words[1].split("\\.");
        int hour = Tools.IntegerValue(time[0].substring(0, 2));
        int minute = Tools.IntegerValue(time[0].substring(2, 4));
        int second = Tools.IntegerValue(time[0].substring(4, 6));
        int ms = Tools.IntegerValue(time[1]);

        int day = Tools.IntegerValue(words[2]);
        int month = Tools.IntegerValue(words[3]);
        int year = Tools.IntegerValue(words[4]);
        _xx = Tools.IntegerValue(words[5]);
        _yy = Tools.IntegerValue(words[6]);
        _time = new Timestamp(year - 1900, month, day, hour, minute, second,
                ms * 1000);

    }
}
