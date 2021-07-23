package com.akarinti.preapproved.service;

import com.akarinti.preapproved.jpa.entity.Holiday;
import com.akarinti.preapproved.jpa.repository.HolidayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class HelperService {

    @Autowired
    HolidayRepository holidayRepository;

    private final int OFFICE_HOUR_START = 9;
    private final int OFFICE_HOUR_END = 17;
    private final int SERVICE_LEVEL = 120;

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate1.isEqual(localDate2);
    }

    public LocalDateTime getOfficeStartDate(Date date) {
        ZoneId zoneId = ZoneId.of("GMT+7");
        LocalDateTime today = date.toInstant()
                .atZone(zoneId).toLocalDateTime().withHour(OFFICE_HOUR_START)
                .withMinute(0)
                .withSecond(0);
        return today;
    }

    public LocalDateTime getOfficeEndDate(Date date) {
        ZoneId zoneId = ZoneId.of("GMT+7");
        LocalDateTime today = date.toInstant()
                .atZone(zoneId).toLocalDateTime().withHour(OFFICE_HOUR_END)
                .withMinute(0)
                .withSecond(0);
        return today;
    }

    public long getServiceLevelRemaining(Date applicationDate) {
        Date now = new Date();
        return getServiceLevelRemaining(applicationDate, now);
    }

    public long getServiceLevelRemaining(Date applicationDate, Date now) {
        ZoneId zoneId = ZoneId.of("GMT+7");
        LocalDateTime ldtApplication = LocalDateTime.ofInstant(applicationDate.toInstant(), zoneId);
        LocalDateTime ldtNow = LocalDateTime.ofInstant(now.toInstant(), zoneId);

        LocalDateTime ldtOfficeStartDate = getOfficeStartDate(now);
        LocalDateTime ldtOfficeEndDate = getOfficeEndDate(now);
        Date officeStartDate = Date.from(ldtOfficeStartDate.atZone(ZoneId.of("GMT+7")).toInstant());
        Date officeEndDate = Date.from(ldtOfficeEndDate.atZone(ZoneId.of("GMT+7")).toInstant());

        if (isSameDay(applicationDate, now)) {
            Holiday checkHoliday = holidayRepository.findByTanggal(now);
            if (checkHoliday != null) return SERVICE_LEVEL;
            // if application date submitted before working hour
            if (applicationDate.before(officeStartDate)) {
                LocalDateTime ldtSL = now.toInstant()
                        .atZone(zoneId).toLocalDateTime()
                        .withHour(OFFICE_HOUR_START)
                        .withMinute(0)
                        .withSecond(0)
                        .plusMinutes(SERVICE_LEVEL);
                Duration dur = Duration.between(ldtNow, ldtSL);
                long SLRemaining = dur.toMinutes();
                return SLRemaining;
            } else if (applicationDate.after(officeStartDate) && applicationDate.before(officeEndDate)) {
                // if application date submitted on working hour
                log.info("-------------------B-----------------");
//                LocalDateTime threshold = now.toInstant()
//                        .atZone(zoneId).toLocalDateTime()
//                        .withHour(OFFICE_HOUR_END)
//                        .withMinute(0)
//                        .withSecond(0)
//                        .minusMinutes(SERVICE_LEVEL);
//                Date thresholdDate = Date.from(threshold.atZone(zoneId).toInstant());
                LocalDateTime ldtSL = ldtApplication
                        .plusMinutes(SERVICE_LEVEL);
                log.info("ldtSL: "+ ldtSL);
                log.info("ldtNow: "+ ldtNow);
                Duration dur = Duration.between(ldtNow, ldtSL);
                long SLRemaining = dur.toMinutes();
                return SLRemaining;
            }
        } else {
            LocalDateTime ldtPreviousDay = applicationDate.toInstant()
                    .atZone(zoneId).toLocalDateTime()
                    .withHour(OFFICE_HOUR_END)
                    .withMinute(0)
                    .withSecond(0);
            Duration dur = Duration.between(ldtApplication, ldtPreviousDay);
            long SLRemaining = dur.toMinutes();
            if (SLRemaining > 0) {
                LocalDateTime threshold = applicationDate.toInstant()
                        .atZone(zoneId).toLocalDateTime()
                        .withHour(OFFICE_HOUR_END)
                        .withMinute(0)
                        .withSecond(0)
                        .minusMinutes(SERVICE_LEVEL);
                Date thresholdDate = Date.from(threshold.atZone(zoneId).toInstant());

                if (applicationDate.before(thresholdDate)) {
                    return 0;
                } else {
                    Date nextDay = getNextDay(applicationDate);

                    LocalDateTime ldtNextDay = nextDay.toInstant()
                            .atZone(zoneId).toLocalDateTime()
                            .withHour(OFFICE_HOUR_START)
                            .withMinute(0)
                            .withSecond(0);

                    LocalDateTime ldtSLEndDate = ldtNextDay.plusMinutes(SLRemaining);
                    if (now.before(nextDay)) {
                        dur = Duration.between(ldtNextDay, ldtSLEndDate);
                        SLRemaining = dur.toMinutes();
                    } else {
                        dur = Duration.between(ldtNow, ldtSLEndDate);
                        SLRemaining = dur.toMinutes();
                        if (SLRemaining < 0) return 0;
                    }
                    return SLRemaining;
                }
            } else {
                Date nextDay = getNextDay(applicationDate);

                LocalDateTime ldtNextDay = nextDay.toInstant()
                        .atZone(zoneId).toLocalDateTime()
                        .withHour(OFFICE_HOUR_START)
                        .withMinute(0)
                        .withSecond(0);

                LocalDateTime ldtSLEndDate = ldtNextDay.plusMinutes(SERVICE_LEVEL);
                if (now.before(nextDay)) {
                    dur = Duration.between(ldtNextDay, ldtSLEndDate);
                    SLRemaining = dur.toMinutes();
                } else {
                    dur = Duration.between(ldtNow, ldtSLEndDate);
                    SLRemaining = dur.toMinutes();
                    if (SLRemaining < 0) return 0;
                }
                return SLRemaining;
            }
        }
        return 0;
    }

    public Date getNextDay(Date currentDate) {
        ZoneId zoneId = ZoneId.of("GMT+7");
        LocalDateTime ldtNextDay = currentDate.toInstant().atZone(zoneId).toLocalDateTime().withHour(OFFICE_HOUR_START).withMinute(0).withSecond(0).plusDays(1);
        Date nextDay = Date.from(ldtNextDay.atZone(zoneId).toInstant());
        Holiday holiday = holidayRepository.findByTanggal(nextDay);
        if (holiday == null) return nextDay;
        else {
            return getNextDay(nextDay);
        }
    }

    public int getWorkingDays(Date startDate, Date endDate) {
        List<Holiday> holidays = holidayRepository.findAllByTanggalBetween(startDate, endDate);
        List<String> holidayString = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for(Holiday h: holidays)
        {
            String libur = sdf.format(h.getTanggal());
            holidayString.add(libur);
        }
        Calendar startCal;
        Calendar endCal;
        startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int workDays = 0;

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        //reverse value if start date after end date
        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
                    && startCal.getTimeInMillis() <= endCal.getTimeInMillis()
                    && !holidayString.contains(sdf.format(startCal.getTime()))) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());

        return workDays;
    }
    public List<Date> getWorkingDaysList(Date startDate, Date endDate) {
        return getWorkingDaysList(startDate, endDate, true);
    }

    public List<Date> getWorkingDaysList(Date startDate, Date endDate, boolean includeSaturday) {
        List<Holiday> holidays = holidayRepository.findAllByTanggalBetween(startDate, endDate);
        List<String> holidayString = new ArrayList<>();
        List<Date> workingDays = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for(Holiday h: holidays)
        {
            String libur = sdf.format(h.getTanggal());
            holidayString.add(libur);
        }
        Calendar startCal;
        Calendar endCal;
        startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return workingDays;
        }

        //reverse value if start date after end date
        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && startCal.getTimeInMillis() <= endCal.getTimeInMillis() && !holidayString.contains(sdf.format(startCal.getTime()))) {
                if (!includeSaturday && startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) continue;

                LocalDate localDate = startCal.getTime().toInstant().atZone(ZoneId.of("GMT+7")).toLocalDate();
                Date date = Date.from(localDate.atStartOfDay(ZoneId.of("GMT+7")).toInstant());
                workingDays.add(date);
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());

        return workingDays;
    }

}
