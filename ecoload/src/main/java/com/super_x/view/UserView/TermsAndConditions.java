package com.super_x.view.UserView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TermsAndConditions {

    private static final String GREEN = "#0B6B22";
    private static final String RED = "#E53935";

    private final Runnable onAgree;

    public TermsAndConditions(
            Runnable onAgree
    ) {

        this.onAgree = onAgree;
    }

    // =========================================================
    // SHOW POPUP
    // =========================================================

    public void show() {

        Stage stage = new Stage();

        stage.initModality(
                Modality.APPLICATION_MODAL
        );

        stage.setTitle(
                "Terms & Conditions"
        );

        // =====================================================
        // TITLE
        // =====================================================

        Text title =
                new Text(
                        "Terms & Conditions"
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        28
                )
        );

        title.setFill(
                Color.web(GREEN)
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        Text subtitle =
                new Text(
                        "Please read and accept the following terms "
                                + "before posting your load."
                );

        subtitle.setFont(
                Font.font(
                        "System",
                        15
                )
        );

        subtitle.setFill(
                Color.web("#6A6A6A")
        );

        // =====================================================
        // TERMS CONTENT
        // =====================================================

        TextFlow termsFlow =
                new TextFlow();

        termsFlow.setPadding(
                new Insets(22)
        );

        termsFlow.setLineSpacing(5);

        termsFlow.setStyle(
                "-fx-background-color: #F8FAF8;"
                        + "-fx-background-radius: 12;"
                        + "-fx-border-color: #DCE5DC;"
                        + "-fx-border-radius: 12;"
        );

        // -----------------------------------------------------
        // TERM 1
        // -----------------------------------------------------

        addTerm(
                termsFlow,
                "1. 📦 Accurate Load Information",
                "Provide correct information about the load type, "
                        + "quantity, weight, pickup location, "
                        + "destination, and delivery schedule."
        );

        // -----------------------------------------------------
        // TERM 2
        // -----------------------------------------------------

        addTerm(
                termsFlow,
                "2. ⚠️ Handle With Care",
                "You must clearly mention if the shipment requires "
                        + "special handling. Drivers should handle the "
                        + "shipment carefully during loading, "
                        + "transportation, and unloading."
        );

        // -----------------------------------------------------
        // TERM 3
        // -----------------------------------------------------

        addTerm(
                termsFlow,
                "3. 🥂 Fragile Items",
                "Clearly identify fragile items such as glass, "
                        + "electronics, furniture, or other breakable "
                        + "goods. Proper packaging is the sender's "
                        + "responsibility."
        );

        // -----------------------------------------------------
        // TERM 4
        // -----------------------------------------------------

        addTerm(
                termsFlow,
                "4. 🧳 Proper Packaging",
                "All goods must be securely packed and protected "
                        + "before pickup. EcoLoad is not responsible "
                        + "for damage caused by inadequate packaging."
        );

        // -----------------------------------------------------
        // TERM 5
        // -----------------------------------------------------

        addTerm(
                termsFlow,
                "5. 🚫 Prohibited & Hazardous Goods",
                "Illegal, dangerous, explosive, toxic, or otherwise "
                        + "prohibited items must not be posted through "
                        + "the platform."
        );

        // -----------------------------------------------------
        // TERM 6
        // -----------------------------------------------------

        addTerm(
                termsFlow,
                "6. 🔍 Driver Assignment",
                "After posting the load, eligible drivers may "
                        + "receive requests or offers for the shipment."
        );

        // -----------------------------------------------------
        // TERM 7
        // -----------------------------------------------------

        addTerm(
                termsFlow,
                "7. ❌ Cancellation",
                "Cancellation after driver acceptance may be "
                        + "subject to applicable platform rules "
                        + "or charges."
        );

        // -----------------------------------------------------
        // TERM 8
        // -----------------------------------------------------

        addTerm(
                termsFlow,
                "8. 🛡️ EcoSafe Guarantee",
                "Your transaction may be protected according to "
                        + "the EcoSafe delivery verification process."
        );

        // -----------------------------------------------------
        // TERM 9
        // -----------------------------------------------------

        addTerm(
                termsFlow,
                "9. 📸 Delivery Verification",
                "Photos, signatures, OTP, or other delivery "
                        + "confirmation may be required to verify "
                        + "successful delivery."
        );

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane(
                        termsFlow
                );

        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(450);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-background: transparent;"
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // =====================================================
        // CHECKBOX
        // =====================================================

        CheckBox agreeCheckBox =
                new CheckBox(
                        "I have read and agree to the Terms & Conditions."
                );

        agreeCheckBox.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        agreeCheckBox.setTextFill(
                Color.web("#333333")
        );

        agreeCheckBox.setWrapText(true);

        // =====================================================
        // CANCEL
        // =====================================================

        Button cancelButton =
                new Button(
                        "Cancel"
                );

        cancelButton.setPrefWidth(150);

        cancelButton.setPrefHeight(52);

        setCancelStyle(
                cancelButton
        );

        cancelButton.setOnAction(
                event ->
                        stage.close()
        );

        // =====================================================
        // AGREE
        // =====================================================

        Button agreeButton =
                new Button(
                        "Agree & Continue"
                );

        agreeButton.setPrefWidth(200);

        agreeButton.setPrefHeight(52);

        agreeButton.setDisable(true);

        setAgreeStyle(
                agreeButton
        );

        // =====================================================
        // CHECKBOX LISTENER
        // =====================================================

        agreeCheckBox.selectedProperty()
                .addListener(
                        (observable,
                         oldValue,
                         selected) -> {

                            agreeButton.setDisable(
                                    !selected
                            );

                            if (selected) {

                                agreeButton.setStyle(
                                        "-fx-background-color: "
                                                + GREEN + ";"
                                                + "-fx-background-radius: 28;"
                                                + "-fx-border-radius: 28;"
                                                + "-fx-text-fill: white;"
                                                + "-fx-font-size: 14px;"
                                                + "-fx-font-weight: bold;"
                                                + "-fx-cursor: hand;"
                                );

                            } else {

                                agreeButton.setStyle(
                                        "-fx-background-color: #A9D2B2;"
                                                + "-fx-background-radius: 28;"
                                                + "-fx-border-radius: 28;"
                                                + "-fx-text-fill: white;"
                                                + "-fx-font-size: 14px;"
                                                + "-fx-font-weight: bold;"
                                );
                            }
                        }
                );

        // =====================================================
        // AGREE ACTION
        // =====================================================

        agreeButton.setOnAction(
                event -> {

                    if (
                            !agreeCheckBox.isSelected()
                    ) {

                        return;
                    }

                    stage.close();

                    if (onAgree != null) {

                        onAgree.run();
                    }
                }
        );

        // =====================================================
        // BUTTON ROW
        // =====================================================

        HBox buttonRow =
                new HBox(14);

        buttonRow.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonRow.getChildren().addAll(
                cancelButton,
                agreeButton
        );

        // =====================================================
        // MAIN POPUP
        // =====================================================

        VBox root =
                new VBox(20);

        root.setPadding(
                new Insets(
                        30,
                        32,
                        28,
                        32
                )
        );

        root.setStyle(
                "-fx-background-color: white;"
        );

        root.getChildren().addAll(
                title,
                subtitle,
                scrollPane,
                agreeCheckBox,
                buttonRow
        );

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        root,
                        780,
                        800
                );

        stage.setScene(scene);

        stage.setResizable(false);

        stage.centerOnScreen();

        stage.showAndWait();
    }

    // =========================================================
    // ADD TERM
    // =========================================================

    private void addTerm(
            TextFlow flow,
            String headingText,
            String descriptionText
    ) {

        Text heading =
                new Text(
                        headingText + "\n"
                );

        heading.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        16
                )
        );

        heading.setFill(
                Color.web(GREEN)
        );

        Text description =
                new Text(
                        descriptionText + "\n\n"
                );

        description.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        description.setFill(
                Color.web("#333333")
        );

        flow.getChildren().addAll(
                heading,
                description
        );
    }

    // =========================================================
    // CANCEL STYLE
    // =========================================================

    private void setCancelStyle(
            Button button
    ) {

        button.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: " + RED + ";"
                        + "-fx-border-width: 1.5;"
                        + "-fx-border-radius: 28;"
                        + "-fx-background-radius: 28;"
                        + "-fx-text-fill: " + RED + ";"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;"
        );
    }

    // =========================================================
    // AGREE STYLE
    // =========================================================

    private void setAgreeStyle(
            Button button
    ) {

        button.setStyle(
                "-fx-background-color: #A9D2B2;"
                        + "-fx-background-radius: 28;"
                        + "-fx-border-radius: 28;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
        );
    }
}