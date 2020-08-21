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
import java.util.Random;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {

    @FXML
    private RadioButton radioAddPartInHouse;
    @FXML
    private RadioButton radioAddPartOutsourced;
    @FXML
    private Label lblAddPartID;
    @FXML
    private TextField txtAddPartName;
    @FXML
    private TextField txtAddPartInv;
    @FXML
    private TextField txtAddPartPrice;
    @FXML
    private TextField txtAddPartMin;
    @FXML
    private TextField txtAddPartMax;
    @FXML
    private Label lblAddPartSource;
    @FXML
    private TextField txtAddPartSource;
    @FXML
    private ImageView imageView;

    private boolean outSourced;
    private String errorMessage = new String();
    private int partID;

    @FXML
    void addPartInHouseRadio(ActionEvent event) {
        outSourced = false;
        lblAddPartSource.setText("Machine ID");
        txtAddPartSource.setPromptText("Machine ID");
        radioAddPartOutsourced.setSelected(false);
    }

    @FXML
    void addPartOutsourcedRadio(ActionEvent event) {
        outSourced = true;
        lblAddPartSource.setText("Company Name");
        txtAddPartSource.setPromptText("Company Name");
        radioAddPartInHouse.setSelected(false);
    }
/* set text for auto fill button for testing purposes*/
    public int getRandomNumber(int minNum, int maxNum) {
     Random random = new Random();
     return random.nextInt(maxNum - minNum) + minNum;
    }

    @FXML
    void addPartAutoFill(ActionEvent event) throws IOException {
        if(getRandomNumber(1, 5) == 1 && outSourced == false) {
            txtAddPartName.setText("Wrench");
            txtAddPartInv.setText("3");
            txtAddPartPrice.setText("1.25");
            txtAddPartMin.setText("1");
            txtAddPartMax.setText("5");
            txtAddPartSource.setText("321123");
        }
        if(getRandomNumber(1, 5) == 2 && outSourced == false) {
            txtAddPartName.setText("Screwdriver");
            txtAddPartInv.setText("2");
            txtAddPartPrice.setText("3.25");
            txtAddPartMin.setText("1");
            txtAddPartMax.setText("5");
            txtAddPartSource.setText("356783");
        }
        if(getRandomNumber(1, 5) == 3 && outSourced == false) {
            txtAddPartName.setText("Level");
            txtAddPartInv.setText("1");
            txtAddPartPrice.setText("10.25");
            txtAddPartMin.setText("1");
            txtAddPartMax.setText("5");
            txtAddPartSource.setText("399995");
        }
        if(getRandomNumber(1, 5) == 4 && outSourced == false) {
            txtAddPartName.setText("Electric drill");
            txtAddPartInv.setText("5");
            txtAddPartPrice.setText("100.00");
            txtAddPartMin.setText("1");
            txtAddPartMax.setText("5");
            txtAddPartSource.setText("939123");
        }
        if(getRandomNumber(1, 5) == 5 && outSourced == false) {
            txtAddPartName.setText("Electric Screwdriver");
            txtAddPartInv.setText("3");
            txtAddPartPrice.setText("11.57");
            txtAddPartMin.setText("1");
            txtAddPartMax.setText("5");
            txtAddPartSource.setText("374761");
        }
        if(getRandomNumber(1, 5) == 1 && outSourced == true) {
            txtAddPartName.setText("5gal Gas can");
            txtAddPartInv.setText("1");
            txtAddPartPrice.setText("10.99");
            txtAddPartMin.setText("1");
            txtAddPartMax.setText("5");
            txtAddPartSource.setText("WalMart");
        }
        if(getRandomNumber(1, 5) == 2 && outSourced == true) {
            txtAddPartName.setText("Socket driver");
            txtAddPartInv.setText("1");
            txtAddPartPrice.setText("25.99");
            txtAddPartMin.setText("1");
            txtAddPartMax.setText("5");
            txtAddPartSource.setText("Ikea");
        }
        if(getRandomNumber(1, 5) == 3 && outSourced == true) {
            txtAddPartName.setText("24 piece wrench set");
            txtAddPartInv.setText("1");
            txtAddPartPrice.setText("12.95");
            txtAddPartMin.setText("1");
            txtAddPartMax.setText("5");
            txtAddPartSource.setText("Ace Hardware");
        }
        if(getRandomNumber(1, 5) == 4 && outSourced == true) {
            txtAddPartName.setText("Ratchet set");
            txtAddPartInv.setText("1");
            txtAddPartPrice.setText("34.95");
            txtAddPartMin.setText("1");
            txtAddPartMax.setText("5");
            txtAddPartSource.setText("Lowes");
        }
        if(getRandomNumber(1, 5) == 5 && outSourced == true) {
            txtAddPartName.setText("Laser Level");
            txtAddPartInv.setText("1");
            txtAddPartPrice.setText("22.57");
            txtAddPartMin.setText("1");
            txtAddPartMax.setText("5");
            txtAddPartSource.setText("Home Depot");
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
    void addPartSave(ActionEvent event) throws IOException {
        String partName = txtAddPartName.getText();
        String partInv = txtAddPartInv.getText();
        String partPrice = txtAddPartPrice.getText();
        String partMin = txtAddPartMin.getText();
        String partMax = txtAddPartMax.getText();
        String partDyn = txtAddPartSource.getText();

        try {
            errorMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), errorMessage);
            if (errorMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage = "";
            }
            else {
                if (outSourced == false) {
                    System.out.println("Part name: " + partName);
                    InHousePart inPart = new InHousePart();
                    inPart.setPartID(partID);
                    inPart.setPartName(partName);
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setPartInStock(Integer.parseInt(partInv));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setPartMachineID(Integer.parseInt(partDyn));
                    Inventory.addPart(inPart);
                } else {
                    System.out.println("Part name: " + partName);
                    OutSourcedPart outPart = new OutSourcedPart();
                    outPart.setPartID(partID);
                    outPart.setPartName(partName);
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setPartInStock(Integer.parseInt(partInv));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setPartCompanyName(partDyn);
                    Inventory.addPart(outPart);
                }

                Parent addPartSave = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
                Scene scene = new Scene(addPartSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch(NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText("Form has blank fields, or form expecting numbers and field contains letters.");
                alert.showAndWait();
        }
    }

    @FXML
    private void addPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Clear fields");
        alert.setHeaderText("Confirm clear");
        alert.setContentText("Are you sure you want to clear all fields?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            //clear text bars
            txtAddPartName.setText("");
            txtAddPartInv.setText("");
            txtAddPartPrice.setText("");
            txtAddPartMin.setText("");
            txtAddPartMax.setText("");
            txtAddPartSource.setText("");
        }
        else {
            System.out.println("Cancelled.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set image --A small manufacturing company--
        File file = new File("src/Resources/project logo.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        partID = Inventory.getPartIDCount();
        lblAddPartID.setText("Auto-Gen: " + partID);
    }
}