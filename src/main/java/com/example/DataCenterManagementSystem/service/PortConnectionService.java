package com.example.DataCenterManagementSystem.service;


import com.example.DataCenterManagementSystem.config.logging.LogActivity;
import com.example.DataCenterManagementSystem.dto.dcim.PortConnectionResponse;
import com.example.DataCenterManagementSystem.entity.dcim.EquipmentType;
import com.example.DataCenterManagementSystem.entity.dcim.Port;
import com.example.DataCenterManagementSystem.entity.dcim.PortConnection;
import com.example.DataCenterManagementSystem.exception.DynamicTextException;
import com.example.DataCenterManagementSystem.repository.PortConnectionRepository;
import com.example.DataCenterManagementSystem.repository.PortRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortConnectionService {

    private final PortRepository portRepository;
    private final PortConnectionRepository connectionRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "CONNECT_PORTS")
    public PortConnectionResponse connectPorts(Long portAId, Long portBId) {

        if (portAId.equals(portBId)) {
            throw new DynamicTextException("Cannot connect port to itself");
        }

        Port portA = portRepository.findByIdForUpdate(portAId)
                .orElseThrow(() -> new RuntimeException("Port A not found"));

        Port portB = portRepository.findByIdForUpdate(portBId)
                .orElseThrow(() -> new RuntimeException("Port B not found"));

        if (portA.getEquipment().getType() == EquipmentType.SERVER && "1".equals(portA.getPortNumber())) {
            if (portB.getEquipment().getType() != EquipmentType.PATCH_PANEL) {
                throw new DynamicTextException("Server port 1 must connect to patch panel");
            }
        }

        if (portA.isConnected()) {
            throw new DynamicTextException("Port A already connected");
        }

        if (portB.isConnected()) {
            throw new DynamicTextException("Port B already connected");
        }

        boolean exists =
                connectionRepository.existsByPortAAndPortB(portA, portB) ||
                        connectionRepository.existsByPortAAndPortB(portB, portA);

        if (exists) {
            throw new DynamicTextException("Connection already exists");
        }

        PortConnection connection = PortConnection.builder()
                .portA(portA)
                .portB(portB)
                .build();

        connectionRepository.save(connection);

        return new PortConnectionResponse(
                connection.getId(),
                portA.getId(),
                portB.getId()
        );
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @LogActivity(action = "DISCONNECT_PORTS", target = "#connectionId")
    public void disconnect(Long connectionId) {

        PortConnection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Connection not found"));

        connectionRepository.delete(connection);
    }
}