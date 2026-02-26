package com.example.DataCenterManagementSystem.service;

import com.example.DataCenterManagementSystem.dto.UpdateUserRequest;
import com.example.DataCenterManagementSystem.dto.UserResponse;
import com.example.DataCenterManagementSystem.entity.security.User;
import com.example.DataCenterManagementSystem.entity.security.UserRole;
import com.example.DataCenterManagementSystem.exception.NotFoundException;
import com.example.DataCenterManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','SUPPORT')")
    public Page<UserResponse> findAll(Pageable pageable) {
        Page<User> page = repository.findAll().stream() // optional: could implement paging in repo
//                .filter(p -> p.getDeletedAt() == null)
                .collect(java.util.stream.Collectors.collectingAndThen(java.util.stream.Collectors.toList(),
                        list -> new org.springframework.data.domain.PageImpl<>(list, pageable, list.size())));
        return page.map(this::mapToResponse);
    }

    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "کاربر "));

        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setRole(UserRole.valueOf(request.role().toUpperCase()));


        User saved = repository.save(user);
        return mapToResponse(saved);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "کاربر "));
        return mapToResponse(user);
    }
}
