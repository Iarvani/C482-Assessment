package Layout;

import Objects.Inventory;
import Objects.Part;
import Objects.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Objects.Inventory.getPartInventory;

public class AddProductController implements Initializable {

    private ObservableList<Part> availableParts = FXCollections.observableArrayList();
    private String errorMessage = new String();
    private int productID;

    @FXML
    private Label lblAddProductID;
    @FXML
    private Label lblAddProductIDNum;
    @FXML
    private TextField txtAddProductName;
    @FXML
    private TextField txtAddProductInv;
    @FXML
    private TextField txtAddProductPrice;
    @FXML
    private TextField txtAddProductMin;
    @FXML
    private TextField txtAddProductMax;
    @FXML
    private TextField txtAddProductSearch;
    @FXML
    private TableView<Part> tableAddProductAdd;
    @FXML
    private TableColumn<Part, Integer> tableAddProductAddID;
    @FXML
    private TableColumn<Part, String> tableAddProductAddName;
    @FXML
    private TableColumn<Part, Integer> tableAddProductAddInv;
    @FXML
    private TableColumn<Part, Double> tableAddProductAddPrice;
    @FXML
    private TableView<Part> tableAddProductDelete;
    @FXML
    private TableColumn<Part, Integer> tableAddProductDeleteID;
    @FXML
    private TableColumn<Part, String> tableAddProductDeleteName;
    @FXML
    private TableColumn<Part, Integer> tableAddProductDeleteInv;
    @FXML
    private TableColumn<Part, Double> tableAddProductDeletePrice;
    @FXML
    private ImageView imageView;

    @FXML
    void clearSearch(ActionEvent event) {
        updateAddPartTableView();
        txtAddProductSearch.setText("");
    }

    @FXML
    void search(ActionEvent event) {
        String searchPart = txtAddProductSearch.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any known parts.");
            alert.showAndWait();
        }
        else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = getPartInventory().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            tableAddProductAdd.setItems(tempPartList);
        }
    }

    @FXML
    void addProductAdd(ActionEvent event) {
        if(tableAddProductAdd.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Select error");
            alert.setHeaderText("Part not selected");
            alert.setContentText("Please select a part to add to the product.");
            alert.showAndWait();
        }
        else {
            Part part = tableAddProductAdd.getSelectionModel().getSelectedItem();
            availableParts.add(part);
            updateDeletePartTableView();
        }
    }

    @FXML
    void addProductDelete(ActionEvent event) {
        if(tableAddProductDelete.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Select error");
            alert.setHeaderText("Part not selected");
            alert.setContentText("Please select a part to delete from the product.");
            alert.showAndWait();
        }
        else {
            Part part = tableAddProductDelete.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Deletion");
            alert.setHeaderText("Confirm");
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + " from parts?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Part deleted.");
                availableParts.remove(part);
            } else {
                System.out.println("You clicked cancel.");
            }
        }
    }

    @FXML
    void addProductSave(ActionEvent event) throws IOException {
        String productName = txtAddProductName.getText();
        String productInv = txtAddProductInv.getText();
        String productPrice = txtAddProductPrice.getText();
        String productMin = txtAddProductMin.getText();
        String productMax = txtAddProductMax.getText();

        try{
            errorMessage = Product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv), Double.parseDouble(productPrice), availableParts, errorMessage);
            if (errorMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Product");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage = "";
            }
            else {
                System.out.println("Product name: " + productName);
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(availableParts);
                Inventory.addProduct(newProduct);

                Parent addProductSaveParent = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
                Scene scene = new Scene(addProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Product");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void openHomeScreen(ActionEvent event) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        Scene addPartScene = new Scene(addPartParent, 850, 450);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
        //setOnTheScreen("HomeScreen.fxml");
    }

    @FXML
    private void exitButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exit program");
        alert.setHeaderText("Confirm exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
        else {
            System.out.println("Cancelled.");
        }
    }

    @FXML
    private void addProductCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Clear Fields?");
        alert.setHeaderText("Confirm clear");
        alert.setContentText("Are you sure you want to clear all fields?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            //clear text bars
            txtAddProductName.setText("");
            txtAddProductInv.setText("");
            txtAddProductPrice.setText("");
            txtAddProductMin.setText("");
            txtAddProductMax.setText("");
        }
        else {
            System.out.println("Cancelled.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File file = new File("src/Resources/project logo.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        tableAddProductAddID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        tableAddProductAddName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tableAddProductAddInv.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        tableAddProductAddPrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        tableAddProductDeleteID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        tableAddProductDeleteName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tableAddProductDeleteInv.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        tableAddProductDeletePrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        updateAddPartTableView();
        updateDeletePartTableView();
        productID = Inventory.getProductIDCount();
        lblAddProductIDNum.setText("Auto-Gen: " + productID);
    }

    public void updateAddPartTableView() {
        tableAddProductAdd.setItems(getPartInventory());
    }

    public void updateDeletePartTableView() {
        tableAddProductDelete.setItems(availableParts);
    }
}