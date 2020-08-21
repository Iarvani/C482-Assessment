package Layout;

import Objects.InHousePart;
import Objects.Inventory;
import Objects.OutSourcedPart;
import Objects.Part;
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

import static Layout.HomeScreenController.partToModifyIndex;
import static Objects.Inventory.getPartInventory;


public class ModPartController implements Initializable {

    private boolean outsourced;
    int partIndex = partToModifyIndex();
    private String errorMessage = new String();
    private int partID;

    @FXML
    private RadioButton radioPartInHouse;
    @FXML
    private RadioButton radioPartOutsourced;
    @FXML
    private Label lblModPartID;
    @FXML
    private TextField txtModPartName;
    @FXML
    private TextField txtModPartInv;
    @FXML
    private TextField txtModPartPrice;
    @FXML
    private TextField txtModPartMin;
    @FXML
    private TextField txtModPartMax;
    @FXML
    private Label lblModPartSource;
    @FXML
    private TextField txtModPartSource;
    @FXML
    private ImageView imageView;

    @FXML
    void modifyPartInHouseRadio(ActionEvent event) {
        outsourced = false;
        radioPartOutsourced.setSelected(false);
        lblModPartSource.setText("Machine ID");
        txtModPartSource.setText("");
        txtModPartSource.setPromptText("Machine ID");
    }

    @FXML
    void modifyPartOutsourcedRadio(ActionEvent event) {
        outsourced = true;
        radioPartInHouse.setSelected(false);
        lblModPartSource.setText("Company Name");
        txtModPartSource.setText("");
        txtModPartSource.setPromptText("Company Name");
    }

    @FXML
    void modPartSave(ActionEvent event) throws IOException {
        String partName = txtModPartName.getText();
        String partInv = txtModPartInv.getText();
        String partPrice = txtModPartPrice.getText();
        String partMin = txtModPartMin.getText();
        String partMax = txtModPartMax.getText();
        String partDyn = txtModPartSource.getText();

        try {
            errorMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), errorMessage);
            if (errorMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage = "";
            }
            else {
                if (outsourced == false) {
                    System.out.println("Part name: " + partName);
                    InHousePart inPart = new InHousePart();
                    inPart.setPartID(partID);
                    inPart.setPartName(partName);
                    inPart.setPartInStock(Integer.parseInt(partInv));
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setPartMachineID(Integer.parseInt(partDyn));
                    Inventory.updatePart(partIndex, inPart);
                }
                else {
                    System.out.println("Part name: " + partName);
                    OutSourcedPart outPart = new OutSourcedPart();
                    outPart.setPartID(partID);
                    outPart.setPartName(partName);
                    outPart.setPartInStock(Integer.parseInt(partInv));
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setPartCompanyName(partDyn);
                    Inventory.updatePart(partIndex, outPart);
                }

                Parent modifyProductSave = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
                Scene scene = new Scene(modifyProductSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Part");
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

    //Cancel button on mo Part returns to home screen
    @FXML
    private void modPartCancel(ActionEvent event) throws IOException {
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

       //cast values for table view
        Part part = getPartInventory().get(partIndex);
        partID = getPartInventory().get(partIndex).getPartID();
        lblModPartID.setText("Auto-Gen: " + partID);
        txtModPartName.setText(part.getPartName());
        txtModPartInv.setText(Integer.toString(part.getPartInStock()));
        txtModPartPrice.setText(Double.toString(part.getPartPrice()));
        txtModPartMin.setText(Integer.toString(part.getPartMin()));
        txtModPartMax.setText(Integer.toString(part.getPartMax()));
        if (part instanceof InHousePart) {
            lblModPartSource.setText("Machine ID");
            txtModPartSource.setText(Integer.toString(((InHousePart) getPartInventory().get(partIndex)).getPartMachineID()));
            radioPartInHouse.setSelected(true);
        }
        else {
            lblModPartSource.setText("Company Name");
            txtModPartSource.setText(((OutSourcedPart) getPartInventory().get(partIndex)).getPartCompanyName());
            radioPartOutsourced.setSelected(true);
        }
    }
}