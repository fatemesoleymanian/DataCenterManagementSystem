package com.example.DataCenterManagementSystem.dto;

import com.example.DataCenterManagementSystem.config.validation.ValidEnum;
import com.example.DataCenterManagementSystem.entity.security.UserRole;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest (

//        @NotBlank(message = "نام کاربری الزامی است")
        @Size(max = 50, message = "نام کاربری نمی‌تواند بیشتر از 50 کاراکتر باشد")
        String username,
//        @NotBlank(message = "رمزعبور الزامی است")
        @Size(max = 20, message = "رمزعبور نمی‌تواند بیشتر از 20 کاراکتر باشد")
        @Size(min = 8, message = "رمزعبور نمی‌تواند کمتر از 8 کاراکتر باشد")
        String password,

//        @NotNull(message = "نقش الزامی است")
        @ValidEnum(enumClass = UserRole.class, message = "نقش واردشده معتبر نیست")
        String role

){
}
