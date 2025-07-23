package com.axai.axai.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Theme {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
