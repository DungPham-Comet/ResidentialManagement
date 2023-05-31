package com.quartermanagement.Controller.NhanKhau;

import com.quartermanagement.Model.NhanKhau;
import com.quartermanagement.Services.NhanKhauServices;
import com.quartermanagement.Utils.ViewUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.quartermanagement.Constants.DBConstants.*;
import static com.quartermanagement.Utils.Utils.*;
import static com.quartermanagement.Utils.Utils.isCccd;

public class NhanKhauDetailViewController implements Initializable {
    @FXML
    private TextField hoVaTenTextField;
    @FXML
    private TextField biDanhTextField;
    @FXML
    private DatePicker ngaySinhDatePicker;
    @FXML
    private TextField cccdTextField;
    @FXML
    private TextField noiSinhTextField;
    @FXML
    private ChoiceBox<String> gioiTinhChoiceBox;
    @FXML
    private TextField nguyenQuanTextField;
    @FXML
    private TextField danTocTextField;
    @FXML
    private TextField noiThuongTruTextField;
    @FXML
    private TextField tonGiaoTextField;
    @FXML
    private TextField quocTichTextField;
    @FXML
    private TextField diaChiHienNayTextField;
    @FXML
    private TextField ngheNghiepTextField;
    @FXML
    private Button add_btn;
    @FXML
    private Button update_btn;
    @FXML
    private Text title;

    private int ID;

    public void setNhanKhau(NhanKhau nhanKhau) {
        hoVaTenTextField.setText(nhanKhau.getHoTen());
        biDanhTextField.setText(nhanKhau.getBiDanh());
        ngaySinhDatePicker.setValue(LOCAL_DATE(nhanKhau.getNgaySinh()));
        cccdTextField.setText(nhanKhau.getCCCD());
        noiSinhTextField.setText(nhanKhau.getNoiSinh());
        gioiTinhChoiceBox.setValue(nhanKhau.getGioiTinh());
        nguyenQuanTextField.setText(nhanKhau.getNguyenQuan());
        danTocTextField.setText(nhanKhau.getDanToc());
        noiThuongTruTextField.setText(nhanKhau.getNoiThuongTru());
        tonGiaoTextField.setText(nhanKhau.getTonGiao());
        quocTichTextField.setText(nhanKhau.getQuocTich());
        diaChiHienNayTextField.setText(nhanKhau.getDiaChiHienNay());
        ngheNghiepTextField.setText(nhanKhau.getNgheNghiep());
    }

    public void goBack(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        viewUtils.switchToNhanKhau_Admin_view(event);
    }


    public void update(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        if (ngaySinhDatePicker.getValue() == null) createDialog(
                Alert.AlertType.WARNING,
                "Đồng chí giữ bình tĩnh",
                "", "Vui lòng nhập đủ thông tin!");
        {
            String hoVaTen = hoVaTenTextField.getText();
            String biDanh = biDanhTextField.getText();
            String ngaySinh = ngaySinhDatePicker.getValue().toString();
            String cccd = cccdTextField.getText();
            String noiSinh = noiSinhTextField.getText();
            String gioiTinh = gioiTinhChoiceBox.getValue();
            String nguyenQuan = nguyenQuanTextField.getText();
            String danToc = danTocTextField.getText();
            String noiThuongTru = noiThuongTruTextField.getText();
            String tonGiao = tonGiaoTextField.getText();
            String quocTich = quocTichTextField.getText();
            String diaChiHienNay = diaChiHienNayTextField.getText();
            String ngheNghiep = ngheNghiepTextField.getText();

            if (ngaySinhDatePicker.isPressed() || hoVaTen.trim().equals("") || ngaySinh.trim().equals("") || cccd.trim().equals("") ||
                    noiSinh.trim().equals("") || gioiTinh.trim().equals("") || nguyenQuan.trim().equals("") || danToc.trim().equals("") ||
                    noiThuongTru.trim().equals("") || diaChiHienNay.trim().equals("")) {

                createDialog(
                        Alert.AlertType.WARNING,
                        "Đồng chí giữ bình tĩnh",
                        "", "Vui lòng nhập đủ thông tin!")
                ;
            } else {
                //regex
                if (isCccd(cccd)) {
                    createDialog(Alert.AlertType.WARNING, "Từ từ thôi đồng chí!", "Hãy nhập đúng định dạng CCCD", "");
                } else {

                    try {
                        Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                        int result1 = NhanKhauServices.updateNhanKhau(conn, hoVaTen, biDanh, ngaySinh, noiSinh,
                                gioiTinh, nguyenQuan, danToc, noiThuongTru, tonGiao, quocTich, diaChiHienNay, ngheNghiep, ID);
                        int result2 = NhanKhauServices.updateCCCD(conn, cccd, ID);
                        if (result1 == 1 && result2 == 1) {
                            createDialog(
                                    Alert.AlertType.CONFIRMATION,
                                    "Thành công",
                                    "", "Đồng chí vất vả rồi!"
                            );
                            //          swtich to admin-nhankhau-view
                            viewUtils.switchToNhanKhau_Admin_view(event);
                        } else {
                            createDialog(
                                    Alert.AlertType.ERROR,
                                    "Thất bại",
                                    "", "Oops, mời đồng chí nhập lại thông tin!"
                            );
                        }
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    public void addnew(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        if (ngaySinhDatePicker.getValue() == null) createDialog(
                Alert.AlertType.WARNING,
                "Đồng chí giữ bình tĩnh",
                "", "Vui lòng nhập đủ thông tin!");
        else {
            String hoVaTen = hoVaTenTextField.getText();
            String biDanh = biDanhTextField.getText();
            String ngaySinh = ngaySinhDatePicker.getValue().toString();
            String cccd = cccdTextField.getText();
            String noiSinh = noiSinhTextField.getText();
            String gioiTinh = gioiTinhChoiceBox.getValue();
            String nguyenQuan = nguyenQuanTextField.getText();
            String danToc = danTocTextField.getText();
            String noiThuongTru = noiThuongTruTextField.getText();
            String tonGiao = tonGiaoTextField.getText();
            String quocTich = quocTichTextField.getText();
            String diaChiHienNay = diaChiHienNayTextField.getText();
            String ngheNghiep = ngheNghiepTextField.getText();
            if (hoVaTen.trim().equals("") || ngaySinh.trim().equals("") || cccd.trim().equals("") ||
                    noiSinh.trim().equals("") || gioiTinh.trim().equals("") || nguyenQuan.trim().equals("") || danToc.trim().equals("") ||
                    noiThuongTru.trim().equals("") || diaChiHienNay.trim().equals("") || cccd.trim().equals("")) {

                createDialog(
                        Alert.AlertType.WARNING,
                        "Đồng chí giữ bình tĩnh",
                        "", "Vui lòng nhập đủ thông tin!")
                ;
            } else {
                //regex
                if (isCccd(cccd)) {
                    createDialog(Alert.AlertType.WARNING, "Từ từ thôi đồng chí!", "Hãy nhập đúng định dạng CCCD", "");
                } else {
                    try {
                        //Add to nhankhau
                        Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                        int result = NhanKhauServices.addNhanKhau(conn, hoVaTen, biDanh, ngaySinh, noiSinh,
                                gioiTinh, nguyenQuan, danToc, noiThuongTru, tonGiao, quocTich, diaChiHienNay, ngheNghiep);
                        //Find to id
                        int ID = NhanKhauServices.findNhanKhauID(conn);
                        //Add to cccd
                        int result2 = NhanKhauServices.addCCCD(conn, ID, cccd);
                        if (result == 1 && result2 == 1) {
                            createDialog(
                                    Alert.AlertType.CONFIRMATION,
                                    "Thành công",
                                    "", "Đồng chí vất vả rồi!"
                            );
                        } else {
                            createDialog(
                                    Alert.AlertType.ERROR,
                                    "Thất bại",
                                    "", "Oops, mời đồng chí nhập lại thông tin!"
                            );
                        }
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    viewUtils.switchToNhanKhau_Admin_view(event);
                }
            }
        }

    }

    public void hide_add_btn() {
        add_btn.setVisible(false);
    }

    public void hide_update_btn() {
        update_btn.setVisible(false);
        add_btn.setTranslateX(100);
    }

    // Getter and setter methods for all
    public TextField getHoVaTenTextField() {
        return hoVaTenTextField;
    }

    public void setHoVaTenTextField(TextField hoVaTenTextField) {
        this.hoVaTenTextField = hoVaTenTextField;
    }

    public TextField getBiDanhTextField() {
        return biDanhTextField;
    }

    public void setBiDanhTextField(TextField biDanhTextField) {
        this.biDanhTextField = biDanhTextField;
    }


    public TextField getCccdTextField() {
        return cccdTextField;
    }

    public void setCccdTextField(TextField cccdTextField) {
        this.cccdTextField = cccdTextField;
    }

    public TextField getNoiSinhTextField() {
        return noiSinhTextField;
    }

    public void setNoiSinhTextField(TextField noiSinhTextField) {
        this.noiSinhTextField = noiSinhTextField;
    }

    public TextField getNguyenQuanTextField() {
        return nguyenQuanTextField;
    }

    public void setNguyenQuanTextField(TextField nguyenQuanTextField) {
        this.nguyenQuanTextField = nguyenQuanTextField;
    }

    public TextField getDanTocTextField() {
        return danTocTextField;
    }

    public void setDanTocTextField(TextField danTocTextField) {
        this.danTocTextField = danTocTextField;
    }

    public TextField getNoiThuongTruTextField() {
        return noiThuongTruTextField;
    }

    public void setNoiThuongTruTextField(TextField noiThuongTruTextField) {
        this.noiThuongTruTextField = noiThuongTruTextField;
    }

    public TextField getTonGiaoTextField() {
        return tonGiaoTextField;
    }

    public void setTonGiaoTextField(TextField tonGiaoTextField) {
        this.tonGiaoTextField = tonGiaoTextField;
    }

    public TextField getQuocTichTextField() {
        return quocTichTextField;
    }

    public void setQuocTichTextField(TextField quocTichTextField) {
        this.quocTichTextField = quocTichTextField;
    }

    public TextField getDiaChiHienNayTextField() {
        return diaChiHienNayTextField;
    }

    public void setDiaChiHienNayTextField(TextField diaChiHienNayTextField) {
        this.diaChiHienNayTextField = diaChiHienNayTextField;
    }

    public TextField getNgheNghiepTextField() {
        return ngheNghiepTextField;
    }

    public void setNgheNghiepTextField(TextField ngheNghiepTextField) {
        this.ngheNghiepTextField = ngheNghiepTextField;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gioiTinhChoiceBox.getItems().add("Nam");
        gioiTinhChoiceBox.getItems().add("Nữ");
        gioiTinhChoiceBox.setValue("Nam");
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
