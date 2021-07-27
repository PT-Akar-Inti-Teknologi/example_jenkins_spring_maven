package com.akarinti.preapproved.jpa.predicate;

import com.akarinti.preapproved.jpa.entity.QAplikasi;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class AplikasiPredicate {

    public static Predicate aplikasiWithSearch(String search) {
        QAplikasi aplikasi = QAplikasi.aplikasi;
        BooleanBuilder builder = new BooleanBuilder();
        if (search != null && !search.equalsIgnoreCase("")) {
            builder.or(aplikasi.namaLengkap.likeIgnoreCase("%" + search + "%"));

            Locale localeId = new Locale("id", "ID");

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy",localeId);
                LocalDate date = LocalDate.parse(search,formatter);
                builder.or(aplikasi.creationDate.between(date.atStartOfDay(), date.plusDays(1).atStartOfDay()));

            } catch (DateTimeParseException ignored) { }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm",localeId);
                LocalDateTime dateTime = LocalDateTime.parse(search,formatter);
                builder.or(aplikasi.creationDate.eq(dateTime));

            } catch (DateTimeParseException ignored) { }

        }
        return builder;
    }

    public static Predicate aplikasiWithStatus(String status) {
        QAplikasi aplikasi = QAplikasi.aplikasi;
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null && !status.equalsIgnoreCase("")) {
            builder.or(aplikasi.status.equalsIgnoreCase(status));
        }
        return builder;
    }
}
