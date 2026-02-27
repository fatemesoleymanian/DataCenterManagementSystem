package com.example.DataCenterManagementSystem.entity.dcim;

import com.example.DataCenterManagementSystem.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true) @Data
@SuperBuilder @NoArgsConstructor
@Entity @Table(name = "equipments")
public class Equipment extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String modelName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentType type;

    @Positive(message = "Unit size must be positive")
    @Column(nullable = false)
    private int unitSize;

    @OneToMany(mappedBy = "occupiedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RackUnit> occupiedUnits = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "rack_id", nullable = false)
//    private Rack rack;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Port> ports = new ArrayList<>();

    public Rack getRack() {
        if (occupiedUnits.isEmpty()) return null;
        return occupiedUnits.get(0).getRack();
    }
    public List<Port> getPorts() {
        if (ports == null) {
            ports = new ArrayList<>();
        }
        return ports;
    }
}
