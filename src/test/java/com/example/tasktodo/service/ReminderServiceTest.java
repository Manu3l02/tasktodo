package com.example.tasktodo.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tasktodo.model.Task;
import com.example.tasktodo.repository.CalendarItemRepository;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

    private CalendarItemRepository calendarItemRepository;
    private ReminderService reminderService;

//    @BeforeEach
//    void setup() {
//        calendarItemRepository = mock(CalendarItemRepository.class);
//        reminderService = new ReminderService(calendarItemRepository);
//    }
//
//    @Test
//    void testReminderTriggeredAtCorrectTime() {
//        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
//        Task task = new Task();
//        task.setId(1L);
//        task.setTitle("Test task");
//        task.setDueDateTime(now.plusMinutes(5));
//        task.setReminderMinutesBefore(5);
//        task.setReminderSent(false);
//
//        when(calendarItemRepository.findPendingReminderItems()).thenReturn(List.of(task));
//
//        reminderService.checkReminders();
//
//        verify(calendarItemRepository, times(1)).save(task);
//    }
//
//    @Test
//    void testReminderSkippedIfAlreadySent() {
//        Task task = new Task();
//        task.setId(2L);
//        task.setTitle("Già notificato");
//        task.setDueDateTime(LocalDateTime.now().plusMinutes(5));
//        task.setReminderMinutesBefore(5);
//        task.setReminderSent(true);
//
//        when(calendarItemRepository.findPendingReminderItems()).thenReturn(List.of(task));
//
//        reminderService.checkReminders();
//
//        verify(calendarItemRepository, never()).save(any());
//    }
//
//    @Test
//    void testReminderSkippedIfDataMissing() {
//        Task task = new Task();
//        task.setId(3L);
//        task.setTitle("Senza data");
//        task.setReminderMinutesBefore(10);
//        task.setReminderSent(false);
//
//        when(calendarItemRepository.findPendingReminderItems()).thenReturn(List.of(task));
//
//        reminderService.checkReminders();
//
//        verify(calendarItemRepository, never()).save(any());
//    }
}