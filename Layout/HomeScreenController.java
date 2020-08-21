package Layout;

import Objects.Inventory;
import Objects.Part;
import Objects.Product;
import static Objects.Inventory.validatePartDelete;
import static Objects.Inventory.validateProductDelete;
import static Objects.Inventory.getPartInventory;
import static Objects.Inventory.deletePart;
import static Objects.Inventory.getProductInventory;
import static Objects.Inventory.removeProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Optional;

public class HomeScreenController implements Initializable {

    @FXML
    private TableView<Part> tableParts;
    @FXML
    private TableColumn<Part, Integer> tablePartsID;
    @FXML
    private TableColumn<Part, String> tablePartsName;
    @FXML
    private TableColumn<Part, Integer> tablePartsInv;
    @FXML
    private TableColumn<Part, Double> tablePartsPrice;
    @FXML
    private TableView<Product> tableProducts;
    @FXML
    private TableColumn<Product, Integer> tableProductsID;
    @FXML
    private TableColumn<Product, String> tableProductsName;
    @FXML
    private TableColumn<Product, Integer> tableProductsInv;
    @FXML
    private TableColumn<Product, Double> tableProductsPrice;
    @FXML
    private TextField txtSearchParts;
    @FXML
    private TextField txtSearchProducts;
    @FXML
    private ImageView imageView;


    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;

    public static int partToModifyIndex() {
        return modifyPartIndex;
    }

    public static int productToModifyIndex() {
        return modifyProductIndex;
    }


    @FXML
    private void clearPartsSearch(ActionEvent event) {
        updatePartsTableView();
        txtSearchParts.setText("");
    }

    @FXML
    private void partsSearch(ActionEvent event) {
        String searchPart = txtSearchParts.getText();
        int partIndex = -1;
        int i = 0;

        if (txtSearchParts.getText() != null) {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            tableParts.setItems(tempPartList);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Field empty");
            alert.setContentText("The search bar is empty, enter a part ot search.");
            alert.showAndWait();
        }
    }

    @FXML
    private void partDelete(ActionEvent event) {
        Part part = tableParts.getSelectionModel().getSelectedItem();
        if(part == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Part not selected.");
            alert.setContentText("Please select a part to be deleted.");
            alert.showAndWait();
        }
        else {
            if (validatePartDelete(part)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Deletion Error");
                alert.setHeaderText("Part cannot be deleted!");
                alert.setContentText("Part is being used by one or more products.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Part Deletion");
                alert.setHeaderText("Confirm?");
                alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    deletePart(part);
                    updatePartsTableView();
                    System.out.println("Part " + part.getPartName() + " was removed.");
                } else {
                    System.out.println("Part " + part.getPartName() + " was not removed.");
                }
            }
        }
    }

    @FXML
    private void openAddPartScreen(ActionEvent event) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene addPartScene = new Scene(addPartParent, 850, 700);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    @FXML
    private void openModifyPartScreen(ActionEvent event) throws IOException {
        if(tableParts.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify Part Error");
            alert.setHeaderText("A part not selected!");
            alert.setContentText("Please select a part to be modified .");
            alert.showAndWait();
        }
        else {
            modifyPart = tableParts.getSelectionModel().getSelectedItem();
            modifyPartIndex = getPartInventory().indexOf(modifyPart);
            Parent modifyPartParent = FXMLLoader.load(getClass().getResource("ModPart.fxml"));
            Scene modifyPartScene = new Scene(modifyPartParent, 850, 700);
            Stage modifyPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyPartStage.setScene(modifyPartScene);
            modifyPartStage.show();
        }
    }


    //// Products section
    @FXML
    private void clearProductsSearch(ActionEvent event) {
        updateProductsTableView();
        txtSearchProducts.setText("");
    }

    @FXML
    private void productSearch(ActionEvent event) {
        String searchProduct = txtSearchProducts.getText();
        int prodIndex = -1;
        if(searchProduct == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Search bar is empty.");
            alert.setContentText("Enter a letter, or ID number of item to be searched.");
            alert.showAndWait();
        }
        else {
            if (Inventory.lookupProduct(searchProduct) == -1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Search Error");
                alert.setHeaderText("Product not found");
                alert.setContentText("The search term entered does not match any known products.");
                alert.showAndWait();
            } else {
                prodIndex = Inventory.lookupProduct(searchProduct);
                Product tempProduct = Inventory.getProductInventory().get(prodIndex);
                ObservableList<Product> tempProductList = FXCollections.observableArrayList();
                tempProductList.add(tempProduct);
                tableProducts.setItems(tempProductList);
            }
        }
    }

    @FXML
    private void productDelete(ActionEvent event) {
        Product product = tableProducts.getSelectionModel().getSelectedItem();
        if(product == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Product not selected.");
            alert.setContentText("Please select a product to be deleted.");
            alert.showAndWait();
        }
        else {
            if (validateProductDelete(product)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Deletion Error");
                alert.setHeaderText("Product cannot be deleted!");
                alert.setContentText("Product contains one or more parts.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Product Deletion");
                alert.setHeaderText("Confirm Delete?");
                alert.setContentText("Are you sure you want to delete " + product.getProductName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    removeProduct(product);
                    updateProductsTableView();
                    System.out.println("Product " + product.getProductName() + " was removed.");
                } else {
                    System.out.println("Product " + product.getProductName() + " was removed.");
                }
            }
        }
    }

    @FXML
    private void openAddProductScreen(ActionEvent event) throws IOException {
        Parent addProductParent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene addProductScene = new Scene(addProductParent,850, 700);
        Stage addProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addProductStage.setScene(addProductScene);
        addProductStage.show();
    }

    @FXML
    private void openModifyProductScreen(ActionEvent event) throws IOException {
        if(tableProducts.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify error");
            alert.setHeaderText("A product not selected to modify.");
            alert.setContentText("Please select a product to modify.");
            alert.showAndWait();
        }
        else {
            modifyProduct = tableProducts.getSelectionModel().getSelectedItem();
            modifyProductIndex = getProductInventory().indexOf(modifyProduct);
            Parent modifyProductParent = FXMLLoader.load(getClass().getResource("ModProduct.fxml"));
            Scene modifyProductScene = new Scene(modifyProductParent, 850, 700);
            Stage modifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyProductStage.setScene(modifyProductScene);
            modifyProductStage.show();
        }
    }


    //// Update table values
    public void updatePartsTableView() {
        tableParts.setItems(getPartInventory());
    }

    public void updateProductsTableView() {
        tableProducts.setItems(getProductInventory());
    }


    //// Confirm exit on Main screen
    @FXML
    private void exitButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            System.out.println("You clicked cancel.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set image --A small manufacturing company--
        File file = new File("src/Resources/project logo.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);

        //set table values
        tablePartsID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        tablePartsName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tablePartsInv.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        tablePartsPrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        tableProductsID.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        tableProductsName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tableProductsInv.setCellValueFactory(cellData -> cellData.getValue().productInvProperty().asObject());
        tableProductsPrice.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        updatePartsTableView();
        updateProductsTableView();
    }
}

