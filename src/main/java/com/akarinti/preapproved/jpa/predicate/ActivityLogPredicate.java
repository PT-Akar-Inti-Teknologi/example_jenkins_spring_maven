package com.akarinti.preapproved.jpa.predicate;

import com.akarinti.preapproved.jpa.entity.QActivityLog;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Slf4j
public class ActivityLogPredicate {

    public static Predicate activityLogWithSearch(String search, String dateCreated) {
        QActivityLog activityLog = QActivityLog.activityLog;
        BooleanBuilder builder = new BooleanBuilder();
        Locale localeId = new Locale("id", "ID");
        if (search != null && !search.equalsIgnoreCase("")) {
            builder.or(activityLog.aplikasi.namaLengkap.likeIgnoreCase("%" + search + "%"))
            .or(activityLog.aplikasi.email.likeIgnoreCase("%" + search + "%"))
            .or(activityLog.userBCA.fullName.likeIgnoreCase("%" + search + "%"));

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", localeId);
                LocalDate date = LocalDate.parse(search, formatter);
                builder.or(activityLog.creationDate.between(date.atStartOfDay(), date.plusDays(1).atStartOfDay()));

            } catch (DateTimeParseException ignored) { }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss", localeId);
                LocalDateTime dateTime = LocalDateTime.parse(search, formatter);
                builder.or(activityLog.creationDate.eq(dateTime));

            } catch (DateTimeParseException ignored) { }

        }
        if (dateCreated != null) {
            String[] strings = dateCreated.split(" - ");
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", localeId);

                String dateFrom = strings[0];
                LocalDate dateFromLD = LocalDate.parse(dateFrom, formatter);

                String dateTo = strings[1];
                LocalDate dateToLD = LocalDate.parse(dateTo, formatter);

                builder.or(activityLog.creationDate.between(dateFromLD.atStartOfDay(), dateToLD.plusDays(1).atStartOfDay()));

            } catch (DateTimeParseException ignored) { }
        }

        return builder;
    }
}
