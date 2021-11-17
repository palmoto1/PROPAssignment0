public class Variable {

    private final String id;
    private double value;

    public Variable(String id, double value){
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Illegal variable id");
        this.id = id;
        this.value = value;
    }


    public String getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public String toString(){
        return id + " = " + value;
    }
}
