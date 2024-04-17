package pl.coderslab.CarServiceApp.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coderslab.CarServiceApp.entities.ScheduledMaintenance;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CalendarService {

    public void createCalendarEvent(String accessToken, ScheduledMaintenance maintenance) throws IOException {
        Calendar service = buildCalendarService(accessToken);

        Event event = new Event()
                .setSummary("Scheduled Maintenance")
                .setDescription("Maintenance details for " + maintenance.getCar().getBrand() + " " + maintenance.getCar().getModel());

        LocalDateTime startDateTime = LocalDateTime.of(maintenance.getDate(), maintenance.getTime());
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Including 'Z' to indicate UTC
        String startString = startDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).format(dateTimeFormatter);
        String endString = endDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).format(dateTimeFormatter);

        EventDateTime start = new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(startString)).setTimeZone("UTC");
        EventDateTime end = new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(endString)).setTimeZone("UTC");
        event.setStart(start);
        event.setEnd(end);

        service.events().insert("primary", event).execute();
    }

    private Calendar buildCalendarService(String accessToken) throws IOException {
        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
            return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential)
                    .setApplicationName("Google Calendar API Java Quickstart")
                    .build();
        } catch (GeneralSecurityException e) {
            throw new IOException("Failed to create trusted transport", e);
        }
    }
}


