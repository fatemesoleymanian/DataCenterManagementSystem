package com.example.DataCenterManagementSystem.entity.dcim;

import com.example.DataCenterManagementSystem.entity.BaseEntity;
import com.example.DataCenterManagementSystem.exception.UnitOccupiedException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@EqualsAndHashCode(callSuper = true) @Data
@SuperBuilder @NoArgsConstructor
@Entity @Table(name = "racks")
public class Rack extends BaseEntity {

    private static final int MAX_UNITS = 42; // Constant for max units

    @Column(nullable = false, length = 50)
    private int rackNumber;

    @Version
    private Long version;

    @OneToMany(mappedBy = "rack", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("unitNumber DESC") // Sort from top to bottom if needed
    @Builder.Default
    private List<RackUnit> units = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rack_row_id", nullable = false)
    private RackRow rackRow;

//    @OneToMany(mappedBy = "rack", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Equipment> equipments = new ArrayList<>();

    @PrePersist
    private void initializeUnits() {

        if (units == null) {
            units = new ArrayList<>();
        }

        if (units.isEmpty()) {
            for (int i = 1; i <= MAX_UNITS; i++) {
                RackUnit unit = RackUnit.builder()
                        .unitNumber(i)
                        .rack(this)
                        .build();
                units.add(unit);
            }
        }
    }

    public int getMaxUnits() {
        return MAX_UNITS;
    }

    public List<RackUnit> allocateUnits(int start, int size) {

        int maxUnits = getMaxUnits();

        if (start < 1 || start + size - 1 > maxUnits) {
            throw new IllegalArgumentException("Invalid start unit or size for rack");
        }

        List<RackUnit> targetUnits = units.stream()
                .filter(u -> u.getUnitNumber() >= start
                        && u.getUnitNumber() < start + size)
                .sorted(Comparator.comparingInt(RackUnit::getUnitNumber))
                .toList();

        if (targetUnits.size() != size) {
            throw new IllegalStateException("Units not initialized properly");
        }

        for (RackUnit unit : targetUnits) {
            if (unit.isOccupied()) {
                throw new UnitOccupiedException(
                        "Unit " + unit.getUnitNumber() + " is occupied");
            }
        }

        return targetUnits;
    }
}
