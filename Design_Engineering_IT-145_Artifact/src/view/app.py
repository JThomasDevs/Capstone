import streamlit as st
import pandas as pd
from datetime import datetime
import sys
from pathlib import Path
sys.path.append(str(Path(__file__).parent.parent))
from controllers.rescue_api import RescueSystemAPI, Dog, Monkey

# Configure the page
st.set_page_config(
    page_title="Rescue Animal System",
    page_icon="🐾",
    layout="wide"
)

# Custom CSS for the navbar
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
api = RescueSystemAPI()

def main():
    st.title("🐾 Rescue Animal System")
    
    # Create navigation bar using columns
    cols = st.columns(4)
    pages = ["Home", "Add New Animal", "View Animals", "Reserve Animal"]
    
    # Get current page from session state or set default
    if 'current_page' not in st.session_state:
        st.session_state.current_page = "Home"
    
    # Create navigation buttons
    for i, page in enumerate(pages):
        selected = st.session_state.current_page == page
        button_style = "selected" if selected else ""
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
    st.header("Welcome to the Rescue Animal System")
    st.write("""
    This system helps manage and track rescue animals that are being trained to perform service tasks.
    Use the sidebar to navigate through different functions:
    
    - **Add New Animal**: Register a new dog or monkey into the system
    - **View Animals**: See all registered animals and their status
    - **Reserve Animal**: Reserve an available service animal
    """)

def show_add_animal():
    st.header("Add New Animal")
    
    animal_type = st.selectbox("Select Animal Type", ["Dog", "Monkey"])
    
    with st.form(f"add_{animal_type.lower()}_form"):
        name = st.text_input("Name")
        gender = st.selectbox("Gender", ["Male", "Female"])
        age = st.text_input("Age")
        weight = st.text_input("Weight")
        acquisition_date = st.date_input("Acquisition Date")
        acquisition_country = st.text_input("Acquisition Country")
        training_status = st.selectbox("Training Status", ["intake", "in service", "phase I", "phase II", "phase III", "phase IV", "phase V"])
        
        if animal_type == "Dog":
            breed = st.text_input("Breed")
        else:  # Monkey
            species = st.selectbox("Species", ["capuchin", "guenon", "macaque", "marmoset", "squirrel monkey", "tamarin"])
            tail_length = st.text_input("Tail Length")
            height = st.text_input("Height")
            body_length = st.text_input("Body Length")
        
        submitted = st.form_submit_button("Add Animal")
        
        if submitted:
            try:
                common_data = {
                    "name": name,
                    "gender": gender.lower(),
                    "age": age,
                    "weight": weight,
                    "acquisition_date": acquisition_date.strftime("%m-%d-%Y"),
                    "acquisition_country": acquisition_country,
                    "training_status": training_status,
                    "reserved": False
                }
                
                if animal_type == "Dog":
                    dog = Dog(**common_data, breed=breed)
                    success = api.add_dog(dog)
                else:
                    monkey = Monkey(
                        **common_data,
                        species=species,
                        tail_length=tail_length,
                        height=height,
                        body_length=body_length
                    )
                    success = api.add_monkey(monkey)
                
                if success:
                    st.success(f"{animal_type} added successfully!")
                else:
                    st.error("Failed to add animal. Please try again.")
            except Exception as e:
                st.error(f"An error occurred: {str(e)}")

def show_view_animals():
    st.header("View Animals")
    
    view_type = st.radio("Select View", ["All Dogs", "All Monkeys", "Available Animals"])
    
    try:
        if view_type == "All Dogs":
            animals = api.get_dogs()
            show_animals_table(animals, "dog")
        elif view_type == "All Monkeys":
            animals = api.get_monkeys()
            show_animals_table(animals, "monkey")
        else:  # Available Animals
            available = api.get_available_animals()
            show_available_animals(available)
    except Exception as e:
        st.error(f"An error occurred: {str(e)}")

def show_animals_table(animals, animal_type):
    if not animals:
        st.info("No animals found.")
        return
    
    # Convert dataclass objects to dictionaries
    animal_dicts = [vars(animal) for animal in animals]
    df = pd.DataFrame(animal_dicts)
    
    if animal_type == "dog":
        st.dataframe(df[["name", "breed", "gender", "age", "training_status", "reserved"]])
    else:  # monkey
        st.dataframe(df[["name", "species", "gender", "age", "training_status", "reserved"]])

def show_available_animals(available):
    st.subheader("Available Dogs")
    show_animals_table(available["dogs"], "dog")
    
    st.subheader("Available Monkeys")
    show_animals_table(available["monkeys"], "monkey")

def show_reserve_animal():
    st.header("Reserve Animal")
    
    animal_type = st.selectbox("Select Animal Type", ["Dog", "Monkey"])
    country = st.text_input("Service Country")
    
    if st.button("Search Available Animals"):
        try:
            available = api.get_available_animals()
            animals = available["dogs"] if animal_type == "Dog" else available["monkeys"]
            
            if animals:
                animal_names = [a.name for a in animals]
                animal_name = st.selectbox("Select Animal", options=animal_names)
                
                if st.button("Reserve"):
                    success = api.reserve_animal(animal_type.lower(), animal_name, country)
                    if success:
                        st.success("Animal reserved successfully!")
                    else:
                        st.error("Failed to reserve animal. Please try again.")
            else:
                st.info(f"No available {animal_type.lower()}s found.")
        except Exception as e:
            st.error(f"An error occurred: {str(e)}")

if __name__ == "__main__":
    main() 