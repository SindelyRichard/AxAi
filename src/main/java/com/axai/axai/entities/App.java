package com.axai.axai.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class App {
    @Id
    private UUID id;
    private String name;
    @ManyToOne
    @JoinColumn(name="menu_id")
    private Menu menu;

}
