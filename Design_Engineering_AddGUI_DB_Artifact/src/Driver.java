import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    private static ArrayList<Dog> dogList = new ArrayList<>();
    private static ArrayList<Monkey> monkeyList = new ArrayList<>();

    // This list will be used to validate the species of a monkey
    private static final ArrayList<String> monkeySpeciesList = new ArrayList<>();


    public static void main(String[] args) {

        initializeDogList();
        initializeMonkeyList();
        // Adding all valid monkey species to the species list
        monkeySpeciesList.add("capuchin");
        monkeySpeciesList.add("guenon");
        monkeySpeciesList.add("macaque");
        monkeySpeciesList.add("marmoset");
        monkeySpeciesList.add("squirrel monkey");
        monkeySpeciesList.add("tamarin");

        // Start of menu loop + menu variables
        Scanner scnr = new Scanner(System.in);
        String menuInput = "";
        while (!menuInput.equals("q")) {
            displayMenu();
            menuInput = scnr.nextLine();
            switch (menuInput) {
                case "1" -> intakeNewDog(scnr);
                case "2" -> intakeNewMonkey(scnr);
                case "3" -> reserveAnimal(scnr);
                case "4" -> printAnimals("dog");
                case "5" -> printAnimals("monkey");
                case "6" -> printAnimals("available");
                case "q" -> System.out.println("Goodbye!");
                default -> System.out.println("\nInvalid menu selection. Please try again.");
            }
        }

    }

    // This method prints the menu options
    public static void displayMenu() {
        System.out.println("\n\n");
        System.out.println("\t\t\t\tRescue Animal System Menu");
        System.out.println("[1] Intake a new dog");
        System.out.println("[2] Intake a new monkey");
        System.out.println("[3] Reserve an animal");
        System.out.println("[4] Print a list of all dogs");
        System.out.println("[5] Print a list of all monkeys");
        System.out.println("[6] Print a list of all animals that are not reserved");
        System.out.println("[q] Quit application");
        System.out.println();
        System.out.println("Enter a menu selection");
    }


    // Adds dogs to a list for testing
    public static void initializeDogList() {
        Dog dog1 = new Dog("Spot", "German Shepherd", "male", "1", "25.6", "05-12-2019", "United States", "intake", false, "United States");
        Dog dog2 = new Dog("Rex", "Great Dane", "male", "3", "35.2", "02-03-2020", "United States", "in service", false, "United States");
        Dog dog3 = new Dog("Bella", "Chihuahua", "female", "4", "25.6", "12-12-2019", "Canada", "in service", true, "Canada");

        dogList.add(dog1);
        dogList.add(dog2);
        dogList.add(dog3);
    }


    // Adds monkeys to a list for testing
    public static void initializeMonkeyList() {
        Monkey monkey1 = new Monkey("Winston", "1.2", "3.3", "4", "Capuchin", "male","2","12.5","05-12-2019", "United States", "intake", false, "United States");
        Monkey monkey2 = new Monkey("Marcel", "1.5", "2.8", "3.2", "Guenon", "female", "3", "10.5", "02-03-2020", "United States", "in service", false, "United States");
        Monkey monkey3 = new Monkey("Shmooby", "1.8", "2.1","2.8","Marmoset","male","4","8.5","12-12-2019","Canada","in service",true,"Canada");

        monkeyList.add(monkey1);
        monkeyList.add(monkey2);
        monkeyList.add(monkey3);
    }

    // This method completes the intake process for a new dog
    // and rejects the intake if the dog is already in the system.
    public static void intakeNewDog(Scanner scanner) {
        System.out.println("What is the dog's name?");
        String name = scanner.nextLine();
        while (name.isEmpty()) {
            System.out.println("Must enter dog's name. Try again:");
            name = scanner.nextLine();
        }
        for (Dog dog: dogList) {
            if (dog.getName().equalsIgnoreCase(name)) {
                System.out.println("\n\nThis dog is already in our system.\n\n");
                return; //returns to menu
            }
        }
        // All variables below have had a while loop added after first
        // input to ensure that no variables can hold an
        // empty string as a value
        System.out.println("What is the dog's breed?");
        String breed = scanner.nextLine();
        while (breed.isEmpty()) {
            System.out.print("Must enter dog's breed. Try again:");
            breed = scanner.nextLine();
        }
        System.out.println("What is the dog's gender?");
        String gender = scanner.nextLine();
        while (gender.isEmpty()) {
            System.out.println("Must enter dog's gender. Try again:");
            gender = scanner.nextLine();
        }
        System.out.println("What is the dog's age?");
        String age = scanner.nextLine();
        while (age.isEmpty()) {
            System.out.println("Must enter dog's age. Try again:");
            age = scanner.nextLine();
        }
        System.out.println("What is the dog's weight?");
        String weight = scanner.nextLine();
        while (weight.isEmpty()) {
            System.out.println("Must enter dog's weight. Try again:");
            weight = scanner.nextLine();
        }
        System.out.println("What is the dog's date of intake?");
        String acquisitionDate = scanner.nextLine();
        while (acquisitionDate.isEmpty()) {
            System.out.println("Must enter dog's date of intake. Try again:");
            acquisitionDate = scanner.nextLine();
        }
        System.out.println("What is the dog's acquisition country?");
        String acquisitionCountry = scanner.nextLine();
        while (acquisitionCountry.isEmpty()) {
            System.out.println("Must enter dog's acquisition country. Try again:");
            acquisitionCountry = scanner.nextLine();
        }
        System.out.println("What is the dog's training status?");
        String trainingStatus = scanner.nextLine();
        while (trainingStatus.isEmpty()) {
            System.out.println("Must enter dog's training status. Try again:");
            trainingStatus = scanner.nextLine();
        }

        boolean reserved;
        String inServiceCountry;
        // Animals are only asked for reservation status and service country if they are in service
        // Otherwise, the animal is assigned the above default values.
        // This section has been copy+pasted from the monkey intake method
        // with changed dialogue.
        if (trainingStatus.equalsIgnoreCase("in service")) {
            System.out.println("Is the dog reserved? (yes/no)");
            String isReserved = scanner.nextLine();
            while (!isReserved.equals("yes") && !isReserved.equals("no")) {
                System.out.println("Must enter yes or no. Try again:");
                isReserved = scanner.nextLine();
            }
            reserved = isReserved.equalsIgnoreCase("yes");
            // The above line sets the reserved boolean to true if the user enters "yes"
            // and false is the user enters "no"
            System.out.println("What is the dog's service country?");
            inServiceCountry = scanner.nextLine();
            while (inServiceCountry.isEmpty()) {
                System.out.println("Must enter dog's service country. Try again:");
                inServiceCountry = scanner.nextLine();
            }
        }
        else {
            reserved = false;
            inServiceCountry = "N/A";
        }

        // New Dog object is created and added to the dog list.
        Dog dog = new Dog(name,breed,gender,age,weight,acquisitionDate,acquisitionCountry,trainingStatus,reserved,inServiceCountry);
        dogList.add(dog);
        System.out.println("The dog has been added to the system\n\n");
    }


    // This method completes the intake process for a new monkey
    // and rejects the intake if the monkey is already in the system.
    public static void intakeNewMonkey(Scanner scanner) {
        System.out.println("What is the monkey's name?");
        String name = scanner.nextLine();
        while (name.isEmpty()) {
            System.out.println("Must enter monkey's name. Try again:");
            name = scanner.nextLine();
        }
        for (Monkey monkey: monkeyList) {
            if (monkey.getName().equalsIgnoreCase(name)) {
                System.out.println("\n\nThis monkey is already in our system.\n\n");
                return;
            }
        }
        // Just like to intakeNewDog method, all variables below
        // have had a while loop added for input validation
        System.out.println("What is the monkey's tail length?");
        String tailLength = scanner.nextLine();
        while (tailLength.isEmpty()) {
            System.out.println("Must enter monkey's tail length. Try again:");
            tailLength = scanner.nextLine();
        }
        System.out.println("What is the monkey's height?");
        String height = scanner.nextLine();
        while (height.isEmpty()) {
            System.out.println("Must enter monkey's height. Try again:");
            height = scanner.nextLine();
        }
        System.out.println("What is the monkey's body length?");
        String bodyLength = scanner.nextLine();
        while (bodyLength.isEmpty()) {
            System.out.println("Must enter monkey's body length. Try again:");
            bodyLength = scanner.nextLine();
        }
        System.out.println("What is the monkey's species?");
        String species = scanner.nextLine();
        while (species.isEmpty()) {
            System.out.println("Must enter monkey's species. Try again:");
            species = scanner.nextLine();
        }
        while (!monkeySpeciesList.contains(species.toLowerCase())) {
            System.out.println("Please enter a valid species:");
            species = scanner.nextLine();
        }
        System.out.println("What is the monkey's gender?");
        String gender = scanner.nextLine();
        while (gender.isEmpty()) {
            System.out.println("Must enter the monkey's gender. Try again:");
            gender = scanner.nextLine();
        }
        System.out.println("What is the monkey's age?");
        String age = scanner.nextLine();
        while (age.isEmpty()) {
            System.out.println("Must enter the monkey's age. Try again:");
            age = scanner.nextLine();
        }
        System.out.println("What is the monkey's weight?");
        String weight = scanner.nextLine();
        while (weight.isEmpty()) {
            System.out.println("Must enter the monkey's weight. Try again:");
            weight = scanner.nextLine();
        }
        System.out.println("What is the monkey's date of intake?");
        String acquisitionDate = scanner.nextLine();
        while (acquisitionDate.isEmpty()) {
            System.out.println("Must enter the monkey's date of intake. Try again:");
            acquisitionDate = scanner.nextLine();
        }
        System.out.println("What is the monkey's acquisition country?");
        String acquisitionCountry = scanner.nextLine();
        while (acquisitionCountry.isEmpty()) {
            System.out.println("Must enter the monkey's acquisition country. Try again:");
            acquisitionCountry = scanner.nextLine();
        }
        System.out.println("What is the monkey's training status?");
        String trainingStatus = scanner.nextLine();
        while (trainingStatus.isEmpty()) {
            System.out.println("Must enter the monkey's training status. Try again:");
            trainingStatus = scanner.nextLine();
        }

        boolean reserved;
        String inServiceCountry;
        // Animals are only asked for reservation status and service country if they are in service
        // Otherwise, the animal is assigned the above default values.
        if (trainingStatus.equalsIgnoreCase("in service")) {
            System.out.println("Is the monkey reserved? (yes/no)");
            String isReserved = scanner.nextLine();
            while (!isReserved.equals("yes") && !isReserved.equals("no")) {
                System.out.println("Must enter yes or no. Try again:");
                isReserved = scanner.nextLine();
            }
            reserved = isReserved.equalsIgnoreCase("yes");
            System.out.println("What is the monkey's service country?");
            inServiceCountry = scanner.nextLine();
            while (inServiceCountry.isEmpty()) {
                System.out.println("Must enter monkey's service country. Try again:");
                inServiceCountry = scanner.nextLine();
            }
        }
        else {
            reserved = false;
            inServiceCountry = "N/A";
        }

        // New Monkey object is created with intake info and then added to the monkey list
        Monkey monkey = new Monkey(name,tailLength,height,bodyLength,species,gender,age,weight,acquisitionDate,acquisitionCountry,trainingStatus,reserved,inServiceCountry);
        monkeyList.add(monkey);
        System.out.println("The monkey has been added to the system.\n\n");
    }

    // This method reserves an animal for a user
    public static void reserveAnimal(Scanner scanner) {
        System.out.println("What type of animal would you like to reserve? (dog/monkey)");
        String animalType = scanner.nextLine();
        // Input verification for invalid animal types
        while (!animalType.equals("dog") && !animalType.equals("monkey")) {
            System.out.println("Please enter a valid animal type:");
            animalType = scanner.nextLine();
        }
        System.out.println("In which country do you require service?");
        String inServiceCountry = scanner.nextLine();
        // Input validation added for inServiceCountry
        while (inServiceCountry.isEmpty()) {
            System.out.println("Must enter a country. Try again:");
            inServiceCountry = scanner.nextLine();
        }
        if (animalType.equals("dog")) {
            for (Dog dog: dogList) {
                if (dog.getInServiceLocation().equalsIgnoreCase(inServiceCountry) && dog.getTrainingStatus().equals("in service") && !dog.getReserved()) {
                    dog.setReserved(true);
                    System.out.println("You have reserved " + dog.getName() + ", the dog.\n\n");
                    return;
                }
            }
            System.out.println("There are no dogs available in this country\n\n");
        }
        else {
            for (Monkey monkey: monkeyList) {
                if (monkey.getInServiceLocation().equalsIgnoreCase(inServiceCountry) && monkey.getTrainingStatus().equals("in service") && !monkey.getReserved()) {
                    monkey.setReserved(true);
                    System.out.println("You have reserved " + monkey.getName() + ", the monkey.\n\n");
                    return;
                }
            }
            System.out.println("There are no monkeys available in this country\n\n");
        }

    }

    // This method prints all animals when given a list type (dog/monkey/available)
    public static void printAnimals(String listType) {
        switch (listType) {   // This switch statement is used to print the correct animals for the given list type
            // No default case is required because it is impossible for
            // the user to enter an invalid list type
            case "dog" -> {
                for (Dog dog : dogList) {
                    System.out.println("Name: " + dog.getName());
                    System.out.println("Training Status: " + dog.getTrainingStatus());
                    System.out.println("Acquired in: " + dog.getAcquisitionLocation());
                    System.out.println("Reserved: " + dog.getReserved());
                    System.out.println();
                }
            }
            case "monkey" -> {
                for (Monkey monkey : monkeyList) {
                    System.out.println("Name: " + monkey.getName());
                    System.out.println("Training Status: " + monkey.getTrainingStatus());
                    System.out.println("Acquired in: " + monkey.getAcquisitionLocation());
                    System.out.println("Reserved: " + monkey.getReserved());
                    System.out.println();
                }
            }
            case "available" -> {
                for (Dog dog : dogList) {
                    if (dog.getTrainingStatus().equals("in service") && !dog.getReserved()) {
                        System.out.println("Name: " + dog.getName());
                        System.out.println("Animal Type: Dog");
                        System.out.println("Training Status: " + dog.getTrainingStatus());
                        System.out.println("Acquired in: " + dog.getAcquisitionLocation());
                        System.out.println();
                    }
                }
                for (Monkey monkey : monkeyList) {
                    if (monkey.getTrainingStatus().equals("in service") && !monkey.getReserved()) {
                        System.out.println("Name: " + monkey.getName());
                        System.out.println("Animal Type: Monkey");
                        System.out.println("Training Status: " + monkey.getTrainingStatus());
                        System.out.println("Acquired in: " + monkey.getAcquisitionLocation());
                        System.out.println();
                    }
                }
            }
        }
    }
}