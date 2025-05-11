public class Monkey extends RescueAnimal {

    // Instance variables
    private String tailLength;
    private String height;
    private String bodyLength;
    private String species;

    // Constructor
    // This constructor requires all possible parameters to exist
    // before a Monkey object can be created.
    public Monkey(String name, String tailLength, String height, String bodyLength, String species, String gender, String age,
                  String weight, String acquisitionDate, String acquisitionCountry,
                  String trainingStatus, boolean reserved, String inServiceCountry) {
        setName(name);
        setTailLength(tailLength);
        setHeight(height);
        setBodyLength(bodyLength);
        setSpecies(species);
        setGender(gender);
        setAge(age);
        setWeight(weight);
        setAcquisitionDate(acquisitionDate);
        setAcquisitionLocation(acquisitionCountry);
        setTrainingStatus(trainingStatus);
        setReserved(reserved);
        setInServiceCountry(inServiceCountry);
    }
    //Accessors and mutators
    public void setTailLength(String tailLength) {
        this.tailLength = tailLength;
    }
    public String getTailLength() {
        return tailLength;
    }
    public void setHeight(String height) {
        this.height = height;
    }
    public String getHeight() {
        return height;
    }
    public void setBodyLength(String bodyLength) {
        this.bodyLength = bodyLength;
    }
    public String getBodyLength() {
        return bodyLength;
    }
    public void setSpecies(String species) {
        this.species = species;
    }
    public String getSpecies() {
        return species;
    }
}
