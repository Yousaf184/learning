package com.ysf.services;

import com.ysf.aspects.LogAspect;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VehicleService {
    private final Logger logger = Logger.getLogger(VehicleService.class.getName());

    public boolean isNewCar() {
        return false;
    }

    public void startVehicle() {
        System.out.println("Starting vehicle...");
        try {
            Thread.sleep(Duration.ofSeconds(2).toMillis());
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Exception in startVehicle method", e);
        }
        System.out.println("Vehicle started");
    }

    public void move(boolean isVehicleStarted) {
        System.out.println("Moving...");
    }

    public void repair() {
        String errorMsg = "Method not implemented yet";
        throw new UnsupportedOperationException(errorMsg);
    }

    @LogAspect
    public void stopVehicle() {
        System.out.println("Stopping vehicle...");
        try {
            Thread.sleep(Duration.ofSeconds(1).toMillis());
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Exception in stopVehicle method", e);
        }
        System.out.println("Vehicle stopped");
    }
}
