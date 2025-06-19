package com.example.tasktodo.service;

import java.security.Principal;
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
        User u = findUser(userDetails);
        Task t = new Task();
        t.setUser(u);
        t.setTitle(form.getTitle());
        t.setDescription(form.getDescription());
        t.setDueDate(form.getDueDate());
        t.setReminderDateTime(form.getReminderDateTime());
        t.setCompleted(false);
        t.setReminderSent(false);
        t = taskRepo.save(t);
        return toDTO(t);
    }

    @Transactional
    public CalendarItemDTO updateTask(Long id, TaskForm form, UserDetails userDetails) {
        Task t = taskRepo.findById(id)
                .filter(task -> task.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        t.setTitle(form.getTitle());
        t.setDescription(form.getDescription());
        t.setDueDate(form.getDueDate());
        t.setReminderDateTime(form.getReminderDateTime());
        t.setReminderSent(false);
        return toDTO(taskRepo.save(t));
    }

    @Transactional
    public void deleteTask(Long id, UserDetails userDetails) {
        Task t = taskRepo.findById(id)
                .filter(task -> task.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        taskRepo.delete(t);
    }

    @Transactional
    public CalendarItemDTO completeTask(Long id, UserDetails userDetails) {
        Task t = taskRepo.findById(id)
                .filter(task -> task.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        t.setCompleted(true);
        return toDTO(taskRepo.save(t));
    }

    @Transactional
    public void markReminderSent(Long id, UserDetails userDetails) {
        Task t = taskRepo.findById(id)
                .filter(task -> task.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        t.setReminderSent(true);
        taskRepo.save(t);
    }

    @Transactional
    public CalendarItemDTO createEvent(EventForm form, UserDetails userDetails) {
        User u = findUser(userDetails);
        Event e = new Event();
        e.setUser(u);
        e.setTitle(form.getTitle());
        e.setDescription(form.getDescription());
        e.setStartDateTime(form.getStartDateTime());
        e.setEndDateTime(form.getEndDateTime());
        e = eventRepo.save(e);
        return toDTO(e);
    }

    @Transactional
    public CalendarItemDTO updateEvent(Long id, EventForm form, UserDetails userDetails) {
        Event e = eventRepo.findById(id)
                .filter(ev -> ev.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        e.setTitle(form.getTitle());
        e.setDescription(form.getDescription());
        e.setStartDateTime(form.getStartDateTime());
        e.setEndDateTime(form.getEndDateTime());
        return toDTO(eventRepo.save(e));
    }

    @Transactional
    public void deleteEvent(Long id, UserDetails userDetails) {
        Event e = eventRepo.findById(id)
                .filter(ev -> ev.getUser().getUsername().equals(userDetails.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        eventRepo.delete(e);
    }

    public CalendarItemDTO toDTO(CalendarItem item) {
        CalendarItemDTO dto = new CalendarItemDTO();
        dto.setId(item.getId());
        dto.setType(item instanceof Task ? "TASK" : "EVENT");
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());

        if (item instanceof Task t) {
            dto.setDueDate(t.getDueDate());
            dto.setCompleted(t.isCompleted());
            dto.setReminderDateTime(t.getReminderDateTime());
            dto.setReminderSent(t.isReminderSent());
            if (t.getDueDate() != null) {
                dto.setSortKey(t.getDueDate().atStartOfDay());
            } else {
                dto.setSortKey(null);
            }
        } else if (item instanceof Event e) {
            dto.setStartDateTime(e.getStartDateTime());
            dto.setEndDateTime(e.getEndDateTime());
            dto.setSortKey(e.getStartDateTime());
        }
        return dto;
    }
}
