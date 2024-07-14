package com.takrimul.basic_crud_app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String gender;
    private LocalDate birthday;
    private String address;
    private String phone;
    private String occupation;
    private String institution;
    private Long NID;

    // Fields for image, video, and resume paths
    private String imagePath;
    private String videoPath;
    private String resumePath;
}
