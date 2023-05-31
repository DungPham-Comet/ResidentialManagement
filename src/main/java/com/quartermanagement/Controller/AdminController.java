package com.quartermanagement.Controller;

import com.quartermanagement.HomeApplication;
import com.quartermanagement.Services.CoSoVatChatServices;
import com.quartermanagement.Services.LichHoatDongServices;
import com.quartermanagement.Services.NhanKhauServices;
import com.quartermanagement.Services.SoHoKhauServices;
import com.quartermanagement.Utils.ViewUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static com.quartermanagement.Constants.DBConstants.*;
import static com.quartermanagement.Constants.FXMLConstants.*;
import static com.quartermanagement.Utils.Utils.toUpperFirstLetter;

public class AdminController implements Initializable {
    @FXML
    private AnchorPane basePane;
    @FXML
    private Button signUpUserButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Label nhankhauLabel, hokhauLabel, usernameLabel;
    @FXML
    private Text lichHoatDongLabel, thoiGianLabel;
    @FXML
    private BarChart facilityChart;
    private Stage stage;
    
    //Save user role
    private static final Preferences userPreferences = Preferences.userRoot();
    public static final String userRole = userPreferences.get("role", "");
    public static final String userName = userPreferences.get("username", "");
    private final ViewUtils viewUtils = new ViewUtils();
    private Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);

    public AdminController() throws SQLException {
    }

    public void switchToDashboard(ActionEvent event) throws IOException {
        viewUtils.changeScene(event, ADMIN_VIEW_FXML);
    }

    public void switchToSignUp() throws IOException {
        viewUtils.changeAnchorPane(basePane, SIGN_UP_USER_VIEW_FXML);
    }

    public void switchToNhanKhau() throws IOException {
        viewUtils.changeAnchorPane(basePane, NHAN_KHAU_VIEW_FXML);

    }

    public void switchToCoSoVatChat() throws IOException {
        viewUtils.changeAnchorPane(basePane, CO_SO_VAT_CHAT_VIEW_FXML);
    }
    public void switchToSoHoKhau() throws IOException {
        viewUtils.changeAnchorPane(basePane, SO_HO_KHAU_VIEW_FXML);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUpUserButton.setVisible(userRole.equals("totruong"));
        nhankhauLabel.setText("" + NhanKhauServices.getTotalNhanKhau());
        hokhauLabel.setText("" + SoHoKhauServices.getTotalSoHoKhau());
        usernameLabel.setText(toUpperFirstLetter(userName));

        ResultSet result = null;
        try {
            result = LichHoatDongServices.getLichHoatDongGanNhat(conn);
            if (result.next()) {
                lichHoatDongLabel.setText(result.getString(1));
                thoiGianLabel.setText(result.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        XYChart.Series dataSeries = new XYChart.Series();
        for (Map.Entry<String, Integer> entry : CoSoVatChatServices.getLeastFiveFacility().entrySet()) {
            dataSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        facilityChart.getData().add(dataSeries);
    }
    
    public void logOut(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource(HOME_VIEW_FXML));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Quarter Management");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLichHoatDong() throws IOException {
        viewUtils.changeAnchorPane(basePane, LICH_HOAT_DONG_VIEW_FXML);
    }
}
