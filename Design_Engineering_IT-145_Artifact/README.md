# Software Design and Engineering Artifact

This project is an enhanced version of my original IT-145 Animal Registry artifact, now featuring a RESTful Java backend and a user-friendly Python GUI. The application allows users to manage, view, and reserve rescue animals (dogs and monkeys) through a modern web interface.

## Features
- **RESTful API (Java, Javalin):** Exposes endpoints for managing animals and reservations.
- **Python Streamlit GUI:** Provides an intuitive web interface for interacting with the backend.
- **Modular Design:** Backend and frontend are decoupled, allowing for future expansion (e.g., database integration).

## API Endpoints
The Java backend exposes the following endpoints (default port: 8647):

- `GET /api/dogs` — List all dogs
- `GET /api/monkeys` — List all monkeys
- `GET /api/available` — List all available (unreserved) animals
- `POST /api/dogs` — Add a new dog (JSON body)
- `POST /api/monkeys` — Add a new monkey (JSON body)
- `POST /api/reserve/{type}/{name}?country=COUNTRY` — Reserve an animal for service in a country

## Requirements

- **Java 17** (or compatible version)
- **Maven** (for building the Java backend)
- **Python 3** (for the GUI)

## Building the Java Backend

1. Make sure you have Java 17 and Maven installed.
2. Open a terminal in the `Design_Engineering_IT-145_Artifact` directory.
3. Build the Java project with:
   ```
   mvn clean package
   ```
   This will create the JAR file in the `target/` directory, required for running the backend server.

## Running the Application

To simplify running both the backend and frontend, use the provided `run_both.py` script. This script automatically starts both the Java API server and the Streamlit GUI, and will shut down both processes when you exit.

### Steps:
1. Make sure you have built the Java project (the JAR file should be in `target/`).
2. Ensure you have Python 3 and the required packages installed:
   ```
   pip install -r requirements.txt
   ```
3. From the `Design_Engineering_IT-145_Artifact` directory, run:
   ```
   python run_both.py
   ```
4. The script will launch both the Java backend and the Streamlit GUI. Open the provided local URL in your browser to use the application.

**What does `run_both.py` do?**
- Starts the Java backend server (API)
- Starts the Streamlit web app (GUI)
- Handles shutdown of both processes if you exit or press Ctrl+C

## Basic Usage
- **Add New Animal:** Use the GUI to register a new dog or monkey. All required fields must be filled.
- **View Animals:** Browse all registered animals, separated by type and availability.
- **Reserve Animal:** Select an available animal and assign it to a service country.

## Original Artifacts
- [Original IT-145 Java Animal Registry App](https://github.com/JThomasDevs/SNHU-Portfolio/tree/main/Java%20Animal%20Registry%20App)

## Future Enhancements
- Database integration for persistent storage
---