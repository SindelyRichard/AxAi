package com.axai.axai.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class SubMenu {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToOne
    @JoinColumn(name="menu_id")
    private Menu menu;

    @ManyToMany
    @JoinTable(
            name = "submenu_apps",
            joinColumns = @JoinColumn(name = "submenu_id"),
            inverseJoinColumns = @JoinColumn(name = "app_id")
    )
    private List<App> apps = new ArrayList<>();
}
