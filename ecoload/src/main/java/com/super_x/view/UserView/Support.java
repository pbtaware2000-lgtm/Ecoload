package com.super_x.view.UserView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import com.super_x.model.usermodel.*;
import com.super_x.config.FirebaseConfig;
import com.super_x.controller.usercontroller.SupportController;
import com.super_x.dao.userdao.SupportDAO;
import com.super_x.model.usermodel.SupportTicket;
import com.super_x.model.usermodel.TimelineEvent;
import com.super_x.model.usermodel.UserModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Support {

    // =========================================================
    // COLORS
    // =========================================================

    private static final String GREEN = "#087A3A";
    private static final String DARK_GREEN = "#064C38";
    private static final String LIGHT_GREEN = "#EAF8EF";
    private static final String PAGE_BG = "#F7F9F7";
    private static final String BORDER = "#DDE5DF";
    private static final String TEXT = "#26312D";
    private static final String MUTED = "#707875";
    private static final String BG = "#E6F1E8";

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

    private String selectedAttachment;

    private final SupportController supportController;

    // =========================================================
    // TICKETS
    // =========================================================

    private final List<SupportTicket> submittedTickets = new ArrayList<>();

    private VBox ticketsContainer;

    // =========================================================
    // SCENE
    // =========================================================

    private Scene supportScene;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Support() {

        SupportDAO supportDAO = new SupportDAO(
                FirebaseConfig.getFireStore());

        this.supportController = new SupportController(
                supportDAO);
    }

    public Support(
            SupportController supportController) {

        this.supportController = supportController;
    }

    // =========================================================
    // MAIN SCENE
    // =========================================================

    public Scene getSupportPageScene() {

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + BG + ";");

        // =====================================================
        // SIDEBAR
        // =====================================================

        root.setLeft(
                UserNavigation.createSidebar("Support"));

        // =====================================================
        // PAGE CONTENT
        // =====================================================

        VBox pageContent = new VBox();

        pageContent.setFillWidth(true);

        pageContent.setStyle(
                "-fx-background-color: " + PAGE_BG + ";");

        // =====================================================
        // NAVBAR
        // =====================================================

        HBox navbar = UserNavigation.createNavbar();

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox main = new VBox(16);

        main.setPadding(
                new Insets(
                        18,
                        24,
                        35,
                        24));

        main.setFillWidth(true);

        main.setStyle(
                "-fx-background-color: " + PAGE_BG + ";");

        // =====================================================
        // HEADER
        // =====================================================

        VBox header = createPageHeader();

        // =====================================================
        // CREATE TICKET + RIGHT SIDE
        // =====================================================

        HBox content = new HBox(18);

        content.setFillHeight(true);

        VBox ticketCard = createTicketCard();

        VBox rightColumn = new VBox(12);

        VBox faqCard = createFAQCard();

        VBox contactCard = createContactCard();

        rightColumn.getChildren().addAll(
                faqCard,
                contactCard);

        HBox.setHgrow(
                ticketCard,
                Priority.ALWAYS);

        HBox.setHgrow(
                rightColumn,
                Priority.ALWAYS);

        content.getChildren().addAll(
                ticketCard,
                rightColumn);

        // =====================================================
        // LOAD REAL USER TICKETS
        // =====================================================

        loadUserTickets();

        // =====================================================
        // SUBMITTED TICKETS
        // =====================================================

        VBox submittedSection = createSubmittedTicketsSection();

        // =====================================================
        // INFORMATION
        // =====================================================

        HBox informationBar = createInformationBar();

        // =====================================================
        // SECURITY
        // =====================================================

        HBox securityBar = createSecurityBar();

        // =====================================================
        // ADD MAIN CONTENT
        // =====================================================

        main.getChildren().addAll(
                header,
                content,
                submittedSection,
                informationBar,
                securityBar);

        // =====================================================
        // PAGE CONTENT
        // =====================================================

        pageContent.getChildren().addAll(
                navbar,
                main);

        // =====================================================
        // FULL CENTER SCROLL PANE
        // =====================================================

        ScrollPane scrollPane = new ScrollPane(pageContent);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setPannable(true);

        scrollPane.setStyle(
                "-fx-background-color: " + PAGE_BG + ";" +
                        "-fx-background: " + PAGE_BG + ";" +
                        "-fx-border-color: transparent;");

        // =====================================================
        // ROOT CENTER
        // =====================================================

        root.setCenter(scrollPane);

        // =====================================================
        // SCENE
        // =====================================================

        supportScene = new Scene(
                root,
                1536,
                750);

        return supportScene;
    }

    // =========================================================
    // PAGE HEADER
    // =========================================================

    private VBox createPageHeader() {

        Label title = new Label(
                "Support Center");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        23));

        title.setTextFill(
                Color.web(TEXT));

        Label subtitle = new Label(
                "Need help with your shipments, vehicles, "
                        + "or trips? Our support team is ready to assist you.");

        subtitle.setFont(
                Font.font(
                        "Arial",
                        12));

        subtitle.setTextFill(
                Color.web(MUTED));

        return new VBox(
                3,
                title,
                subtitle);
    }

    // =========================================================
    // CREATE SUPPORT TICKET
    // =========================================================

    private VBox createTicketCard() {

        VBox card = new VBox(9);

        card.setPadding(
                new Insets(18));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 16;");

        // =====================================================
        // TITLE
        // =====================================================

        Label icon = new Label("▣");

        icon.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        19));

        icon.setTextFill(
                Color.web(GREEN));

        StackPane iconBox = new StackPane(icon);

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

        Label title = new Label(
                "Create Support Ticket");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16));

        title.setTextFill(
                Color.web(TEXT));

        HBox cardTitle = new HBox(
                10,
                iconBox,
                title);

        cardTitle.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // ISSUE TYPE
        // =====================================================

        Label issueLabel = createFieldLabel(
                "Issue Type");

        issueType = new ComboBox<>();

        issueType.getItems().addAll(
                "Technical Issue",
                "Shipment Issue",
                "Pickup Issue",
                "Delivery Issue",
                "Vehicle / Fleet Issue",
                "Payment & Settlement Issue",
                "Load Assignment Issue",
                "Document Issue",
                "Account Issue",
                "Other");

        issueType.setValue(
                "Technical Issue");

        issueType.setMaxWidth(
                Double.MAX_VALUE);

        issueType.setPrefHeight(
                38);

        styleControl(issueType);

        VBox issueBox = new VBox(
                4,
                issueLabel,
                issueType);

        // =====================================================
        // PRIORITY
        // =====================================================

        Label priorityLabel = createFieldLabel(
                "Priority");

        ToggleGroup priorityGroup = new ToggleGroup();

        low = createRadioButton(
                "Low",
                priorityGroup);

        medium = createRadioButton(
                "Medium",
                priorityGroup);

        high = createRadioButton(
                "High",
                priorityGroup);

        medium.setSelected(true);

        HBox priorityBox = new HBox(
                10,
                low,
                medium,
                high);

        priorityBox.setAlignment(
                Pos.CENTER_LEFT);

        VBox priorityContainer = new VBox(
                6,
                priorityLabel,
                priorityBox);

        HBox issuePriority = new HBox(
                16,
                issueBox,
                priorityContainer);

        HBox.setHgrow(
                issueBox,
                Priority.ALWAYS);

        HBox.setHgrow(
                priorityContainer,
                Priority.ALWAYS);

        // =====================================================
        // SUBJECT
        // =====================================================

        Label subjectLabel = createFieldLabel(
                "Subject");

        subjectField = new TextField();

        subjectField.setPromptText(
                "Brief summary of the issue");

        subjectField.setPrefHeight(
                38);

        styleControl(subjectField);

        VBox subjectBox = new VBox(
                4,
                subjectLabel,
                subjectField);

        // =====================================================
        // DESCRIPTION
        // =====================================================

        Label descriptionLabel = createFieldLabel(
                "Description");

        descriptionArea = new TextArea();

        descriptionArea.setPromptText(
                "Provide shipment ID, vehicle number, "
                        + "trip details, and as much information as possible...");

        descriptionArea.setWrapText(true);

        descriptionArea.setPrefHeight(
                75);

        descriptionArea.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-font-size: 13px;");

        VBox descriptionBox = new VBox(
                4,
                descriptionLabel,
                descriptionArea);

        // =====================================================
        // ATTACHMENT
        // =====================================================

        Label attachmentTitle = createFieldLabel(
                "Attachments");

        Button uploadButton = new Button();

        uploadButton.setMaxWidth(
                Double.MAX_VALUE);

        uploadButton.setPrefHeight(
                110);

        uploadButton.setMinHeight(
                110);

        uploadButton.setCursor(
                Cursor.HAND);

        attachmentLabel = new Label(
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

        uploadButton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #BFC8C1;" +
                        "-fx-border-style: dashed;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;");

        uploadButton.setOnAction(
                e -> chooseAttachment());

        VBox attachmentBox = new VBox(
                4,
                attachmentTitle,
                uploadButton);

        // =====================================================
        // RESET
        // =====================================================

        Button reset = new Button(
                "Reset");

        reset.setPrefWidth(
                95);

        reset.setPrefHeight(
                38);

        reset.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        reset.setCursor(
                Cursor.HAND);

        reset.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-border-color: #CDD5CF;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;");

        reset.setOnAction(
                e -> resetForm());

        // =====================================================
        // SUBMIT
        // =====================================================

        Button submit = new Button(
                "Submit Ticket");

        submit.setPrefHeight(
                38);

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

        submit.setStyle(
                "-fx-background-color: " +
                        GREEN +
                        ";" +
                        "-fx-background-radius: 9;");

        submit.setOnMouseEntered(
                e -> submit.setStyle(
                        "-fx-background-color: " +
                                DARK_GREEN +
                                ";" +
                                "-fx-background-radius: 9;"));

        submit.setOnMouseExited(
                e -> submit.setStyle(
                        "-fx-background-color: " +
                                GREEN +
                                ";" +
                                "-fx-background-radius: 9;"));

        submit.setOnAction(
                e -> submitTicket());

        HBox.setHgrow(
                submit,
                Priority.ALWAYS);

        HBox buttons = new HBox(
                12,
                reset,
                submit);

        // =====================================================
        // ADD CONTENT
        // =====================================================

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
    // SUBMITTED TICKETS SECTION
    // =========================================================

    private VBox createSubmittedTicketsSection() {

        VBox section = new VBox(12);

        section.setFillWidth(true);

        Label title = new Label(
                "My Recent Support Tickets");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        20));

        title.setTextFill(
                Color.web(TEXT));

        Label subtitle = new Label(
                "Click any ticket to view the actions taken by the support team.");

        subtitle.setFont(
                Font.font(
                        "Arial",
                        12));

        subtitle.setTextFill(
                Color.web(MUTED));

        VBox heading = new VBox(
                2,
                title,
                subtitle);

        ticketsContainer = new VBox(10);

        refreshTicketList();

        section.getChildren().addAll(
                heading,
                ticketsContainer);

        return section;
    }

    // =========================================================
    // REFRESH TICKET LIST
    // =========================================================

    private void refreshTicketList() {

        if (ticketsContainer == null) {
            return;
        }

        ticketsContainer.getChildren().clear();

        for (SupportTicket ticket : submittedTickets) {

            ticketsContainer.getChildren().add(
                    createTicketRow(ticket));
        }
    }

    // =========================================================
    // TICKET ROW
    // =========================================================

    private HBox createTicketRow(
            SupportTicket ticket) {

        HBox card = new HBox(16);

        card.setPadding(
                new Insets(16, 18, 16, 18));

        card.setAlignment(
                Pos.CENTER_LEFT);

        card.setMaxWidth(
                Double.MAX_VALUE);

        String normalStyle = "-fx-background-color: white;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-cursor: hand;";

        String hoverStyle = "-fx-background-color: #FBFFFC;" +
                "-fx-border-color: " + GREEN + ";" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-cursor: hand;";

        card.setStyle(normalStyle);

        // =====================================================
        // TICKET ICON
        // =====================================================

        Label ticketIcon = new Label("▣");

        ticketIcon.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        19));

        ticketIcon.setTextFill(
                Color.web(GREEN));

        StackPane iconBox = new StackPane(
                ticketIcon);

        iconBox.setPrefSize(
                42,
                42);

        iconBox.setMaxSize(
                42,
                42);

        iconBox.setStyle(
                "-fx-background-color: " +
                        LIGHT_GREEN +
                        ";" +
                        "-fx-background-radius: 11;");

        // =====================================================
        // MAIN DETAILS
        // =====================================================

        VBox details = new VBox(5);

        HBox topLine = new HBox(10);

        Label id = new Label(ticket.getId());

        id.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        id.setTextFill(
                Color.web(GREEN));

        Label issue = new Label(ticket.getIssueType());

        issue.setFont(
                Font.font(
                        "Arial",
                        11));

        issue.setTextFill(
                Color.web(MUTED));

        topLine.getChildren().addAll(
                id,
                issue);

        Label subject = new Label(
                ticket.getSubject());

        subject.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15));

        subject.setTextFill(
                Color.web(TEXT));

        Label dates = new Label(
                "Created: " +
                        ticket.getCreatedAt() +
                        "    •    Updated: " +
                        ticket.getUpdatedAt());

        dates.setFont(
                Font.font(
                        "Arial",
                        11));

        dates.setTextFill(
                Color.web(MUTED));

        details.getChildren().addAll(
                topLine,
                subject,
                dates);

        HBox.setHgrow(
                details,
                Priority.ALWAYS);

        // =====================================================
        // STATUS
        // =====================================================

        Label status = new Label(
                "● " + ticket.getStatus());

        status.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        status.setTextFill(
                getStatusColor(ticket.getStatus()));

        // =====================================================
        // ARROW
        // =====================================================

        Label arrow = new Label(
                "View Timeline  →");

        arrow.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        arrow.setTextFill(
                Color.web(GREEN));

        VBox right = new VBox(
                10,
                status,
                arrow);

        right.setAlignment(
                Pos.CENTER_RIGHT);

        card.getChildren().addAll(
                iconBox,
                details,
                right);

        // =====================================================
        // CLICK
        // =====================================================

        card.setOnMouseClicked(
                e -> showTicketTimeline(ticket));

        // =====================================================
        // HOVER
        // =====================================================

        card.setOnMouseEntered(
                e -> card.setStyle(hoverStyle));

        card.setOnMouseExited(
                e -> card.setStyle(normalStyle));

        return card;
    }

    // =========================================================
    // STATUS COLOR
    // =========================================================

    private Color getStatusColor(
            String status) {

        switch (status) {

            case "Resolved":
                return Color.web("#1976D2");

            case "Closed":
                return Color.web("#757575");

            case "In Progress":
                return Color.web("#D98C00");

            default:
                return Color.web(GREEN);
        }
    }

    // =========================================================
    // SHOW TIMELINE
    // =========================================================

    private void showTicketTimeline(
            SupportTicket ticket) {

        VBox timelinePage = createTimelinePage(ticket);

        if (ticketsContainer == null) {
            return;
        }

        VBox parent = (VBox) ticketsContainer.getParent();

        parent.getChildren().remove(
                ticketsContainer);

        parent.getChildren().add(
                timelinePage);
    }

    // =========================================================
    // TIMELINE PAGE
    // =========================================================

    private VBox createTimelinePage(
            SupportTicket ticket) {

        VBox container = new VBox(16);

        container.setPadding(
                new Insets(4, 0, 8, 0));

        // =====================================================
        // BACK BUTTON
        // =====================================================

        Button back = new Button(
                "←  Back to My Tickets");

        back.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13));

        back.setTextFill(
                Color.web(GREEN));

        back.setCursor(
                Cursor.HAND);

        back.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-padding: 4 0 4 0;");

        back.setOnAction(
                e -> showTicketListAgain(container));

        // =====================================================
        // TICKET HEADER
        // =====================================================

        VBox header = new VBox(7);

        Label ticketId = new Label(
                ticket.getId());

        ticketId.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13));

        ticketId.setTextFill(
                Color.web(GREEN));

        Label subject = new Label(
                ticket.getSubject());

        subject.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22));

        subject.setTextFill(
                Color.web(TEXT));

        HBox statusRow = new HBox(14);

        statusRow.setAlignment(
                Pos.CENTER_LEFT);

        Label status = new Label(
                "● " + ticket.getStatus());

        status.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        status.setTextFill(
                getStatusColor(ticket.getStatus()));

        Label priority = new Label(
                "Priority: " +
                        ticket.getPriority());

        priority.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        priority.setTextFill(
                Color.web(TEXT));

        Label issue = new Label(
                ticket.getIssueType());

        issue.setFont(
                Font.font(
                        "Arial",
                        12));

        issue.setTextFill(
                Color.web(MUTED));

        statusRow.getChildren().addAll(
                status,
                priority,
                issue);

        header.getChildren().addAll(
                ticketId,
                subject,
                statusRow);

        // =====================================================
        // DESCRIPTION CARD
        // =====================================================

        VBox descriptionCard = new VBox(7);

        descriptionCard.setPadding(
                new Insets(18));

        descriptionCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 14;");

        Label descriptionTitle = new Label(
                "Issue Description");

        descriptionTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14));

        descriptionTitle.setTextFill(
                Color.web(TEXT));

        Label description = new Label(
                ticket.getDescription());

        description.setWrapText(true);

        description.setFont(
                Font.font(
                        "Arial",
                        13));

        description.setTextFill(
                Color.web(MUTED));

        descriptionCard.getChildren().addAll(
                descriptionTitle,
                description);

        // =====================================================
        // TIMELINE CARD
        // =====================================================

        VBox timelineCard = new VBox(0);

        timelineCard.setPadding(
                new Insets(22));

        timelineCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 16;");

        Label timelineTitle = new Label(
                "Support Action Timeline");

        timelineTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        19));

        timelineTitle.setTextFill(
                Color.web(TEXT));

        timelineCard.getChildren().add(
                timelineTitle);

        for (int i = 0; i < ticket.getTimeline().size(); i++) {

            TimelineEvent event = ticket.getTimeline().get(i);

            boolean last = i == ticket.getTimeline().size() - 1;

            timelineCard.getChildren().add(
                    createTimelineEvent(
                            event,
                            last));
        }

        container.getChildren().addAll(
                back,
                header,
                descriptionCard,
                timelineCard);

        return container;
    }

    // =========================================================
    // TIMELINE EVENT
    // =========================================================

    private HBox createTimelineEvent(
            TimelineEvent event,
            boolean last) {

        HBox row = new HBox(15);

        row.setPadding(
                new Insets(
                        18,
                        8,
                        last ? 5 : 18,
                        8));

        // =====================================================
        // DOT + LINE
        // =====================================================

        VBox indicator = new VBox();

        indicator.setAlignment(
                Pos.TOP_CENTER);

        StackPane dot = new StackPane();

        dot.setPrefSize(
                22,
                22);

        dot.setMaxSize(
                22,
                22);

        Label dotLabel = new Label(
                event.isCompleted()
                        ? "✓"
                        : "○");

        dotLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        dotLabel.setTextFill(
                event.isCompleted()
                        ? Color.WHITE
                        : Color.web(MUTED));

        dot.setStyle(
                event.isCompleted()
                        ? "-fx-background-color: " +
                                GREEN +
                                ";" +
                                "-fx-background-radius: 50%;"
                        : "-fx-background-color: #EEF2EF;" +
                                "-fx-border-color: #B9C4BD;" +
                                "-fx-border-radius: 50%;");

        dot.getChildren().add(
                dotLabel);

        indicator.getChildren().add(dot);

        if (!last) {

            Region line = new Region();

            line.setPrefWidth(2);

            line.setMinWidth(2);

            line.setMaxWidth(2);

            line.setPrefHeight(65);

            line.setStyle(
                    event.isCompleted()
                            ? "-fx-background-color: #B9DEC7;"
                            : "-fx-background-color: #DDE4DF;");

            indicator.getChildren().add(
                    line);
        }

        // =====================================================
        // EVENT CONTENT
        // =====================================================

        VBox content = new VBox(5);

        Label title = new Label(
                event.getTitle());

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14));

        title.setTextFill(
                event.isCompleted()
                        ? Color.web(TEXT)
                        : Color.web(MUTED));

        Label description = new Label(
                event.getDescription());

        description.setWrapText(true);

        description.setFont(
                Font.font(
                        "Arial",
                        12));

        description.setTextFill(
                Color.web(MUTED));

        Label time = new Label(
                event.getTime());

        time.setFont(
                Font.font(
                        "Arial",
                        11));

        time.setTextFill(
                Color.web(GREEN));

        content.getChildren().addAll(
                title,
                description);

        if (!event.getTime().isEmpty()) {

            content.getChildren().add(
                    time);
        }

        HBox.setHgrow(
                content,
                Priority.ALWAYS);

        row.getChildren().addAll(
                indicator,
                content);

        return row;
    }

    // =========================================================
    // BACK TO TICKET LIST
    // =========================================================

    private void showTicketListAgain(
            VBox timelineContainer) {

        VBox parent = (VBox) timelineContainer.getParent();

        int index = parent.getChildren().indexOf(
                timelineContainer);

        parent.getChildren().remove(
                timelineContainer);

        ticketsContainer = new VBox(10);

        refreshTicketList();

        parent.getChildren().add(
                index,
                ticketsContainer);
    }

    // =========================================================
    // FAQ CARD
    // =========================================================

    private VBox createFAQCard() {

        VBox card = new VBox(12);

        card.setPadding(
                new Insets(16));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 16;");

        Label title = new Label(
                "Frequently Asked Questions");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16));

        title.setTextFill(
                Color.web(TEXT));

        VBox faq1 = createFAQ(
                "How do I accept a shipment?",
                "Go to the Available Loads or Shipments section "
                        + "from the sidebar. Review the shipment details, "
                        + "pickup location, delivery location, and payment "
                        + "information before accepting the shipment.");

        VBox faq2 = createFAQ(
                "How do I update shipment status?",
                "Open your active shipment and select the relevant "
                        + "status option such as Picked Up, In Transit, "
                        + "or Delivered.");

        VBox faq3 = createFAQ(
                "When will I receive my payment?",
                "Payments are generally processed after successful "
                        + "shipment completion and verification.");

        VBox faq4 = createFAQ(
                "What should I do if I have a vehicle problem?",
                "If your vehicle develops a problem during an active "
                        + "shipment, contact support immediately and "
                        + "provide your shipment ID and vehicle number.");

        card.getChildren().addAll(
                title,
                faq1,
                faq2,
                faq3,
                faq4);

        return card;
    }

    // =========================================================
    // FAQ ITEM
    // =========================================================

    private VBox createFAQ(
            String question,
            String answer) {

        VBox box = new VBox();

        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;");

        Button questionButton = new Button(
                question + "   ⌄");

        questionButton.setMaxWidth(
                Double.MAX_VALUE);

        questionButton.setAlignment(
                Pos.CENTER_LEFT);

        questionButton.setPrefHeight(
                36);

        questionButton.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        questionButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + TEXT + ";");

        Label answerLabel = new Label(answer);

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

        VBox answerBox = new VBox(
                answerLabel);

        box.getChildren().add(
                questionButton);

        questionButton.setOnAction(
                e -> {

                    if (box.getChildren()
                            .contains(answerBox)) {

                        box.getChildren()
                                .remove(answerBox);

                        questionButton.setText(
                                question + "   ⌄");

                    } else {

                        box.getChildren()
                                .add(answerBox);

                        questionButton.setText(
                                question + "   ⌃");
                    }
                });

        return box;
    }

    // =========================================================
    // CONTACT CARD
    // =========================================================

    private VBox createContactCard() {

        VBox card = new VBox(10);

        card.setPadding(
                new Insets(16));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 16;");

        Label title = new Label(
                "Other Ways To Contact");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16));

        title.setTextFill(
                Color.web(TEXT));

        Label phoneIcon = new Label("☎");

        phoneIcon.setFont(
                Font.font(
                        "Arial",
                        17));

        phoneIcon.setTextFill(
                Color.web(GREEN));

        StackPane phoneCircle = new StackPane(
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

        Label phoneTitle = new Label(
                "SUPPORT PHONE");

        phoneTitle.setFont(
                Font.font(
                        "Arial",
                        10));

        phoneTitle.setTextFill(
                Color.web(MUTED));

        Label phone = new Label(
                "1800-123-4567");

        phone.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14));

        phone.setTextFill(
                Color.web(TEXT));

        VBox phoneText = new VBox(
                2,
                phoneTitle,
                phone);

        HBox phoneRow = new HBox(
                12,
                phoneCircle,
                phoneText);

        phoneRow.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // EMAIL
        // =====================================================

        Label emailIcon = new Label("✉");

        emailIcon.setFont(
                Font.font(
                        "Arial",
                        17));

        emailIcon.setTextFill(
                Color.web(GREEN));

        StackPane emailCircle = new StackPane(
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

        Label emailTitle = new Label(
                "EMAIL ADDRESS");

        emailTitle.setFont(
                Font.font(
                        "Arial",
                        10));

        emailTitle.setTextFill(
                Color.web(MUTED));

        Label email = new Label(
                "support@ecoload.com");

        email.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13));

        email.setTextFill(
                Color.web(TEXT));

        VBox emailText = new VBox(
                2,
                emailTitle,
                email);

        HBox emailRow = new HBox(
                12,
                emailCircle,
                emailText);

        emailRow.setAlignment(
                Pos.CENTER_LEFT);

        Separator separator = new Separator();

        Button chat = new Button(
                "▣   Chat Bot");

        chat.setMaxWidth(
                Double.MAX_VALUE);

        chat.setPrefHeight(
                38);

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

        chat.setOnAction(
                e -> showAlert(
                        "ChatBot",
                        "ChatBot will be available soon."));

        Label response = new Label(
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

        Label icon = new Label("ⓘ");

        icon.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        20));

        icon.setTextFill(
                Color.web(GREEN));

        Label text = new Label(
                "Our support team usually replies within a few hours. "
                        + "Please have your Shipment ID, Vehicle Number, "
                        + "or Trip ID ready for faster resolution.");

        text.setWrapText(true);

        text.setFont(
                Font.font(
                        "Arial",
                        12));

        text.setTextFill(
                Color.web(TEXT));

        HBox bar = new HBox(
                12,
                icon,
                text);

        bar.setPadding(
                new Insets(
                        10,
                        14,
                        10,
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

        Label icon = new Label("⬟");

        icon.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18));

        icon.setTextFill(
                Color.web("#3267C7"));

        Label text = new Label(
                "Security Alert: Never share your password, OTP, "
                        + "banking PIN, or payment credentials with anyone, "
                        + "including EcoLoad support staff.");

        text.setWrapText(true);

        text.setFont(
                Font.font(
                        "Arial",
                        13));

        text.setTextFill(
                Color.web("#304D87"));

        HBox bar = new HBox(
                12,
                icon,
                text);

        bar.setPadding(
                new Insets(
                        10,
                        14,
                        10,
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
    // FIELD LABEL
    // =========================================================

    private Label createFieldLabel(
            String text) {

        Label label = new Label(text);

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

        RadioButton radio = new RadioButton(text);

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
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 14px;");
    }

    // =========================================================
    // ATTACHMENT
    // =========================================================

    private void chooseAttachment() {

        FileChooser chooser = new FileChooser();

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

        File file = chooser.showOpenDialog(
                null);

        if (file == null) {
            return;
        }

        long size = file.length();

        if (size > 5 * 1024 * 1024) {

            showAlert(
                    "File Too Large",
                    "Please select a file smaller than 5 MB.");

            return;
        }

        selectedAttachment = file.getAbsolutePath();

        attachmentLabel.setText(
                "✓  " + file.getName());
    }

    // =========================================================
    // LOAD USER TICKETS
    // =========================================================

    private void loadUserTickets() {

        try {

            String email = getCurrentUserEmail();

            if (email == null || email.isBlank()) {

                System.err.println(
                        "No logged-in Firebase user found.");

                return;
            }

            submittedTickets.clear();

            submittedTickets.addAll(
                    supportController.getUserTickets(email));

            System.out.println(
                    "Loaded " + submittedTickets.size()
                            + " support tickets for " + email);

        } catch (Exception e) {

            System.err.println(
                    "Failed to load user support tickets.");

            e.printStackTrace();
        }
    }

    private String getCurrentUserEmail() {

        UserModel user = CurrentUser.getInstance().getUser();

        if (user == null) {
            return null;
        }

        return user.getEmail();
    }

    // =========================================================
    // RESET FORM
    // =========================================================

    private void resetForm() {

        issueType.setValue(
                "Technical Issue");

        medium.setSelected(
                true);

        subjectField.clear();

        descriptionArea.clear();

        selectedAttachment = null;

        attachmentLabel.setText(
                "☁   Drag & Drop files here\n" +
                        "PDF, PNG, JPG (Max 5MB)");
    }

    // =========================================================
    // SUBMIT TICKET
    // =========================================================

    private void submitTicket() {

        if (subjectField.getText()
                .trim()
                .isEmpty()) {

            showAlert(
                    "Missing Subject",
                    "Please enter the subject of your issue.");

            return;
        }

        if (descriptionArea.getText()
                .trim()
                .isEmpty()) {

            showAlert(
                    "Missing Description",
                    "Please describe your issue.");

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

        try {

            String email = getCurrentUserEmail();

            if (email == null || email.isBlank()) {

                showAlert(
                        "Login Required",
                        "Please login again before submitting a support ticket.");

                return;
            }

            SupportTicket ticket = supportController.createTicket(
                    email,
                    issueType.getValue(),
                    priority,
                    subjectField.getText().trim(),
                    descriptionArea.getText().trim(),
                    selectedAttachment);

            submittedTickets.add(0, ticket);

            refreshTicketList();

            showAlert(
                    "Support Ticket Submitted",
                    "Your support ticket has been submitted successfully.\n\n"
                            + "Ticket ID: " + ticket.getId() + "\n"
                            + "Status: " + ticket.getStatus());

            resetForm();

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    "Submission Failed",
                    e.getMessage() != null
                            ? e.getMessage()
                            : "Unable to submit support ticket.");
        }
    }

    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert(
            String title,
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(
                title);

        alert.setHeaderText(
                null);

        alert.setContentText(
                message);

        alert.showAndWait();
    }
}