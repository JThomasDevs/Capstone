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
from api import RescueAPI
from animals import Dog, Monkey

# Configure the page
# Use a wide layout and custom title.
# This ensures the app uses the full browser width
st.set_page_config(
    page_title="Rescue Animal Management System",
    page_icon="🐾",
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
            ["intake", "in service"],
            help="Select the current training phase of the animal"
        )
        
        # Service Country field: always enabled, only required if in service
        service_country = st.text_input(
            "Service Country*",
            help="Enter the country where the animal will serve"
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
            
            if training_status == "in service" and not service_country:
                st.error("Please enter the service country for an animal in service.")
                return
            
            try:
                # Create the base animal data
                animal_data = {
                    "name": name.strip(),
                    "gender": gender.lower(),
                    "age": str(age_float),
                    "weight": str(weight_float),
                    "acquisitionDate": acquisition_date.strftime("%Y-%m-%d"),
                    "acquisitionCountry": acquisition_country.strip(),
                    "trainingStatus": training_status,
                    "reserved": False,
                    "inServiceCountry": service_country.strip() if service_country else None
                }
                
                if animal_type == "Dog":
                    if not breed:
                        st.error("Please enter the dog's breed")
                        return
                    if training_status == "in service" and not service_country:
                        st.error("Please enter the service country for an animal in service.")
                        return
                    
                    animal_data["breed"] = breed.strip()
                    dog = Dog(**animal_data)
                    success = api.add_dog(dog)
                    st.success(f"Dog {name} added successfully!")
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
                    if training_status == "in service" and not service_country:
                        st.error("Please enter the service country for an animal in service.")
                        return
                    monkey = Monkey(**animal_data)
                    success = api.add_monkey(monkey)
                    st.success(f"Monkey {name} added successfully!")
                
                if success:
                    st.session_state.current_page = "View Animals"
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
            dogs = api.get_dogs()
            show_animals_table(dogs, "dog")
            
        with tab2:  # Monkeys
            monkeys = api.get_monkeys()
            show_animals_table(monkeys, "monkey")
            
        with tab3:  # Available Animals
            available_animals = api.get_available_animals()
            show_available_animals(available_animals)
    except Exception as e:
        st.error(f"Error fetching animals: {str(e)}")

def show_animals_table(animals, animal_type):
    """
    Displays a table of animals (dogs or monkeys) with user-friendly column names and units.
    Uses a FILLER placeholder to insert type-specific columns in a consistent order.
    Ensures only existing columns are displayed, preventing index errors.
    """
    if animal_type == "dog":
        columns = [
            ("Name", "name"),
            ("Gender", "gender"),
            ("Age (years)", "age"),
            ("Weight (lbs)", "weight"),
            ("Breed", "breed"),
            ("Acquisition Date", "acquisitionDate"),
            ("Acquisition Country", "acquisitionCountry"),
            ("Service Country", "inServiceCountry"),
            ("Training Status", "trainingStatus"),
            ("Reserved", "reserved"),
        ]
    elif animal_type == "monkey":
        columns = [
            ("Name", "name"),
            ("Gender", "gender"),
            ("Age (years)", "age"),
            ("Weight (lbs)", "weight"),
            ("Species", "species"),
            ("Tail Length (ft)", "tailLength"),
            ("Height (ft)", "height"),
            ("Body Length (ft)", "bodyLength"),
            ("Acquisition Date", "acquisitionDate"),
            ("Acquisition Country", "acquisitionCountry"),
            ("Service Country", "inServiceCountry"),
            ("Training Status", "trainingStatus"),
            ("Reserved", "reserved"),
        ]
    else:
        return

    data = []
    for animal in animals:
        row = []
        for col, attr in columns:
            value = getattr(animal, attr, "")
            if attr == "reserved":
                value = "Yes" if value else "No"
            elif attr == "age":
                value = f"{value} years"
            elif attr == "weight":
                value = f"{value} lbs"
            elif attr in ("tailLength", "height", "bodyLength"):
                value = f"{value} ft"
            row.append(value)
        data.append(row)

    # Remove the index column by setting index=False
    df = pd.DataFrame(data, columns=[col for col, _ in columns])
    st.dataframe(df, hide_index=True)

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
        available_animals = api.get_available_animals()
        
        # Let user choose animal type
        animal_type = st.selectbox("Select Animal Type", ["Dog", "Monkey"])
        
        # Get the appropriate list of animals
        animals = available_animals["dogs"] if animal_type == "Dog" else available_animals["monkeys"]
        
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