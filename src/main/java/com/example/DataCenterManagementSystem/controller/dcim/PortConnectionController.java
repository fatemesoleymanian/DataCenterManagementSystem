package com.example.DataCenterManagementSystem.controller.dcim;

import com.example.DataCenterManagementSystem.dto.dcim.ConnectPortsRequest;
import com.example.DataCenterManagementSystem.dto.dcim.PortConnectionResponse;
import com.example.DataCenterManagementSystem.service.PortConnectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connections")
@RequiredArgsConstructor
public class PortConnectionController {

    private final PortConnectionService connectionService;

    @PostMapping
    public ResponseEntity<PortConnectionResponse> connect(
            @Valid @RequestBody ConnectPortsRequest request) {

        PortConnectionResponse response =
                connectionService.connectPorts(
                        request.portAId(),
                        request.portBId()
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disconnect(@PathVariable Long id) {

        connectionService.disconnect(id);

        return ResponseEntity.noContent().build();
    }
}
