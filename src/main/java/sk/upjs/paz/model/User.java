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

    public User(
            String email,
            String passwordHash,
            String name,
            String surname,
            LocalDate dateOfBirth,
            long districtId,
            String address,
            String phoneNumber,
            String personalNumber
    ) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.dateOfBirth = dateOfBirth;
        this.districtId = districtId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.roleId = 3; // default role: user
        this.personalNumber = personalNumber;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}