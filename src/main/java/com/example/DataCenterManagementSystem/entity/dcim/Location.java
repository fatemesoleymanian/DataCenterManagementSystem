package com.example.DataCenterManagementSystem.entity.dcim;

import com.example.DataCenterManagementSystem.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Check;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true) @Data
@SuperBuilder
@NoArgsConstructor
@Entity @Table(name = "locations")
//indexes = {
//@Index(name = "idx_hotel_city", columnList = "city_name"),
//@Index(name = "idx_hotel_deleted", columnList = "deleted_at")
//        }
public class Location extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Check(constraints = "level between 1 and 4")
    @Column(nullable = false)
    private int level;// Country 1, Province 2, City 3, District 4

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Location parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> children = new ArrayList<>();
}
