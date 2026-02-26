package com.example.DataCenterManagementSystem.entity.dcim;

import com.example.DataCenterManagementSystem.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true) @Data
@SuperBuilder @NoArgsConstructor
@Entity @Table(name = "rack_rows")
public class RackRow extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String rowName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_center_id", nullable = false)
    private DataCenter dataCenter;

    @OneToMany(mappedBy = "rackRow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rack> racks = new ArrayList<>();
}
