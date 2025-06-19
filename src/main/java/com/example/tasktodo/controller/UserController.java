package com.example.tasktodo.controller;

import com.example.tasktodo.model.User;
import com.example.tasktodo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final String uploadDir = "uploads/";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{id}/upload-profile-image")
    public ResponseEntity<?> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findById(id);
        if (!user.getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).body("Accesso negato");
        }

        try {
            Files.createDirectories(Paths.get(uploadDir));
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = "/uploads/" + filename;
            user.setProfileImageUrl(fileUrl);
            userService.updateUserProfile(user);
            System.out.println("Profile image uploaded: " + fileUrl);

            return ResponseEntity.ok(Map.of("url", fileUrl));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Errore durante l'upload");
        }
    }
}
