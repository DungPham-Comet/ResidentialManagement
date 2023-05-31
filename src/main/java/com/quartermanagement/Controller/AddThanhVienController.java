package com.quartermanagement.Controller;

import com.quartermanagement.Controller.NhanKhau.NhanKhauDetailViewController;
import com.quartermanagement.Controller.SoHoKhau.add_shk_controller;
import com.quartermanagement.Model.NhanKhau;
import com.quartermanagement.Model.SoHoKhau;
import com.quartermanagement.Utils.ViewUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.quartermanagement.Constants.DBConstants.*;
import static com.quartermanagement.Constants.FXMLConstants.ADD_SOHOKHAU_VIEW_FXML;
import static com.quartermanagement.Constants.FXMLConstants.DETAIL_NHAN_KHAU_VIEW_FXML;
import static com.quartermanagement.Utils.Utils.convertDate;
import static com.quartermanagement.Utils.Utils.createDialog;

public class AddThanhVienController {
    @FXML
    private AnchorPane basePane;
    @FXML
    private TableView<NhanKhau> tableView;
    @FXML
    private TableColumn indexColumn;
    @FXML
    private TableColumn<NhanKhau, String> hoVaTenColumn, biDanhColumn, ngaySinhColumn, cccdColumn, noiSinhColumn, gioiTinhColumn,
            nguyenQuanColumn, danTocColumn, noiThuongTruColumn, tonGiaoColumn, quocTichColumn, diaChiHienNayColumn, ngheNghiepColumn;
    private ObservableList<NhanKhau> nhanKhauList = FXCollections.observableArrayList();
    private int idHoKhau;
    private SoHoKhau soHoKhau;
    public void initTable(SoHoKhau soHoKhau) throws SQLException {
        System.out.println("initTable");
        Connection conn;
        PreparedStatement preparedStatement = null;
        conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        String SELECT_QUERY = "SELECT nhankhau.*, cccd.CCCD\n" +
                "FROM nhankhau\n" +
                "JOIN cccd\n" +
                "ON nhankhau.ID = cccd.idNhankhau\n"+
                "WHERE nhankhau.ID not in (SELECT idNhanKhau FROM thanhviencuaho)"+
                "and nhankhau.ID not in (SELECT MaChuHo FROM sohokhau)";
        preparedStatement = conn.prepareStatement(SELECT_QUERY);
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            nhanKhauList.add(new NhanKhau(result.getInt("ID"),result.getString("HoTen"), result.getString("BiDanh"),
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
        cccdColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("CCCD"));
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


    public void add(ActionEvent event) throws IOException, SQLException {
        NhanKhau selected = tableView.getSelectionModel().getSelectedItem();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nhập thông tin Quan hệ với chủ hộ");
        dialog.setHeaderText("Quan hệ với chủ hộ:");
        dialog.setContentText("Quan hệ với chủ hộ:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(quanHe -> {
            System.out.println(quanHe);
            try {
                Connection conn;
                PreparedStatement preparedStatement = null;
                conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                String INSERT_QUERY = "INSERT INTO thanhviencuaho (idNhanKhau, idHoKhau, quanHeVoiChuHo) VALUES (?,?,?)";
                preparedStatement = conn.prepareStatement(INSERT_QUERY);
                preparedStatement.setInt(1,selected.getID());
                preparedStatement.setInt(2,getIdHoKhau());
                preparedStatement.setString(3,quanHe);
                System.out.println(preparedStatement);
                int res = preparedStatement.executeUpdate();
                nhanKhauList.remove(selected);
                tableView.setItems(FXCollections.observableArrayList(nhanKhauList));
                if (res==1) createDialog(Alert.AlertType.INFORMATION, "Thông báo", "Thêm thành công!", "");
                else createDialog(Alert.AlertType.WARNING, "Thông báo", "Có lỗi, thử lại sau!", "");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }
    public void back(ActionEvent event) throws IOException, SQLException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ADD_SOHOKHAU_VIEW_FXML));
        Parent studentViewParent = loader.load();
        Scene scene = new Scene(studentViewParent);
        add_shk_controller controller= loader.getController();
        controller.setSoHoKhau(getSoHoKhau());
        controller.hide_add_btn();
        controller.setTitle("Cập nhật hộ khẩu mới");
        controller.setDisableForDetail();
        stage.setScene(scene);
    }

    public int getIdHoKhau() {
        return idHoKhau;
    }

    public void setIdHoKhau(int idHoKhau) {
        this.idHoKhau = idHoKhau;
    }

    public SoHoKhau getSoHoKhau() {
        return soHoKhau;
    }

    public void setSoHoKhau(SoHoKhau soHoKhau) {
        this.soHoKhau = soHoKhau;
    }
}
