"""
Streamlit frontend for the Rescue Animal System.

This module provides a user-friendly web interface for interacting with the Java REST API backend.
It allows users to add, view, and reserve rescue animals (dogs and monkeys).

Design rationale:
- Uses Streamlit for rapid UI development and easy deployment.
- Communicates with the Java backend via HTTP/REST, decoupling frontend and backend logic.
- Data models mirror Java classes for seamless serialization/deserialization.
- Explicit column mapping and ordering for clear, user-friendly data presentation.
"""

import streamlit as st
import pandas as pd
import requests
import json
from dataclasses import dataclass
from typing import Optional, List, Dict

# Data models to match Java classes
@dataclass
class Animal:
    """
    Base data model for all animals.
    Mirrors the Java RescueAnimal class for consistent API communication.
    Fields are kept as strings for easy JSON serialization and display.
    """
    name: str
    gender: str
    age: str
    weight: str
    acquisitionDate: str
    acquisitionCountry: str
    trainingStatus: str
    reserved: bool
    inServiceCountry: Optional[str] = None

@dataclass
class Dog(Animal):
    """
    Data model for dogs, extends Animal.
    Includes breed as a unique field.
    """
    breed: str = None

@dataclass
class Monkey(Animal):
    """
    Data model for monkeys, extends Animal.
    Includes species and measurement fields unique to monkeys.
    """
    species: str = None
    tailLength: str = None
    height: str = None
    bodyLength: str = None

# API Client for RescueServer.java
class RescueAPI:
    """
    API client for communicating with the Java backend (RescueServer.java).
    Encapsulates all HTTP requests, providing a clean interface for the frontend.
    Handles double-encoded JSON responses from the backend for robustness.
    """
    def __init__(self, base_url="http://localhost:8647/api"):
        self.base_url = base_url

    def get_dogs(self) -> List[Dog]:
        """
        Fetches all dogs from the backend.
        Returns a list of Dog objects.
        Handles double-encoded JSON for compatibility with Java's response format.
        """
        response = requests.get(f"{self.base_url}/dogs")
        response.raise_for_status()
        data = response.json()
        if isinstance(data, str):
            data = json.loads(data)
        return [Dog(**dog_data) for dog_data in data.get("dogs", [])]

    def get_monkeys(self) -> List[Monkey]:
        """
        Fetches all monkeys from the backend.
        Returns a list of Monkey objects.
        Handles double-encoded JSON for compatibility with Java's response format.
        """
        response = requests.get(f"{self.base_url}/monkeys")
        response.raise_for_status()
        data = response.json()
        if isinstance(data, str):
            data = json.loads(data)
        return [Monkey(**monkey_data) for monkey_data in data.get("monkeys", [])]

    def get_available(self) -> Dict[str, List]:
        """
        Fetches all available (unreserved) animals from the backend.
        Returns a dict with lists of Dog and Monkey objects.
        """
        response = requests.get(f"{self.base_url}/available")
        response.raise_for_status()
        data = response.json()
        if isinstance(data, str):
            data = json.loads(data)
        available = {"dogs": [], "monkeys": []}
        for animal in data.get("available", []):
            if "breed" in animal:
                available["dogs"].append(Dog(**animal))
            else:
                available["monkeys"].append(Monkey(**animal))
        return available

    def add_dog(self, dog: Dog) -> bool:
        """
        Adds a new dog to the backend.
        Returns True if successful, False otherwise.
        """
        response = requests.post(f"{self.base_url}/dogs", json=dog.__dict__)
        response.raise_for_status()
        return response.json()["success"]

    def add_monkey(self, monkey: Monkey) -> bool:
        """
        Adds a new monkey to the backend.
        Returns True if successful, False otherwise.
        """
        response = requests.post(f"{self.base_url}/monkeys", json=monkey.__dict__)
        response.raise_for_status()
        return response.json()["success"]

    def reserve_animal(self, animal_type: str, name: str, country: str) -> bool:
        """
        Reserves an animal for service in a given country.
        Returns True if successful, False otherwise.
        """
        response = requests.post(
            f"{self.base_url}/reserve/{animal_type}/{name}",
            params={"country": country}
        )
        response.raise_for_status()
        return response.json()["success"]

# Configure the page
# Use a wide layout and custom title.
# This ensures the app uses the full browser width
st.set_page_config(
    page_title="Rescue Animal System",
    page_icon=None,
    layout="wide"
)

# CSS for the navbar
st.markdown("""
<style>
    .nav-button {
        width: 100%;
        padding: 10px;
        text-align: center;
        background-color: #f0f2f6;
        border-radius: 5px;
        margin: 2px;
        cursor: pointer;
    }
    .nav-button:hover {
        background-color: #e0e2e6;
    }
    .selected {
        background-color: #1f77b4;
        color: white;
    }
</style>
""", unsafe_allow_html=True)

# Initialize the API
api = RescueAPI()

def main():
    """
    Main entry point for the Streamlit app.
    Sets up navigation and routes to the appropriate page handler.
    Uses session state to persist navigation state across reruns.
    """
    st.title("Rescue Animal System")
    
    # Create navigation bar using columns
    cols = st.columns(4)
    pages = ["Home", "Add New Animal", "View Animals", "Reserve Animal"]
    
    # Get current page from session state or set default
    if 'current_page' not in st.session_state:
        st.session_state.current_page = "Home"
    
    # Create navigation buttons
    for i, page in enumerate(pages):
        selected = st.session_state.current_page == page
        if cols[i].button(page, key=f"nav_{page}", use_container_width=True):
            st.session_state.current_page = page
    
    st.markdown("---")  # Horizontal line under navbar
    
    # Display the selected page
    if st.session_state.current_page == "Home":
        show_home()
    elif st.session_state.current_page == "Add New Animal":
        show_add_animal()
        
    elif st.session_state.current_page == "View Animals":
        show_view_animals()
    elif st.session_state.current_page == "Reserve Animal":
        show_reserve_animal()

def show_home():
    """
    Displays the home page with a welcome message and usage instructions.
    Keeps the UI user-friendly and self-explanatory for new users.
    """
    st.header("Welcome to the Rescue Animal System")
    st.write("""
    This system helps manage and track rescue animals that are being trained to perform service tasks.
    Use the sidebar to navigate through different functions:
    
    - **Add New Animal**: Register a new dog or monkey into the system
    - **View Animals**: See all registered animals and their status
    - **Reserve Animal**: Reserve an available service animal
    """)

def show_add_animal():
    """
    Displays the form for adding a new animal (dog or monkey).
    Handles all validation and feedback for user input.
    Uses session state to prevent duplicate submissions on rerun.
    """
    st.header("Add New Animal")
    
    # Use session state to track form submission
    if 'form_submitted' not in st.session_state:
        st.session_state.form_submitted = False
    
    animal_type = st.selectbox("Select Animal Type", ["Dog", "Monkey"])
    
    with st.form(f"add_{animal_type.lower()}_form"):
        # Add help text explaining the form
        st.markdown("""
        Please fill out all fields. Fields marked with * are required.
        All measurements should be in the specified units.
        """)
        
        name = st.text_input("Name*", help="Enter the animal's name (must be unique)")
        gender = st.selectbox("Gender*", ["Male", "Female"])
        
        # Age validation
        age_col, weight_col = st.columns(2)
        with age_col:
            age = st.text_input("Age* (years)", 
                              help="Enter the animal's age in years (must be a positive number)")
        
        # Weight validation
        with weight_col:
            weight = st.text_input("Weight* (lbs)", 
                                 help="Enter the animal's weight in pounds (must be a positive number)")
        
        acquisition_date = st.date_input("Acquisition Date*", 
                                       help="Select the date when the animal was acquired")
        
        acquisition_country = st.text_input("Acquisition Country*", 
                                          help="Enter the country where the animal was acquired")
        
        training_status = st.selectbox(
            "Training Status*",
            ["intake", "in service", "phase I", "phase II", "phase III", "phase IV", "phase V"],
            help="Select the current training phase of the animal"
        )
        
        if animal_type == "Dog":
            breed = st.text_input("Breed*", help="Enter the dog's breed")
        else:  # Monkey
            species = st.selectbox(
                "Species*",
                ["capuchin", "guenon", "macaque", "marmoset", "squirrel monkey", "tamarin"],
                help="Select the monkey's species"
            )
            
            # Organize monkey measurements in columns
            col1, col2, col3 = st.columns(3)
            with col1:
                tail_length = st.text_input("Tail Length* (ft)", 
                                          help="Enter the tail length in feet")
            with col2:
                height = st.text_input("Height* (ft)", 
                                     help="Enter the height in feet")
            with col3:
                body_length = st.text_input("Body Length* (ft)", 
                                          help="Enter the body length in feet")
        
        submitted = st.form_submit_button("Add Animal")
        
        if submitted:
            # Validate required fields
            if not all([name, age, weight, acquisition_country]):
                st.error("Please fill in all required fields marked with *")
                return
            
            # Validate numeric fields
            try:
                age_float = float(age)
                weight_float = float(weight)
                if age_float <= 0 or weight_float <= 0:
                    st.error("Age and weight must be positive numbers")
                    return
            except ValueError:
                st.error("Age and weight must be valid numbers")
                return
            
            try:
                # Create the base animal data
                animal_data = {
                    "name": name.strip(),
                    "gender": gender.lower(),
                    "age": str(age_float),
                    "weight": str(weight_float),
                    "acquisitionDate": acquisition_date.strftime("%m-%d-%Y"),
                    "acquisitionCountry": acquisition_country.strip(),
                    "trainingStatus": training_status,
                    "reserved": False,
                    "inServiceCountry": None
                }
                
                if animal_type == "Dog":
                    if not breed:
                        st.error("Please enter the dog's breed")
                        return
                    
                    # Create dog with breed
                    animal_data["breed"] = breed.strip()
                    # Debug logging
                    st.write("Creating dog with data:", animal_data)
                    dog = Dog(**animal_data)
                    success = api.add_dog(dog)
                    # Debug logging
                    st.write("Add dog result:", success)
                else:
                    # Validate monkey measurements
                    try:
                        tail_float = float(tail_length)
                        height_float = float(height)
                        body_float = float(body_length)
                        if any(m <= 0 for m in [tail_float, height_float, body_float]):
                            st.error("All measurements must be positive numbers")
                            return
                    except ValueError:
                        st.error("All measurements must be valid numbers")
                        return
                    
                    # Create monkey with measurements
                    animal_data.update({
                        "species": species,
                        "tailLength": str(tail_float),
                        "height": str(height_float),
                        "bodyLength": str(body_float)
                    })
                    monkey = Monkey(**animal_data)
                    success = api.add_monkey(monkey)
                
                if success:
                    st.success(f"{animal_type} added successfully!")
                    st.session_state.form_submitted = True
                    st.rerun()
                else:
                    st.error(f"Failed to add {animal_type.lower()}. The name may already be taken.")
            except Exception as e:
                st.error(f"An error occurred: {str(e)}")
                # Debug logging
                st.write("Exception details:", str(e))

def show_view_animals():
    """
    Displays all animals in the system, separated by type and availability.
    Uses tabs for a clean, organized view.
    Handles API errors gracefully for a robust user experience.
    """
    st.header("View Animals")
    
    # Create tabs for different views
    tab1, tab2, tab3 = st.tabs(["Dogs", "Monkeys", "Available Animals"])
    
    try:
        with tab1:  # Dogs
            animals = api.get_dogs()
            show_animals_table(animals, "dog")
            
        with tab2:  # Monkeys
            animals = api.get_monkeys()
            show_animals_table(animals, "monkey")
            
        with tab3:  # Available Animals
            available = api.get_available()
            show_available_animals(available)
    except Exception as e:
        st.error(f"Error fetching animals: {str(e)}")

def show_animals_table(animals, animal_type):
    """
    Displays a table of animals (dogs or monkeys) with user-friendly column names and units.
    Uses a FILLER placeholder to insert type-specific columns in a consistent order.
    Ensures only existing columns are displayed, preventing index errors.
    """
    if not animals:
        st.warning(f"No {animal_type}s currently available.")
        return
    
    # Convert to DataFrame for display
    df = pd.DataFrame([vars(animal) for animal in animals])
    
    # Add units to measurements
    if 'age' in df.columns:
        df['age'] = df['age'].astype(str) + ' years'
    if 'weight' in df.columns:
        df['weight'] = df['weight'].astype(str) + ' lbs'
    if 'tailLength' in df.columns:
        df['tailLength'] = df['tailLength'].astype(str) + ' ft'
    if 'height' in df.columns:
        df['height'] = df['height'].astype(str) + ' ft'
    if 'bodyLength' in df.columns:
        df['bodyLength'] = df['bodyLength'].astype(str) + ' ft'
    
    # Format boolean values first
    if 'reserved' in df.columns:
        df['reserved'] = df['reserved'].map({True: 'Yes', False: 'No'})
    
    # Rename columns for better display
    column_map = {
        'name': 'Name',
        'gender': 'Gender',
        'breed': 'Breed',
        'species': 'Species',
        'age': 'Age (years)',
        'weight': 'Weight (lbs)',
        'acquisitionDate': 'Acquisition Date',
        'acquisitionCountry': 'Acquisition Country',
        'trainingStatus': 'Training Status',
        'inServiceCountry': 'Service Country',
        'tailLength': 'Tail Length (ft)',
        'bodyLength': 'Body Length (ft)',
        'height': 'Height (ft)',
        'reserved': 'Reserved'
    }
    df = df.rename(columns=column_map)
    
    # Define special columns for each type
    dog_special = ['Breed']
    monkey_special = ['Species', 'Tail Length (ft)', 'Height (ft)', 'Body Length (ft)']
    
    # Define base columns with FILLER placeholder
    columns = ','.join(['Name', 'Gender', 'Age (years)', 'Weight (lbs)', 'FILLER', 'Acquisition Date', 'Acquisition Country', 'Service Country', 'Training Status', 'Reserved'])
    
    # Replace FILLER with type-specific columns
    if animal_type == "dog":
        columns = columns.replace('FILLER', ','.join(dog_special))
    else:  # monkey
        columns = columns.replace('FILLER', ','.join(monkey_special))
    
    # Convert back to list and filter existing columns
    final_cols = [col for col in columns.split(',') if col in df.columns]
    df = df[final_cols]
    
    # Display the DataFrame
    st.dataframe(
        df,
        use_container_width=True,
        hide_index=True
    )

def show_available_animals(available):
    """
    Displays available (unreserved) dogs and monkeys in separate sections.
    Calls show_animals_table for each type for consistent formatting.
    """
    st.subheader("Available Dogs")
    show_animals_table(available["dogs"], "dog")
    
    st.subheader("Available Monkeys")
    show_animals_table(available["monkeys"], "monkey")

def show_reserve_animal():
    """
    Displays the reservation form for available animals.
    Handles user selection and reservation logic, including error feedback.
    Updates the UI and session state on successful reservation.
    """
    st.header("Reserve Animal")
    
    try:
        # Get available animals
        available = api.get_available()
        
        # Let user choose animal type
        animal_type = st.selectbox("Select Animal Type", ["Dog", "Monkey"])
        
        # Get the appropriate list of animals
        animals = available["dogs"] if animal_type == "Dog" else available["monkeys"]
        
        if not animals:
            st.warning(f"No {animal_type.lower()}s available for reservation")
            return
        
        # Create selection options
        animal_names = [animal.name for animal in animals]
        selected_name = st.selectbox(f"Select {animal_type}", animal_names)
        
        # Get country for service
        country = st.text_input("Service Country")
        
        if st.button("Reserve Animal"):
            if not country:
                st.error("Please enter a service country")
                return
            
            try:
                success = api.reserve_animal(
                    animal_type.lower(),
                    selected_name,
                    country
                )
                
                if success:
                    st.success(f"{animal_type} reserved successfully!")
                    # Clear the form
                    st.session_state.current_page = "View Animals"
                    st.rerun()
                else:
                    st.error("Failed to reserve animal. Please try again.")
            
            except Exception as e:
                st.error(f"Error reserving animal: {str(e)}")
    
    except Exception as e:
        st.error(f"Error loading available animals: {str(e)}")

if __name__ == "__main__":
    main() 