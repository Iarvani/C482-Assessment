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

import static Layout.HomeScreenController.productToModifyIndex;
import static Objects.Inventory.getPartInventory;
import static Objects.Inventory.getProductInventory;

public class ModProductController implements Initializable {

    private ObservableList<Part> availableParts = FXCollections.observableArrayList();
    private int productIndex = productToModifyIndex();
    private String errorMessage = new String();
    private int productID;

    @FXML
    private Label lblModProductID;
    @FXML
    private Label lblModProductIDNum;
    @FXML
    private TextField txtModProductName;
    @FXML
    private TextField txtModProductInv;
    @FXML
    private TextField txtModProductPrice;
    @FXML
    private TextField txtModProductMin;
    @FXML
    private TextField txtModProductMax;
    @FXML
    private TextField txtModProductSearch;
    @FXML
    private TableView<Part> tableModProductAdd;
    @FXML
    private TableColumn<Part, Integer> tableModProductAddID;
    @FXML
    private TableColumn<Part, String> tableModProductAddName;
    @FXML
    private TableColumn<Part, Integer> tableModProductAddInv;
    @FXML
    private TableColumn<Part, Double> tableModProductAddPrice;
    @FXML
    private TableView<Part> tableModProductDelete;
    @FXML
    private TableColumn<Part, Integer> tableModProductDeleteID;
    @FXML
    private TableColumn<Part, String> tableModProductDeleteName;
    @FXML
    private TableColumn<Part, Integer> tableModProductDeleteInv;
    @FXML
    private TableColumn<Part, Double> tableModProductDeletePrice;
    @FXML
    private ImageView imageView;

    @FXML
    void clearSearch(ActionEvent event) {
        updateAddPartsTableView();
        txtModProductSearch.setText("");
    }

    @FXML
    void search(ActionEvent event) {
        String searchPart = txtModProductSearch.getText();
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
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            tableModProductAdd.setItems(tempPartList);
        }
    }

    @FXML
    void modProductAdd(ActionEvent event) {
        if(tableModProductAdd.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selection Error");
            alert.setHeaderText("Part not selected");
            alert.setContentText("Please select a part to add to the product.");
            alert.showAndWait();
        }
        else {
            Part part = tableModProductAdd.getSelectionModel().getSelectedItem();
            availableParts.add(part);
            updateDeletePartsTableView();
        }
    }

    @FXML
    void modProductDelete(ActionEvent event) {
        if(tableModProductDelete.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selection Error");
            alert.setHeaderText("Part not selected");
            alert.setContentText("Please select a part to delete from the product.");
            alert.showAndWait();
        }
        else {
            Part part = tableModProductDelete.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Deletion");
            alert.setHeaderText("Confirm");
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + " from parts?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                availableParts.remove(part);
            } else {
                System.out.println("You clicked cancel.");
            }
        }
    }

    @FXML
    private void modProductSave(ActionEvent event) throws IOException {
        String productName = txtModProductName.getText();
        String productInv = txtModProductInv.getText();
        String productPrice = txtModProductPrice.getText();
        String productMin = txtModProductMin.getText();
        String productMax = txtModProductMax.getText();

        try {
            errorMessage = Product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv), Double.parseDouble(productPrice), availableParts, errorMessage);
            if (errorMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Product");
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
                Inventory.updateProduct(productIndex, newProduct);

                Parent modifyProductSaveParent = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
                Scene scene = new Scene(modifyProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Product");
            alert.setContentText("Form contains blank fields, or expecting numbers and field contains letters.");
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

    //Cancel button on Mod products returns you to home page
    @FXML
    private void modProductCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setContentText("Cancel and return to home screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent modifyProductCancelParent = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
            Scene scene = new Scene(modifyProductCancelParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else {
            System.out.println("Command cancelled.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set image --A small manufacturing company--
        File file = new File("src/Resources/project logo.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);

        Product product = getProductInventory().get(productIndex);

        //casting values for table
        productID = getProductInventory().get(productIndex).getProductID();
        lblModProductIDNum.setText("" + productID);
        txtModProductName.setText(product.getProductName());
        txtModProductInv.setText(Integer.toString(product.getProductInStock()));
        txtModProductPrice.setText(Double.toString(product.getProductPrice()));
        txtModProductMin.setText(Integer.toString(product.getProductMin()));
        txtModProductMax.setText(Integer.toString(product.getProductMax()));
        availableParts = product.getProductParts();

        //set table view values
        tableModProductAddID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        tableModProductAddName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tableModProductAddInv.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        tableModProductAddPrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        tableModProductDeleteID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        tableModProductDeleteName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tableModProductDeleteInv.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        tableModProductDeletePrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        updateAddPartsTableView();
        updateDeletePartsTableView();
    }

    public void updateAddPartsTableView() {
        tableModProductAdd.setItems(getPartInventory());
    }

    public void updateDeletePartsTableView() {
        tableModProductDelete.setItems(availableParts);
    }
}