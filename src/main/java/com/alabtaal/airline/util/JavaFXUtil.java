package com.alabtaal.airline.util;

import com.alabtaal.airline.util.constant.FxmlView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

public class JavaFXUtil {
    public static javafx.stage.Stage Stage = null;
    private final ApplicationContext applicationContext;

    public JavaFXUtil(ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }


    public void switchScene(FxmlView fxmlView) throws IOException {
        FXMLLoader loader = new FXMLLoader(JavaFXUtil.class.getResource(fxmlView.getFilePath()));
        loader.setControllerFactory(applicationContext::getBean);
        Parent root = loader.load();
        Stage.setTitle(fxmlView.getTitle());
        Stage.setScene(new Scene(root));
        Stage.show();

    }
    public static void showMessage(Alert.AlertType alertType, String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
