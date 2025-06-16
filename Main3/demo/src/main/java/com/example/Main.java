package com.example;

//the stuff needed for javafx to work
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Hashtable;

public class Main extends Application {
@Override
public void start(Stage primaryStage) {
    primaryStage.setTitle("Currency Converter");

    //the first amount box
    TextField amount1 = new TextField();
    amount1.setPromptText("Amount");
    amount1.setPrefWidth(100);

    //the country you want to convert from
    TextField countryfrom = new TextField();
    countryfrom.setPromptText("From (country/currency)");
    countryfrom.setPrefWidth(140);

    //the second amount box
    TextField amount2 = new TextField();
    amount2.setPromptText("Amount");
    amount2.setPrefWidth(100);
    amount2.setEditable(false); 

    //the country you want to convert to
    TextField countryto = new TextField();
    countryto.setPromptText("To (country/currency)");
    countryto.setPrefWidth(140);

    Button swap = new Button("⇄");
    swap.setStyle("-fx-font-size: 20px; -fx-padding: 0 10 0 10;");

    HBox box1 = new HBox(10, amount1, countryfrom);
    HBox box2 = new HBox(10, amount2, countryto);

    HBox doubleBox = new HBox(20, box1, swap, box2);
    doubleBox.setStyle("-fx-alignment: center;");

    Button convertButton = new Button("Convert");
    convertButton.setStyle("-fx-background-color: #4285f4; -fx-text-fill: white; -fx-font-weight: bold;");

    Label resultLabel = new Label();
    resultLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    resultLabel.setMinHeight(40);

    Label darkmode = new Label("Dark Mode");
    CheckBox darkModeCheckBox = new CheckBox();
    HBox darkModeBox = new HBox(8, darkmode, darkModeCheckBox);
    darkModeBox.setStyle("-fx-alignment: center-right;");
    darkModeBox.setSpacing(8);

    // Create history section
    Label historyLabel = new Label("History");
    historyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
    VBox historyBox = new VBox(10, historyLabel);
    historyBox.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-width: 1; -fx-alignment: top_center;");
    historyBox.setPrefWidth(180);
    historyBox.setMinHeight(250);

    // Add a list to to store the last 6 conversions
    java.util.LinkedList<String> historyList = new java.util.LinkedList<>();
    Label[] historyLabels = new Label[6];
    for (int i = 0; i < 6; i++) {
        historyLabels[i] = new Label("");
        historyBox.getChildren().add(historyLabels[i]);
    }

    VBox mainContent = new VBox(20, doubleBox, convertButton, resultLabel, darkModeBox);
    mainContent.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #ffffff;");
    mainContent.setSpacing(20);

    // Layout: main content on left, history on right
    HBox mainLayout = new HBox(30, mainContent, historyBox);
    mainLayout.setStyle("-fx-alignment: center_left;");

    Scene scene = new Scene(mainLayout, 850, 350);

    // Store the last conversion 
    final String[] lastConversion = {""};

    //does the mathimatical conversion with USD rate because api wont let me just get the rate straight up.
    convertButton.setOnAction(e -> {
        try {
            Hashtable<String, String> dict = Methods.getCountryCurrencyDict();
            String from = dict.getOrDefault(countryfrom.getText().toLowerCase(), countryfrom.getText().toUpperCase());
            String to = dict.getOrDefault(countryto.getText().toLowerCase(), countryto.getText().toUpperCase());
            double rate = Methods.getRate(from, to);
            double rate2 = Methods.getRate(to, from);
            double value = Double.parseDouble(amount1.getText());
            double result = value * (rate / rate2);
            amount2.setText(String.format("%.2f", result));
            String conversion = String.format("%.2f %s = %.2f %s", value, from, result, to);
            resultLabel.setText(conversion);
            lastConversion[0] = conversion;
            // Update history list (newest at top)
            if (historyList.size() == 6) {
                historyList.removeLast();
            }
            historyList.addFirst(conversion);
            // Update history labels
            for (int i = 0; i < 6; i++) {
                if (i < historyList.size()) {
                    historyLabels[i].setText(historyList.get(i));
                } else {
                    historyLabels[i].setText("");
                }
            }
    
        } catch (NumberFormatException ex) {
            resultLabel.setText("Please enter a valid number.");
        } catch (Exception ex) {
            resultLabel.setText("Error: " + ex.getMessage());
        }
    });

    //makes the swap button swap the amount and currency
    swap.setOnAction(e -> {
        String tempAmount = amount1.getText();
        String tempCurrency = countryfrom.getText();
        amount1.setText(amount2.getText());
        countryfrom.setText(countryto.getText());
        amount2.setText(tempAmount);
        countryto.setText(tempCurrency);
        resultLabel.setText(""); // Clear the result label after swapping
    });

    // the darkmode checkbox that makes everything dark instead of light 
    darkModeCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
        if (isSelected) {
            mainContent.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #222;");
            countryfrom.setStyle("-fx-text-fill: #fff; -fx-background-color: #333;");
            amount1.setStyle("-fx-text-fill: #fff; -fx-background-color: #333;");
            countryto.setStyle("-fx-text-fill: #fff; -fx-background-color: #333;");
            amount2.setStyle("-fx-text-fill: #fff; -fx-background-color: #333;");
            resultLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #fff;");
            darkmode.setStyle("-fx-text-fill: #fff;");
            historyBox.setStyle("-fx-background-color: #333; -fx-padding: 15; -fx-border-color: #444; -fx-border-width: 1; -fx-alignment: top_center;");
            historyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 0 0 10 0; -fx-text-fill: #fff;");
            for (Label l : historyLabels) l.setStyle("-fx-text-fill: #fff;");
        } else {
            mainContent.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #fff;");
            countryfrom.setStyle("");
            amount1.setStyle("");
            countryto.setStyle("");
            amount2.setStyle("");
            resultLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            darkmode.setStyle("");
            historyBox.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-width: 1; -fx-alignment: top_center;");
            historyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
            for (Label l : historyLabels) l.setStyle("");
        }
    });

    // makes it so pressing enter converts 
    //got it from https://stackoverflow.com/questions/32038418/javafx-how-to-bind-the-enter-key-to-a-button-and-fire-off-an-event-when-it-is-c
    scene.setOnKeyPressed(event -> {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            convertButton.fire();
        }
    });

    primaryStage.setScene(scene);
    primaryStage.show();
}

public static void main(String[] args) {
    launch(args);
}
}
