package com.quartermanagement.Controller.SoHoKhau;

import com.quartermanagement.Model.SoHoKhau;
import com.quartermanagement.Services.SoHoKhauServices;
import com.quartermanagement.Utils.ViewUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import static com.quartermanagement.Constants.FXMLConstants.*;
import static com.quartermanagement.Utils.Utils.createDialog;


public class SoHoKhauController implements Initializable {
    @FXML
    private AnchorPane basePane;
    @FXML
    private TableView<SoHoKhau> tableView;
    @FXML
    private TableColumn indexColumn;
    @FXML
    private TableColumn<SoHoKhau, String> tenChuHoColumn;

    @FXML
    private TableColumn<SoHoKhau, String> diaChiColumn;
    @FXML
    private TableColumn<SoHoKhau, Integer> maHoKhauColumn;
    @FXML
    private TableColumn<SoHoKhau, Integer> soLuongColumn;

    @FXML
    private Pagination pagination;

    private ObservableList<SoHoKhau> SoHoKhauList = FXCollections.observableArrayList();
    // Connect to database
    private Connection conn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            ResultSet result = SoHoKhauServices.getAllSoHoKhau(conn);
            // Loop the list of sohokhau
            while (result.next()) {
                SoHoKhauList.add(new SoHoKhau(result.getString("HoTen"), result.getString("DiaChi"), result.getString("MaHoKhau"), result.getInt("SoLuong")));
            }
            // Add sohokhau to table
            tableView.setItems(SoHoKhauList);
            conn.close();
        } catch (SQLException ignored) {
        }

        int soDu = SoHoKhauList.size() % ROWS_PER_PAGE;
        if (soDu != 0) pagination.setPageCount(SoHoKhauList.size() / ROWS_PER_PAGE + 1);
        else pagination.setPageCount(SoHoKhauList.size() / ROWS_PER_PAGE);
        pagination.setMaxPageIndicatorCount(5);
        pagination.setPageFactory(this::createTableView);

        tableView.setRowFactory(tv -> {
            TableRow<SoHoKhau> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // Perform actions with rowData
                    try {
                        detail(event);
                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });
    }


    public void add(ActionEvent event) throws IOException, SQLException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ADD_SOHOKHAU_VIEW_FXML));
        Parent studentViewParent = loader.load();
        Scene scene = new Scene(studentViewParent);
        add_shk_controller controller = loader.getController();
        controller.hide_update_btn();
        controller.khoiTaoBangChuHo();
        controller.setDisableForAdd();
        stage.setScene(scene);
    }

    public void delete(ActionEvent event) {
        SoHoKhau selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null)
            createDialog(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn hộ khẩu để tiếp tục", "");
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa hộ khẩu");
            alert.setContentText("Đồng chí muốn xóa hộ khẩu này?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    SoHoKhauList.remove(selected);
                    try {
                        int result = SoHoKhauServices.deleteSoHoKhau(conn, selected);
                        if (result == 1) createDialog(Alert.AlertType.INFORMATION, "Thông báo", "Xóa thành công!", "");
                        else createDialog(Alert.AlertType.WARNING, "Thông báo", "Có lỗi, thử lại sau!", "");
                        ViewUtils viewUtils = new ViewUtils();
                        viewUtils.changeAnchorPane(basePane, SO_HO_KHAU_VIEW_FXML);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void detail(MouseEvent event) throws IOException, SQLException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ADD_SOHOKHAU_VIEW_FXML));
        Parent studentViewParent = loader.load();
        Scene scene = new Scene(studentViewParent);
        add_shk_controller controller = loader.getController();
        SoHoKhau selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) createDialog(Alert.AlertType.WARNING, "Từ từ đã đồng chí", "", "Vui lòng chọn hộ khẩu");
        else {
            controller.setSoHoKhau(selected);
            controller.hide_add_btn();
            controller.setTitle("Cập nhật hộ khẩu mới");
            controller.setDisableForDetail();
            stage.setScene(scene);
        }

    }

    public Node createTableView(int pageIndex) {
        indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<SoHoKhau, SoHoKhau>, ObservableValue<SoHoKhau>>) p -> new ReadOnlyObjectWrapper(p.getValue()));

        indexColumn.setCellFactory(new Callback<TableColumn<SoHoKhau, SoHoKhau>, TableCell<SoHoKhau, SoHoKhau>>() {
            @Override
            public TableCell<SoHoKhau, SoHoKhau> call(TableColumn<SoHoKhau, SoHoKhau> param) {
                return new TableCell<SoHoKhau, SoHoKhau>() {
                    @Override
                    protected void updateItem(SoHoKhau item, boolean empty) {
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

        tenChuHoColumn.setCellValueFactory(new PropertyValueFactory<SoHoKhau, String>("tenChuHo"));

        diaChiColumn.setCellValueFactory(new PropertyValueFactory<SoHoKhau, String>("DiaChi"));

        maHoKhauColumn.setCellValueFactory(new PropertyValueFactory<SoHoKhau, Integer>("MaHoKhau"));

        soLuongColumn.setCellValueFactory(new PropertyValueFactory<SoHoKhau, Integer>("soLuongThanhVien"));

        int lastIndex = 0;
        int displace = SoHoKhauList.size() % ROWS_PER_PAGE;
        if (displace > 0) {
            lastIndex = SoHoKhauList.size() / ROWS_PER_PAGE;
        } else {
            lastIndex = SoHoKhauList.size() / ROWS_PER_PAGE - 1;
        }
        if (lastIndex == pageIndex && displace > 0) {
            tableView.setItems(FXCollections.observableArrayList(SoHoKhauList.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + displace)));
        } else {
            tableView.setItems(FXCollections.observableArrayList(SoHoKhauList.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + ROWS_PER_PAGE)));
        }
        return tableView;

    }

    @FXML
    private TextField searchTextField;

    public void search() {
        FilteredList<SoHoKhau> filteredData = new FilteredList<>(SoHoKhauList, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(soHoKhau -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (soHoKhau.getDiaChi().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
            int soDu = filteredData.size() % ROWS_PER_PAGE;
            if (soDu != 0) pagination.setPageCount(filteredData.size() / ROWS_PER_PAGE + 1);
            else pagination.setPageCount(filteredData.size() / ROWS_PER_PAGE);
            pagination.setMaxPageIndicatorCount(5);
            pagination.setPageFactory(pageIndex -> {
                indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<SoHoKhau, SoHoKhau>, ObservableValue<SoHoKhau>>) p -> new ReadOnlyObjectWrapper(p.getValue()));

                indexColumn.setCellFactory(new Callback<TableColumn<SoHoKhau, SoHoKhau>, TableCell<SoHoKhau, SoHoKhau>>() {
                    @Override
                    public TableCell<SoHoKhau, SoHoKhau> call(TableColumn<SoHoKhau, SoHoKhau> param) {
                        return new TableCell<SoHoKhau, SoHoKhau>() {
                            @Override
                            protected void updateItem(SoHoKhau item, boolean empty) {
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

                tenChuHoColumn.setCellValueFactory(new PropertyValueFactory<SoHoKhau, String>("tenChuHo"));

                diaChiColumn.setCellValueFactory(new PropertyValueFactory<SoHoKhau, String>("DiaChi"));

                maHoKhauColumn.setCellValueFactory(new PropertyValueFactory<SoHoKhau, Integer>("MaHoKhau"));

                soLuongColumn.setCellValueFactory(new PropertyValueFactory<SoHoKhau, Integer>("soLuongThanhVien"));


                int lastIndex = 0;
                int displace = filteredData.size() % ROWS_PER_PAGE;
                if (displace > 0) {
                    lastIndex = filteredData.size() / ROWS_PER_PAGE;
                } else {
                    lastIndex = filteredData.size() / ROWS_PER_PAGE - 1;
                }
                if (lastIndex == pageIndex && displace > 0) {
                    tableView.setItems(FXCollections.observableArrayList(filteredData.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + displace)));
                } else {
                    tableView.setItems(FXCollections.observableArrayList(filteredData.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + ROWS_PER_PAGE)));
                }
                return tableView;
            });
        });
    }
}


