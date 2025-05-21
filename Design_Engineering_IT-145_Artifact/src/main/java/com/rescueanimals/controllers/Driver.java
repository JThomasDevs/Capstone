package com.rescueanimals.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rescueanimals.models.Dog;
import com.rescueanimals.models.Monkey;

public class Driver {
    private static final RescueController controller = new RescueController();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Command required");
            System.exit(1);
        }

        String command = args[0];
        try {
            String response;
            switch (command) {
                case "list-dogs":
                    response = controller.listDogs();
                    break;
                case "list-monkeys":
                    response = controller.listMonkeys();
                    break;
                case "list-available":
                    response = controller.listAvailable();
                    break;
                case "add-dog":
                    if (args.length < 10) {
                        throw new IllegalArgumentException("Insufficient arguments for add-dog");
                    }
                    Dog dog = new Dog(args[1], args[2], args[3], args[4], args[5], 
                                    args[6], args[7], args[8], 
                                    Boolean.parseBoolean(args[9]), args[10]);
                    response = gson.toJson(new StatusResponse(controller.addDog(dog)));
                    break;
                case "add-monkey":
                    if (args.length < 13) {
                        throw new IllegalArgumentException("Insufficient arguments for add-monkey");
                    }
                    Monkey monkey = new Monkey(args[1], args[2], args[3], args[4], args[5],
                                            args[6], args[7], args[8], args[9], args[10],
                                            args[11], Boolean.parseBoolean(args[12]), args[13]);
                    response = gson.toJson(new StatusResponse(controller.addMonkey(monkey)));
                    break;
                case "reserve":
                    if (args.length < 3) {
                        throw new IllegalArgumentException("Insufficient arguments for reserve");
                    }
                    response = gson.toJson(new StatusResponse(controller.reserveAnimal(args[1], args[2], args[3])));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown command: " + command);
            }
            System.out.println(response);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static class StatusResponse {
        private final String status;

        public StatusResponse(boolean success) {
            this.status = success ? "success" : "failed";
        }
    }
}