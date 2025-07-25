package com.axai.axai.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@Entity
@Data
public class Menu {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @OneToOne(mappedBy = "menu")
    private User user;

    @OneToMany(mappedBy = "menu")
    private List<SubMenu> subMenus = new ArrayList<>();
}
