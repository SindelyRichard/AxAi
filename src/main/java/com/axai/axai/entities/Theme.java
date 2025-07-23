package com.axai.axai.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Theme {
    @Id
    private UUID id;

    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
