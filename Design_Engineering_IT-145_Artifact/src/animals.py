# Data models to match Java classes
from dataclasses import dataclass
from typing import Optional

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
