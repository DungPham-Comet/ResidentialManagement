package com.quartermanagement.Controller.CoSoVatChat;

import com.quartermanagement.Model.CoSoVatChat;
import com.quartermanagement.Services.CoSoVatChatServices;
import com.quartermanagement.Utils.ViewUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ResourceBundle;

import static com.quartermanagement.Constants.DBConstants.*;
import static com.quartermanagement.Utils.Utils.createDialog;


public class CoSoVatChatDeTailController implements Initializable {
    @FXML
    private TextField maDoDungTextField;

    @FXML
    private TextField tenDoDungTextField;

    @FXML
    private TextField soLuongTextField;

    @FXML
    private TextField soLuongKhaDungTextField;

    @FXML
    private Button add_btn;

    @FXML
    private Button update_btn;

    @FXML
    private Text title;

    @FXML
    private Pane maDoDungPane, soLuongKhaDungPane;


    public void setCoSoVatChat(CoSoVatChat coSoVatChat) {
        maDoDungTextField.setText(String.valueOf(coSoVatChat.getMaDoDung()));
        tenDoDungTextField.setText(coSoVatChat.getTenDoDung());
        soLuongTextField.setText(String.valueOf(coSoVatChat.getSoLuong()));
        soLuongKhaDungTextField.setText(String.valueOf(coSoVatChat.getSoLuongKhaDung()));

    }

    public void goBack(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        viewUtils.switchToCoSoVatChat_Admin_view(event);
    }

    public void update(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        String maDoDung = maDoDungTextField.getText();
        String tenDoDung = tenDoDungTextField.getText();
        String soLuong = soLuongTextField.getText();
        String soLuongKhaDung = soLuongKhaDungTextField.getText();


        if (maDoDung.trim().equals("") || tenDoDung.trim().equals("") || soLuong.trim().equals("") || soLuongKhaDung.trim().equals("")) {

            createDialog(
                    Alert.AlertType.WARNING,
                    "Đồng chí giữ bình tĩnh",
                    "", "Vui lòng nhập đủ thông tin!"
            );
        } else if (Integer.parseInt(soLuongKhaDung) > Integer.parseInt(soLuong) || Integer.parseInt(soLuongKhaDung) < 0) {
            createDialog(
                    Alert.AlertType.WARNING,
                    "Mời đồng chí nhập lại!",
                    "", "Mong đồng chí tham khảo thêm các định luật bảo toàn vật chất"
            );
        } else {
            try {
                Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                int result = CoSoVatChatServices.updateFacility(conn, maDoDung, tenDoDung, soLuong, soLuongKhaDung);
                if (result == 1) {
                    createDialog(
                            Alert.AlertType.CONFIRMATION,
                            "Thành công",
                            "", "Đồng chí vất vả rồi!"
                    );
                    viewUtils.switchToCoSoVatChat_Admin_view(event);
                } else {
                    createDialog(
                            Alert.AlertType.ERROR,
                            "Thất bại",
                            "", "Thất bại là mẹ thành công! Mong đồng chí thử lại"
                    );
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addnew(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        String maDoDung;
        String tenDoDung = tenDoDungTextField.getText();
        String soLuong = soLuongTextField.getText();
        String soLuongKhaDung = soLuongTextField.getText();
        if (tenDoDung.trim().equals("") || soLuong.trim().equals("")) {

            createDialog(
                    Alert.AlertType.WARNING,
                    "Đồng chí giữ bình tĩnh",
                    "", "Vui lòng nhập đủ thông tin!"
            );
        } else {
            try {
                Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                PreparedStatement preparedStatement;
                ResultSet rs;
                do {
                    int rand = ThreadLocalRandom.current().nextInt(100000, 999999);
                    maDoDung = String.valueOf(rand);
                    PreparedStatement check = conn.prepareStatement("SELECT MaDoDung From cosovatchat WHERE `MADoDung` =?");
                    check.setInt(1, rand);
                    rs = check.executeQuery();
                } while (rs.next());
                int result = CoSoVatChatServices.addFacility(conn, maDoDung, tenDoDung, soLuong, soLuongKhaDung);
                if (result == 1) {
                    createDialog(
                            Alert.AlertType.CONFIRMATION,
                            "Thành công",
                            "", "Đồng chí vất vả rồi!"
                    );
                } else {
                    createDialog(
                            Alert.AlertType.ERROR,
                            "Thất bại",
                            "", "Thất bại là mẹ thành công! Mong đồng chí thử lại"
                    );
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            viewUtils.switchToCoSoVatChat_Admin_view(event);
        }
    }

    public void hide_add_btn() {
        add_btn.setVisible(false);
    }

    public void hide_update_btn() {
        update_btn.setVisible(false);
        add_btn.setTranslateX(100);
    }

    public void hide_Pane() {
        maDoDungPane.setVisible(false);
        soLuongKhaDungPane.setVisible(false);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
