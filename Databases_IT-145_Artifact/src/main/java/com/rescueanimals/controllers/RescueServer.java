package com.rescueanimals.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.rescueanimals.models.Dog;
import com.rescueanimals.models.Monkey;
import com.rescueanimals.models.StatusResponse;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.json.JsonMapper;

/**
 * REST API server for the Rescue Animal System.
 * Exposes endpoints for managing dogs and monkeys, and for reserving animals.
 * Uses Javalin for lightweight HTTP routing and JSON serialization.
 * This design decouples the backend logic from the frontend, enabling flexible UI options.
 */
public class RescueServer {
    private static final RescueController controller = new RescueController();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final int PORT = 8647;

    /**
     * Starts the Javalin server and sets up all API routes.
     * 
     * This method configures the JSON mapper to use Gson for serialization and deserialization,
     * then registers all REST API endpoints for dogs, monkeys, and animal reservation. Each endpoint
     * delegates business logic to the RescueController, keeping the server focused on HTTP concerns.
     * The design allows for easy extension and clear separation of concerns between API and logic.
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JsonMapper() {
                @Override
                public String toJsonString(Object obj) {
                    return gson.toJson(obj);
                }

                @Override
                public <T> T fromJsonString(String json, Class<T> targetClass) {
                    return gson.fromJson(json, targetClass);
                }
            });
        }).start(PORT);

        // List endpoints
        app.get("/dogs", ctx -> ctx.json(controller.getAllDogs()));
        app.get("/monkeys", ctx -> ctx.json(controller.getAllMonkeys()));
        app.get("/available", ctx -> ctx.json(controller.getAvailableAnimals()));

        // Add endpoints
        app.post("/dogs", RescueServer::saveDog);
        app.post("/monkeys", RescueServer::saveMonkey);

        // Reserve endpoint
        app.post("/reserve/{type}/{name}", RescueServer::reserveAnimal);

        System.out.println("Server started on port " + PORT);
    }

    /**
     * Handles POST requests to add a new dog.
     * 
     * Parses the request body as a Dog object, delegates to the controller, and returns a JSON status response.
     * Catches JSON and null errors to provide a clear 400 error for malformed input.
     * @param ctx Javalin HTTP context
     */
    private static void saveDog(Context ctx) {
        try {
            Dog dog = gson.fromJson(ctx.body(), Dog.class);
            controller.saveDog(dog);
            ctx.json(new StatusResponse(true));
        } catch (JsonSyntaxException | NullPointerException e) {
            ctx.status(400).json(new StatusResponse(false));
        }
    }

    /**
     * Handles POST requests to add a new monkey.
     * 
     * Parses the request body as a Monkey object, delegates to the controller, and returns a JSON status response.
     * Catches JSON and null errors to provide a clear 400 error for malformed input.
     * @param ctx Javalin HTTP context
     */
    private static void saveMonkey(Context ctx) {
        try {
            Monkey monkey = gson.fromJson(ctx.body(), Monkey.class);
            controller.saveMonkey(monkey);
            ctx.json(new StatusResponse(true));
        } catch (JsonSyntaxException | NullPointerException e) {
            ctx.status(400).json(new StatusResponse(false));
        }
    }

    /**
     * Handles POST requests to reserve an animal for service.
     * 
     * Extracts animal type, name, and service country from the request, delegates to the controller,
     * and returns a JSON status response. Validates input and provides a 400 error for missing or malformed data.
     * @param ctx Javalin HTTP context
     */
    private static void reserveAnimal(Context ctx) {
        try {
            String type = ctx.pathParam("type");
            String name = ctx.pathParam("name");
            String country = ctx.queryParam("country");
            
            if (country == null || country.isEmpty()) {
                ctx.status(400).json(new StatusResponse(false));
                return;
            }

            boolean success = controller.reserveAnimal(type, name, country);
            ctx.json(new StatusResponse(success));
        } catch (JsonSyntaxException | NullPointerException e) {
            ctx.status(400).json(new StatusResponse(false));
        }
    }
} 