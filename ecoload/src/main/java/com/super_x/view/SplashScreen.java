package com.super_x.view;

import javafx.animation.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SplashScreen {

    private final Stage stage;
    //private MediaPlayer mediaPlayer;

    // =========================================================
    // ECOLOAD COLOR SYSTEM
    // =========================================================

    private static final Color GREEN = Color.web("#0B6B2A");

    private static final Color SOFT_GREEN = Color.web("#66b674");

    private static final Color LIGHT_MINT = Color.web("#a1cea8");

    private static final Color TEAL = Color.web("#1cb1c4");

    private final List<Animation> animations = new ArrayList<>();

    private final Random random = new Random();

    public SplashScreen(Stage stage) {
        this.stage = stage;
    }

    // =========================================================
    // SHOW
    // =========================================================

//     private void playSplashMusic() {
//     try {

//         String musicPath = getClass()
//                 .getResource("/assets/audio/splashmusic.mp3")
//                 .toExternalForm();

//         Media media = new Media(musicPath);

//         mediaPlayer = new MediaPlayer(media);

//         mediaPlayer.setVolume(0.7);

//         mediaPlayer.play();

//         System.out.println("Splash music started successfully.");

//     } catch (Exception e) {
//         System.out.println("Splash music error: " + e.getMessage());
//         e.printStackTrace();
//     }
// }
    public void show(Runnable nextPage) {
        //playSplashMusic();

        // =====================================================
        // ROOT
        // =====================================================

        StackPane root = new StackPane();

        // =====================================================
        // BACKGROUND
        // =====================================================

        LinearGradient backgroundGradient = new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,

                new Stop(
                        0,
                        Color.web("#b1d3b9")),

                new Stop(
                        0.45,
                        Color.web("#a1ceda")),

                new Stop(
                        1,
                        Color.web("#3e9481")));

        root.setBackground(
                new Background(
                        new BackgroundFill(
                                backgroundGradient,
                                CornerRadii.EMPTY,
                                null)));

        // =====================================================
        // LARGE CENTER ATMOSPHERE
        // =====================================================

        Circle atmosphere = new Circle(420);

        atmosphere.setFill(
                radialGradient(
                        Color.rgb(
                                11,
                                107,
                                42,
                                0.065),
                        Color.rgb(
                                11,
                                107,
                                42,
                                0.025)));

        atmosphere.setMouseTransparent(true);

        root.getChildren().add(
                atmosphere);

        // =====================================================
        // TEAL ATMOSPHERE
        // =====================================================

        Circle tealAtmosphere = new Circle(280);

        tealAtmosphere.setFill(
                radialGradient(
                        Color.rgb(
                                47,
                                184,
                                201,
                                0.045),
                        Color.TRANSPARENT));

        tealAtmosphere.setTranslateX(
                420);

        tealAtmosphere.setTranslateY(
                180);

        root.getChildren().add(
                tealAtmosphere);

        // =====================================================
        // AMBIENT BACKGROUND MOVEMENT
        // =====================================================

        TranslateTransition atmosphereMove = new TranslateTransition(
                Duration.seconds(8),
                tealAtmosphere);

        atmosphereMove.setFromX(420);
        atmosphereMove.setFromY(180);

        atmosphereMove.setToX(-300);
        atmosphereMove.setToY(-170);

        atmosphereMove.setAutoReverse(true);

        atmosphereMove.setCycleCount(
                Animation.INDEFINITE);

        atmosphereMove.setInterpolator(
                Interpolator.EASE_BOTH);

        atmosphereMove.play();

        animations.add(
                atmosphereMove);

        // =====================================================
        // AMBIENT PARTICLES
        // =====================================================

        createAmbientParticles(root);

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox content = new VBox(14);

        content.setAlignment(
                Pos.CENTER);

        // =====================================================
        // LOGO AREA
        // =====================================================

        StackPane logoArea = new StackPane();

        logoArea.setPrefSize(
                390,
                390);

        logoArea.setMinSize(
                390,
                390);

        logoArea.setMaxSize(
                390,
                390);

        // =====================================================
        // OUTER FAINT GLOW
        // =====================================================

        Circle outerGlow = createGlow(
                235,
                0.045,
                0.018);

        // =====================================================
        // MIDDLE GLOW
        // =====================================================

        Circle middleGlow = createGlow(
                195,
                0.09,
                0.035);

        // =====================================================
        // INNER GLOW
        // =====================================================

        Circle innerGlow = createGlow(
                150,
                0.16,
                0.065);

        // =====================================================
        // ADD GLOWS
        // =====================================================

        logoArea.getChildren().addAll(
                outerGlow,
                middleGlow,
                innerGlow);

        // =====================================================
        // LARGE ENERGY WAVE
        // =====================================================

        Circle energyWave1 = createEnergyWave(
                145);

        Circle energyWave2 = createEnergyWave(
                145);

        logoArea.getChildren().addAll(
                energyWave1,
                energyWave2);

        // =====================================================
        // ROTATING ARC 1
        // =====================================================

        Arc arc1 = createArc(
                168,
                GREEN,
                0.35);

        arc1.setStartAngle(
                15);

        arc1.setLength(
                105);

        // =====================================================
        // ROTATING ARC 2
        // =====================================================

        Arc arc2 = createArc(
                178,
                TEAL,
                0.22);

        arc2.setStartAngle(
                190);

        arc2.setLength(
                70);

        // =====================================================
        // ROTATING ARC 3
        // =====================================================

        Arc arc3 = createArc(
                188,
                GREEN,
                0.15);

        arc3.setStartAngle(
                300);

        arc3.setLength(
                45);

        logoArea.getChildren().addAll(
                arc1,
                arc2,
                arc3);

        // =====================================================
        // LOGO IMAGE
        // =====================================================

        Image image = null;

        try {

            InputStream inputStream = getClass().getResourceAsStream(
                    "/assets/icons/EcoloadLogo.png");

            if (inputStream != null) {

                image = new Image(
                        inputStream);

            } else {

                System.out.println(
                        "Logo not found: " +
                                "/assets/icons/EcoloadLogo.png");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        // IMPORTANT:
        // One ImageView only.
        final ImageView logoView = new ImageView(image);

        // =====================================================
        // LOGO SIZE
        // =====================================================

        logoView.setFitWidth(
                300);

        logoView.setFitHeight(
                300);

        logoView.setPreserveRatio(
                true);

        logoView.setSmooth(
                true);

        // =====================================================
        // LOGO INITIAL STATE
        // =====================================================

        logoView.setOpacity(
                0);

        logoView.setScaleX(
                0.65);

        logoView.setScaleY(
                0.65);

        // =====================================================
        // LOGO SHADOW
        // =====================================================

        DropShadow logoShadow = new DropShadow();

        logoShadow.setRadius(
                12);

        logoShadow.setSpread(
                0.01);

        logoShadow.setColor(
                Color.rgb(
                        11,
                        107,
                        42,
                        0.12));

        logoView.setEffect(
                logoShadow);

        logoArea.getChildren().add(
                logoView);

        // =====================================================
        // ORBIT PARTICLES
        // =====================================================

        Circle orbitGreen = createOrbitParticle(
                GREEN,
                4);

        Circle orbitTeal = createOrbitParticle(
                TEAL,
                3);

        Circle orbitSoft = createOrbitParticle(
                SOFT_GREEN,
                3.5);

        logoArea.getChildren().addAll(
                orbitGreen,
                orbitTeal,
                orbitSoft);

        animateOrbit(
                orbitGreen,
                170,
                5.5,
                0);

        animateOrbit(
                orbitTeal,
                178,
                6.5,
                1.4);

        animateOrbit(
                orbitSoft,
                188,
                7.5,
                2.5);

        // =====================================================
        // LOGO LIGHT SWEEP
        // =====================================================

        Rectangle sweep = new Rectangle(
                65,
                285);

        sweep.setRotate(
                25);

        sweep.setOpacity(
                0);

        sweep.setFill(
                new LinearGradient(
                        0,
                        0,
                        1,
                        0,
                        true,
                        CycleMethod.NO_CYCLE,

                        new Stop(
                                0,
                                Color.TRANSPARENT),

                        new Stop(
                                0.48,
                                Color.rgb(
                                        255,
                                        255,
                                        255,
                                        0.35)),

                        new Stop(
                                1,
                                Color.TRANSPARENT)));

        logoArea.getChildren().add(
                sweep);

        // =====================================================
        // TAGLINE
        // =====================================================

        Label tagline = new Label(
                "SMART • SUSTAINABLE • CONNECTED");

        tagline.setStyle(
                "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-letter-spacing: 2.5px;" +
                        "-fx-text-fill: #0B6B2A;");

        tagline.setOpacity(
                0);

        tagline.setTranslateY(
                12);

        // =====================================================
        // LOADING AREA
        // =====================================================

        StackPane loading = new StackPane();

        loading.setPrefSize(
                190,
                5);

        loading.setMaxSize(
                190,
                5);

        // =====================================================
        // TRACK
        // =====================================================

        Rectangle track = new Rectangle(
                180,
                2.5);

        track.setArcWidth(
                8);

        track.setArcHeight(
                8);

        track.setFill(
                Color.rgb(
                        11,
                        107,
                        42,
                        0.10));

        // =====================================================
        // BEAD
        // =====================================================

        Rectangle bead = new Rectangle(
                45,
                5);

        bead.setArcWidth(
                10);

        bead.setArcHeight(
                10);

        bead.setFill(
                new LinearGradient(
                        0,
                        0,
                        1,
                        0,
                        true,
                        CycleMethod.NO_CYCLE,

                        new Stop(
                                0,
                                Color.TRANSPARENT),

                        new Stop(
                                0.25,
                                GREEN),

                        new Stop(
                                0.65,
                                SOFT_GREEN),

                        new Stop(
                                1,
                                Color.TRANSPARENT)));

        bead.setTranslateX(
                -105);

        loading.getChildren().addAll(
                track,
                bead);

        loading.setOpacity(
                0);

        // =====================================================
        // ADD CONTENT
        // =====================================================

        content.getChildren().addAll(
                logoArea,
                tagline,
                loading);

        root.getChildren().add(
                content);

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene = new Scene(
                root,
                1536,
                750);

        scene.setFill(
                LIGHT_MINT);

        stage.setScene(
                scene);

        stage.setTitle(
                "EcoLoad");

        stage.centerOnScreen();

        // =====================================================
        // LOGO FADE
        // =====================================================

        FadeTransition logoFade = new FadeTransition(
                Duration.seconds(1.15),
                logoView);

        logoFade.setFromValue(
                0);

        logoFade.setToValue(
                1);

        // =====================================================
        // LOGO SCALE
        // =====================================================

        ScaleTransition logoScale = new ScaleTransition(
                Duration.seconds(1.25),
                logoView);

        logoScale.setFromX(
                0.65);

        logoScale.setFromY(
                0.65);

        logoScale.setToX(
                1);

        logoScale.setToY(
                1);

        logoScale.setInterpolator(
                Interpolator.EASE_OUT);

        // =====================================================
        // LOGO ROTATION
        // =====================================================

        RotateTransition logoRotate = new RotateTransition(
                Duration.seconds(1.3),
                logoView);

        logoRotate.setFromAngle(
                -5);

        logoRotate.setToAngle(
                0);

        logoRotate.setInterpolator(
                Interpolator.EASE_OUT);

        // =====================================================
        // MAIN LOGO INTRO
        // =====================================================

        ParallelTransition logoIntro = new ParallelTransition(
                logoFade,
                logoScale,
                logoRotate);

        logoIntro.play();

        // =====================================================
        // LOGO BOUNCE
        // =====================================================

        logoIntro.setOnFinished(e -> {

            ScaleTransition bounce = new ScaleTransition(
                    Duration.seconds(0.18),
                    logoView);

            bounce.setFromX(
                    1);

            bounce.setFromY(
                    1);

            bounce.setToX(
                    1.035);

            bounce.setToY(
                    1.035);

            bounce.setAutoReverse(
                    true);

            bounce.setCycleCount(
                    2);

            bounce.play();

            // =================================================
            // CONTINUOUS BREATHING
            // =================================================

            bounce.setOnFinished(event -> {

                ScaleTransition breathe = new ScaleTransition(
                        Duration.seconds(2.8),
                        logoView);

                breathe.setFromX(
                        1);

                breathe.setFromY(
                        1);

                breathe.setToX(
                        1.018);

                breathe.setToY(
                        1.018);

                breathe.setAutoReverse(
                        true);

                breathe.setCycleCount(
                        Animation.INDEFINITE);

                breathe.setInterpolator(
                        Interpolator.EASE_BOTH);

                breathe.play();

                animations.add(
                        breathe);
            });
        });

        // =====================================================
        // GLOW BUILD-UP
        // =====================================================

        animateGlow(
                outerGlow,
                0.15);

        animateGlow(
                middleGlow,
                0.45);

        animateGlow(
                innerGlow,
                0.75);

        // =====================================================
        // GLOW BREATHING
        // =====================================================

        Timeline glowPulse = new Timeline(

                new KeyFrame(
                        Duration.seconds(0),

                        new KeyValue(
                                innerGlow.opacityProperty(),
                                0.75),

                        new KeyValue(
                                middleGlow.opacityProperty(),
                                0.55),

                        new KeyValue(
                                outerGlow.opacityProperty(),
                                0.35)),

                new KeyFrame(
                        Duration.seconds(1.8),

                        new KeyValue(
                                innerGlow.opacityProperty(),
                                1),

                        new KeyValue(
                                middleGlow.opacityProperty(),
                                0.82),

                        new KeyValue(
                                outerGlow.opacityProperty(),
                                0.60)),

                new KeyFrame(
                        Duration.seconds(3.6),

                        new KeyValue(
                                innerGlow.opacityProperty(),
                                0.75),

                        new KeyValue(
                                middleGlow.opacityProperty(),
                                0.55),

                        new KeyValue(
                                outerGlow.opacityProperty(),
                                0.35)));

        glowPulse.setDelay(
                Duration.seconds(2.5));

        glowPulse.setCycleCount(
                Animation.INDEFINITE);

        glowPulse.play();

        animations.add(
                glowPulse);

        // =====================================================
        // ENERGY WAVE 1
        // =====================================================

        animateEnergyWave(
                energyWave1,
                0.7);

        // =====================================================
        // ENERGY WAVE 2
        // =====================================================

        animateEnergyWave(
                energyWave2,
                2.0);

        // =====================================================
        // ROTATING ARCS
        // =====================================================

        rotateArc(
                arc1,
                9,
                360);

        rotateArc(
                arc2,
                13,
                -360);

        rotateArc(
                arc3,
                17,
                360);

        // =====================================================
        // LIGHT SWEEP
        // =====================================================

        FadeTransition sweepFade = new FadeTransition(
                Duration.seconds(0.35),
                sweep);

        sweepFade.setFromValue(
                0);

        sweepFade.setToValue(
                0.8);

        sweepFade.setDelay(
                Duration.seconds(1.15));

        TranslateTransition sweepMove = new TranslateTransition(
                Duration.seconds(1.1),
                sweep);

        sweepMove.setFromX(
                -240);

        sweepMove.setToX(
                240);

        sweepMove.setDelay(
                Duration.seconds(1.15));

        ParallelTransition sweepAnimation = new ParallelTransition(
                sweepFade,
                sweepMove);

        sweepAnimation.setOnFinished(
                e -> sweep.setOpacity(0));

        sweepAnimation.play();

        // =====================================================
        // TAGLINE CHARACTER ANIMATION
        // =====================================================

        animateTagline(
                tagline);

        // =====================================================
        // LOADING FADE
        // =====================================================

        FadeTransition loadingFade = new FadeTransition(
                Duration.seconds(0.5),
                loading);

        loadingFade.setFromValue(
                0);

        loadingFade.setToValue(
                1);

        loadingFade.setDelay(
                Duration.seconds(2.5));

        loadingFade.play();

        // =====================================================
        // LOADING BEAD
        // =====================================================

        TranslateTransition beadAnimation = new TranslateTransition(
                Duration.seconds(1.35),
                bead);

        beadAnimation.setFromX(
                -105);

        beadAnimation.setToX(
                105);

        beadAnimation.setInterpolator(
                Interpolator.EASE_BOTH);

        beadAnimation.setDelay(
                Duration.seconds(2.5));

        beadAnimation.setCycleCount(
                Animation.INDEFINITE);

        beadAnimation.play();

        animations.add(
                beadAnimation);

        // =====================================================
        // LOADING PULSE
        // =====================================================

        Timeline loadingPulse = new Timeline(

                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(
                                track.opacityProperty(),
                                0.35)),

                new KeyFrame(
                        Duration.seconds(0.8),
                        new KeyValue(
                                track.opacityProperty(),
                                0.75)),

                new KeyFrame(
                        Duration.seconds(1.6),
                        new KeyValue(
                                track.opacityProperty(),
                                0.35)));

        loadingPulse.setDelay(
                Duration.seconds(2.5));

        loadingPulse.setCycleCount(
                Animation.INDEFINITE);

        loadingPulse.play();

        animations.add(
                loadingPulse);

        // =====================================================
        // FINAL EXIT
        // =====================================================

        PauseTransition finish = new PauseTransition(
                Duration.seconds(6.8));

        finish.setOnFinished(
                e -> {

                    // Stop running animations
                    for (Animation animation : animations) {
                        animation.stop();
                    }
                    // Stop splash music
                //     if (mediaPlayer != null) {
                //     mediaPlayer.stop();
                //     mediaPlayer.dispose();
                // }

                    // =========================================
                    // FINAL LOGO ZOOM
                    // =========================================

                    ScaleTransition finalZoom = new ScaleTransition(
                            Duration.seconds(0.7),
                            logoView);

                    finalZoom.setFromX(
                            logoView.getScaleX());

                    finalZoom.setFromY(
                            logoView.getScaleY());

                    finalZoom.setToX(
                            1.08);

                    finalZoom.setToY(
                            1.08);

                    finalZoom.setInterpolator(
                            Interpolator.EASE_IN);

                    // =========================================
                    // ROOT FADE
                    // =========================================

                    FadeTransition fadeOut = new FadeTransition(
                            Duration.seconds(0.8),
                            root);

                    fadeOut.setFromValue(
                            1);

                    fadeOut.setToValue(
                            0);

                    fadeOut.setInterpolator(
                            Interpolator.EASE_IN);

                    ParallelTransition exit = new ParallelTransition(
                            finalZoom,
                            fadeOut);

                    exit.setOnFinished(
                            event -> {

                                root.setOpacity(
                                        1);

                                if (nextPage != null) {
                                    nextPage.run();
                                }
                            });

                    exit.play();
                });

        finish.play();

        // =====================================================
        // SHOW
        // =====================================================

        stage.show();
    }

    // =========================================================
    // RADIAL GRADIENT
    // =========================================================

    private RadialGradient radialGradient(
            Color center,
            Color outer) {

        return new RadialGradient(
                0,
                0,
                0.5,
                0.5,
                0.5,
                true,
                CycleMethod.NO_CYCLE,

                new Stop(
                        0,
                        center),

                new Stop(
                        0.45,
                        outer),

                new Stop(
                        1,
                        Color.TRANSPARENT));
    }

    // =========================================================
    // CREATE LOGO GLOW
    // =========================================================

    private Circle createGlow(
            double radius,
            double centerOpacity,
            double middleOpacity) {

        Circle glow = new Circle(radius);

        glow.setFill(
                new RadialGradient(
                        0,
                        0,
                        0.5,
                        0.5,
                        0.5,
                        true,
                        CycleMethod.NO_CYCLE,

                        new Stop(
                                0,
                                Color.rgb(
                                        11,
                                        107,
                                        42,
                                        centerOpacity)),

                        new Stop(
                                0.25,
                                Color.rgb(
                                        11,
                                        107,
                                        42,
                                        middleOpacity)),

                        new Stop(
                                0.55,
                                Color.rgb(
                                        11,
                                        107,
                                        42,
                                        middleOpacity * 0.35)),

                        new Stop(
                                0.78,
                                Color.rgb(
                                        11,
                                        107,
                                        42,
                                        middleOpacity * 0.10)),

                        new Stop(
                                1,
                                Color.TRANSPARENT)));

        glow.setOpacity(
                0);

        glow.setMouseTransparent(
                true);

        return glow;
    }

    // =========================================================
    // GLOW ANIMATION
    // =========================================================

    private void animateGlow(
            Circle glow,
            double delay) {

        FadeTransition fade = new FadeTransition(
                Duration.seconds(1.8),
                glow);

        fade.setFromValue(
                0);

        fade.setToValue(
                1);

        fade.setDelay(
                Duration.seconds(delay));

        fade.setInterpolator(
                Interpolator.EASE_OUT);

        ScaleTransition scale = new ScaleTransition(
                Duration.seconds(1.8),
                glow);

        scale.setFromX(
                0.65);

        scale.setFromY(
                0.65);

        scale.setToX(
                1);

        scale.setToY(
                1);

        scale.setDelay(
                Duration.seconds(delay));

        scale.setInterpolator(
                Interpolator.EASE_OUT);

        ParallelTransition animation = new ParallelTransition(
                fade,
                scale);

        animation.play();
    }

    // =========================================================
    // ENERGY WAVE
    // =========================================================

    private Circle createEnergyWave(
            double radius) {

        Circle wave = new Circle(radius);

        wave.setFill(
                Color.TRANSPARENT);

        wave.setStroke(
                Color.rgb(
                        11,
                        107,
                        42,
                        0.18));

        wave.setStrokeWidth(
                1.2);

        wave.setOpacity(
                0);

        wave.setScaleX(
                0.65);

        wave.setScaleY(
                0.65);

        return wave;
    }

    // =========================================================
    // ENERGY WAVE ANIMATION
    // =========================================================

    private void animateEnergyWave(
            Circle wave,
            double delay) {

        ScaleTransition scale = new ScaleTransition(
                Duration.seconds(2.8),
                wave);

        scale.setFromX(
                0.65);

        scale.setFromY(
                0.65);

        scale.setToX(
                1.35);

        scale.setToY(
                1.35);

        FadeTransition fade = new FadeTransition(
                Duration.seconds(2.8),
                wave);

        fade.setFromValue(
                0.30);

        fade.setToValue(
                0);

        ParallelTransition animation = new ParallelTransition(
                scale,
                fade);

        animation.setDelay(
                Duration.seconds(delay));

        animation.setCycleCount(
                Animation.INDEFINITE);

        animation.play();

        animations.add(
                animation);
    }

    // =========================================================
    // ARC
    // =========================================================

    private Arc createArc(
            double radius,
            Color color,
            double opacity) {

        Arc arc = new Arc(
                0,
                0,
                radius,
                radius,
                0,
                60);

        arc.setType(
                ArcType.OPEN);

        arc.setFill(
                Color.TRANSPARENT);

        arc.setStroke(
                color);

        arc.setStrokeWidth(
                2);

        arc.setOpacity(
                opacity);

        return arc;
    }

    // =========================================================
    // ARC ROTATION
    // =========================================================

    private void rotateArc(
            Arc arc,
            double seconds,
            double angle) {

        RotateTransition rotate = new RotateTransition(
                Duration.seconds(seconds),
                arc);

        rotate.setFromAngle(
                0);

        rotate.setToAngle(
                angle);

        rotate.setCycleCount(
                Animation.INDEFINITE);

        rotate.setInterpolator(
                Interpolator.LINEAR);

        rotate.play();

        animations.add(
                rotate);
    }

    // =========================================================
    // ORBIT PARTICLE
    // =========================================================

    private Circle createOrbitParticle(
            Color color,
            double radius) {

        Circle particle = new Circle(radius);

        particle.setFill(
                color);

        DropShadow shadow = new DropShadow();

        shadow.setRadius(
                8);

        shadow.setColor(
                Color.rgb(
                        11,
                        107,
                        42,
                        0.22));

        particle.setEffect(
                shadow);

        return particle;
    }

    // =========================================================
    // ORBIT ANIMATION
    // =========================================================

    private void animateOrbit(
            Circle particle,
            double radius,
            double seconds,
            double delay) {

        Circle orbit = new Circle(
                radius);

        orbit.setFill(
                Color.TRANSPARENT);

        PathTransition transition = new PathTransition();

        transition.setPath(
                orbit);

        transition.setNode(
                particle);

        transition.setDuration(
                Duration.seconds(seconds));

        transition.setDelay(
                Duration.seconds(delay));

        transition.setCycleCount(
                Animation.INDEFINITE);

        transition.setOrientation(
                PathTransition.OrientationType.NONE);

        transition.play();

        animations.add(
                transition);
    }

    // =========================================================
    // AMBIENT PARTICLES
    // =========================================================

    private void createAmbientParticles(
            StackPane root) {

        for (int i = 0; i < 18; i++) {

            Circle particle = new Circle(
                    0.8 +
                            random.nextDouble() * 1.8);

            particle.setFill(
                    Color.rgb(
                            11,
                            107,
                            42,
                            0.08 +
                                    random.nextDouble() * 0.15));

            particle.setTranslateX(
                    -600 +
                            random.nextDouble() * 1200);

            particle.setTranslateY(
                    -330 +
                            random.nextDouble() * 660);

            root.getChildren().add(
                    0,
                    particle);

            animateAmbientParticle(
                    particle);
        }
    }

    // =========================================================
    // AMBIENT PARTICLE ANIMATION
    // =========================================================

    private void animateAmbientParticle(
            Circle particle) {

        double startX = particle.getTranslateX();

        double startY = particle.getTranslateY();

        TranslateTransition move = new TranslateTransition(
                Duration.seconds(
                        3 +
                                random.nextDouble() * 4),
                particle);

        move.setFromX(
                startX);

        move.setFromY(
                startY);

        move.setToX(
                startX +
                        (-30 +
                                random.nextDouble() * 60));

        move.setToY(
                startY -
                        (30 +
                                random.nextDouble() * 90));

        move.setAutoReverse(
                true);

        move.setCycleCount(
                Animation.INDEFINITE);

        move.setInterpolator(
                Interpolator.EASE_BOTH);

        FadeTransition fade = new FadeTransition(
                Duration.seconds(
                        2 +
                                random.nextDouble() * 2),
                particle);

        fade.setFromValue(
                0.1);

        fade.setToValue(
                0.65);

        fade.setAutoReverse(
                true);

        fade.setCycleCount(
                Animation.INDEFINITE);

        ParallelTransition animation = new ParallelTransition(
                move,
                fade);

        animation.play();

        animations.add(
                animation);
    }

    // =========================================================
    // TAGLINE ANIMATION
    // =========================================================

    private void animateTagline(
            Label tagline) {

        FadeTransition fade = new FadeTransition(
                Duration.seconds(0.8),
                tagline);

        fade.setFromValue(
                0);

        fade.setToValue(
                1);

        fade.setDelay(
                Duration.seconds(2.0));

        TranslateTransition move = new TranslateTransition(
                Duration.seconds(0.8),
                tagline);

        move.setFromY(
                12);

        move.setToY(
                0);

        move.setDelay(
                Duration.seconds(2.0));

        ParallelTransition animation = new ParallelTransition(
                fade,
                move);

        animation.play();
    }
}