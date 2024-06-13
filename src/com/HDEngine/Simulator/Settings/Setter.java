package com.HDEngine.Simulator.Settings;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Setter implements Runnable{
    private final Scanner input;

    public Setter() {
        input = new Scanner(System.in);
    }
    @Override
    public void run() {
        while (true) {
            System.out.println("Enter setting to change");
            System.out.println("1. fps\n2. tps\n3. speed rate\n4. toggle resume/pause\n5. toggle kill congested vehicle\n6. congestion timeout\n7. toggle speed up at intersection\n8. toggle debug mode");
            try {
                int setting = input.nextInt();
                switch (setting) {
                    case 1:
                        System.out.println("Current FPS: " + Settings.fps + ". Enter new FPS:");
                        Settings.fps = input.nextInt();
                        break;
                    case 2:
                        System.out.println("Current TPS: " + Settings.tps + ". Enter new TPS:");
                        Settings.tps = input.nextInt();
                        break;
                    case 3:
                        System.out.println("Current Speed: " + Settings.speed + ". Enter new Speed:");
                        Settings.speed = input.nextFloat();
                        break;
                    case 4:
                        Settings.running = !Settings.running;
                        System.out.println("Running State is set to: " + Settings.running);
                        break;
                    case 5:
                        Settings.killCongestedVehicle = !Settings.killCongestedVehicle;
                        System.out.println("killCongestedVehicle state is set to: " + Settings.killCongestedVehicle);
                        break;
                    case 6:
                        System.out.println("Current timeout: " + Settings.congestionTimeout + ". Enter new timeout");
                        Settings.congestionTimeout = input.nextInt();
                        break;
                    case 7:
                        Settings.speedUpAtIntersection = !Settings.speedUpAtIntersection;
                        System.out.println("speedUpAtIntersection state is set to: " + Settings.speedUpAtIntersection);
                        break;
                    case 8:
                        Settings.debugMode = !Settings.debugMode;
                        System.out.println("debugMode state is set to: " + Settings.debugMode);
                        break;
                    default:
                        System.out.println("Invalid setting. Try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input type. Please enter the correct type for the setting.");
                input.nextLine();
            }
        }
    }
}
