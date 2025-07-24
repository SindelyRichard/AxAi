package com.axai.axai.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String password;

    @OneToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @OneToMany(mappedBy = "user")
    private List<Background> background = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    public User() {

    }
}
