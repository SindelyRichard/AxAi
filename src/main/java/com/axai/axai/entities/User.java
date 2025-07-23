package com.axai.axai.entities;

import jakarta.persistence.*;
import lombok.Data;

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

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Background background;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Theme theme;

    public User() {

    }

    public User(UUID id, String username, String password, Menu menu, Background background, Theme theme) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.menu = menu;
        this.background = background;
        this.theme = theme;
    }
}
