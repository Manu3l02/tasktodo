package com.example.tasktodo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tasktodo.dto.CalendarItemDTO;
import com.example.tasktodo.dto.EventForm;
import com.example.tasktodo.dto.TaskForm;
import com.example.tasktodo.exception.ResourceNotFoundException;
import com.example.tasktodo.model.CalendarItem;
import com.example.tasktodo.model.Event;
import com.example.tasktodo.model.Task;
import com.example.tasktodo.model.User;
import com.example.tasktodo.repository.EventRepository;
import com.example.tasktodo.repository.TaskRepository;

@Service
public class CalendarService {

    private final TaskRepository taskRepo;
    private final EventRepository eventRepo;
    private final UserService userService;

    public CalendarService(TaskRepository taskRepo,
                           EventRepository eventRepo,
                           UserService userService) {
        this.taskRepo = taskRepo;
        this.eventRepo = eventRepo;
        this.userService = userService;
    }

    private User findUser(UserDetails userDetails) {
        return userService.findByUsername(userDetails.getUsername());
    }

    @Transactional(readOnly = true)
    public List<CalendarItemDTO> findAll(UserDetails userDetails) {
        Long userId = findUser(userDetails).getUserId();
        List<CalendarItem> all = new ArrayList<>();
        all.addAll(taskRepo.findByUserUserId(userId));
        all.addAll(eventRepo.findByUserUserId(userId));
        return all.stream()
                .map(this::toDTO)
                .sorted(Comparator.comparing(CalendarItemDTO::getSortKey, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @Transactional
    public CalendarItemDTO createTask(TaskForm form, UserDetails userDetails) {
        User user = findUser(userDetails);
        Task task = new Task();
        task.setUser(user);
        task.setTitle(form.getTitle());
        task.setDescription(form.getDescription());
        task.setDueDateTime(form.getDueDateTime());
        task.setReminderMinutesBefore(form.getReminderMinutesBefore());
        task.setReminderSent(false);
        task.setCompleted(false);
        return toDTO(taskRepo.save(task));
    }

    @Transactional
    public CalendarItemDTO updateTask(Long id, TaskForm form, UserDetails userDetails) {
        Task task = taskRepo.findById(id)
                .filter(t -> t.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        task.setTitle(form.getTitle());
        task.setDescription(form.getDescription());
        task.setDueDateTime(form.getDueDateTime());
        task.setReminderMinutesBefore(form.getReminderMinutesBefore());
        task.setReminderSent(false);
        return toDTO(taskRepo.save(task));
    }

    @Transactional
    public void deleteTask(Long id, UserDetails userDetails) {
        Task task = taskRepo.findById(id)
                .filter(t -> t.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        taskRepo.delete(task);
    }

    @Transactional
    public CalendarItemDTO completeTask(Long id, UserDetails userDetails) {
        Task task = taskRepo.findById(id)
                .filter(t -> t.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        task.setCompleted(true);
        return toDTO(taskRepo.save(task));
    }

    @Transactional
    public void markReminderSent(Long id, UserDetails userDetails) {
        Task task = taskRepo.findById(id)
                .filter(t -> t.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        task.setReminderSent(true);
        taskRepo.save(task);
    }

    @Transactional
    public CalendarItemDTO createEvent(EventForm form, UserDetails userDetails) {
        User user = findUser(userDetails);
        Event event = new Event();
        event.setUser(user);
        event.setTitle(form.getTitle());
        event.setDescription(form.getDescription());
        event.setStartDateTime(form.getStartDateTime());
        event.setEndDateTime(form.getEndDateTime());
        event.setReminderMinutesBefore(form.getReminderMinutesBefore());
        event.setReminderSent(false);
        return toDTO(eventRepo.save(event));
    }

    @Transactional
    public CalendarItemDTO updateEvent(Long id, EventForm form, UserDetails userDetails) {
        Event event = eventRepo.findById(id)
                .filter(e -> e.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        event.setTitle(form.getTitle());
        event.setDescription(form.getDescription());
        event.setStartDateTime(form.getStartDateTime());
        event.setEndDateTime(form.getEndDateTime());
        event.setReminderMinutesBefore(form.getReminderMinutesBefore());
        event.setReminderSent(false);
        return toDTO(eventRepo.save(event));
    }

    @Transactional
    public void deleteEvent(Long id, UserDetails userDetails) {
        Event event = eventRepo.findById(id)
                .filter(e -> e.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        eventRepo.delete(event);
    }

    public CalendarItemDTO toDTO(CalendarItem item) {
        CalendarItemDTO dto = new CalendarItemDTO();
        dto.setId(item.getId());
        dto.setType(item instanceof Task ? "TASK" : "EVENT");
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setReminderMinutesBefore(item.getReminderMinutesBefore());
        dto.setReminderSent(item.isReminderSent());

        if (item instanceof Task task) {
            dto.setDueDateTime(task.getDueDateTime());
            dto.setCompleted(task.isCompleted());
            dto.setSortKey(task.getDueDateTime());
        } else if (item instanceof Event event) {
            dto.setStartDateTime(event.getStartDateTime());
            dto.setEndDateTime(event.getEndDateTime());
            dto.setSortKey(event.getStartDateTime());
        }

        return dto;
    }
}
