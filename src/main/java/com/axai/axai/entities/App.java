package com.axai.axai.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class App {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "apps")
    private List<SubMenu> subMenuList = new ArrayList<>();

    private String iconName;


}
