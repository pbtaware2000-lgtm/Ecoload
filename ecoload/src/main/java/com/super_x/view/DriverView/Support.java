package com.super_x.view.DriverView;

import com.super_x.controller.drivercontroller.SupportController;
import com.super_x.model.drivermodel.CurrentDriver;
import com.super_x.model.drivermodel.SupportTicket;
import com.super_x.model.drivermodel.TimelineEvent;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Support {

    // =========================================================
    // COLORS
    // =========================================================

    private static final String GREEN = "#087A3A";
    private static final String DARK_GREEN = "#064C38";
    private static final String GREEN_HOVER = "#0A8A4A";
    private static final String LIGHT_GREEN = "#EAF8EF";

    private static final String PAGE_BG = "#F7F9F7";
    private static final String BORDER = "#DDE5DF";
    private static final String TEXT = "#26312D";
    private static final String MUTED = "#707875";

    private static final String BLUE = "#3267C7";

    // =========================================================
    // CONTROLLER
    // =========================================================

    private final SupportController supportController;

    // =========================================================
    // DRIVER EMAIL
    // =========================================================

    private String driverEmail;

    // =========================================================
    // FORM CONTROLS
    // =========================================================

    private ComboBox<String> issueType;

    private RadioButton low;
    private RadioButton medium;
    private RadioButton high;

    private TextField subjectField;
    private TextArea descriptionArea;

    private Label attachmentLabel;

    private File selectedAttachment;

    // =========================================================
    // TICKETS
    // =========================================================

    private final List<SupportTicket> tickets =
            new ArrayList<>();

    private VBox ticketList;

    private Label ticketCountLabel;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Support(SupportController supportController) {

        if (supportController == null) {

            throw new IllegalArgumentException(
                    "SupportController cannot be null.");
        }

        this.supportController =
                supportController;
    }

    // =========================================================
    // SECOND CONSTRUCTOR
    // USE THIS IF DRIVER EMAIL IS AVAILABLE
    // =========================================================

    public Support(
            SupportController supportController,
            String driverEmail) {

        if (supportController == null) {

            throw new IllegalArgumentException(
                    "SupportController cannot be null.");
        }

        this.supportController =
                supportController;

        this.driverEmail =
                driverEmail;
    }

    // =========================================================
    // SET DRIVER EMAIL
    // =========================================================

    public void setDriverEmail(
            String driverEmail) {

        this.driverEmail =
                driverEmail;
    }

    // =========================================================
    // GET DRIVER EMAIL
    // =========================================================

    public String getDriverEmail() {

        return driverEmail;
    }

    // =========================================================
    // SUPPORT PAGE
    // =========================================================

    public Scene getSupportPageScene() {

        loadTickets();

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: " +
                        PAGE_BG +
                        ";");

        // =====================================================
        // SIDEBAR
        // =====================================================

        VBox sidebar =
                DriverNavigation.createSidebar(
                        "Support");

        root.setLeft(sidebar);

        // =====================================================
        // NAVBAR
        // =====================================================

        HBox navbar =
                DriverNavigation.createNavbar();

        // =====================================================
        // SUPPORT CONTENT
        // =====================================================

        VBox supportContent =
                new VBox(10);

        supportContent.setPadding(
                new Insets(
                        14,
                        20,
                        15,
                        20));

        supportContent.setFillWidth(true);

        supportContent.setStyle(
                "-fx-background-color: " +
                        PAGE_BG +
                        ";");

        // =====================================================
        // HEADER
        // =====================================================

        Label title =
                new Label(
                        "Support Center");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22));

        title.setTextFill(
                Color.web(TEXT));

        Label subtitle =
                new Label(
                        "Need help? Our support team is ready to assist you.");

        subtitle.setFont(
                Font.font(
                        "Arial",
                        12));

        subtitle.setTextFill(
                Color.web(MUTED));

        VBox header =
                new VBox(
                        2,
                        title,
                        subtitle);

        // =====================================================
        // TWO COLUMNS
        // =====================================================

        HBox content =
                new HBox(16);

        content.setFillHeight(false);

        // =====================================================
        // LEFT
        // =====================================================

        VBox leftColumn =
                new VBox(12);

        VBox createTicketCard =
                createTicketCard();

        VBox previousTicketsCard =
                createPreviousTicketsCard();

        leftColumn.getChildren().addAll(
                createTicketCard,
                previousTicketsCard);

        // =====================================================
        // RIGHT
        // =====================================================

        VBox rightColumn =
                new VBox(10);

        VBox faqCard =
                createFAQCard();

        VBox contactCard =
                createContactCard();

        rightColumn.getChildren().addAll(
                faqCard,
                contactCard);

        rightColumn.setPrefWidth(390);
        rightColumn.setMinWidth(350);
        rightColumn.setMaxWidth(410);

        HBox.setHgrow(
                leftColumn,
                Priority.ALWAYS);

        content.getChildren().addAll(
                leftColumn,
                rightColumn);

        // =====================================================
        // INFORMATION
        // =====================================================

        HBox informationBar =
                createInformationBar();

        // =====================================================
        // SECURITY
        // =====================================================

        HBox securityBar =
                createSecurityBar();

        // =====================================================
        // ADD CONTENT
        // =====================================================

        supportContent.getChildren().addAll(
                header,
                content,
                informationBar,
                securityBar);

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane supportScroll =
                new ScrollPane(
                        supportContent);

        supportScroll.setFitToWidth(true);

        supportScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        supportScroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);

        supportScroll.setPannable(true);

        supportScroll.setStyle(
                "-fx-background-color: " +
                        PAGE_BG +
                        ";" +
                        "-fx-background-insets: 0;" +
                        "-fx-padding: 0;");

        // =====================================================
        // CENTER
        // =====================================================

        VBox center =
                new VBox();

        center.setFillWidth(true);
        center.setMinHeight(0);

        center.getChildren().addAll(
                navbar,
                supportScroll);

        VBox.setVgrow(
                supportScroll,
                Priority.ALWAYS);

        root.setCenter(center);

        // =====================================================
        // SCENE
        // =====================================================

        return new Scene(
                root,
                1536,
                750);
    }

    // =========================================================
    // CREATE TICKET CARD
    // =========================================================

    private VBox createTicketCard() {

        VBox card =
                createCard();

        Label icon =
                new Label("▣");

        icon.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18));

        icon.setTextFill(
                Color.web(GREEN));

        StackPane iconBox =
                new StackPane(icon);

        iconBox.setPrefSize(
                34,
                34);

        iconBox.setMaxSize(
                34,
                34);

        iconBox.setStyle(
                "-fx-background-color: " +
                        LIGHT_GREEN +
                        ";" +
                        "-fx-background-radius: 9;");

        Label title =
                new Label(
                        "Create Support Ticket");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16));

        title.setTextFill(
                Color.web(TEXT));

        HBox cardTitle =
                new HBox(
                        10,
                        iconBox,
                        title);

        cardTitle.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // ISSUE TYPE
        // =====================================================

        Label issueLabel =
                createFieldLabel(
                        "Issue Type");

        issueType =
                new ComboBox<>();

        issueType.getItems().addAll(
                "Technical Issue",
                "Payment Issue",
                "Trip Issue",
                "Vehicle Issue",
                "Load Issue",
                "Account Issue",
                "Document Issue",
                "Other");

        issueType.setValue(
                "Technical Issue");

        issueType.setMaxWidth(
                Double.MAX_VALUE);

        issueType.setPrefHeight(38);

        styleControl(issueType);

        VBox issueBox =
                new VBox(
                        4,
                        issueLabel,
                        issueType);

        // =====================================================
        // PRIORITY
        // =====================================================

        Label priorityLabel =
                createFieldLabel(
                        "Priority");

        ToggleGroup priorityGroup =
                new ToggleGroup();

        low =
                createRadioButton(
                        "Low",
                        priorityGroup);

        medium =
                createRadioButton(
                        "Medium",
                        priorityGroup);

        high =
                createRadioButton(
                        "High",
                        priorityGroup);

        medium.setSelected(true);

        HBox priorityBox =
                new HBox(
                        10,
                        low,
                        medium,
                        high);

        priorityBox.setAlignment(
                Pos.CENTER_LEFT);

        VBox priorityContainer =
                new VBox(
                        6,
                        priorityLabel,
                        priorityBox);

        HBox.setHgrow(
                issueBox,
                Priority.ALWAYS);

        HBox.setHgrow(
                priorityContainer,
                Priority.ALWAYS);

        HBox issuePriority =
                new HBox(
                        16,
                        issueBox,
                        priorityContainer);

        // =====================================================
        // SUBJECT
        // =====================================================

        Label subjectLabel =
                createFieldLabel(
                        "Subject");

        subjectField =
                new TextField();

        subjectField.setPromptText(
                "Brief summary of the issue");

        subjectField.setPrefHeight(38);

        styleControl(subjectField);

        VBox subjectBox =
                new VBox(
                        4,
                        subjectLabel,
                        subjectField);

        // =====================================================
        // DESCRIPTION
        // =====================================================

        Label descriptionLabel =
                createFieldLabel(
                        "Description");

        descriptionArea =
                new TextArea();

        descriptionArea.setPromptText(
                "Provide as much detail as possible...");

        descriptionArea.setWrapText(true);

        descriptionArea.setPrefHeight(70);
        descriptionArea.setMinHeight(70);
        descriptionArea.setMaxHeight(70);

        descriptionArea.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " +
                        BORDER +
                        ";" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-font-size: 13px;");

        VBox descriptionBox =
                new VBox(
                        4,
                        descriptionLabel,
                        descriptionArea);

        // =====================================================
        // ATTACHMENT
        // =====================================================

        Label attachmentTitle =
                createFieldLabel(
                        "Attachments");

        Button uploadButton =
                new Button();

        uploadButton.setMaxWidth(
                Double.MAX_VALUE);

        uploadButton.setPrefHeight(90);
        uploadButton.setMinHeight(90);
        uploadButton.setMaxHeight(90);

        uploadButton.setCursor(
                Cursor.HAND);

        attachmentLabel =
                new Label(
                        "☁   Drag & Drop files here\n" +
                                "PDF, PNG, JPG (Max 5MB)");

        attachmentLabel.setTextAlignment(
                javafx.scene.text.TextAlignment.CENTER);

        attachmentLabel.setAlignment(
                Pos.CENTER);

        attachmentLabel.setFont(
                Font.font(
                        "Arial",
                        12));

        attachmentLabel.setTextFill(
                Color.web(TEXT));

        uploadButton.setGraphic(
                attachmentLabel);

        setUploadButtonStyle(
                uploadButton,
                false);

        uploadButton.setOnMouseEntered(
                e -> setUploadButtonStyle(
                        uploadButton,
                        true));

        uploadButton.setOnMouseExited(
                e -> setUploadButtonStyle(
                        uploadButton,
                        false));

        uploadButton.setOnAction(
                e -> chooseAttachment());

        VBox attachmentBox =
                new VBox(
                        4,
                        attachmentTitle,
                        uploadButton);

        // =====================================================
        // RESET
        // =====================================================

        Button reset =
                new Button(
                        "Reset");

        reset.setPrefWidth(95);
        reset.setPrefHeight(38);

        reset.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        reset.setCursor(
                Cursor.HAND);

        setResetButtonStyle(
                reset,
                false);

        reset.setOnMouseEntered(
                e -> setResetButtonStyle(
                        reset,
                        true));

        reset.setOnMouseExited(
                e -> setResetButtonStyle(
                        reset,
                        false));

        reset.setOnAction(
                e -> resetForm());

        // =====================================================
        // SUBMIT
        // =====================================================

        Button submit =
                new Button(
                        "Submit Ticket");

        submit.setPrefHeight(38);

        submit.setMaxWidth(
                Double.MAX_VALUE);

        submit.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        submit.setTextFill(
                Color.WHITE);

        submit.setCursor(
                Cursor.HAND);

        setSubmitButtonStyle(
                submit,
                false);

        submit.setOnMouseEntered(
                e -> setSubmitButtonStyle(
                        submit,
                        true));

        submit.setOnMouseExited(
                e -> setSubmitButtonStyle(
                        submit,
                        false));

        submit.setOnAction(
                e -> submitTicket());

        HBox buttons =
                new HBox(
                        12,
                        reset,
                        submit);

        HBox.setHgrow(
                submit,
                Priority.ALWAYS);

        card.getChildren().addAll(
                cardTitle,
                issuePriority,
                subjectBox,
                descriptionBox,
                attachmentBox,
                buttons);

        return card;
    }

    // =========================================================
    // PREVIOUS TICKETS
    // =========================================================

    private VBox createPreviousTicketsCard() {

        VBox card =
                createCard();

        HBox header =
                new HBox();

        VBox titleBox =
                new VBox(2);

        Label title =
                new Label(
                        "My Support Tickets");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16));

        title.setTextFill(
                Color.web(TEXT));

        Label subtitle =
                new Label(
                        "Track your previous requests and support actions");

        subtitle.setFont(
                Font.font(
                        "Arial",
                        11));

        subtitle.setTextFill(
                Color.web(MUTED));

        titleBox.getChildren().addAll(
                title,
                subtitle);

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        ticketCountLabel =
                new Label();

        ticketCountLabel.setPadding(
                new Insets(
                        5,
                        10,
                        5,
                        10));

        ticketCountLabel.setStyle(
                "-fx-background-color: " +
                        LIGHT_GREEN +
                        ";" +
                        "-fx-background-radius: 20;");

        ticketCountLabel.setTextFill(
                Color.web(GREEN));

        ticketCountLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11));

        header.getChildren().addAll(
                titleBox,
                spacer,
                ticketCountLabel);

        ticketList =
                new VBox(7);

        refreshTicketList();

        card.getChildren().addAll(
                header,
                ticketList);

        return card;
    }

    // =========================================================
    // LOAD ONLY CURRENT DRIVER'S TICKETS
    // =========================================================

    private void loadTickets() {

        try {

            if (driverEmail == null ||
                    driverEmail.isBlank()) {

                tickets.clear();

                return;
            }

            List<SupportTicket> loadedTickets =
                    supportController.getDriverTickets(
                            driverEmail);

            tickets.clear();

            if (loadedTickets != null) {
                tickets.addAll(
                        loadedTickets);
            }

        } catch (Exception e) {

            e.printStackTrace();

            tickets.clear();

            showAlert(
                    "Support Error",
                    "Unable to load your support tickets.\n\n"
                            + e.getMessage());
        }
    }

    // =========================================================
    // REFRESH LIST
    // =========================================================

    private void refreshTicketList() {

        if (ticketList == null) {
            return;
        }

        ticketList.getChildren().clear();

        if (tickets.isEmpty()) {

            Label empty =
                    new Label(
                            driverEmail == null ||
                                    driverEmail.isBlank()
                                    ? "Driver account email is not available."
                                    : "No support tickets found.");

            empty.setFont(
                    Font.font(
                            "Arial",
                            12));

            empty.setTextFill(
                    Color.web(MUTED));

            empty.setPadding(
                    new Insets(12));

            ticketList.getChildren().add(
                    empty);

        } else {

            for (SupportTicket ticket :
                    tickets) {

                ticketList.getChildren().add(
                        createTicketRow(ticket));
            }
        }

        updateTicketCount();
    }

    // =========================================================
    // UPDATE COUNT
    // =========================================================

    private void updateTicketCount() {

        if (ticketCountLabel == null) {
            return;
        }

        int count =
                tickets.size();

        ticketCountLabel.setText(
                count +
                        (count == 1
                                ? " Ticket"
                                : " Tickets"));
    }

    // =========================================================
    // TICKET ROW
    // =========================================================

    private HBox createTicketRow(
            SupportTicket ticket) {

        HBox row =
                new HBox(10);

        row.setPadding(
                new Insets(
                        7,
                        9,
                        7,
                        9));

        row.setAlignment(
                Pos.CENTER_LEFT);

        row.setCursor(
                Cursor.HAND);

        row.setStyle(
                ticketNormalStyle());

        // =====================================================
        // ICON
        // =====================================================

        Label icon =
                new Label(
                        getTicketIcon(
                                ticket.getIssueType()));

        icon.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15));

        icon.setTextFill(
                Color.web(GREEN));

        StackPane iconBox =
                new StackPane(icon);

        iconBox.setPrefSize(
                32,
                32);

        iconBox.setMaxSize(
                32,
                32);

        iconBox.setStyle(
                "-fx-background-color: " +
                        LIGHT_GREEN +
                        ";" +
                        "-fx-background-radius: 9;");

        // =====================================================
        // INFO
        // =====================================================

        VBox info =
                new VBox(2);

        HBox subjectLine =
                new HBox(8);

        Label id =
                new Label(
                        "#" +
                                safe(ticket.getId()));

        id.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11));

        id.setTextFill(
                Color.web(GREEN));

        Label subject =
                new Label(
                        safe(ticket.getSubject()));

        subject.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13));

        subject.setTextFill(
                Color.web(TEXT));

        subjectLine.getChildren().addAll(
                id,
                subject);

        Label details =
                new Label(
                        safe(ticket.getIssueType()) +
                                "  •  " +
                                safe(ticket.getPriority()) +
                                "  •  " +
                                safe(ticket.getCreatedAt()));

        details.setFont(
                Font.font(
                        "Arial",
                        10));

        details.setTextFill(
                Color.web(MUTED));

        info.getChildren().addAll(
                subjectLine,
                details);

        HBox.setHgrow(
                info,
                Priority.ALWAYS);

        // =====================================================
        // SPACER
        // =====================================================

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        // =====================================================
        // STATUS
        // =====================================================

        Label status =
                createStatusBadge(
                        ticket.getStatus());

        // =====================================================
        // VIEW
        // =====================================================

        Button view =
                new Button(
                        "View Details");

        view.setPrefHeight(30);

        view.setCursor(
                Cursor.HAND);

        setDetailsButtonStyle(
                view,
                false);

        view.setOnMouseEntered(
                e -> setDetailsButtonStyle(
                        view,
                        true));

        view.setOnMouseExited(
                e -> setDetailsButtonStyle(
                        view,
                        false));

        view.setOnAction(
                e -> showTicketDetails(ticket));

        row.getChildren().addAll(
                iconBox,
                info,
                spacer,
                status,
                view);

        row.setOnMouseClicked(
                e -> {

                    if (!isInsideNode(
                            view,
                            e.getTarget())) {

                        showTicketDetails(ticket);
                    }
                });

        row.setOnMouseEntered(
                e -> row.setStyle(
                        ticketHoverStyle()));

        row.setOnMouseExited(
                e -> row.setStyle(
                        ticketNormalStyle()));

        return row;
    }

    // =========================================================
    // TICKET DETAILS
    // =========================================================

    private void showTicketDetails(
            SupportTicket ticket) {

        Stage dialog =
                new Stage();

        dialog.initModality(
                Modality.APPLICATION_MODAL);

        dialog.setTitle(
                "Ticket #" +
                        safe(ticket.getId()));

        VBox content =
                new VBox(14);

        content.setPadding(
                new Insets(22));

        content.setStyle(
                "-fx-background-color: " +
                        PAGE_BG +
                        ";");

        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                new HBox();

        VBox titleBox =
                new VBox(3);

        Label ticketId =
                new Label(
                        "Ticket #" +
                                safe(ticket.getId()));

        ticketId.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        20));

        ticketId.setTextFill(
                Color.web(TEXT));

        Label date =
                new Label(
                        "Created " +
                                safe(ticket.getCreatedAt()));

        date.setFont(
                Font.font(
                        "Arial",
                        11));

        date.setTextFill(
                Color.web(MUTED));

        titleBox.getChildren().addAll(
                ticketId,
                date);

        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS);

        Label status =
                createStatusBadge(
                        ticket.getStatus());

        header.getChildren().addAll(
                titleBox,
                headerSpacer,
                status);

        // =====================================================
        // SUBJECT
        // =====================================================

        Label subject =
                new Label(
                        safe(ticket.getSubject()));

        subject.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18));

        subject.setTextFill(
                Color.web(TEXT));

        // =====================================================
        // METADATA
        // =====================================================

        HBox metadata =
                new HBox(
                        8,
                        createInfoBadge(
                                ticket.getIssueType()),
                        createInfoBadge(
                                ticket.getPriority()));

        // =====================================================
        // DESCRIPTION
        // =====================================================

        VBox descriptionCard =
                createCard();

        Label descriptionTitle =
                new Label(
                        "Issue Description");

        descriptionTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14));

        Label description =
                new Label(
                        safe(ticket.getDescription()));

        description.setWrapText(true);

        description.setFont(
                Font.font(
                        "Arial",
                        12));

        description.setTextFill(
                Color.web(MUTED));

        descriptionCard.getChildren().addAll(
                descriptionTitle,
                description);

        // =====================================================
        // ATTACHMENT
        // =====================================================

        VBox attachmentCard =
                null;

        if (ticket.getAttachment() != null &&
                !ticket.getAttachment().isEmpty()) {

            attachmentCard =
                    createCard();

            Label attachmentTitle =
                    new Label(
                            "Attachment");

            attachmentTitle.setFont(
                    Font.font(
                            "Arial",
                            FontWeight.BOLD,
                            14));

            Label attachment =
                    new Label(
                            "📎 " +
                                    ticket.getAttachment());

            attachment.setFont(
                    Font.font(
                            "Arial",
                            12));

            attachment.setTextFill(
                    Color.web(GREEN));

            attachmentCard.getChildren().addAll(
                    attachmentTitle,
                    attachment);
        }

        // =====================================================
        // TIMELINE
        // =====================================================

        VBox timelineCard =
                createCard();

        Label timelineTitle =
                new Label(
                        "Support Activity");

        timelineTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15));

        VBox timeline =
                new VBox(0);

        List<TimelineEvent> events =
                ticket.getTimeline();

        if (events != null) {

            for (int i = 0;
                 i < events.size();
                 i++) {

                TimelineEvent event =
                        events.get(i);

                timeline.getChildren().add(
                        createTimelineEvent(
                                event,
                                i ==
                                        events.size() - 1));
            }
        }

        timelineCard.getChildren().addAll(
                timelineTitle,
                timeline);

        // =====================================================
        // RESOLUTION
        // =====================================================

        VBox actionCard =
                createCard();

        Label actionTitle =
                new Label(
                        "Action Taken / Resolution");

        actionTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15));

        Label actionText =
                new Label(
                        safe(ticket.getResolution()));

        actionText.setWrapText(true);

        actionText.setFont(
                Font.font(
                        "Arial",
                        12));

        actionText.setTextFill(
                Color.web(MUTED));

        actionCard.getChildren().addAll(
                actionTitle,
                actionText);

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        content.getChildren().addAll(
                header,
                subject,
                metadata,
                descriptionCard);

        if (attachmentCard != null) {

            content.getChildren().add(
                    attachmentCard);
        }

        content.getChildren().addAll(
                timelineCard,
                actionCard);

        // =====================================================
        // FEEDBACK
        // =====================================================

        String currentStatus =
                ticket.getStatus();

        if ("Resolved".equalsIgnoreCase(
                currentStatus) ||
                "Closed".equalsIgnoreCase(
                        currentStatus)) {

            content.getChildren().add(
                    createFeedbackCard(
                            ticket,
                            dialog));
        }

        // =====================================================
        // CLOSE
        // =====================================================

        Button close =
                new Button("Close");

        close.setPrefWidth(90);
        close.setPrefHeight(36);

        close.setCursor(
                Cursor.HAND);

        close.setOnAction(
                e -> dialog.close());

        HBox bottom =
                new HBox(close);

        bottom.setAlignment(
                Pos.CENTER_RIGHT);

        content.getChildren().add(
                bottom);

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scroll =
                new ScrollPane(
                        content);

        scroll.setFitToWidth(true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setStyle(
                "-fx-background-color: " +
                        PAGE_BG +
                        ";");

        Scene scene =
                new Scene(
                        scroll,
                        720,
                        760);

        dialog.setScene(scene);

        dialog.showAndWait();
    }

    // =========================================================
    // TIMELINE
    // =========================================================

    private HBox createTimelineEvent(
            TimelineEvent event,
            boolean last) {

        HBox container =
                new HBox(12);

        container.setPadding(
                new Insets(
                        8,
                        0,
                        8,
                        0));

        VBox line =
                new VBox();

        line.setAlignment(
                Pos.TOP_CENTER);

        Circle circle =
                new Circle(
                        6,
                        Color.web(GREEN));

        Region vertical =
                new Region();

        vertical.setPrefWidth(2);

        vertical.setPrefHeight(
                last
                        ? 0
                        : 45);

        vertical.setStyle(
                "-fx-background-color: #B8D7C7;");

        line.getChildren().addAll(
                circle,
                vertical);

        VBox info =
                new VBox(2);

        Label action =
                new Label(
                        safe(event.getAction()));

        action.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13));

        action.setTextFill(
                Color.web(TEXT));

        Label time =
                new Label(
                        safe(event.getTime()));

        time.setFont(
                Font.font(
                        "Arial",
                        10));

        time.setTextFill(
                Color.web(MUTED));

        Label description =
                new Label(
                        safe(
                                event.getDescription()));

        description.setWrapText(true);

        description.setFont(
                Font.font(
                        "Arial",
                        11));

        description.setTextFill(
                Color.web(MUTED));

        info.getChildren().addAll(
                action,
                time,
                description);

        HBox.setHgrow(
                info,
                Priority.ALWAYS);

        container.getChildren().addAll(
                line,
                info);

        return container;
    }

    // =========================================================
    // FEEDBACK
    // =========================================================

    private VBox createFeedbackCard(
            SupportTicket ticket,
            Stage dialog) {

        VBox card =
                createCard();

        Label title =
                new Label(
                        ticket.getRating() > 0
                                ? "Your Support Feedback"
                                : "How was your support experience?");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14));

        HBox stars =
                new HBox(5);

        ToggleGroup group =
                new ToggleGroup();

        List<RadioButton> starButtons =
                new ArrayList<>();

        for (int i = 1; i <= 5; i++) {

            final int rating = i;

            RadioButton star =
                    new RadioButton("★");

            star.setToggleGroup(
                    group);

            star.setUserData(
                    rating);

            star.setFont(
                    Font.font(
                            "Arial",
                            25));

            star.setCursor(
                    Cursor.HAND);

            star.setTextFill(
                    Color.web(
                            rating <=
                                    ticket.getRating()
                                    ? "#F4B400"
                                    : "#D1D8D5"));

            star.setOnAction(
                    e -> {

                        for (
                                RadioButton s :
                                starButtons) {

                            int value =
                                    (int)
                                            s.getUserData();

                            s.setTextFill(
                                    Color.web(
                                            value <= rating
                                                    ? "#F4B400"
                                                    : "#D1D8D5"));
                        }
                    });

            starButtons.add(
                    star);

            stars.getChildren().add(
                    star);
        }

        TextArea comment =
                new TextArea();

        comment.setPromptText(
                "Tell us about your experience...");

        comment.setPrefRowCount(2);

        comment.setWrapText(true);

        comment.setText(
                ticket.getFeedback() == null
                        ? ""
                        : ticket.getFeedback());

        comment.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " +
                        BORDER +
                        ";" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;");

        Button submit =
                new Button(
                        ticket.getRating() > 0
                                ? "Update Feedback"
                                : "Submit Feedback");

        submit.setPrefHeight(35);

        submit.setCursor(
                Cursor.HAND);

        submit.setTextFill(
                Color.WHITE);

        submit.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11));

        submit.setStyle(
                "-fx-background-color: " +
                        GREEN +
                        ";" +
                        "-fx-background-radius: 8;");

        submit.setOnAction(
                e -> {

                    if (group.getSelectedToggle()
                            == null &&
                            ticket.getRating() == 0) {

                        showAlert(
                                "Feedback Required",
                                "Please select a rating before submitting your feedback.");

                        return;
                    }

                    int rating =
                            ticket.getRating();

                    if (group.getSelectedToggle()
                            != null) {

                        rating =
                                (int)
                                        group
                                                .getSelectedToggle()
                                                .getUserData();
                    }

                    String feedback =
                            comment.getText()
                                    .trim();

                    try {

                        boolean success =
                                supportController
                                        .submitFeedback(
                                                ticket.getId(),
                                                rating,
                                                feedback);

                        if (!success) {

                            showAlert(
                                    "Feedback Error",
                                    "Unable to save your feedback.");

                            return;
                        }

                        ticket.setRating(
                                rating);

                        ticket.setFeedback(
                                feedback);

                        showAlert(
                                "Feedback Submitted",
                                "Thank you for your feedback.");

                        dialog.close();

                    } catch (Exception ex) {

                        ex.printStackTrace();

                        showAlert(
                                "Feedback Error",
                                "Unable to save your feedback.\n\n"
                                        + ex.getMessage());
                    }
                });

        card.getChildren().addAll(
                title,
                stars,
                comment,
                submit);

        return card;
    }

    // =========================================================
    // FAQ
    // =========================================================

    private VBox createFAQCard() {

        VBox card =
                createCard();

        Label title =
                new Label(
                        "Frequently Asked Questions");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16));

        VBox faq1 =
                createFAQ(
                        "How do I accept a load?",
                        "Open the Available Loads section, select the load you want, review its details and select Accept Load.");

        VBox faq2 =
                createFAQ(
                        "How do I cancel an active trip?",
                        "Open Active Trip and select the trip you want to cancel. Follow the cancellation instructions shown by the application.");

        VBox faq3 =
                createFAQ(
                        "How long does payout take?",
                        "Payout processing depends on successful trip completion and payment verification. You can check the payment status from the relevant trip information.");

        card.getChildren().addAll(
                title,
                faq1,
                faq2,
                faq3);

        return card;
    }

    // =========================================================
    // FAQ ITEM
    // =========================================================

    private VBox createFAQ(
            String question,
            String answer) {

        VBox box =
                new VBox();

        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " +
                        BORDER +
                        ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;");

        Button questionButton =
                new Button(
                        question +
                                "   ⌄");

        questionButton.setMaxWidth(
                Double.MAX_VALUE);

        questionButton.setAlignment(
                Pos.CENTER_LEFT);

        questionButton.setPrefHeight(36);

        questionButton.setCursor(
                Cursor.HAND);

        questionButton.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        questionButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " +
                        TEXT +
                        ";");

        Label answerLabel =
                new Label(answer);

        answerLabel.setWrapText(true);

        answerLabel.setFont(
                Font.font(
                        "Arial",
                        11));

        answerLabel.setTextFill(
                Color.web(MUTED));

        answerLabel.setPadding(
                new Insets(
                        2,
                        10,
                        10,
                        10));

        VBox answerBox =
                new VBox(
                        answerLabel);

        questionButton.setOnAction(
                e -> {

                    if (box.getChildren()
                            .contains(answerBox)) {

                        box.getChildren()
                                .remove(
                                        answerBox);

                        questionButton.setText(
                                question +
                                        "   ⌄");

                    } else {

                        box.getChildren()
                                .add(
                                        answerBox);

                        questionButton.setText(
                                question +
                                        "   ⌃");
                    }
                });

        box.getChildren().add(
                questionButton);

        return box;
    }

    // =========================================================
    // CONTACT CARD
    // =========================================================

    private VBox createContactCard() {

        VBox card =
                createCard();

        Label title =
                new Label(
                        "Other Ways To Contact");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16));

        // =====================================================
        // PHONE
        // =====================================================

        Label phoneIcon =
                new Label("☎");

        phoneIcon.setFont(
                Font.font(
                        "Arial",
                        17));

        phoneIcon.setTextFill(
                Color.web(GREEN));

        StackPane phoneCircle =
                new StackPane(
                        phoneIcon);

        phoneCircle.setPrefSize(
                36,
                36);

        phoneCircle.setMaxSize(
                36,
                36);

        phoneCircle.setStyle(
                "-fx-background-color: " +
                        LIGHT_GREEN +
                        ";" +
                        "-fx-background-radius: 50;");

        Label phoneTitle =
                new Label(
                        "SUPPORT PHONE");

        phoneTitle.setFont(
                Font.font(
                        "Arial",
                        10));

        phoneTitle.setTextFill(
                Color.web(MUTED));

        Label phone =
                new Label(
                        "1800-123-4567");

        phone.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14));

        VBox phoneText =
                new VBox(
                        2,
                        phoneTitle,
                        phone);

        HBox phoneRow =
                new HBox(
                        12,
                        phoneCircle,
                        phoneText);

        phoneRow.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // EMAIL
        // =====================================================

        Label emailIcon =
                new Label("✉");

        emailIcon.setFont(
                Font.font(
                        "Arial",
                        17));

        StackPane emailCircle =
                new StackPane(
                        emailIcon);

        emailCircle.setPrefSize(
                36,
                36);

        emailCircle.setMaxSize(
                36,
                36);

        emailCircle.setStyle(
                "-fx-background-color: #EEF1F0;" +
                        "-fx-background-radius: 50;");

        Label emailTitle =
                new Label(
                        "EMAIL ADDRESS");

        emailTitle.setFont(
                Font.font(
                        "Arial",
                        10));

        emailTitle.setTextFill(
                Color.web(MUTED));

        Label email =
                new Label(
                        "support@ecoload.com");

        email.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13));

        VBox emailText =
                new VBox(
                        2,
                        emailTitle,
                        email);

        HBox emailRow =
                new HBox(
                        12,
                        emailCircle,
                        emailText);

        emailRow.setAlignment(
                Pos.CENTER_LEFT);

        Separator separator =
                new Separator();

        // =====================================================
        // CHATBOT
        // =====================================================

        Button chat =
                new Button(
                        "▣  Chat Bot");

        chat.setMaxWidth(
                Double.MAX_VALUE);

        chat.setPrefHeight(38);

        chat.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        chat.setTextFill(
                Color.WHITE);

        chat.setCursor(
                Cursor.HAND);

        chat.setStyle(
                "-fx-background-color: " +
                        DARK_GREEN +
                        ";" +
                        "-fx-background-radius: 9;");

        chat.setOnMouseEntered(
                e -> chat.setStyle(
                        "-fx-background-color: " +
                                GREEN +
                                ";" +
                                "-fx-background-radius: 9;"));

        chat.setOnMouseExited(
                e -> chat.setStyle(
                        "-fx-background-color: " +
                                DARK_GREEN +
                                ";" +
                                "-fx-background-radius: 9;"));

        chat.setOnAction(
                e -> showAlert(
                        "ChatBot",
                        "ChatBot will be available shortly."));

        Label response =
                new Label(
                        "Response time: Usually within 2-4 hours");

        response.setFont(
                Font.font(
                        "Arial",
                        FontPosture.ITALIC,
                        11));

        response.setTextFill(
                Color.web(MUTED));

        card.getChildren().addAll(
                title,
                phoneRow,
                emailRow,
                separator,
                response);

        return card;
    }

    // =========================================================
    // INFORMATION BAR
    // =========================================================

    private HBox createInformationBar() {

        Label icon =
                new Label("ⓘ");

        icon.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        19));

        icon.setTextFill(
                Color.web(GREEN));

        Label text =
                new Label(
                        "Our support team usually replies within a few hours. " +
                                "Please have your Trip ID ready for faster resolution.");

        text.setFont(
                Font.font(
                        "Arial",
                        12));

        text.setTextFill(
                Color.web(TEXT));

        HBox bar =
                new HBox(
                        12,
                        icon,
                        text);

        bar.setPadding(
                new Insets(
                        8,
                        14,
                        8,
                        14));

        bar.setAlignment(
                Pos.CENTER_LEFT);

        bar.setStyle(
                "-fx-background-color: #F4FBF6;" +
                        "-fx-border-color: #C8E2D0;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;");

        return bar;
    }

    // =========================================================
    // SECURITY BAR
    // =========================================================

    private HBox createSecurityBar() {

        Label icon =
                new Label("⬟");

        icon.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18));

        icon.setTextFill(
                Color.web(BLUE));

        Label text =
                new Label(
                        "Security Alert: Never share your password or OTP " +
                                "with anyone, including EcoLoad support staff.");

        text.setFont(
                Font.font(
                        "Arial",
                        13));

        text.setTextFill(
                Color.web("#304D87"));

        HBox bar =
                new HBox(
                        12,
                        icon,
                        text);

        bar.setPadding(
                new Insets(
                        8,
                        14,
                        8,
                        14));

        bar.setAlignment(
                Pos.CENTER_LEFT);

        bar.setStyle(
                "-fx-background-color: #F0F6FF;" +
                        "-fx-border-color: #C7DAF5;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;");

        return bar;
    }

    // =========================================================
    // SUBMIT TICKET
    // =========================================================

    private void submitTicket() {

        if (driverEmail == null ||
                driverEmail.isBlank()) {

            showAlert(
                    "Driver Account Error",
                    "Driver email is not available.\n\n" +
                            "Please make sure the logged-in driver's email " +
                            "is passed to the Support page.");

            return;
        }

        String subject =
                subjectField.getText()
                        .trim();

        String description =
                descriptionArea.getText()
                        .trim();

        // =====================================================
        // VALIDATION
        // =====================================================

        if (subject.isEmpty()) {

            showAlert(
                    "Missing Subject",
                    "Please enter the subject of your issue.");

            subjectField.requestFocus();

            return;
        }

        if (description.isEmpty()) {

            showAlert(
                    "Missing Description",
                    "Please describe your issue.");

            descriptionArea.requestFocus();

            return;
        }

        String priority;

        if (low.isSelected()) {

            priority = "Low";

        } else if (high.isSelected()) {

            priority = "High";

        } else {

            priority = "Medium";
        }
        CurrentDriver currentDriver =
        CurrentDriver.getInstance();

        String driverEmail = currentDriver.getDriver().getEmail();

        String driverName = currentDriver.getDriver().getUsername();

        try {

            // =================================================
            // CONTROLLER CREATES + SAVES DRIVER TICKET
            // =================================================

            SupportTicket ticket =
                    supportController.createTicket(
                            driverEmail,
                            driverName,
                            issueType.getValue(),
                            priority,
                            subject,
                            description,
                            selectedAttachment == null
                                    ? null
                                    : selectedAttachment.getAbsolutePath());

            if (ticket == null) {

                showAlert(
                        "Support Error",
                        "The ticket could not be created.");

                return;
            }

            // =================================================
            // UPDATE UI
            // =================================================

            tickets.add(
                    0,
                    ticket);

            refreshTicketList();

            resetForm();

            showAlert(
                    "Support Ticket Submitted",
                    "Your support ticket has been submitted successfully.\n\n" +
                            "Ticket ID: #" +
                            ticket.getId());

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    "Support Error",
                    "Unable to submit support ticket.\n\n"
                            + e.getMessage());
        }
    }

    // =========================================================
    // ATTACHMENT
    // =========================================================

    private void chooseAttachment() {

        FileChooser chooser =
                new FileChooser();

        chooser.setTitle(
                "Select Attachment");

        chooser.getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(
                                "Supported Files",
                                "*.pdf",
                                "*.png",
                                "*.jpg",
                                "*.jpeg"));

        File file =
                chooser.showOpenDialog(null);

        if (file == null) {
            return;
        }

        long size =
                file.length();

        // 5 MB
        if (size >
                5 * 1024 * 1024) {

            showAlert(
                    "File Too Large",
                    "Please select a file smaller than 5 MB.");

            return;
        }

        selectedAttachment =
                file;

        attachmentLabel.setText(
                "✓  " +
                        file.getName());
    }

    // =========================================================
    // RESET
    // =========================================================

    private void resetForm() {

        issueType.setValue(
                "Technical Issue");

        medium.setSelected(true);

        subjectField.clear();

        descriptionArea.clear();

        selectedAttachment = null;

        attachmentLabel.setText(
                "☁   Drag & Drop files here\n" +
                        "PDF, PNG, JPG (Max 5MB)");
    }

    // =========================================================
    // CARD
    // =========================================================

    private VBox createCard() {

        VBox card =
                new VBox(9);

        card.setPadding(
                new Insets(16));

        card.setMaxWidth(
                Double.MAX_VALUE);

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " +
                        BORDER +
                        ";" +
                        "-fx-border-radius: 16;");

        return card;
    }

    // =========================================================
    // FIELD LABEL
    // =========================================================

    private Label createFieldLabel(
            String text) {

        Label label =
                new Label(text);

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13));

        label.setTextFill(
                Color.web(TEXT));

        return label;
    }

    // =========================================================
    // RADIO BUTTON
    // =========================================================

    private RadioButton createRadioButton(
            String text,
            ToggleGroup group) {

        RadioButton radio =
                new RadioButton(text);

        radio.setToggleGroup(
                group);

        radio.setFont(
                Font.font(
                        "Arial",
                        13));

        radio.setTextFill(
                Color.web(TEXT));

        return radio;
    }

    // =========================================================
    // CONTROL STYLE
    // =========================================================

    private void styleControl(
            Control control) {

        control.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " +
                        BORDER +
                        ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 14px;");
    }

    // =========================================================
    // STATUS BADGE
    // =========================================================

    private Label createStatusBadge(
            String status) {

        status =
                status == null
                        ? "Unknown"
                        : status;

        Label badge =
                new Label(status);

        badge.setPadding(
                new Insets(
                        5,
                        9,
                        5,
                        9));

        badge.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10));

        String background;
        String color;

        switch (status) {

            case "Resolved":

                background = "#DFF5E7";
                color = "#087A3A";

                break;

            case "In Progress":

                background = "#FFF0D5";
                color = "#C87500";

                break;

            case "Open":

                background = "#E2EEFF";
                color = "#2865B0";

                break;

            case "Closed":

                background = "#E9EEEC";
                color = "#596765";

                break;

            default:

                background = LIGHT_GREEN;
                color = GREEN;
        }

        badge.setStyle(
                "-fx-background-color: " +
                        background +
                        ";" +
                        "-fx-background-radius: 20;");

        badge.setTextFill(
                Color.web(color));

        return badge;
    }

    // =========================================================
    // INFO BADGE
    // =========================================================

    private Label createInfoBadge(
            String text) {

        Label badge =
                new Label(
                        safe(text));

        badge.setPadding(
                new Insets(
                        5,
                        9,
                        5,
                        9));

        badge.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10));

        badge.setStyle(
                "-fx-background-color: " +
                        LIGHT_GREEN +
                        ";" +
                        "-fx-background-radius: 20;");

        badge.setTextFill(
                Color.web(GREEN));

        return badge;
    }

    // =========================================================
    // TICKET ICON
    // =========================================================

    private String getTicketIcon(
            String issueType) {

        if (issueType == null) {
            return "✦";
        }

        switch (issueType) {

            case "Payment Issue":
                return "₹";

            case "Load Issue":
                return "▣";

            case "Trip Issue":
                return "↗";

            case "Vehicle Issue":
                return "▤";

            case "Account Issue":
                return "●";

            case "Document Issue":
                return "▤";

            default:
                return "✦";
        }
    }

    // =========================================================
    // NORMAL TICKET STYLE
    // =========================================================

    private String ticketNormalStyle() {

        return
                "-fx-background-color: white;" +
                        "-fx-border-color: #E0E7E3;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;";
    }

    // =========================================================
    // HOVER TICKET STYLE
    // =========================================================

    private String ticketHoverStyle() {

        return
                "-fx-background-color: #F5FBF7;" +
                        "-fx-border-color: #0A8A57;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;";
    }

    // =========================================================
    // UPLOAD STYLE
    // =========================================================

    private void setUploadButtonStyle(
            Button button,
            boolean hover) {

        if (hover) {

            button.setStyle(
                    "-fx-background-color: #F8FCF9;" +
                            "-fx-border-color: " +
                            GREEN +
                            ";" +
                            "-fx-border-style: dashed;" +
                            "-fx-border-width: 1.5;" +
                            "-fx-border-radius: 10;" +
                            "-fx-background-radius: 10;");

        } else {

            button.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #BFC8C1;" +
                            "-fx-border-style: dashed;" +
                            "-fx-border-width: 1.5;" +
                            "-fx-border-radius: 10;" +
                            "-fx-background-radius: 10;");
        }
    }

    // =========================================================
    // RESET BUTTON STYLE
    // =========================================================

    private void setResetButtonStyle(
            Button button,
            boolean hover) {

        if (hover) {

            button.setStyle(
                    "-fx-background-color: #F3F6F4;" +
                            "-fx-text-fill: " +
                            TEXT +
                            ";" +
                            "-fx-border-color: #AEBBB4;" +
                            "-fx-border-radius: 9;" +
                            "-fx-background-radius: 9;");

        } else {

            button.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-text-fill: " +
                            TEXT +
                            ";" +
                            "-fx-border-color: #CDD5CF;" +
                            "-fx-border-radius: 9;" +
                            "-fx-background-radius: 9;");
        }
    }

    // =========================================================
    // SUBMIT BUTTON STYLE
    // =========================================================

    private void setSubmitButtonStyle(
            Button button,
            boolean hover) {

        button.setStyle(
                "-fx-background-color: " +
                        (hover
                                ? GREEN_HOVER
                                : GREEN) +
                        ";" +
                        "-fx-background-radius: 9;");
    }

    // =========================================================
    // DETAILS BUTTON STYLE
    // =========================================================

    private void setDetailsButtonStyle(
            Button button,
            boolean hover) {

        if (hover) {

            button.setStyle(
                    "-fx-background-color: " +
                            LIGHT_GREEN +
                            ";" +
                            "-fx-border-color: " +
                            GREEN +
                            ";" +
                            "-fx-border-radius: 7;" +
                            "-fx-background-radius: 7;" +
                            "-fx-font-size: 11px;" +
                            "-fx-font-weight: bold;");

        } else {

            button.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #CDD8D2;" +
                            "-fx-border-radius: 7;" +
                            "-fx-background-radius: 7;" +
                            "-fx-font-size: 11px;" +
                            "-fx-font-weight: bold;");
        }
    }

    // =========================================================
    // CHECK NODE
    // =========================================================

    private boolean isInsideNode(
            Node node,
            Object target) {

        if (!(target instanceof Node)) {
            return false;
        }

        Node current =
                (Node) target;

        while (current != null) {

            if (current == node) {
                return true;
            }

            current =
                    current.getParent();
        }

        return false;
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }

    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(
                message);

        alert.showAndWait();
    }
}