package com.axai.axai.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class App {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @ManyToOne
    @JoinColumn(name="menu_id")
    private Menu menu;

}
