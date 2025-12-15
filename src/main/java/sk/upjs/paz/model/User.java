package sk.upjs.paz.model;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private long idUser;
    @Getter
    private String name;
    @Getter
    private String surname;
    @Getter
    private String email;
    @Getter
    private String phoneNumber;
    @Getter
    private String passwordHash;
    @Getter
    private String personalNumber;
    @Getter
    private LocalDate dateOfBirth;
    @Getter
    private String address;
    @Getter
    private int roleId;
    @Getter
    private long districtId;
    @Getter
    private LocalDateTime createdAt;
    @Getter
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

