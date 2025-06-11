import requests
from animals import Dog, Monkey

# API Client for RescueServer.java
class RescueAPI:
    """
    API client for communicating with the Java backend (RescueServer.java).
    
    This class encapsulates all HTTP requests to the rescue system backend, providing a clean interface for the frontend. It handles double-encoded JSON responses from the backend for robustness.
    
    Attributes:
        base_url (str): The base URL for the API endpoints
    """
    def __init__(self, base_url="http://localhost:8647"):
        """
        Initialize the RescueAPI client.
        
        Args:
            base_url (str): The base URL for the API endpoints. Defaults to http://localhost:8647
        """
        self.base_url = base_url

    def _fill_missing_dog_fields(self, data):
        """
        Fill in missing fields for a dog object with None values.
        
        Args:
            data (dict): Dictionary containing dog data
            
        Returns:
            dict: Dictionary with all required dog fields, missing fields filled with None
        """
        required = ["name", "breed", "age", "gender", "weight", "acquisitionDate", "acquisitionCountry", "trainingStatus", "reserved", "inServiceCountry"]
        return {k: data.get(k, None) for k in required}

    def _fill_missing_monkey_fields(self, data):
        """
        Fill in missing fields for a monkey object with None values.
        
        Args:
            data (dict): Dictionary containing monkey data
            
        Returns:
            dict: Dictionary with all required monkey fields, missing fields filled with None
        """
        required = ["name", "species", "age", "gender", "weight", "acquisitionDate", "acquisitionCountry", "trainingStatus", "reserved", "inServiceCountry", "tailLength", "height", "bodyLength"]
        return {k: data.get(k, None) for k in required}

    def get_dogs(self):
        """
        Retrieve all dogs from the rescue system.
        
        Returns:
            list[Dog]: List of Dog objects representing all dogs in the system
        """
        response = requests.get(f"{self.base_url}/dogs")
        data = response.json()
        return [Dog(**self._fill_missing_dog_fields(dog)) for dog in data]

    def get_monkeys(self):
        """
        Retrieve all monkeys from the rescue system.
        
        Returns:
            list[Monkey]: List of Monkey objects representing all monkeys in the system
        """
        response = requests.get(f"{self.base_url}/monkeys")
        data = response.json()
        return [Monkey(**self._fill_missing_monkey_fields(monkey)) for monkey in data]

    def get_available_animals(self):
        """
        Retrieve all available (non-reserved) animals from the rescue system.
        
        Returns:
            dict: Dictionary containing two lists:
                - dogs: List of available Dog objects
                - monkeys: List of available Monkey objects
        """
        response = requests.get(f"{self.base_url}/available")
        data = response.json()
        dogs = [Dog(**self._fill_missing_dog_fields(dog)) for dog in data.get("dogs", [])]
        monkeys = [Monkey(**self._fill_missing_monkey_fields(monkey)) for monkey in data.get("monkeys", [])]
        return {"dogs": dogs, "monkeys": monkeys}

    def add_dog(self, dog: Dog) -> bool:
        """
        Add a new dog to the rescue system.
        
        Args:
            dog (Dog): Dog object to add to the system
            
        Returns:
            bool: True if the dog was successfully added, False otherwise
            
        Raises:
            requests.exceptions.HTTPError: If the request fails
        """
        response = requests.post(f"{self.base_url}/dogs", json=dog.__dict__)
        response.raise_for_status()
        return response.json()["success"]

    def add_monkey(self, monkey: Monkey) -> bool:
        """
        Add a new monkey to the rescue system.
        
        Args:
            monkey (Monkey): Monkey object to add to the system
            
        Returns:
            bool: True if the monkey was successfully added, False otherwise
            
        Raises:
            requests.exceptions.HTTPError: If the request fails
        """
        response = requests.post(f"{self.base_url}/monkeys", json=monkey.__dict__)
        response.raise_for_status()
        return response.json()["success"]

    def reserve_animal(self, animal_type: str, name: str, country: str) -> bool:
        """
        Reserve an animal for service in a specific country.
        
        Args:
            animal_type (str): Type of animal ('dog' or 'monkey')
            name (str): Name of the animal to reserve
            country (str): Country where the animal will be in service
            
        Returns:
            bool: True if the animal was successfully reserved, False otherwise
            
        Raises:
            requests.exceptions.HTTPError: If the request fails
        """
        response = requests.post(
            f"{self.base_url}/reserve/{animal_type}/{name}",
            params={"country": country}
        )
        response.raise_for_status()
        return response.json()["success"]
