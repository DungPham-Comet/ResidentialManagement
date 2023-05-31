package com.quartermanagement.Controller.NhanKhau;

import com.quartermanagement.Services.NhanKhauServices;
import com.quartermanagement.Utils.ViewUtils;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Pagination;
import com.quartermanagement.Model.NhanKhau;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.quartermanagement.Constants.DBConstants.*;
import static com.quartermanagement.Constants.FXMLConstants.NHAN_KHAU_VIEW_FXML;
import static com.quartermanagement.Utils.Utils.convertDate;
import static com.quartermanagement.Utils.Utils.createDialog;
import static com.quartermanagement.Constants.FXMLConstants.DETAIL_NHAN_KHAU_VIEW_FXML;

public class NhanKhauController implements Initializable {
    @FXML
    private AnchorPane basePane;
    @FXML
    private TableView<NhanKhau> tableView;
    @FXML
    private TableColumn indexColumn;
    @FXML
    private TableColumn<NhanKhau, String> hoVaTenColumn, biDanhColumn, ngaySinhColumn, cccdColumn, noiSinhColumn, gioiTinhColumn,
            nguyenQuanColumn, danTocColumn, noiThuongTruColumn, tonGiaoColumn, quocTichColumn, diaChiHienNayColumn, ngheNghiepColumn;
//    @FXML
//    private TableColumn<NhanKhau, Integer> idColumn;
    @FXML
    private Pagination pagination;

    private ObservableList<NhanKhau> nhanKhauList = FXCollections.observableArrayList();
    // Connect to database
    private Connection conn;
    private PreparedStatement preparedStatement = null;
    @FXML
    private TextField searchTextField;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ResultSet result = NhanKhauServices.getAllNhanKhau();
            while (result.next()) {
                nhanKhauList.add(new NhanKhau(result.getInt("ID"),result.getString("HoTen"), result.getString("BiDanh"),
                        convertDate(result.getString("NgaySinh")), result.getString("CCCD"), result.getString("NoiSinh"),
                        result.getString("GioiTinh"), result.getString("NguyenQuan"), result.getString("DanToc"),
                        result.getString("NoiThuongTru"), result.getString("TonGiao"), result.getString("QuocTich"),
                        result.getString("DiaChiHienNay"), result.getString("NgheNghiep")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int soDu = nhanKhauList.size() % ROWS_PER_PAGE;
        if (soDu != 0) pagination.setPageCount(nhanKhauList.size() / ROWS_PER_PAGE + 1);
        else pagination.setPageCount(nhanKhauList.size() / ROWS_PER_PAGE);
        pagination.setMaxPageIndicatorCount(5);
        pagination.setPageFactory(this::createTableView);

        tableView.setRowFactory(tv -> {
            TableRow<NhanKhau> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // Perform actions with rowData
                    try {
                        detail(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row ;
        });

    }


    public void add(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(DETAIL_NHAN_KHAU_VIEW_FXML));
        Parent studentViewParent = loader.load();
        Scene scene = new Scene(studentViewParent);
        NhanKhauDetailViewController controller = loader.getController();
        controller.hide_update_btn();
        stage.setScene(scene);
    }


    public void delete() {
        NhanKhau selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) createDialog(Alert.AlertType.WARNING,
                "Cảnh báo",
                "Vui lòng chọn nhân khẩu để tiếp tục", "");
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa nhân khẩu");
            alert.setContentText("Đồng chí muốn xóa nhân khẩu này?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    // Delete in Database
                    try {
                        int ID = selected.getID();
                        int result1 = NhanKhauServices.deleteNhanKhauViaCCCD(ID);
                        int result2 = NhanKhauServices.deleteNhanKhau(ID);
                        if (result1 == 1 && result2 == 1) createDialog(Alert.AlertType.INFORMATION, "Thông báo", "Xóa thành công!", "");
                        else createDialog(Alert.AlertType.WARNING, "Thông báo", "Có lỗi, thử lại sau!", "");
                        ViewUtils viewUtils = new ViewUtils();
                        viewUtils.changeAnchorPane(basePane, NHAN_KHAU_VIEW_FXML);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }


    public void detail(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(DETAIL_NHAN_KHAU_VIEW_FXML));
        Parent studentViewParent = loader.load();
        Scene scene = new Scene(studentViewParent);
        NhanKhauDetailViewController controller = loader.getController();
        NhanKhau selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null)
            createDialog(Alert.AlertType.WARNING, "Từ từ đã đồng chí", "", "Vui lòng chọn một nhân khẩu");
        else {
            controller.setNhanKhau(selected);
            controller.setID(selected.getID());
            controller.hide_add_btn();
            controller.setTitle("Cập nhật nhân khẩu mới");
            stage.setScene(scene);
        }
    }

    public Node createTableView(int pageIndex) {

        indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<NhanKhau, NhanKhau>, ObservableValue<NhanKhau>>) p -> new ReadOnlyObjectWrapper(p.getValue()));

        indexColumn.setCellFactory(new Callback<TableColumn<NhanKhau, NhanKhau>, TableCell<NhanKhau, NhanKhau>>() {
            @Override
            public TableCell<NhanKhau, NhanKhau> call(TableColumn<NhanKhau, NhanKhau> param) {
                return new TableCell<NhanKhau, NhanKhau>() {
                    @Override
                    protected void updateItem(NhanKhau item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex() + 1 + pageIndex * ROWS_PER_PAGE + "");
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
        int lastIndex = 0;
        int displace = nhanKhauList.size() % ROWS_PER_PAGE;
        if (displace > 0) {
            lastIndex = nhanKhauList.size() / ROWS_PER_PAGE;
        } else {
            lastIndex = nhanKhauList.size() / ROWS_PER_PAGE - 1;
        }
        // Add nhankhau to table
        if (nhanKhauList.isEmpty()) tableView.setItems(FXCollections.observableArrayList(nhanKhauList));
        else {
            if (lastIndex == pageIndex && displace > 0) {
                tableView.setItems(FXCollections.observableArrayList(nhanKhauList.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + displace)));
            } else {
                tableView.setItems(FXCollections.observableArrayList(nhanKhauList.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + ROWS_PER_PAGE)));
            }
        }
        return tableView;
    }

    public void search() {
        FilteredList<NhanKhau> filteredData = new FilteredList<>(nhanKhauList, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = searchTextField.getText().toLowerCase();
                if (person.getHoTen().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
            int soDu = filteredData.size() % ROWS_PER_PAGE;
            if (soDu != 0) pagination.setPageCount(filteredData.size() / ROWS_PER_PAGE + 1);
            else pagination.setPageCount(filteredData.size() / ROWS_PER_PAGE);
            pagination.setMaxPageIndicatorCount(5);
            pagination.setPageFactory(pageIndex->{
                indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<NhanKhau, NhanKhau>, ObservableValue<NhanKhau>>) p -> new ReadOnlyObjectWrapper(p.getValue()));

                indexColumn.setCellFactory(new Callback<TableColumn<NhanKhau, NhanKhau>, TableCell<NhanKhau, NhanKhau>>() {
                    @Override
                    public TableCell<NhanKhau, NhanKhau> call(TableColumn<NhanKhau, NhanKhau> param) {
                        return new TableCell<NhanKhau, NhanKhau>() {
                            @Override
                            protected void updateItem(NhanKhau item, boolean empty) {
                                super.updateItem(item, empty);

                                if (this.getTableRow() != null && item != null) {
                                    setText(this.getTableRow().getIndex() + 1 + pageIndex * ROWS_PER_PAGE + "");
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
                int lastIndex = 0;
                int displace = filteredData.size() % ROWS_PER_PAGE;
                if (displace > 0) {
                    lastIndex = filteredData.size() / ROWS_PER_PAGE;
                } else {
                    lastIndex = filteredData.size() / ROWS_PER_PAGE - 1;
                }
                // Add nhankhau to table
                if (filteredData.isEmpty()) tableView.setItems(FXCollections.observableArrayList(filteredData));
                else {
                    if (lastIndex == pageIndex && displace > 0) {
                        tableView.setItems(FXCollections.observableArrayList(filteredData.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + displace)));
                    } else {
                        tableView.setItems(FXCollections.observableArrayList(filteredData.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + ROWS_PER_PAGE)));
                    }
                }
                return tableView;
            });
        });


    }


}

