package com.example.DataCenterManagementSystem.config.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserRoleTypeValidator.class)
@Documented
public @interface ValidUserRoleType {

    String message() default "For CUSTOMER role, userType must not be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
