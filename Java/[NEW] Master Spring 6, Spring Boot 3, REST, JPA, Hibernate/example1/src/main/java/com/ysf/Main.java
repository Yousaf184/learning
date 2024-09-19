package com.ysf;

import com.ysf.beans.Vehicle;
import com.ysf.config.SpringConfig;
import com.ysf.services.VehicleService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(SpringConfig.class);

//        Vehicle vehicle = context.getBean("vehicle", Vehicle.class);
//        System.out.println(vehicle.getName());

        // register bean programmatically
//        context.registerBean("runtimeVehicle", Vehicle.class, "Toyota");

//        Vehicle newVehicle = context.getBean("runtimeVehicle", Vehicle.class);
//        System.out.println(newVehicle.getName());

        // Aspect test
        VehicleService vehicleService = context.getBean("vehicleService", VehicleService.class);
//        vehicleService.startVehicle();
//        vehicleService.move(false);
//        vehicleService.repair();
//        vehicleService.isNewCar();
        vehicleService.stopVehicle();

        context.close();
    }
}