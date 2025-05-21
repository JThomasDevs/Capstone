import subprocess
import json
import os
from typing import List, Dict, Optional
from dataclasses import dataclass
from datetime import datetime

@dataclass
class Animal:
    name: str
    gender: str
    age: str
    weight: str
    acquisition_date: str
    acquisition_country: str
    training_status: str
    reserved: bool
    in_service_country: Optional[str] = None

@dataclass
class Dog(Animal):
    breed: str

@dataclass
class Monkey(Animal):
    tail_length: str
    height: str
    body_length: str
    species: str

class RescueSystemAPI:
    def __init__(self, java_path: str = None):
        self.java_cmd = "java -cp ../../target/classes:../../target/dependency/* Driver"
        if os.name == 'nt':  # Windows
            self.java_cmd = "java -cp ../../target/classes;../../target/dependency/* Driver"
        
        if java_path:
            self.java_cmd = f"java -cp {java_path} Driver"

    def _run_java_command(self, args: List[str]) -> dict:
        """Execute a Java command and return the parsed JSON response"""
        try:
            cmd = f"{self.java_cmd} {' '.join(args)}"
            result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
            
            if result.returncode != 0:
                raise Exception(f"Java command failed: {result.stderr}")
            
            return json.loads(result.stdout)
        except json.JSONDecodeError:
            raise Exception("Invalid JSON response from Java backend")
        except Exception as e:
            raise Exception(f"Error running Java command: {str(e)}")

    def get_dogs(self) -> List[Dog]:
        """Get all dogs in the system"""
        result = self._run_java_command(["list-dogs"])
        return [Dog(**dog) for dog in result.get("dogs", [])]

    def get_monkeys(self) -> List[Monkey]:
        """Get all monkeys in the system"""
        result = self._run_java_command(["list-monkeys"])
        return [Monkey(**monkey) for monkey in result.get("monkeys", [])]

    def get_available_animals(self) -> Dict[str, List[Animal]]:
        """Get all available animals"""
        result = self._run_java_command(["list-available"])
        available = result.get("available", [])
        
        dogs = []
        monkeys = []
        for animal in available:
            if "breed" in animal:
                dogs.append(Dog(**animal))
            else:
                monkeys.append(Monkey(**animal))
        
        return {"dogs": dogs, "monkeys": monkeys}

    def add_dog(self, dog: Dog) -> bool:
        """Add a new dog to the system"""
        args = [
            "add-dog",
            dog.name,
            dog.breed,
            dog.gender,
            dog.age,
            dog.weight,
            dog.acquisition_date,
            dog.acquisition_country,
            dog.training_status,
            str(dog.reserved).lower(),
            dog.in_service_country or "N/A"
        ]
        result = self._run_java_command(args)
        return result.get("status") == "success"

    def add_monkey(self, monkey: Monkey) -> bool:
        """Add a new monkey to the system"""
        args = [
            "add-monkey",
            monkey.name,
            monkey.tail_length,
            monkey.height,
            monkey.body_length,
            monkey.species,
            monkey.gender,
            monkey.age,
            monkey.weight,
            monkey.acquisition_date,
            monkey.acquisition_country,
            monkey.training_status,
            str(monkey.reserved).lower(),
            monkey.in_service_country or "N/A"
        ]
        result = self._run_java_command(args)
        return result.get("status") == "success"

    def reserve_animal(self, animal_type: str, name: str, country: str) -> bool:
        """Reserve an animal for service"""
        if animal_type not in ["dog", "monkey"]:
            raise ValueError("Invalid animal type. Must be 'dog' or 'monkey'")
        
        args = ["reserve", animal_type, name, country]
        result = self._run_java_command(args)
        return result.get("status") == "success" 