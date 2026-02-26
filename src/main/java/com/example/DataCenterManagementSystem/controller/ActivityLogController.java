package com.example.DataCenterManagementSystem.controller;


import com.example.DataCenterManagementSystem.config.ApiResponse;
import com.example.DataCenterManagementSystem.dto.ActivityLogResponse;
import com.example.DataCenterManagementSystem.service.ActivityLogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity-logs")
@RequiredArgsConstructor
@CrossOrigin
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @Operation(summary = "Get activity log by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityLogResponse>> getActivityLogById(@PathVariable Long id) {
        ActivityLogResponse response = activityLogService.getActivityLogById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all activity logs")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityLogResponse>>> getAllActivityLogs() {
        List<ActivityLogResponse> responses = activityLogService.getAllActivityLogs();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}

