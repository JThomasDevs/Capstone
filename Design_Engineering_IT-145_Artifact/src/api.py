import requests
import json
from typing import List, Dict
from animals import Dog, Monkey

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
