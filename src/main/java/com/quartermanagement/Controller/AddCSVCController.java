package com.quartermanagement.Controller;

import com.quartermanagement.Controller.LichHoatDong.LichHoatDongDetailController;
import com.quartermanagement.Model.CoSoVatChat;
import com.quartermanagement.Model.CoSoVatChatHienCo;
import com.quartermanagement.Model.LichHoatDong;
import com.quartermanagement.Model.NhanKhau;
import com.quartermanagement.Services.CoSoVatChatServices;
import com.quartermanagement.Services.LichHoatDongServices;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.quartermanagement.Constants.DBConstants.*;
import static com.quartermanagement.Constants.FXMLConstants.DETAIL_LICH_HOAT_DONG_VIEW_FXML;
import static com.quartermanagement.Utils.Utils.createDialog;

public class AddCSVCController implements Initializable {

    public AddCSVCController() throws SQLException {
    }

    private Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
    @FXML
    private TableView<CoSoVatChatHienCo> coSoVatChatHienCoTableView;
    @FXML
    private TableView<CoSoVatChat> tableView;
    @FXML
    private Label tenHoatDong;
    @FXML
    TableColumn indexColumn;
    @FXML
    TableColumn<CoSoVatChat, Integer> maDoDungColumn;
    @FXML
    private TableColumn<CoSoVatChat, String> tenDoDungColumn1;
    @FXML
    private TableColumn<CoSoVatChat, Integer> soLuongColumn1;
    @FXML
    private TableColumn<CoSoVatChat, Integer> soLuongKhaDungColumn;

    @FXML
    private TableColumn<CoSoVatChatHienCo, String> tenDoDungColumn;
    @FXML
    private TableColumn<CoSoVatChatHienCo, Integer> soLuongColumn;
    private ObservableList<CoSoVatChatHienCo> coSoVatChatHienCoList = FXCollections.observableArrayList();;
    private ObservableList<CoSoVatChat> coSoVatChatList = FXCollections.observableArrayList();;

    private LichHoatDong lichHoatDong;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        try {
            conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            ResultSet result = CoSoVatChatServices.getAllFacility(conn);
            while (result.next()) {
                coSoVatChatList.add(new CoSoVatChat(result.getInt("maDoDung"), result.getString("tenDoDung"),
                        result.getInt("soLuong"), result.getInt("soLuongKhaDung")));
            }
            indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<CoSoVatChat, CoSoVatChat>, ObservableValue<CoSoVatChat>>) p -> new ReadOnlyObjectWrapper(p.getValue()));
            indexColumn.setCellFactory(new Callback<TableColumn<CoSoVatChat, CoSoVatChat>, TableCell<CoSoVatChat, CoSoVatChat>>() {
                @Override
                public TableCell<CoSoVatChat, CoSoVatChat> call(TableColumn<CoSoVatChat, CoSoVatChat> param) {
                    return new TableCell<CoSoVatChat, CoSoVatChat>() {
                        @Override
                        protected void updateItem(CoSoVatChat item, boolean empty) {
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
            maDoDungColumn.setCellValueFactory(new PropertyValueFactory<CoSoVatChat, Integer>("maDoDung"));
            tenDoDungColumn1.setCellValueFactory((new PropertyValueFactory<CoSoVatChat, String>("tenDoDung")));
            soLuongColumn1.setCellValueFactory(new PropertyValueFactory<CoSoVatChat, Integer>("soLuong"));
            soLuongKhaDungColumn.setCellValueFactory(new PropertyValueFactory<CoSoVatChat, Integer>("soLuongKhaDung"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(FXCollections.observableArrayList(coSoVatChatList));
        tableView.setRowFactory(tv -> {
            TableRow<CoSoVatChat> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    CoSoVatChat selected = tableView.getSelectionModel().getSelectedItem();
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Nhập số lượng: ");
                    dialog.setHeaderText(selected.getTenDoDung());
                    dialog.setContentText("Số lượng:");
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(soLuong -> {
                        System.out.println(soLuong);
                        try {
                            Connection conn;
                            PreparedStatement preparedStatement = null;
                            conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                            String INSERT_QUERY = "INSERT INTO hoatdong_cosovatchat VALUES " +
                                    "(?,?,?)";
                            preparedStatement = conn.prepareStatement(INSERT_QUERY);
                            preparedStatement.setInt(1,lichHoatDong.getMaHoatDong());
                            preparedStatement.setInt(2,selected.getMaDoDung());
                            preparedStatement.setInt(3,Integer.valueOf(soLuong));
                            System.out.println(preparedStatement);
                            int res = preparedStatement.executeUpdate();
                            coSoVatChatHienCoList.add(new CoSoVatChatHienCo(selected.getTenDoDung(), Integer.valueOf(soLuong)));
                            tableView.setItems(FXCollections.observableArrayList(coSoVatChatList));
                            coSoVatChatHienCoTableView.setItems(FXCollections.observableArrayList(coSoVatChatHienCoList));
                            if (res==1) createDialog(Alert.AlertType.INFORMATION, "Thông báo", "Thêm thành công!", "");
                            else createDialog(Alert.AlertType.WARNING, "Thông báo", "Có lỗi, thử lại sau!", "");
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
            return row;
        });
    }
    public void init(LichHoatDong lichHoatDong) throws SQLException {
        this.lichHoatDong = lichHoatDong;
        tenHoatDong.setText(lichHoatDong.getTenHoatDong());
        System.out.println(lichHoatDong.getMaHoatDong());
        ResultSet rs = LichHoatDongServices.getCoSoVatChatFromLichHoatDong(conn,lichHoatDong);
        while (rs.next()) {
            coSoVatChatHienCoList.add(new CoSoVatChatHienCo(rs.getString(1), rs.getInt(2)));
        }
        tenDoDungColumn.setCellValueFactory(new PropertyValueFactory<CoSoVatChatHienCo, String>("TenDoDung"));
        soLuongColumn.setCellValueFactory(new PropertyValueFactory<CoSoVatChatHienCo,Integer>("SoLuong"));
        coSoVatChatHienCoTableView.setItems(FXCollections.observableArrayList(coSoVatChatHienCoList));

        coSoVatChatHienCoTableView.setRowFactory(tv -> {
            TableRow<CoSoVatChatHienCo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // Perform actions with rowData
                    CoSoVatChatHienCo selected = coSoVatChatHienCoTableView.getSelectionModel().getSelectedItem();
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Nhập số lượng cập nhật: ");
                    dialog.setHeaderText(selected.getTenDoDung());
                    dialog.setContentText("Số lượng cập nhật:");
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(soLuong -> {
                        System.out.println(soLuong);
                        try {
                            Connection conn;
                            PreparedStatement preparedStatement = null;
                            conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                            String UPDATE_QUERY ="UPDATE hoatdong_cosovatchat SET SoLuong = ? WHERE MaDoDung IN (SELECT MaDoDung FROM cosovatchat csvc WHERE csvc.TenDoDung = ?)";
                            preparedStatement = conn.prepareStatement(UPDATE_QUERY);
                            preparedStatement.setInt(1,Integer.valueOf(soLuong));
                            preparedStatement.setString(2,selected.getTenDoDung());
                            System.out.println(preparedStatement);
                            int res = preparedStatement.executeUpdate();
                            coSoVatChatHienCoList.remove(selected);
                            coSoVatChatHienCoList.add(new CoSoVatChatHienCo(selected.getTenDoDung(), Integer.valueOf(soLuong)));
                            coSoVatChatHienCoTableView.setItems(FXCollections.observableArrayList(coSoVatChatHienCoList));
                            if (res==1) createDialog(Alert.AlertType.INFORMATION, "Thông báo", "Cập nhật thành công!", "");
                            else createDialog(Alert.AlertType.WARNING, "Thông báo", "Có lỗi, thử lại sau!", "");
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
            return row;
        });
    }

    public void delete(ActionEvent event) throws SQLException {
        CoSoVatChatHienCo selected = coSoVatChatHienCoTableView.getSelectionModel().getSelectedItem();
        conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        String DELETE_QUERY ="DELETE FROM hoatdong_cosovatchat WHERE MaDoDung IN (SELECT MaDoDung FROM cosovatchat csvc WHERE csvc.TenDoDung = ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
        preparedStatement.setString(1, selected.getTenDoDung());
        System.out.println(preparedStatement);
        coSoVatChatHienCoList.remove(selected);
        coSoVatChatHienCoTableView.setItems(coSoVatChatHienCoList);
        preparedStatement.executeUpdate();
    }
    public void back(ActionEvent event) throws IOException, SQLException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(DETAIL_LICH_HOAT_DONG_VIEW_FXML));
        Parent studentViewParent = loader.load();
        Scene scene = new Scene(studentViewParent);
        LichHoatDongDetailController controller = loader.getController();
        System.out.println(lichHoatDong.getMaHoatDong());
        controller.setLichHoatDong(lichHoatDong);
        controller.hide_add_btn();
        controller.hide_maHoatDongPane();
        controller.setTitle("Cập nhật lịch hoat động");
        stage.setScene(scene);

    }
}
