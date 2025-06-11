class Dog:
    """
    A class representing a service dog in the rescue system.
    
    Attributes:
        name (str): The name of the dog
        breed (str): The breed of the dog
        age (int): The age of the dog in years
        gender (str): The gender of the dog
        weight (float): The weight of the dog in pounds
        acquisitionDate (str): The date when the dog was acquired
        acquisitionCountry (str): The country where the dog was acquired
        trainingStatus (str): The current training status of the dog
        reserved (bool): Whether the dog is reserved for service
        inServiceCountry (str): The country where the dog is currently in service
    """
    def __init__(self, name, breed, age, gender, weight, acquisitionDate, acquisitionCountry, trainingStatus, reserved, inServiceCountry):
        self.name = name
        self.breed = breed
        self.age = age
        self.gender = gender
        self.weight = weight
        self.acquisitionDate = acquisitionDate
        self.acquisitionCountry = acquisitionCountry
        self.trainingStatus = trainingStatus
        self.reserved = reserved
        self.inServiceCountry = inServiceCountry

class Monkey:
    """
    A class representing a service monkey in the rescue system.
    
    Attributes:
        name (str): The name of the monkey
        species (str): The species of the monkey
        age (int): The age of the monkey in years
        gender (str): The gender of the monkey
        weight (float): The weight of the monkey in pounds
        acquisitionDate (str): The date when the monkey was acquired
        acquisitionCountry (str): The country where the monkey was acquired
        trainingStatus (str): The current training status of the monkey
        reserved (bool): Whether the monkey is reserved for service
        inServiceCountry (str): The country where the monkey is currently in service
        tailLength (float): The length of the monkey's tail in inches
        height (float): The height of the monkey in inches
        bodyLength (float): The body length of the monkey in inches
    """
    def __init__(self, name, species, age, gender, weight, acquisitionDate, acquisitionCountry, trainingStatus, reserved, inServiceCountry, tailLength, height, bodyLength):
        self.name = name
        self.species = species
        self.age = age
        self.gender = gender
        self.weight = weight
        self.acquisitionDate = acquisitionDate
        self.acquisitionCountry = acquisitionCountry
        self.trainingStatus = trainingStatus
        self.reserved = reserved
        self.inServiceCountry = inServiceCountry
        self.tailLength = tailLength
        self.height = height
        self.bodyLength = bodyLength 