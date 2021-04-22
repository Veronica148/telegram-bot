package com.app.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class TimeStampHelper {

    public static final String START_DATE = "1900-01-01 00:00:00";
    public static final String END_DATE = "3000-12-31 23:59:59";

    private final Timestamp startDate;
    private final Timestamp endDate;

    public static TimeStampHelper getTimeStampHelperFromIntervals(String startDate, String endDate) throws NumberFormatException{
        try {
            Timestamp localStartDate = Timestamp.valueOf(START_DATE);
            Timestamp localEndDate = Timestamp.valueOf(END_DATE);

            if (startDate != null && endDate != null) {
                localStartDate = Timestamp.valueOf(startDate);
                localEndDate = Timestamp.valueOf(endDate);
            } else if (endDate != null) {
                localEndDate = Timestamp.valueOf(endDate);
            } else if (startDate != null) {
                localStartDate = Timestamp.valueOf(startDate);
            }
            return new TimeStampHelper(localStartDate, localEndDate);
        } catch (Exception ex) {
            throw new NumberFormatException();
        }
    }

}
