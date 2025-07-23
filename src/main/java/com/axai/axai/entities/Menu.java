package com.axai.axai.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@Entity
@Data
public class Menu {
    @Id
    private UUID id;

    private String name;

    @OneToOne(mappedBy = "menu")
    private User user;

    @OneToMany(mappedBy = "menu")
    private List<App> items = new ArrayList<>();
}
