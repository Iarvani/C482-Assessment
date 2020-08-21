package Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static jdk.nashorn.internal.runtime.JSType.isString;

public class Inventory {

    private static ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private static ObservableList<Part> partInventory = FXCollections.observableArrayList();
    //private static ObservableList<Part> partName = FXCollections.observableArrayList();
    private static int partIDCount = 0;
    private static int productIDCount = 0;
    public static boolean isIdentified;
    public static int index;

    public static void addPart(Part part) {
        partInventory.add(part);
    }

    public static void deletePart(Part part) {
        partInventory.remove(part);
    }

    public static void updatePart(int index, Part part) {
        partInventory.set(index, part);
    }

    public static void updateProduct(int index, Product product) {
        productInventory.set(index, product);
    }

    public static ObservableList<Product> getProductInventory() {
        return productInventory;
    }

    public static void addProduct(Product product) {
        productInventory.add(product);
    }

    public static void removeProduct(Product product) {
        productInventory.remove(product);
    }

    public static ObservableList<Part> getPartInventory() {
        return partInventory;
    }

    //public static ObservableList<Part> getPartName() {return partName; }

    public static int getPartIDCount() {
        partIDCount++;
        return partIDCount;
    }

    public static boolean validatePartDelete(Part part) {
        boolean isFound = false;
        for (int i = 0; i < productInventory.size(); i++) {
            if (productInventory.get(i).getProductParts().contains(part)) {
                isFound = true;
            }
        }
        return isFound;
    }

    public static boolean validateProductDelete(Product product) {
        boolean isFound = false;
        int productID = product.getProductID();
        for (int i = 0; i < productInventory.size(); i++) {
            if (productInventory.get(i).getProductID() == productID) {
                if (!productInventory.get(i).getProductParts().isEmpty()) {
                    isFound = true;
                }
            }
        }
        return isFound;
    }

    public static int lookupPart(String searchType) {
        char token;

        //check for numeric characters in search word
        //run a check for similarity of char 0 in observable array
        if (isInteger(searchType)) {
            for (int i = 0; i < partInventory.size(); i++) {
                if (Integer.parseInt(searchType) == partInventory.get(i).getPartID()) {
                    index = i;
                    isIdentified = true;
                    //testing purposes
                    System.out.println("(numeric)." + " " + partInventory.get(i).getPartID() + " " + partInventory.get(i).getPartName());

                    return index;
                }
            }
        }

        //check for alpha numeric characters in search word
        //run a check for similarity of char 0 in observable array
        if (isString(searchType)) {
            for (int i = 0; i < partInventory.size(); i++) {
                searchType = searchType.toLowerCase();
                token = searchType.charAt(0);

                if ((partInventory.get(i).getPartName().toLowerCase().charAt(0)) == token) {
                    index = i;
                    isIdentified = true;
                    //testing purposes

                    System.out.println("(alpha numeric)." + " " + partInventory.get(i).getPartName() + " found, searched for items starting with: " + token);

                    return index;
                }
            }
        }

        if (isIdentified == true) {
            System.out.println("parts found." + index);
            return index;

        }
        else {
            System.out.println("No parts found.");
            return 0;
        }
    }

    public static int getProductIDCount() {
        productIDCount++;
        return productIDCount;
    }

    public static int lookupProduct(String searchType) {
        char token;

        //check for numeric characters in search word
        //check for similarity of char 0 in observable array
        if (isInteger(searchType)) {
            for (int i = 0; i < productInventory.size(); i++) {
                if (Integer.parseInt(searchType) == productInventory.get(i).getProductID()) {
                    index = i;
                    isIdentified = true;
                    System.out.println("(numeric)." + " " + productInventory.get(i).getProductID() + " " + productInventory.get(i).getProductName());
                    return index;
                }
            }
        }

        //check for alpha numeric characters in search word
        //run a check for similarity of char 0 in observable array
        else if (isString(searchType)) {
            for (int i = 0; i < productInventory.size(); i++) {
                searchType = searchType.toLowerCase();
                token = searchType.charAt(0);

                if ((productInventory.get(i).getProductName().toLowerCase().charAt(0)) == token) {
                    index = i;
                    isIdentified = true;
                    System.out.println("(alpha numeric)." + " " + productInventory.get(i).getProductName() + " found searched for items starting with: " + token);
                    return index;
                }
            }
        }

        if (isIdentified == true) {
            System.out.println("products found." + index);
            return index;

        }
        else {
            System.out.println("No products found.");
            return 0;
        }
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}