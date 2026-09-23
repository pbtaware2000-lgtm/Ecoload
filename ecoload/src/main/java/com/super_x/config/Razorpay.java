package com.super_x.config;

import java.awt.Desktop;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONObject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.util.function.Consumer;

public class Razorpay {

    /** Amount comes from Razorpay's verified payment-link response, in paise. */
    public record VerifiedPayment(String paymentLinkId, String paymentId, long amountInPaise) { }

    private static final String KEY_ID = "key_id";
    private static final String KEY_SECRET = "Kay_secret";
    private final String loadId;
    private final Consumer<VerifiedPayment> verifiedPaymentHandler;
    private final Consumer<String> paymentLinkCreatedHandler;
    private String paymentLinkId;

    public Razorpay() {
        this(null, null, null);
    }

    /** Associates this payment link with exactly one existing load document. */
    public Razorpay(String loadId, Consumer<VerifiedPayment> verifiedPaymentHandler) {
        this(loadId, null, verifiedPaymentHandler);
    }

    /**
     * The link ID is persisted before the browser is opened, so a later
     * verified payment can only update the load for which that link was made.
     */
    public Razorpay(String loadId, Consumer<String> paymentLinkCreatedHandler,
                    Consumer<VerifiedPayment> verifiedPaymentHandler) {
        this.loadId = loadId;
        this.paymentLinkCreatedHandler = paymentLinkCreatedHandler;
        this.verifiedPaymentHandler = verifiedPaymentHandler;
    }

    public Parent getView() {

        Label titleLabel = new Label("Razorpay Payment");

        titleLabel.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        Label amountLabel = new Label("Enter Amount");

        TextField amountField = new TextField();

        amountField.setPromptText("Enter amount");

        amountField.setMaxWidth(320);

        amountField.setStyle(
                "-fx-padding: 12px;" +
                "-fx-font-size: 15px;"
        );

        Button payButton = new Button("Pay Now");
        Button verifyButton = new Button("I've completed payment – Verify");
        verifyButton.setDisable(true);

        payButton.setStyle(
                "-fx-background-color: #2563EB;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12px 40px;" +
                "-fx-background-radius: 8px;"
        );

        payButton.setOnAction(event -> {

            try {

                String amountText =
                        amountField.getText();

                if (amountText == null ||
                        amountText.trim().isEmpty()) {

                    showError(
                            "Please enter amount"
                    );

                    return;
                }

                double amount =
                        Double.parseDouble(
                                amountText
                        );

                if (amount <= 0) {

                    showError(
                            "Please enter valid amount"
                    );

                    return;
                }

                JSONObject paymentLink = createPaymentLink(amount);
                paymentLinkId = paymentLink.getString("id");
                if (paymentLinkCreatedHandler != null && loadId != null) {
                    paymentLinkCreatedHandler.accept(paymentLinkId);
                }
                String paymentUrl = paymentLink.getString("short_url");
                verifyButton.setDisable(false);

                Desktop.getDesktop().browse(
                        new URI(paymentUrl)
                );

            } catch (NumberFormatException e) {

                showError(
                        "Amount must be a number"
                );

            } catch (Exception e) {

                e.printStackTrace();

                showError(
                        "Unable to start payment"
                );
            }
        });

        verifyButton.setOnAction(event -> {
            if (paymentLinkId == null) return;
            try {
                JSONObject verifiedLink = fetchPaymentLink(paymentLinkId);
                if (!"paid".equalsIgnoreCase(verifiedLink.optString("status"))) {
                    showError("Payment is not completed yet. No load status was changed.");
                    return;
                }
                if (verifiedPaymentHandler != null && loadId != null) {
                    verifiedPaymentHandler.accept(new VerifiedPayment(
                            paymentLinkId,
                            verifiedLink.optString("payment_id", paymentLinkId),
                            verifiedLink.optLong("amount_paid",
                                    verifiedLink.optLong("amount", 0))));
                }
                verifyButton.setDisable(true);
            } catch (Exception e) {
                e.printStackTrace();
                showError("Unable to verify payment status");
            }
        });


        VBox root = new VBox(
                20,
                titleLabel,
                amountLabel,
                amountField,
                payButton,
                verifyButton
        );

        root.setAlignment(
                Pos.CENTER
        );

        root.setPadding(
                new Insets(40)
        );

        root.setStyle(
                "-fx-background-color: #F5F7FB;"
        );

        return root;
    }


    private JSONObject createPaymentLink(
            double amount) throws Exception {

        long amountInPaise =
                Math.round(
                        amount * 100
                );

        JSONObject requestBody =
                new JSONObject();

        requestBody.put(
                "amount",
                amountInPaise
        );

        requestBody.put(
                "currency",
                "INR"
        );

        requestBody.put(
                "description",
                "AI Startup Builder Payment"
        );
        if (loadId != null) {
            requestBody.put("reference_id", loadId + "-" + System.currentTimeMillis());
            requestBody.put("notes", new JSONObject().put("loadId", loadId));
        }


        String credentials =
                KEY_ID + ":" + KEY_SECRET;


        String encodedCredentials =
                Base64.getEncoder()
                        .encodeToString(
                                credentials.getBytes(
                                        StandardCharsets.UTF_8
                                )
                        );


        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        "https://api.razorpay.com/v1/payment_links"
                                )
                        )
                        .header(
                                "Authorization",
                                "Basic " + encodedCredentials
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(
                                        requestBody.toString()
                                )
                        )
                        .build();


        HttpClient client =
                HttpClient.newHttpClient();


        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        System.out.println(
                "Razorpay Response : "
                        + response.body()
        );


        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Razorpay Error : "
                            + response.body()
            );
        }


        JSONObject responseJson =
                new JSONObject(
                        response.body()
                );


        return responseJson;
    }

    /** One on-demand Razorpay read; browser closure is never treated as success. */
    private JSONObject fetchPaymentLink(String linkId) throws Exception {
        String credentials = KEY_ID + ":" + KEY_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(
                credentials.getBytes(StandardCharsets.UTF_8));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.razorpay.com/v1/payment_links/" + linkId))
                .header("Authorization", "Basic " + encodedCredentials)
                .GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(
                request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("Razorpay verification failed: " + response.body());
        }
        return new JSONObject(response.body());
    }


    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Payment Error"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


	
}
