package com.alabtaal.airline.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class frontPageController implements Initializable {

    @FXML
    private ProgressBar progressBar;
    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressBar.setProgress(0);

        URL imageUrl = getClass().getResource("/image/parwaz airline.png");
        if (imageUrl != null) {
            imageView.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            System.out.println("ERROR: Image not found!");
        }

        animateProgressBar();

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(5));
        pause.setOnFinished(event -> loadNextScreen());
        pause.play();
    }

    private void animateProgressBar() {
        final double[] progress = {0};
        Timeline timeline = new Timeline();

        for (int i = 0; i <= 100; i++) {
            final int j = i;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * 0.05), e -> {
                progressBar.setProgress(j / 100.0);  // Update progress (0 to 1)
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setCycleCount(1);

        timeline.play();
    }

    private void loadNextScreen() {
        loadScene("/fxml/AdminVsPassenger.fxml", "Choose Admin or Passenger");
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) imageView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
