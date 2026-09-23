package com.super_x.ai;

import com.super_x.model.usermodel.Load;
import com.super_x.controller.usercontroller.LoadController;

import java.util.List;

public class LoadDataTest {

    public static void main(String[] args) {

        try {

            LoadController loadController =
                    new LoadController();

            List<Load> loads =
                    loadController.getAllLoads();

            System.out.println();
            System.out.println("===== ECOLOAD LOAD DATA =====");

            System.out.println(
                    "Total Loads: " + loads.size()
            );

            for (Load load : loads) {

                System.out.println(
                        "Load ID: " +
                        load.getLoadId()
                );

                System.out.println(
                        "Pickup: " +
                        load.getPickupLocation()
                );

                System.out.println(
                        "Destination: " +
                        load.getDestination()
                );

                System.out.println(
                        "Load Type: " +
                        load.getLoadType()
                );

                System.out.println(
                        "Weight: " +
                        load.getWeight() +
                        " " +
                        load.getWeightUnit()
                );

                System.out.println(
                        "Truck Type: " +
                        load.getTruckType()
                );

                System.out.println(
                        "Status: " +
                        load.getStatus()
                );

                System.out.println(
                        "--------------------------------"
                );
            }

            System.out.println(
                    "==============================="
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to load EcoLoad data."
            );

            e.printStackTrace();
        }
    }
}