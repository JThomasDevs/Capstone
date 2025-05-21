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
    breed: str = None

@dataclass
class Monkey(Animal):
    species: str = None
    tail_length: str = None
    height: str = None
    body_length: str = None

class RescueSystemAPI:
    def __init__(self, java_path: str = None):
        if java_path:
            self.java_cmd = f"java -cp {java_path} com.rescueanimals.controllers.Driver"
        else:
            base_path = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
            target_path = os.path.join(base_path, "target", "classes")
            deps_path = os.path.join(base_path, "target", "dependency", "gson-2.10.1.jar")
            classpath = f"{target_path}{os.pathsep}{deps_path}"
            self.java_cmd = f"java -cp \"{classpath}\" com.rescueanimals.controllers.Driver"

    def _run_java_command(self, args: List[str]) -> dict:
        """Execute a Java command and return the parsed JSON response"""
        try:
            cmd = f"{self.java_cmd} {' '.join(args)}"
            print(f"Executing command: {cmd}")  # Debug output
            result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
            
            print(f"Return code: {result.returncode}")  # Debug output
            print(f"Stdout: {result.stdout}")  # Debug output
            print(f"Stderr: {result.stderr}")  # Debug output
            
            if result.returncode != 0:
                raise Exception(f"Java command failed: {result.stderr}")
            
            if not result.stdout.strip():
                raise Exception("Empty response from Java backend")
            
            return json.loads(result.stdout)
        except json.JSONDecodeError as e:
            print(f"JSON decode error: {str(e)}")  # Debug output
            raise Exception(f"Invalid JSON response from Java backend: {str(e)}")
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