package Objects;

import javafx.beans.property.*;

public abstract class Part {

    private final IntegerProperty partID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty inStock;
    private final IntegerProperty min;
    private final IntegerProperty max;


    //// Constructor
    public Part() {
        partID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }


    //// Getters
    public IntegerProperty partIDProperty() {
        return partID;
    }

    public StringProperty partNameProperty() {
        return name;
    }

    public DoubleProperty partPriceProperty() {
        return price;
    }

    public IntegerProperty partInvProperty() {
        return inStock;
    }

    public IntegerProperty partMinProperty() {
        return min;
    }

    public IntegerProperty partMaxProperty() {
        return max;
    }

    public int getPartID() {
        return this.partID.get();
    }

    public String getPartName() {
        return this.name.get();
    }

    public double getPartPrice() {
        return this.price.get();
    }

    public int getPartInStock() {
        return this.inStock.get();
    }

    public int getPartMin() {
        return this.min.get();
    }

    public int getPartMax() {
        return this.max.get();
    }


    //// Setters
    public void setPartID(int partID) {
        this.partID.set(partID);
    }

    public void setPartName(String name) {
        this.name.set(name);
    }

    public void setPartPrice(double price) {
        this.price.set(price);
    }

    public void setPartInStock(int inStock) {
        this.inStock.set(inStock);
    }

    public void setPartMin(int min) {
        this.min.set(min);
    }

    public void setPartMax(int max) {
        this.max.set(max);
    }


    //// Validation
    public static String isPartValid(String name, int min, int max, int inv, double price, String error){
        if (name == null) {
            error = error + "The name field is required. ";
        }
        if (inv < 1) {
            error = error + "The inventory count cannot be less than 1. ";
        }
        if (price <= 0) {
            error = error + "The price must be greater than $0. ";
        }
        if (max < min) {
            error = error + "The Max must be greater than or equal to the Min. ";
        }
        if (inv < min || inv > max) {
            error = error + "The inventory must be between the Min and Max values. ";
        }
        return error;
    }
}