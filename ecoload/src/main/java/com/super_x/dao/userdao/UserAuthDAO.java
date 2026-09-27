package com.super_x.dao.userdao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.super_x.config.ApiKeyConfig;

public class UserAuthDAO {

    // =========================================================
    // FIREBASE WEB API KEY
    // =========================================================

    private static final String API_KEY =
            ApiKeyConfig.required("FIREBASE_WEB_API_KEY");


    // =========================================================
    // FIREBASE AUTH URLS
    // =========================================================

    private static final String SIGN_UP_URL =
            "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="
                    + API_KEY;

    private static final String LOGIN_URL =
            "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                    + API_KEY;


    // =========================================================
    // HTTP CLIENT + GSON
    // =========================================================

    private final HttpClient httpClient;
    private final Gson gson;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public UserAuthDAO() {

        httpClient = HttpClient.newHttpClient();

        gson = new Gson();
    }


    // =========================================================
    // CREATE FIREBASE AUTH ACCOUNT
    // =========================================================

    public boolean createAccount(
            String email,
            String password) {

        if (email == null ||
                email.trim().isEmpty()) {

            System.out.println(
                    "Email cannot be empty.");

            return false;
        }

        if (password == null ||
                password.isEmpty()) {

            System.out.println(
                    "Password cannot be empty.");

            return false;
        }

        try {

            // -------------------------------------------------
            // REQUEST BODY
            // -------------------------------------------------

            JsonObject requestBody =
                    new JsonObject();

            requestBody.addProperty(
                    "email",
                    email.trim());

            requestBody.addProperty(
                    "password",
                    password);

            requestBody.addProperty(
                    "returnSecureToken",
                    true);


            // -------------------------------------------------
            // HTTP REQUEST
            // -------------------------------------------------

            HttpRequest request =
                    HttpRequest.newBuilder()

                            .uri(
                                    URI.create(
                                            SIGN_UP_URL))

                            .header(
                                    "Content-Type",
                                    "application/json")

                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    gson.toJson(
                                                            requestBody)))

                            .build();


            // -------------------------------------------------
            // SEND REQUEST
            // -------------------------------------------------

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers
                                    .ofString());


            System.out.println(
                    "Firebase Auth Response Code: "
                            + response.statusCode());


            // -------------------------------------------------
            // SUCCESS
            // -------------------------------------------------

            if (response.statusCode() == 200) {

                System.out.println(
                        "Firebase Authentication account created.");

                return true;
            }


            // -------------------------------------------------
            // FAILURE
            // -------------------------------------------------

            System.out.println(
                    "Firebase Authentication failed.");

            System.out.println(
                    response.body());

            return false;


        } catch (Exception e) {

            System.out.println(
                    "Firebase Authentication error.");

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // LOGIN USER
    // =========================================================

    public boolean login(
            String email,
            String password) {

        if (email == null ||
                email.trim().isEmpty()) {

            System.out.println(
                    "Email cannot be empty.");

            return false;
        }

        if (password == null ||
                password.isEmpty()) {

            System.out.println(
                    "Password cannot be empty.");

            return false;
        }

        try {

            // -------------------------------------------------
            // REQUEST BODY
            // -------------------------------------------------

            JsonObject requestBody =
                    new JsonObject();

            requestBody.addProperty(
                    "email",
                    email.trim());

            requestBody.addProperty(
                    "password",
                    password);

            requestBody.addProperty(
                    "returnSecureToken",
                    true);


            // -------------------------------------------------
            // HTTP REQUEST
            // -------------------------------------------------

            HttpRequest request =
                    HttpRequest.newBuilder()

                            .uri(
                                    URI.create(
                                            LOGIN_URL))

                            .header(
                                    "Content-Type",
                                    "application/json")

                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    gson.toJson(
                                                            requestBody)))

                            .build();


            // -------------------------------------------------
            // SEND REQUEST
            // -------------------------------------------------

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers
                                    .ofString());


            System.out.println(
                    "Firebase Login Response Code: "
                            + response.statusCode());


            // -------------------------------------------------
            // LOGIN SUCCESS
            // -------------------------------------------------

            if (response.statusCode() == 200) {

                System.out.println(
                        "Firebase Authentication login successful.");

                System.out.println(
                        "Email: " + email);

                return true;
            }


            // -------------------------------------------------
            // LOGIN FAILURE
            // -------------------------------------------------

            System.out.println(
                    "Firebase Authentication login failed.");

            System.out.println(
                    response.body());

            return false;


        } catch (Exception e) {

            System.out.println(
                    "Firebase Authentication login error.");

            e.printStackTrace();

            return false;
        }
    }
}