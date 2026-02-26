package com.example.DataCenterManagementSystem.entity.dcim;

import com.example.DataCenterManagementSystem.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true) @Data
@SuperBuilder @NoArgsConstructor
@Entity @Table(name = "ports")
public class Port extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String portNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @OneToOne(mappedBy = "portA", fetch = FetchType.LAZY)
    private PortConnection connectionAsA;

    @OneToOne(mappedBy = "portB", fetch = FetchType.LAZY)
    private PortConnection connectionAsB;

    public boolean isConnected() {
        return connectionAsA != null || connectionAsB != null;
    }

}
