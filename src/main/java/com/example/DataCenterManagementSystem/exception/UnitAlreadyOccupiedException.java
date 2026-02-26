package com.example.DataCenterManagementSystem.exception;

public class UnitAlreadyOccupiedException extends BusinessException {
    public UnitAlreadyOccupiedException(int unitNumber) {
        super("Unit " + unitNumber + " is already occupied");
    }
}