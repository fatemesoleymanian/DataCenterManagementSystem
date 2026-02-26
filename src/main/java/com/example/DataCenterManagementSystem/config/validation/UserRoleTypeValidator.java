package com.example.DataCenterManagementSystem.config.validation;

import com.example.DataCenterManagementSystem.entity.security.User;
import com.example.DataCenterManagementSystem.entity.security.UserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserRoleTypeValidator
        implements ConstraintValidator<ValidUserRoleType, User> {

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {

        if (user == null) return true;

        if (user.getRole() == UserRole.CUSTOMER && user.getType() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "userType is required when role is CUSTOMER"
            ).addPropertyNode("type").addConstraintViolation();
            return false;
        }

        return true;
    }
}