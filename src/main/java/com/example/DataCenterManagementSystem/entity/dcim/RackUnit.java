package com.example.DataCenterManagementSystem.entity.dcim;

import com.example.DataCenterManagementSystem.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true) @Data
@SuperBuilder @NoArgsConstructor
@Entity @Table(name = "rack_units",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"rack_id", "unitNumber"})})
public class RackUnit extends BaseEntity {

    @Min(value = 1, message = "Unit number must be at least 1")
    @Column(nullable = false)
    private int unitNumber; // 1 to 42 typically

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment occupiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rack_id", nullable = false)
    private Rack rack;

    public boolean isOccupied() {
        return occupiedBy != null;
    }
}
