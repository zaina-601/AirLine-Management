package com.alabtaal.airline;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class AirlineFXApplication extends Application {

    protected ConfigurableApplicationContext springContext;

    @Override
    public void start(Stage primaryStage) throws Exception {
        displayInitialScene(primaryStage);
    }

    @Override
    public void init() throws Exception {
        springContext = springBootContext();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }

    public void displayInitialScene(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/frontPage.fxml"));
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Airline Management System");
        primaryStage.show();
    }

    private ConfigurableApplicationContext springBootContext() {
        final SpringApplicationBuilder builder = new SpringApplicationBuilder(JavaFxApplication.class);
        builder.headless(false);
        final String[] args = getParameters().getRaw().toArray(String[]::new);
        return builder.run(args);
    }
}
