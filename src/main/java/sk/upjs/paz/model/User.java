package sk.upjs.paz.model;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
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
            long idUser,
            String name,
            String surname,
            String email,
            String phoneNumber,
            String passwordHash,
            String personalNumber,
            LocalDate dateOfBirth,
            String address,
            int roleId,
            long districtId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.idUser = idUser;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash;
        this.personalNumber = personalNumber;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.roleId = roleId;
        this.districtId = districtId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}