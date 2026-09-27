package com.super_x.view.Chatbot;

import com.super_x.ai.EcoloadChatService;
import com.super_x.view.HomePage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/** Persistent assistant state with a per-screen JavaFX overlay. */
public final class EcoloadAssistantOverlay {
    private static final ChatSession TRANSPORTER = new ChatSession("transporter");
    private static final ChatSession DRIVER = new ChatSession("driver");

    private EcoloadAssistantOverlay() {
    }

    public static void installForTransporter() {
        install(TRANSPORTER);
    }

    public static void installForDriver() {
        install(DRIVER);
    }

    private static void install(ChatSession session) {
        // Sidebar construction precedes Scene assignment. Deferring attaches to
        // the scene that has just become active without changing navigation.
        Platform.runLater(() -> {
            if (HomePage.homeStage != null && HomePage.homeStage.getScene() != null) {
                Scene scene = HomePage.homeStage.getScene();
                if (!(scene.getRoot() instanceof OverlayRoot)) {
                    scene.setRoot(new OverlayRoot(scene.getRoot(), session));
                }
            }
        });
    }

    private static final class OverlayRoot extends StackPane {
        private final VBox transcriptBox = new VBox(8);
        private final VBox panel = new VBox();
        private final TextField input = new TextField();
        private final Button send = new Button("Send");
        private final ProgressIndicator loading = new ProgressIndicator();
        private final ChatSession session;

        private OverlayRoot(Parent content, ChatSession session) {
            this.session = session;
            getChildren().add(content);
            setPickOnBounds(false);

            Button launcher = new Button("💬");
            launcher.setText("\uD83D\uDCAC");
            launcher.setAccessibleText("Open Ecoload Assistant");
            launcher.setPrefSize(58, 58);
            launcher.setMinSize(58, 58);
            launcher.setStyle("-fx-background-color:#014B3A;-fx-text-fill:white;-fx-font-size:24px;"
                    + "-fx-background-radius:29;-fx-cursor:hand;"
                    + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,.28),12,0,0,4);");
            launcher.setOnAction(event -> setPanelVisible(!panel.isVisible()));
            buildPanel();
            // A screen initially shows only the launcher. Keeping the panel
            // unmanaged as well prevents it from affecting the screen layout.
            setPanelVisible(false);
            getChildren().addAll(panel, launcher);
            StackPane.setAlignment(launcher, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(launcher, new Insets(0, 24, 24, 0));
            StackPane.setAlignment(panel, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(panel, new Insets(0, 24, 94, 0));
        }

        private void buildPanel() {
            panel.setPrefSize(390, 500);
            panel.setMaxSize(390, 500);
            panel.setStyle("-fx-background-color:white;-fx-background-radius:16;-fx-border-color:#DDE7E2;"
                    + "-fx-border-radius:16;-fx-effect:dropshadow(gaussian,rgba(0,0,0,.25),18,0,0,6);");
            Label title = new Label("Ecoload Assistant");
            title.setStyle("-fx-font-size:17px;-fx-font-weight:bold;-fx-text-fill:white;");
            Button close = new Button("×");
            close.setText("\u00D7");
            close.setAccessibleText("Close Ecoload Assistant");
            close.setStyle("-fx-background-color:transparent;-fx-text-fill:white;-fx-font-size:25px;-fx-cursor:hand;");
            close.setOnAction(event -> setPanelVisible(false));
            HBox header = new HBox(title, spacer(), close);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setPadding(new Insets(10, 14, 10, 16));
            header.setStyle("-fx-background-color:#014B3A;-fx-background-radius:16 16 0 0;");

            transcriptBox.setPadding(new Insets(14));
            rebuildTranscript();
            ScrollPane transcript = new ScrollPane(transcriptBox);
            transcript.setFitToWidth(true);
            transcript.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            transcript.setStyle("-fx-background:#F7FAF8;-fx-background-color:#F7FAF8;");
            VBox.setVgrow(transcript, Priority.ALWAYS);

            loading.setMaxSize(18, 18);
            loading.setVisible(false);
            loading.setManaged(false);
            input.setPromptText("Ask about Ecoload...");
            input.setStyle("-fx-background-radius:18;-fx-border-radius:18;-fx-border-color:#CBD8D1;");
            HBox.setHgrow(input, Priority.ALWAYS);
            send.setStyle("-fx-background-color:#014B3A;-fx-text-fill:white;-fx-font-weight:bold;"
                    + "-fx-background-radius:18;-fx-cursor:hand;");
            HBox composer = new HBox(8, input, loading, send);
            composer.setAlignment(Pos.CENTER);
            composer.setPadding(new Insets(12));
            composer.setStyle("-fx-border-color:#E5ECE8;-fx-border-width:1 0 0 0;");
            send.setOnAction(event -> sendMessage(transcript));
            input.setOnAction(event -> sendMessage(transcript));
            panel.getChildren().addAll(header, transcript, composer);
        }

        private void setPanelVisible(boolean visible) {
            panel.setVisible(visible);
            panel.setManaged(visible);
        }

        private void sendMessage(ScrollPane transcript) {
            String question = input.getText().trim();
            if (question.isEmpty() || session.waiting)
                return;
            session.waiting = true;
            session.messages.add(new Message(true, question));
            input.clear();
            rebuildTranscript();
            scrollBottom(transcript);
            send.setDisable(true);
            input.setDisable(true);
            loading.setManaged(true);
            loading.setVisible(true);
            session.service.sendMessage(question).whenComplete((answer, error) -> Platform.runLater(() -> {
                session.waiting = false;
                session.messages.add(new Message(false, error == null ? answer
                        : "Sorry, I couldn't reach the Ecoload Assistant. Please try again."));
                rebuildTranscript();
                scrollBottom(transcript);
                send.setDisable(false);
                input.setDisable(false);
                loading.setVisible(false);
                loading.setManaged(false);
            }));
        }

        private void rebuildTranscript() {
            transcriptBox.getChildren().clear();
            if (session.messages.isEmpty())
                session.messages.add(new Message(false,
                        "Hi! I can help you use the Ecoload application."));
            for (Message message : session.messages) {
                String displayText = message.user()
                        ? message.text()
                        : cleanResponse(message.text());

                Label bubble = new Label(displayText);

                bubble.setWrapText(true);
                bubble.setMaxWidth(285);

                bubble.setStyle("-fx-padding:9 12;-fx-font-size:13px;-fx-background-radius:14;"
                        + (message.user() ? "-fx-background-color:#014B3A;-fx-text-fill:white;"
                                : "-fx-background-color:#E7F2EB;-fx-text-fill:#1E3228;"));

                HBox row = new HBox(bubble);
                row.setAlignment(message.user() ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                transcriptBox.getChildren().add(row);
            }
        }
        private String cleanResponse(String text) {
            if (text == null) {
                return "";
            }

            return text
                    .replace("**", "")
                    .replace("__", "")
                    .replace("`", "");
        }

        private void scrollBottom(ScrollPane transcript) {
            Platform.runLater(() -> transcript.setVvalue(1));
        }

        private Region spacer() {
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            return region;
        }
    }

    private static final class ChatSession {
        private final EcoloadChatService service;
        private final List<Message> messages = new ArrayList<>();
        private boolean waiting;

        private ChatSession(String audience) {
            service = new EcoloadChatService(audience);
        }
    }

    private record Message(boolean user, String text) {
    }
}