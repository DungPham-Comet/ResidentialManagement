package com.quartermanagement.Controller.SoHoKhau;


import com.quartermanagement.Controller.AddThanhVienController;
import com.quartermanagement.Controller.AdminController;
import com.quartermanagement.Model.NhanKhau;
import com.quartermanagement.Model.SoHoKhau;
import com.quartermanagement.Model.ThanhVien;
import com.quartermanagement.Services.SoHoKhauServices;
import com.quartermanagement.Utils.ViewUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.quartermanagement.Constants.DBConstants.*;

import static com.quartermanagement.Constants.FXMLConstants.ADD_SOHOKHAU_VIEW_FXML;
import static com.quartermanagement.Constants.FXMLConstants.NHAN_KHAU_VIEW_FXML;
import static com.quartermanagement.Utils.Utils.*;

public class add_shk_controller {
    @FXML
    AnchorPane basePane;
    @FXML
    private TextField maChuHoTextField;

    @FXML
    private TextField diaChiTextField;

    @FXML
    private TextField maHoKhauTextField;
    @FXML
    private Button add_btn;
    @FXML
    private Button update_btn;
    @FXML
    private Text title;
    @FXML
    private TextField tenChuHoTextField;
    @FXML
    TableView<ThanhVien> thanhVienTable;
    @FXML
    TableColumn<ThanhVien, String> hoTenColumn;
    @FXML
    TableColumn<ThanhVien, String> cccdColumn;
    @FXML
    TableColumn<ThanhVien, String> quanHeColumn;
    private ObservableList<ThanhVien> thanhVienList = FXCollections.observableArrayList();
    @FXML
    private Button addThanhVienBtn;
    @FXML
    private Button deleteThanhVienBtn;
    @FXML
    private Button updateThanhVienBtn;
    @FXML
    private TextField ngayTaoTextField;
    private SoHoKhau soHoKhau;
    private int idHoKhau;
    private Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);

    public add_shk_controller() throws SQLException {
    }

    public void setSoHoKhau(SoHoKhau soHoKhau) throws SQLException {
        this.soHoKhau = soHoKhau;
        Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        ;
        ResultSet result = SoHoKhauServices.getSoHoKhauViaMaHoKhau(conn, soHoKhau);
        if (result.next()) {
            tenChuHoTextField.setText(result.getString("HoTen"));
            maChuHoTextField.setText(result.getString("CCCD"));
            ngayTaoTextField.setText(result.getString("NgayLap"));
            idHoKhau = result.getInt(1);
        }
        diaChiTextField.setText(soHoKhau.getDiaChi());
        maHoKhauTextField.setText(String.valueOf(soHoKhau.getMaHoKhau()));
        ResultSet result2 = SoHoKhauServices.getThanhVienCuaHo(conn, soHoKhau);
        while (result2.next()) {
            thanhVienList.add(new ThanhVien(result2.getString("HoTen"), result2.getString("CCCD"), result2.getString("quanHeVoiChuHo")));
        }
        thanhVienTable.setItems(FXCollections.observableArrayList(thanhVienList));
        hoTenColumn.setCellValueFactory(new PropertyValueFactory<ThanhVien, String>("HoTen"));
        cccdColumn.setCellValueFactory(new PropertyValueFactory<ThanhVien, String>("CCCD"));
        quanHeColumn.setCellValueFactory(new PropertyValueFactory<ThanhVien, String>("quanHeVoiChuHo"));

    }


    //    them chu ho
    @FXML
    private TableView<NhanKhau> tableView;
    @FXML
    private TableColumn indexColumn;
    @FXML
    private TableColumn<NhanKhau, String> hoVaTenColumn, biDanhColumn, ngaySinhColumn, cccd2Column, noiSinhColumn, gioiTinhColumn,
            nguyenQuanColumn, danTocColumn, noiThuongTruColumn, tonGiaoColumn, quocTichColumn, diaChiHienNayColumn, ngheNghiepColumn;
    private ObservableList<NhanKhau> nhanKhauList = FXCollections.observableArrayList();

    public void khoiTaoBangChuHo() throws SQLException {
        Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        ResultSet result = SoHoKhauServices.getChuHo(conn);
        while (result.next()) {
            nhanKhauList.add(new NhanKhau(result.getInt("ID"), result.getString("HoTen"), result.getString("BiDanh"),
                    convertDate(result.getString("NgaySinh")), result.getString("CCCD"), result.getString("NoiSinh"),
                    result.getString("GioiTinh"), result.getString("NguyenQuan"), result.getString("DanToc"),
                    result.getString("NoiThuongTru"), result.getString("TonGiao"), result.getString("QuocTich"),
                    result.getString("DiaChiHienNay"), result.getString("NgheNghiep")
            ));
        }
        indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<NhanKhau, NhanKhau>, ObservableValue<NhanKhau>>) p -> new ReadOnlyObjectWrapper(p.getValue()));

        indexColumn.setCellFactory(new Callback<TableColumn<NhanKhau, NhanKhau>, TableCell<NhanKhau, NhanKhau>>() {
            @Override
            public TableCell<NhanKhau, NhanKhau> call(TableColumn<NhanKhau, NhanKhau> param) {
                return new TableCell<NhanKhau, NhanKhau>() {
                    @Override
                    protected void updateItem(NhanKhau item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex() + 1 + "");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        indexColumn.setSortable(false);
//        idColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, Integer>("ID"));
        hoVaTenColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("HoTen"));
        biDanhColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("BiDanh"));
        ngaySinhColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("NgaySinh"));
        cccd2Column.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("CCCD"));
        noiSinhColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("NoiSinh"));
        gioiTinhColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("GioiTinh"));
        nguyenQuanColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("NguyenQuan"));
        danTocColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("DanToc"));
        noiThuongTruColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("NoiThuongTru"));
        tonGiaoColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("TonGiao"));
        quocTichColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("QuocTich"));
        diaChiHienNayColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("DiaChiHienNay"));
        ngheNghiepColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("NgheNghiep"));
        tableView.setItems(FXCollections.observableArrayList(nhanKhauList));
    }


    public void goBack(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        viewUtils.switchToSoHoKhau_Admin_view(event);
    }

    public void hide_add_btn() {
        add_btn.setVisible(false);
    }

    public void hide_update_btn() {
        update_btn.setVisible(false);
        add_btn.setTranslateX(100);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }


    public void addnew(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        NhanKhau selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) createDialog(Alert.AlertType.WARNING, "Từ từ đã đồng chí", "", "Vui lòng chọn hộ khẩu");
        else {
            String diaChi = diaChiTextField.getText();
            String maHoKhau = generateRandomNumber(9);

            if (diaChi.trim().equals("") || maHoKhau
                    .trim().equals("")) {

                createDialog(
                        Alert.AlertType.WARNING,
                        "Đồng chí giữ bình tĩnh",
                        "", "Vui lòng nhập đủ thông tin!")
                ;
            } else {
                try {
                    Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                    PreparedStatement preparedStatement = SoHoKhauServices.addSoHoKhau(conn, maHoKhau, diaChi, selected);
                    int result = preparedStatement.executeUpdate();
                    ResultSet keys = preparedStatement.getGeneratedKeys();
                    if (keys.next()) {
                        idHoKhau = keys.getInt(1);
                    }
                    System.out.println("thuc hien thanh cong");

                    if (result == 1) {
                        //switch to admin-sohokhau-view
                        createDialog(
                                Alert.AlertType.CONFIRMATION,
                                "Thành công",
                                "", "Đồng chí vất vả rồi!"
                        );
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Hãy đưa ra lựa chọn");
                        alert.setHeaderText("Đồng chí có muốn thêm thành viên vào trong sổ hộ khẩu luôn không?");
                        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                        Optional<ButtonType> ketqua = alert.showAndWait();
                        if (ketqua.get() == ButtonType.YES) {
                            // Code for Option 1
                            soHoKhau = new SoHoKhau(selected.getHoTen(), diaChi, maHoKhau, 1);
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/com/quartermanagement/views/addthanhvien-view.fxml"));
                            Parent studentViewParent = loader.load();
                            Scene scene = new Scene(studentViewParent);
                            AddThanhVienController controller = loader.getController();
                            controller.setIdHoKhau(idHoKhau);
                            controller.setSoHoKhau(soHoKhau);
                            controller.initTable(soHoKhau);
                            stage.setScene(scene);
                        } else {
                            viewUtils.switchToSoHoKhau_Admin_view(event);
                        }

                    } else {
                        createDialog(
                                Alert.AlertType.ERROR,
                                "Thất bại",
                                "", "Oops, mời đồng chí nhập lại thông tin!"
                        );
                    }

                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void update(ActionEvent event) throws IOException, SQLException {
        ViewUtils viewUtils = new ViewUtils();
        SoHoKhauServices.update(conn, idHoKhau, maHoKhauTextField.getText(), diaChiTextField.getText());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận đổi chủ hộ");
        alert.setContentText("Đồng chí có muốn thay đổi chủ hộ?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                try {
                    setDisableForAdd();
                    maHoKhauLabel.setVisible(true);
                    maHoKhauTextField.setVisible(true);
                    ngayTaoLabel.setVisible(true);
                    ngayTaoTextField.setVisible(true);
                    tableView.setVisible(true);
                    luaChonLabel.setVisible(true);
                    doiChuHoBtn.setVisible(true);
                    khoiTaoBangChuHo();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                createDialog(
                        Alert.AlertType.CONFIRMATION,
                        "Thành công",
                        "", "Đồng chí vất vả rồi!"
                );
                try {
                    viewUtils.switchToSoHoKhau_Admin_view(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void doiChuHo(ActionEvent event) throws SQLException, IOException {
        ViewUtils viewUtils = new ViewUtils();
        NhanKhau selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) createDialog(Alert.AlertType.WARNING, "Từ từ đã đồng chí", "", "Vui lòng chọn hộ khẩu");
        else {
            Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            int result = SoHoKhauServices.updateChuHo(conn, idHoKhau, selected);
            if (result == 1) {
                //switch to admin-sohokhau-view
                createDialog(
                        Alert.AlertType.CONFIRMATION,
                        "Thành công",
                        "", "Đồng chí vất vả rồi!"
                );
                viewUtils.switchToSoHoKhau_Admin_view(event);
            }

        }
    }

    public void addthanhvien(ActionEvent event) throws IOException, SQLException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/quartermanagement/views/addthanhvien-view.fxml"));
        Parent studentViewParent = loader.load();
        Scene scene = new Scene(studentViewParent);
        AddThanhVienController controller = loader.getController();
        controller.setIdHoKhau(idHoKhau);
        controller.setSoHoKhau(soHoKhau);
        controller.initTable(soHoKhau);
        stage.setScene(scene);
    }

    public void deletethanhvien(ActionEvent event) {
        ThanhVien selected = thanhVienTable.getSelectionModel().getSelectedItem();
        if (selected == null) createDialog(Alert.AlertType.WARNING,
                "Cảnh báo",
                "Vui lòng chọn nhân khẩu để tiếp tục", "");
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa nhân khẩu");
            alert.setContentText("Đồng chí muốn xóa nhân khẩu này khỏi sổ hộ khẩu?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    thanhVienList.remove(selected);
                    // Delete in Database
                    try {
                        Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                        int result = SoHoKhauServices.deleteThanhVienInSoHoKhau(conn, selected);
                        if (result == 1) createDialog(Alert.AlertType.INFORMATION, "Thông báo", "Xóa thành công!", "");
                        else createDialog(Alert.AlertType.WARNING, "Thông báo", "Có lỗi, thử lại sau!", "");
                        thanhVienList.clear();
                        setSoHoKhau(soHoKhau);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void updatethanhvien(ActionEvent event) {
        ThanhVien selected = thanhVienTable.getSelectionModel().getSelectedItem();
        if (selected == null) createDialog(Alert.AlertType.WARNING,
                "Cảnh báo",
                "Vui lòng chọn nhân khẩu để tiếp tục", "");
        else {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Cập nhật Quan hệ với chủ hộ");
            dialog.setHeaderText("Quan hệ với chủ hộ:");
            dialog.setContentText("Quan hệ với chủ hộ:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(quanHe -> {
                System.out.println(quanHe);
                try {
                    Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                    int res = SoHoKhauServices.updateThanhVienInSoHoKhau(conn, quanHe, selected);
                    if (res == 1) createDialog(Alert.AlertType.INFORMATION, "Thông báo", "Cập nhật thành công!", "");
                    else createDialog(Alert.AlertType.WARNING, "Thông báo", "Có lỗi, thử lại sau!", "");
                    thanhVienList.clear();
                    setSoHoKhau(soHoKhau);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private Text ngayTaoLabel, cccdLabel, tenChuHoLabel, maHoKhauLabel;
    @FXML
    private Button doiChuHoBtn;

    public void setDisableForAdd() {
        ngayTaoLabel.setVisible(false);
        cccdLabel.setVisible(false);
        tenChuHoLabel.setVisible(false);
        ngayTaoTextField.setVisible(false);
        maChuHoTextField.setVisible(false);
        tenChuHoTextField.setVisible(false);
        thanhVienTable.setVisible(false);
        addThanhVienBtn.setVisible(false);
        deleteThanhVienBtn.setVisible(false);
        updateThanhVienBtn.setVisible(false);
        maHoKhauLabel.setVisible(false);
        maHoKhauTextField.setVisible(false);
        doiChuHoBtn.setVisible(false);
    }

    @FXML
    Text luaChonLabel;

    public void setDisableForDetail() throws SQLException {
        doiChuHoBtn.setVisible(false);
        luaChonLabel.setVisible(false);
        tableView.setVisible(false);

    }
    // Getter and setter methods for all

    public TextField getMaChuHoTextField() {
        return maChuHoTextField;
    }

    public void setMaChuHoTextField(TextField maChuHoTextField) {
        this.maChuHoTextField = maChuHoTextField;
    }

    public TextField getDiaChiTextField() {
        return diaChiTextField;
    }

    public void setDiaChiTextField(TextField diaChiTextField) {
        this.diaChiTextField = diaChiTextField;
    }

    public TextField getMaHoKhauTextField() {
        return maHoKhauTextField;
    }

    public void setMaHoKhauTextField(TextField maHoKhauTextField) {
        this.maHoKhauTextField = maHoKhauTextField;
    }

    public Button getAdd_btn() {
        return add_btn;
    }

    public void setAdd_btn(Button add_btn) {
        this.add_btn = add_btn;
    }

    public Button getUpdate_btn() {
        return update_btn;
    }

    public void setUpdate_btn(Button update_btn) {
        this.update_btn = update_btn;
    }

    public Text getTitle() {
        return title;
    }

    public void setTitle(Text title) {
        this.title = title;
    }

    public SoHoKhau getSoHoKhau() {
        return soHoKhau;
    }
}
