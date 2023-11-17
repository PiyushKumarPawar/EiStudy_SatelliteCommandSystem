import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Define the Satellite class to manage satellite operations
class Satellite {
    private String stateFile = "satellite_state.txt"; // File to store satellite state
    private String orientation; // Satellite orientation (North/South/East/West)
    private String solarPanels; // Status of solar panels (Active/Inactive)
    private int dataCollected; // Amount of data collected by the satellite

    // Constructor to initialize the satellite state by loading it from a file
    public Satellite() {
        loadState();
    }

    // Method to load the satellite state from a file
    private void loadState() {
        try {
            // Read from the state file
            BufferedReader reader = new BufferedReader(new FileReader(stateFile));
            // Read each line of the state file
            String orientation = reader.readLine();
            String solarPanels = reader.readLine();
            String dataCollected = reader.readLine();

            // If all necessary data is available, set the satellite state
            if (orientation != null && solarPanels != null && dataCollected != null) {
                this.orientation = orientation;
                this.solarPanels = solarPanels;
                this.dataCollected = Integer.parseInt(dataCollected);
                System.out.println("Satellite state loaded.");
            } else {
                // If data is incomplete or file is corrupt, set default state
                System.out.println("Invalid state file. Using default state.");
                initializeDefaultState();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            // If the state file is not found, set default state
            System.out.println("State file not found. Using default state.");
            initializeDefaultState();
        } catch (IOException | NumberFormatException e) {
            // If an error occurs while reading state, set default state
            System.out.println("Error occurred while loading state: " + e.getMessage());
            initializeDefaultState();
        }
    }

    // Method to set default values for the satellite state
    private void initializeDefaultState() {
        this.orientation = "North";
        this.solarPanels = "Inactive";
        this.dataCollected = 0;
    }

    // Method to save the satellite state to a file
    private void saveState() {
        try {
            // Write the satellite state to the state file
            BufferedWriter writer = new BufferedWriter(new FileWriter(stateFile));
            writer.write(this.orientation + "\n" + this.solarPanels + "\n" + this.dataCollected);
            writer.close();
            System.out.println("Satellite state saved.");
        } catch (IOException e) {
            // If an error occurs while saving, display an error message
            System.out.println("Error occurred while saving state: " + e.getMessage());
        }
    }

    // Method to log the satellite state changes
    private void logState(String instruction) {
        System.out.println(instruction + " - Current State: " + getState());
    }

    // Method to get the current satellite state as a map
    public Map<String, Object> getState() {
        Map<String, Object> stateMap = new HashMap<>();
        stateMap.put("Orientation", this.orientation);
        stateMap.put("Solar Panels", this.solarPanels);
        stateMap.put("Data Collected", this.dataCollected);
        return stateMap;
    }

    // Method to rotate the satellite to a specified direction
    public void rotate(String direction) {
        System.out.println("Rotating satellite to " + direction + ".");
        this.orientation = direction;
        saveState();
        logState("Rotate");
    }

    // Method to activate the solar panels of the satellite
    public void activatePanels() {
        System.out.println("Activating solar panels.");
        this.solarPanels = "Active";
        saveState();
        logState("Activate Panels");
    }

    // Method to deactivate the solar panels of the satellite
    public void deactivatePanels() {
        System.out.println("Deactivating solar panels.");
        this.solarPanels = "Inactive";
        saveState();
        logState("Deactivate Panels");
    }

    // Method to collect data using the satellite
    public void collectData() {
        try {
            if (this.solarPanels.equals("Active")) {
                System.out.println("Collecting data.");
                this.dataCollected += 10;
                saveState();
                logState("Collect Data");
            } else {
                System.out.println("Cannot collect data. Solar panels are inactive.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred during data collection: " + e.getMessage());
        }
    }
}

// Define the SatelliteController class to manage satellite commands
class SatelliteController {
    private Satellite satellite;

    // Constructor to initialize with a Satellite object
    public SatelliteController(Satellite satellite) {
        this.satellite = satellite;
    }

    // Method to display the command menu
    public void displayMenu() {
        System.out.println("\nSatellite Command System Menu:");
        System.out.println("1. Display State");
        System.out.println("2. Rotate");
        System.out.println("3. Activate Solar Panels");
        System.out.println("4. Deactivate Solar Panels");
        System.out.println("5. Collect Data");
        System.out.println("6. Exit");
    }

    // Method to execute user commands based on input choice
    public void executeCommand(String choice) {
        try {
            switch (choice) {
                case "1":
                    System.out.println("Current State - " + satellite.getState());
                    break;
                case "2":
                    System.out.print("Enter the rotation direction (North/South/East/West): ");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String direction = reader.readLine().toUpperCase();
                    satellite.rotate(direction);
                    break;
                case "3":
                    satellite.activatePanels();
                    break;
                case "4":
                    satellite.deactivatePanels();
                    break;
                case "5":
                    satellite.collectData();
                    break;
                case "6":
                    System.out.println("Exiting the Satellite Command System.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error occurred while executing the command: " + e.getMessage());
        }
    }
}

// Main class to run the satellite control system
public class Main {
    public static void main(String[] args) {
        try {
            Satellite satellite = new Satellite();
            SatelliteController controller = new SatelliteController(satellite);

            // Continuously display the menu and execute commands until user exits
            while (true) {
                controller.displayMenu();
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter your choice (1-6): ");
                String userChoice = reader.readLine();
                controller.executeCommand(userChoice);
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
