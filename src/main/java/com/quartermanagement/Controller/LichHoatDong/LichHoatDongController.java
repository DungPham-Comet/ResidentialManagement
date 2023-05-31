package com.quartermanagement.Controller.LichHoatDong;

import com.quartermanagement.Model.LichHoatDong;
import com.quartermanagement.Model.NhanKhau;
import com.quartermanagement.Services.LichHoatDongServices;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Pagination;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.quartermanagement.Constants.DBConstants.*;
import static com.quartermanagement.Constants.DBConstants.ROWS_PER_PAGE;
import static com.quartermanagement.Constants.FXMLConstants.*;
import static com.quartermanagement.Utils.Utils.*;

public class LichHoatDongController implements Initializable {
    @FXML
    private AnchorPane basePane;
    @FXML
    private TableView<LichHoatDong> tableView;
    @FXML private TableColumn indexColumn;
    @FXML
    private TableColumn<LichHoatDong, String> tenHoatDongColumn,
            startTimeColumn, endTimeColumn, statusColumn, madeTimeColumn;
    @FXML
    private TableColumn<LichHoatDong, Integer> maHoatDongColumn;
    @FXML
    private TableColumn<LichHoatDong, String> maNguoiTaoColumn;
    @FXML
    private Pagination pagination;
    private ObservableList<LichHoatDong> lichHoatDongList = FXCollections.observableArrayList();
    private Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
    private PreparedStatement preparedStatement = null;

    public LichHoatDongController() throws SQLException {
    }

    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Connecting Database
            String SELECT_QUERY = "SELECT * FROM lichhoatdong";
            conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            preparedStatement = conn.prepareStatement(SELECT_QUERY);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                lichHoatDongList.add(new LichHoatDong(result.getInt("MaHoatDong"),result.getString("TenHoatDong"),
                        convertTime(result.getString("ThoiGianBatDau")), convertTime(result.getString("ThoiGianKetThuc")),
                        result.getString("DuocDuyet"), convertTime(result.getString("ThoiGianTao")),
                                result.getInt("MaNguoiTao")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int soDu = lichHoatDongList.size() % ROWS_PER_PAGE;
        if (soDu != 0) pagination.setPageCount(lichHoatDongList.size() / ROWS_PER_PAGE + 1);
        else pagination.setPageCount(lichHoatDongList.size() / ROWS_PER_PAGE);
        pagination.setMaxPageIndicatorCount(5);
        pagination.setPageFactory(this::createTableView);

        tableView.setRowFactory(tv -> {
            TableRow<LichHoatDong> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // Perform actions with rowData
                    try {
                        detail(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
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
        loader.setLocation(getClass().getResource(DETAIL_LICH_HOAT_DONG_VIEW_FXML));
        Parent studentViewParent = loader.load();
        Scene scene = new Scene(studentViewParent);
        LichHoatDongDetailController controller = loader.getController();
        controller.hide_update_btn();
        controller.hide_statusPane();
        controller.hide_maHoatDongPane();
        stage.setScene(scene);
    }

    public void delete() {
        LichHoatDong selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) createDialog(Alert.AlertType.WARNING,
                "Cảnh báo",
                "", "Đồng chí vui lòng chọn 1 mục để tiếp tục");
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xoá ");
            alert.setContentText("Đồng chí muốn xoá nội dung này?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    try {
                        if (selected.getStatus().equals("Chấp nhận")) {
                            LichHoatDongServices.updateCongSoLuongKhaDung(conn, selected);
                        }
                        int result = LichHoatDongServices.deleteLichHoatDong(conn, selected);
                        if (result == 1)
                            createDialog(Alert.AlertType.INFORMATION, "Xoá thành công!", "", "Quá đơn giản phải không đồng chí?");
                        else createDialog(Alert.AlertType.WARNING, "Thông báo", "", "Oops, mời đồng chí thử lại!");
                        ViewUtils viewUtils = new ViewUtils();
                        viewUtils.changeAnchorPane(basePane, LICH_HOAT_DONG_VIEW_FXML);
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
        loader.setLocation(getClass().getResource(DETAIL_LICH_HOAT_DONG_VIEW_FXML));
        Parent studentViewParent = loader.load();
        Scene scene = new Scene(studentViewParent);
        LichHoatDongDetailController controller = loader.getController();
        LichHoatDong selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) createDialog(Alert.AlertType.WARNING, "Từ từ đã đồng chí", "", "Vui lòng chọn 1 mục để tiếp tục");
        else {
            controller.setLichHoatDong(selected);
            controller.hide_add_btn();
            //controller.hide_maHoatDongPane();
            controller.setTitle("Cập nhật lịch hoat động");
            stage.setScene(scene);
        }
    }

    public Node createTableView(int pageIndex) {

        indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<LichHoatDong, LichHoatDong>, ObservableValue<LichHoatDong>>) p -> new ReadOnlyObjectWrapper(p.getValue()));
        indexColumn.setCellFactory(new Callback<TableColumn<LichHoatDong, LichHoatDong>, TableCell<LichHoatDong, LichHoatDong>>() {
            @Override
            public TableCell<LichHoatDong, LichHoatDong> call(TableColumn<LichHoatDong, LichHoatDong> param) {
                return new TableCell<LichHoatDong, LichHoatDong>() {
                    @Override
                    protected void updateItem(LichHoatDong item, boolean empty) {
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
        maHoatDongColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, Integer>("maHoatDong"));
        tenHoatDongColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("tenHoatDong"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("endTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("status"));
        madeTimeColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("madeTime"));
        maNguoiTaoColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("tenNguoiTao"));

        int lastIndex = 0;
        int displace = lichHoatDongList.size() % ROWS_PER_PAGE;
        if (displace > 0) {
            lastIndex = lichHoatDongList.size() / ROWS_PER_PAGE;
        } else {
            lastIndex = lichHoatDongList.size() / ROWS_PER_PAGE - 1;
        }
        if (lichHoatDongList.isEmpty()) tableView.setItems(FXCollections.observableArrayList(lichHoatDongList));
        else {
            if (lastIndex == pageIndex && displace > 0) {
                tableView.setItems(FXCollections.observableArrayList(lichHoatDongList.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + displace)));
            } else {
                tableView.setItems(FXCollections.observableArrayList(lichHoatDongList.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + ROWS_PER_PAGE)));
            }
        }
        return tableView;

    }
    @FXML
    private TextField searchTextField;
    public void search() {
        FilteredList<LichHoatDong> filteredData = new FilteredList<>(lichHoatDongList, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(lichHoatDong -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (lichHoatDong.getTenHoatDong().toLowerCase().contains(lowerCaseFilter)) {
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
                indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<LichHoatDong, LichHoatDong>, ObservableValue<LichHoatDong>>) p -> new ReadOnlyObjectWrapper(p.getValue()));
                indexColumn.setCellFactory(new Callback<TableColumn<LichHoatDong, LichHoatDong>, TableCell<LichHoatDong, LichHoatDong>>() {
                    @Override
                    public TableCell<LichHoatDong, LichHoatDong> call(TableColumn<LichHoatDong, LichHoatDong> param) {
                        return new TableCell<LichHoatDong, LichHoatDong>() {
                            @Override
                            protected void updateItem(LichHoatDong item, boolean empty) {
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
                maHoatDongColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, Integer>("maHoatDong"));
                tenHoatDongColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("tenHoatDong"));
                startTimeColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("startTime"));
                endTimeColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("endTime"));
                statusColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("status"));
                madeTimeColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("madeTime"));
                maNguoiTaoColumn.setCellValueFactory(new PropertyValueFactory<LichHoatDong, String>("tenNguoiTao"));

                int lastIndex = 0;
                int displace = filteredData.size() % ROWS_PER_PAGE;
                if (displace > 0) {
                    lastIndex = filteredData.size() / ROWS_PER_PAGE;
                } else {
                    lastIndex = filteredData.size() / ROWS_PER_PAGE - 1;
                }
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
