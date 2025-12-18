package sk.upjs.paz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class User {
    private long idUser;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private String personalNumber;
    private LocalDate dateOfBirth;
    private String address;
    private int roleId;
    private long districtId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}