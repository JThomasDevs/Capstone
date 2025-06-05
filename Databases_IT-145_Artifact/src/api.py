import requests
from animals import Dog, Monkey

# API Client for RescueServer.java
class RescueAPI:
    """
    API client for communicating with the Java backend (RescueServer.java).
    Encapsulates all HTTP requests, providing a clean interface for the frontend.
    Handles double-encoded JSON responses from the backend for robustness.
    """
    def __init__(self, base_url="http://localhost:8647"):
        self.base_url = base_url

    def _fill_missing_dog_fields(self, data):
        required = ["name", "breed", "age", "gender", "weight", "acquisitionDate", "acquisitionCountry", "trainingStatus", "reserved", "inServiceCountry"]
        return {k: data.get(k, None) for k in required}

    def _fill_missing_monkey_fields(self, data):
        required = ["name", "species", "age", "gender", "weight", "acquisitionDate", "acquisitionCountry", "trainingStatus", "reserved", "inServiceCountry", "tailLength", "height", "bodyLength"]
        return {k: data.get(k, None) for k in required}

    def get_dogs(self):
        response = requests.get(f"{self.base_url}/dogs")
        data = response.json()
        return [Dog(**self._fill_missing_dog_fields(dog)) for dog in data]

    def get_monkeys(self):
        response = requests.get(f"{self.base_url}/monkeys")
        data = response.json()
        return [Monkey(**self._fill_missing_monkey_fields(monkey)) for monkey in data]

    def get_available_animals(self):
        response = requests.get(f"{self.base_url}/available")
        data = response.json()
        dogs = [Dog(**self._fill_missing_dog_fields(dog)) for dog in data.get("dogs", [])]
        monkeys = [Monkey(**self._fill_missing_monkey_fields(monkey)) for monkey in data.get("monkeys", [])]
        return {"dogs": dogs, "monkeys": monkeys}

    def add_dog(self, dog: Dog) -> bool:
        response = requests.post(f"{self.base_url}/dogs", json=dog.__dict__)
        response.raise_for_status()
        return response.json()["success"]

    def add_monkey(self, monkey: Monkey) -> bool:
        response = requests.post(f"{self.base_url}/monkeys", json=monkey.__dict__)
        response.raise_for_status()
        return response.json()["success"]

    def reserve_animal(self, animal_type: str, name: str, country: str) -> bool:
        response = requests.post(
            f"{self.base_url}/reserve/{animal_type}/{name}",
            params={"country": country}
        )
        response.raise_for_status()
        return response.json()["success"]
