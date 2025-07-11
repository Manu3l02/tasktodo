package com.example.tasktodo.service;

import com.example.tasktodo.repository.CalendarItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Service
public class ReminderService {

    private final CalendarItemRepository calendarRepo;
    private final EntityManager em;
    private final EmailNotifier emailNotifier;

    public ReminderService(CalendarItemRepository calendarRepo,
                           EntityManager em,
                           EmailNotifier emailNotifier) {
        this.calendarRepo = calendarRepo;
        this.em = em;
        this.emailNotifier = emailNotifier;
    }

    /** Eseguito ogni minuto al secondo 0 */
    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        List<Object[]> rawItems = calendarRepo.findReminderEligibleItemsRaw();
        rawItems.forEach(row -> processRow(row, now));
    }

    private void processRow(Object[] row, LocalDateTime now) {
        Long id = ((Number) row[0]).longValue();
        String title = (String) row[1];
        Integer minutesBefore = row[2] != null ? ((Number) row[2]).intValue() : null;
        Timestamp dueTs = row[4] instanceof Timestamp ? (Timestamp) row[4] : null;
        Timestamp startTs = row[5] instanceof Timestamp ? (Timestamp) row[5] : null;
        String itemType = (String) row[7];

        LocalDateTime targetDateTime =
                "TASK".equals(itemType)  ? (dueTs   != null ? dueTs.toLocalDateTime()   : null) :
                "EVENT".equals(itemType) ? (startTs != null ? startTs.toLocalDateTime() : null) : null;

        if (targetDateTime == null || minutesBefore == null) return;

        if (now.equals(targetDateTime.minusMinutes(minutesBefore))) {
            // 1) Recupera l’e‑mail dell’utente proprietario
            String email = (String) em.createNativeQuery("""
                    SELECT u.email FROM user u
                    JOIN calendar_item ci ON ci.user_id = u.user_id
                    WHERE ci.id = :id
                    """)
                    .setParameter("id", id)
                    .getSingleResult();

            // 2) Invia la mail
            boolean sent = emailNotifier.sendReminder(
                    email,
                    "Promemoria • " + title,
                    buildHtmlBody(title, targetDateTime, itemType));

            // 3) Marca come inviato se tutto OK
            if (sent) {
                em.createNativeQuery("UPDATE calendar_item SET reminder_sent = true WHERE id = :id")
                  .setParameter("id", id)
                  .executeUpdate();
            }
        }
    }

    private String buildHtmlBody(String title, LocalDateTime dateTime, String type) {
        String formattedDate = formatFancyDate(dateTime);

        return """
        <div style="font-family: 'Segoe UI', sans-serif; padding: 20px; color: #333;">
          <h2 style="color: #4CAF50;">📌 Promemoria %s</h2>
          <p style="font-size: 16px;">Ciao,</p>
          <p style="font-size: 16px;">
            Questo è un promemoria per il tuo %s: <strong>%s</strong>.
          </p>
          <p style="font-size: 16px;">📅 Data/ora prevista: <strong>%s</strong></p>
          <hr style="margin-top: 20px;">
          <p style="font-size: 14px; color: #888;">
            TaskTodo • Promemoria automatico — non rispondere a questa email.
          </p>
        </div>
        """.formatted(type, type.toLowerCase(), title, formattedDate);
    }
    
    private String formatFancyDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy 'alle' HH:mm", Locale.ITALY);
        return dateTime.format(formatter);
    }

 // Se in futuro volessi adattare il formato alla lingua dell’utente registrato, 
//    private String formatLocalizedDate(LocalDateTime dateTime, Locale locale) {
//        DateTimeFormatter formatter = DateTimeFormatter
//                .ofLocalizedDateTime(FormatStyle.SHORT)
//                .withLocale(locale);
//
//        return dateTime.format(formatter);
//    }
    
    
    // potrei salvare la preferenza nello User model:
    // Local locale = Locale.forLanguageTag(user.getLocale());  es: "it", "en-US"



}