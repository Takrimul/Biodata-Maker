package com.takrimul.basic_crud_app.controller;

import com.takrimul.basic_crud_app.model.User;
import com.takrimul.basic_crud_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserRepository userRepository;
    private final Path fileStorageLocation;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "form";
    }

    @PostMapping("/form")
    public String submitForm(@ModelAttribute User user,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
                             @RequestParam(value = "resumeFile", required = false) MultipartFile resumeFile,
                             Model model) {
        try {
            // Save the image, video, and resume files if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageFileName = saveFile(imageFile);
                user.setImagePath(imageFileName);
            }
            if (videoFile != null && !videoFile.isEmpty()) {
                String videoFileName = saveFile(videoFile);
                user.setVideoPath(videoFileName);
            }
            if (resumeFile != null && !resumeFile.isEmpty()) {
                String resumeFileName = saveFile(resumeFile);
                user.setResumePath(resumeFileName);
            }

            // Save the user
            userRepository.save(user);

            model.addAttribute("message", "Saved successfully");
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
        }
        return "result";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("Invalid user id " + id));
        model.addAttribute("user", user);
        return "form"; // Assuming "form.html" is used for both create and update
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute User updatedUser,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
                             @RequestParam(value = "resumeFile", required = false) MultipartFile resumeFile,
                             Model model) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("Invalid user id " + id));

            // Update user fields
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setGender(updatedUser.getGender());
            user.setBirthday(updatedUser.getBirthday());
            user.setAddress(updatedUser.getAddress());
            user.setPhone(updatedUser.getPhone());
            user.setOccupation(updatedUser.getOccupation());
            user.setInstitution(updatedUser.getInstitution());
            user.setNID(updatedUser.getNID());

            // Save the image, video, and resume files if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageFileName = saveFile(imageFile);
                user.setImagePath(imageFileName);
            }
            if (videoFile != null && !videoFile.isEmpty()) {
                String videoFileName = saveFile(videoFile);
                user.setVideoPath(videoFileName);
            }
            if (resumeFile != null && !resumeFile.isEmpty()) {
                String resumeFileName = saveFile(resumeFile);
                user.setResumePath(resumeFileName);
            }

            // Save the user
            userRepository.save(user);

            model.addAttribute("message", "Updated successfully");
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "form";
        }
        return "redirect:/home";
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }
        Path destinationFile = this.fileStorageLocation.resolve(
                        Paths.get(file.getOriginalFilename()))
                .normalize().toAbsolutePath();
        if (!destinationFile.getParent().equals(this.fileStorageLocation.toAbsolutePath())) {
            throw new IOException("Cannot store file outside current directory.");
        }
        try (java.io.InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return destinationFile.toString();
    }
}
