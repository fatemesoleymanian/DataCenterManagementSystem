package com.example.DataCenterManagementSystem.entity.dcim;

import com.example.DataCenterManagementSystem.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "port_connections",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "port_a_id"),
                @UniqueConstraint(columnNames = "port_b_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortConnection extends BaseEntity {

    @OneToOne(optional = false)
    @JoinColumn(name = "port_a_id", nullable = false, unique = true)
    private Port portA;

    @OneToOne(optional = false)
    @JoinColumn(name = "port_b_id", nullable = false, unique = true)
    private Port portB;
}
