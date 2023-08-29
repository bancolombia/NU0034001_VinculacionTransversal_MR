package co.com.bancolombia.commonsvnt.usecase.util;

import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RequiredArgsConstructor
public class CoreFunctionDate {
    public static final String TIME_ZONE = "America/Bogota";
    public static final String DIFF_DATE_TIME = "dd' dias' HH' hrs' mm' mins' ss' segs'";
    public static final String DIFF_TIME = "HH' hrs' mm' mins' ss' segs'";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Getter
    private ZoneId zoneId = ZoneId.of(TIME_ZONE);


    public LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now(zoneId);
    }

    public LocalDate getLocalDateNow() {
        return LocalDate.now(zoneId);
    }

    public Date getDatetime() {
        LocalDateTime localDate = getLocalDateTimeNow();
        return Date.from(localDate.atZone(zoneId).toInstant());
    }

    public Date getDateFromString(String date, String pattern) {
        if (date == null){
            return null;
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        LocalDate dater = LocalDate.parse(date, format);
        return Date.from(dater.atStartOfDay(zoneId).toInstant());
    }

    public Date toDate(String strDate) {
        return this.toDate(this.getLocalDateTimeFromString(strDate));
    }

    public LocalDateTime getLocalDateTimeFromString(String localDateTime) {
        return LocalDateTime.parse(localDateTime, formatter);
    }

    /**
     * Transforms the given date in LocalDate.
     * @param date
     * @return LocalDate or null.
     */
    public LocalDate fromDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Transforms the given date in LocalDate.
     * @param date
     * @return LocalDate or null.
     */
    public LocalDateTime fromDateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public Date toDate(LocalDateTime dateToConvert) {
        return Timestamp.valueOf(dateToConvert);
    }

    public Date toDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(zoneId).toInstant());
    }

    public String compareDifferenceTime(Date initialTime, String maxTime, boolean plusHours, boolean withDiff){
        LocalDateTime now = getLocalDateTimeNow();
        String difference = null;

        LocalDateTime dateBlockToken = fromDateToLocalDateTime(initialTime);
        if (plusHours){
            dateBlockToken = dateBlockToken
                    .plusHours(Integer.parseInt(maxTime)/ Numbers.SIXTY.getIntNumber());
        }

        if (now.isBefore(dateBlockToken)==withDiff){
            if (withDiff){
                Duration between = Duration.between(now, dateBlockToken);
                String format = plusHours ? DIFF_TIME:DIFF_DATE_TIME;
                difference = DurationFormatUtils.formatDuration(between.toMillis(), format);
            }else{ difference = Constants.C_NOT_NULL; }
        }
        return difference;
    }

    public String minutesFormat(int minutes){
        LocalTime dateBlockToken = LocalTime.of(minutes/Numbers.SIXTY.getIntNumber(), 0, 0);
        return dateBlockToken.format(DateTimeFormatter.ofPattern(DIFF_TIME));
    }

    public String localDateTimePlusTime(int days, int hours, int minutes, int seconds){
        LocalDateTime localDateTime = getLocalDateTimeNow();
        localDateTime = localDateTime
                .plusDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);
        return localDateTime.format(DateTimeFormatter.ofPattern(DIFF_DATE_TIME));
    }

    public String toFormatDate(Date date){
        return simpleDateFormat.format(date);
    }

    public int returnAge(Date dateBirthday) {
        LocalDate currentDate = this.getLocalDateNow();
        return Period.between(this.fromDate(dateBirthday), currentDate).getYears();
    }

    public LocalDateTime dayUnLock(LocalDateTime now){
        return now
                .plusHours( (Constants.HOUR_PER_DAY-now.getHour()))
                .plusMinutes(Constants.MINUTE_PER_HOUR-now.getMinute())
                .plusSeconds(Constants.SECOND_PER_MINUTE-now.getSecond());
    }

    public LocalDateTime monthUnLock(LocalDateTime now){
        return now.plusMonths(1).minusDays(now.getDayOfMonth()-1L);
    }

    public LocalDateTime yearUnLock(LocalDateTime now){
        return now.plusYears(1).minusMonths(now.getMonthValue()-1L);
    }

    /**
     * Calculate time to unlock by code (DAY="1", MONTH="2",YEAR="3").
     * @param code
     * @return Date unlock.
     */
    public Date timeUnBlockCustomer(String code){
        LocalDateTime timeUnBlocK = null;
        LocalDateTime now = getLocalDateTimeNow();
        if(Constants.DAY.equals(code)){
            timeUnBlocK = dayUnLock(now);
        }else if(Constants.MONTH.equals(code)){
            timeUnBlocK = monthUnLock(dayUnLock(now));
        }else if(Constants.YEAR.equals(code)){
            timeUnBlocK = yearUnLock(monthUnLock(dayUnLock(now)));
        }
        return toDate(timeUnBlocK);
    }

    public Date expirationTimeMillis(int minutes){
        Date expiration = getDatetime();
        long expTimeMillis = expiration.getTime();
        expTimeMillis +=
                (long) Numbers.THOUSAND.getIntNumber() *
                        Numbers.SIXTY.getIntNumber() * minutes;
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
