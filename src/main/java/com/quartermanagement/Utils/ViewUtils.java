package com.quartermanagement.Utils;

import com.quartermanagement.Controller.AdminController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

import static com.quartermanagement.Constants.FXMLConstants.ADMIN_VIEW_FXML;

public class ViewUtils {
    public void changeScene(ActionEvent event, String viewSource) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewSource));
        root = loader.load();
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void changeAnchorPane(AnchorPane currentPane, String viewSource) throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(viewSource)));
        currentPane.getChildren().setAll(node);
    }

    public void switchToNhanKhau_Admin_view(Event event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ADMIN_VIEW_FXML));
        root = loader.load();
        AdminController controller = loader.getController();
        controller.switchToNhanKhau();
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // cosovatchat
    public void switchToCoSoVatChat_Admin_view(Event event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ADMIN_VIEW_FXML));
        root = loader.load();
        AdminController controller = loader.getController();
        controller.switchToCoSoVatChat();
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
// so ho khau
    public void switchToSoHoKhau_Admin_view (ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ADMIN_VIEW_FXML));
        root = loader.load();
        AdminController controller = loader.getController();
        controller.switchToSoHoKhau();
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLichHoatDong_Admin_view(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ADMIN_VIEW_FXML));
        root = loader.load();
        AdminController controller = loader.getController();
        controller.switchToLichHoatDong();
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
