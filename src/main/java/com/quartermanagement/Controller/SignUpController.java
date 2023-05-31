package com.quartermanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.quartermanagement.HomeApplication;

import static com.quartermanagement.Constants.DBConstants.*;
import static com.quartermanagement.Constants.FXMLConstants.HOME_VIEW_FXML;
import static com.quartermanagement.Utils.Utils.hashPassword;
import static com.quartermanagement.Utils.Utils.createDialog;

public class SignUpController implements Initializable {
    @FXML
    private TextField signUpUsername, signUpPassword;
//    @FXML
//    private ImageView myReturn;
    @FXML
    private Button returnButton;
    @FXML
    private RadioButton isAdmin, isOfficer;
    private final ToggleGroup toggleRole = new ToggleGroup();
    private Stage stage;
    
    
    public void returnToLogin(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource(HOME_VIEW_FXML));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Quarter Management");
        stage.setScene(scene);
        //myReturn.setImage(myImage);
        stage.show();
    }

    public void handleSignUp() {
        String inputUsername = signUpUsername.getText();
        String inputPassword = signUpPassword.getText();
        String role = "";

        if (inputUsername.trim().equals("") || inputPassword.trim().equals("")) {
            createDialog(
                    Alert.AlertType.WARNING,
                    "Khoan nào cán bộ",
                    "", "Vui lòng nhập đủ username và password!"
            );

        }   else {
            if (!isOfficer.isSelected() && !isAdmin.isSelected()) {
                createDialog(
                        Alert.AlertType.WARNING,
                        "Khoan nào cán bộ",
                        "", "Vui lòng chọn role cho username!"
                );
            }   else {
                if (isOfficer.isSelected()) role = "canbo";
                if (isAdmin.isSelected()) role = "totruong";
                String CREATE_QUERY = "INSERT INTO user (username, password, role) VALUES (?,?,?)";
                try {
                    Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                    PreparedStatement preparedStatement = conn.prepareStatement(CREATE_QUERY);
                    preparedStatement.setString(1, inputUsername);
                    preparedStatement.setString(2, hashPassword(inputPassword));
                    preparedStatement.setString(3, role);
                    int result = preparedStatement.executeUpdate();
                    if (result == 1) {
                        signUpPassword.clear();
                        signUpUsername.clear();
                        isAdmin.setSelected(false);
                        isOfficer.setSelected(false);
                        createDialog(
                                Alert.AlertType.CONFIRMATION,
                                "Thành công",
                                "", "Đăng ký người dùng mới thành công!"
                        );
                    }   else {
                        createDialog(
                                Alert.AlertType.ERROR,
                                "Thất bại",
                                "", "Đăng ký người dùng mới thất bại!"
                        );
                    }
                }   catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isAdmin.setToggleGroup(toggleRole);
        isOfficer.setToggleGroup(toggleRole);
    }
}
