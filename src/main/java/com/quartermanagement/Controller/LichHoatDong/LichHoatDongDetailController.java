package com.quartermanagement.Controller.LichHoatDong;

import com.quartermanagement.Controller.AddCSVCController;
import com.quartermanagement.Controller.AddThanhVienController;
import com.quartermanagement.Model.LichHoatDong;
import com.quartermanagement.Model.NhanKhau;
import com.quartermanagement.Controller.NhanKhau.NhanKhauController;
import com.quartermanagement.Model.SoHoKhau;
import com.quartermanagement.Services.LichHoatDongServices;
import com.quartermanagement.Services.NhanKhauServices;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import static com.quartermanagement.Constants.DBConstants.*;
import static com.quartermanagement.Utils.Utils.*;
import static com.quartermanagement.Controller.AdminController.userRole;

public class LichHoatDongDetailController implements Initializable {
    @FXML
    private Button add_btn, update_btn;
    @FXML
    private Button doiNguoiTaoBtn, addCSVCBtn;
    @FXML
    private TextField endTimeTextField, startTimeTextField, maHoatDongTextField, tenHoatDongTextField, nguoiTaoTextField;
    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private Pane maHoatDongPane;
    @FXML
    private Pane statusPane;
    @FXML
    private Text title;
    private LichHoatDong lichHoatDong;

    @FXML
    private TableView<NhanKhau> tableView;
    @FXML
    private TableColumn indexColumn;
    @FXML
    private TableColumn<NhanKhau, String> hoVaTenColumn, biDanhColumn, ngaySinhColumn, cccdColumn, noiSinhColumn, gioiTinhColumn,
            nguyenQuanColumn, danTocColumn, noiThuongTruColumn, tonGiaoColumn, quocTichColumn, diaChiHienNayColumn, ngheNghiepColumn;
    @FXML
    private Pagination pagination;
    private ObservableList<NhanKhau> nhanKhauList = FXCollections.observableArrayList();
    // Connect to database
    private Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
    private PreparedStatement preparedStatement = null;
    private String pre_status = "";
    public LichHoatDongDetailController() throws SQLException {
    }

    public void setLichHoatDong(LichHoatDong lichHoatDong) throws SQLException {
        this.lichHoatDong = lichHoatDong;
        System.out.println(lichHoatDong.getMaHoatDong());
        pre_status = lichHoatDong.getStatus();
        System.out.println(pre_status);
        maHoatDongTextField.setText(String.valueOf(lichHoatDong.getMaHoatDong()));
        tenHoatDongTextField.setText(lichHoatDong.getTenHoatDong());
        String startTime = lichHoatDong.getStartTime();System.out.println(startTime);
        String [] starttime = startTime.split(" ");
        startDatePicker.setValue(LOCAL_DATE(starttime[1]));
        startTimeTextField.setText(starttime[0].substring(0,8));
        String endTime = lichHoatDong.getEndTime();
        String[] endtime = endTime.split(" ");
        endDatePicker.setValue(LOCAL_DATE(endtime[1]));
        endTimeTextField.setText(endtime[0].substring(0,8));
        statusChoiceBox.setValue(String.valueOf(lichHoatDong.getStatus()));
        nguoiTaoTextField.setText(String.valueOf(LichHoatDongServices.getNamebyID(conn, lichHoatDong.getMaNguoiTao())));
    }

    public void goBack(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        viewUtils.switchToLichHoatDong_Admin_view(event);
    }

    public void update(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        String maHoatDong = maHoatDongTextField.getText();
        String tenHoatDong = tenHoatDongTextField.getText();
        String startTime = startTimeTextField.getText();
        String endTime = endTimeTextField.getText();
        String status = statusChoiceBox.getValue();
        String maNguoiTao = String.valueOf(lichHoatDong.getMaNguoiTao());
        NhanKhau selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            maNguoiTao = String.valueOf(selected.getID());
        }
        if (maHoatDong.trim().equals("") || tenHoatDong.trim().equals("") || startTime.trim().equals("") || endTime.trim().equals("")
                || startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            createDialog(
                    Alert.AlertType.WARNING,
                    "Đồng chí giữ bình tĩnh",
                    "", "Vui lòng nhập đủ thông tin!"
            );
        } else {
            String startDateTime = startDatePicker.getValue().toString();
            String starttime = startDateTime + " " + startTime;
            String endDateTime = endDatePicker.getValue().toString();
            String endtime = endDateTime + " " + endTime;


            if (!isValidTime(startTime) || !isValidTime(endTime)) {
                createDialog(Alert.AlertType.WARNING, "Từ từ thôi đồng chí!", "Hãy chọn đúng định dạng hh:mm:ss", "");
            } else if (!greaterTime(startDateTime, startTime, endDateTime, endTime)) {
                createDialog(Alert.AlertType.WARNING, "Từ từ thôi đồng chí!", "Thời gian kết thúc phải lớn hơn thời gian bắt đầu!", "");
            } else {
                try {
                    Connection conn;
                    PreparedStatement preparedStatement;
                    conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);

                    int result = 0;

                    System.out.println(pre_status + "|||" + status);

                    if (pre_status.equals("Chưa duyệt") && status.equals("Chấp nhận")) {
                        boolean check = true;
                        ResultSet rs = LichHoatDongServices.getCheck(conn, lichHoatDong);
                        while (rs.next()) {
                            if (rs.getInt(1) > rs.getInt(2)) check = false;
                        }

                        if (check) {
                            result = LichHoatDongServices.updateLichHoatDong(conn, maHoatDong, tenHoatDong, starttime, endtime, status, maNguoiTao);
                            LichHoatDongServices.updateTruSoLuongKhaDung(conn, lichHoatDong);
                            if (result == 1) {
                                createDialog(
                                        Alert.AlertType.CONFIRMATION,
                                        "Thành công",
                                        "", "Đồng chí vất vả rồi!"
                                );
                                viewUtils.switchToLichHoatDong_Admin_view(event);
                            }
                        }
                        else {
                            createDialog(
                                    Alert.AlertType.ERROR,
                                    "Thất bại",
                                    "", "Có vẻ như hoạt động này yêu cầu nhiều hơn số lượng khả dụng hiện có"
                            );
                        }
                    }

                    else if (pre_status.equals("Chấp nhận") && status.equals("Chưa duyệt")) {
                        result = LichHoatDongServices.updateLichHoatDong(conn, maHoatDong, tenHoatDong, starttime, endtime, status, maNguoiTao);
                        LichHoatDongServices.updateCongSoLuongKhaDung(conn, lichHoatDong);
                        if (result == 1) {
                            createDialog(
                                    Alert.AlertType.CONFIRMATION,
                                    "Thành công",
                                    "", "Đồng chí vất vả rồi!"
                            );
                            viewUtils.switchToLichHoatDong_Admin_view(event);
                        } else {
                            createDialog(
                                    Alert.AlertType.ERROR,
                                    "Thất bại",
                                    "", "Thất bại là mẹ thành công! Mong đồng chí thử lại"
                            );
                        }

                    }
                    else {
                        result = LichHoatDongServices.updateLichHoatDong(conn, maHoatDong, tenHoatDong, starttime, endtime, status, maNguoiTao);
                        if (result == 1) {
                            createDialog(
                                    Alert.AlertType.CONFIRMATION,
                                    "Thành công",
                                    "", "Đồng chí vất vả rồi!"
                            );
                            viewUtils.switchToLichHoatDong_Admin_view(event);
                        } else {
                            createDialog(
                                    Alert.AlertType.ERROR,
                                    "Thất bại",
                                    "", "Thất bại là mẹ thành công! Mong đồng chí thử lại"
                            );
                        }


                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void addnew(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        String maHoatDong;
        String tenHoatDong = tenHoatDongTextField.getText();
        String startTime = startTimeTextField.getText();
        String endTime = endTimeTextField.getText();
        String status = "Chưa duyệt";
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String thoiGianTao = dtf.format(currentTime);

        NhanKhau selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) createDialog(Alert.AlertType.WARNING, "Từ từ đã đồng chí", "", "Vui lòng chọn nhân khẩu");
        if (tenHoatDong.trim().equals("") || startTime.trim().equals("") || endTime.trim().equals("")
                || startDatePicker.getValue() == null || endDatePicker.getValue() == null) {

            createDialog(
                    Alert.AlertType.WARNING,
                    "Đồng chí giữ bình tĩnh",
                    "", "Vui lòng nhập đủ thông tin!"
            );
        } else {
            String startDateTime = startDatePicker.getValue().toString();
            String starttime = startDateTime + " " + startTime;
            String endDateTime = endDatePicker.getValue().toString();
            String endtime = endDateTime + " " + endTime;

            if (!isValidTime(startTime) || !isValidTime(endTime)) {
                createDialog(Alert.AlertType.WARNING, "Từ từ thôi đồng chí!", "Hãy chọn đúng định dạng hh:mm:ss", "");
            } else if (!greaterTime(startDateTime, startTime, endDateTime, endTime)) {
                createDialog(Alert.AlertType.WARNING, "Từ từ thôi đồng chí!", "Thời gian kết thúc phải lớn hơn thời gian bắt đầu!", "");
            } else {

                try {
                    Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                    PreparedStatement preparedStatement;
                    ResultSet rs;
                    do {
                        int rand = ThreadLocalRandom.current().nextInt(100000, 999999);
                        maHoatDong = String.valueOf(rand);
                        PreparedStatement check = conn.prepareStatement("SELECT MaHoatDong From lichhoatdong WHERE `MaHoatDong` =?");
                        check.setInt(1, rand);
                        rs = check.executeQuery();
                    } while (rs.next());

                    int result = LichHoatDongServices.insertLichHoatDong(conn, maHoatDong, tenHoatDong, starttime, endtime, status, thoiGianTao, selected);
                    if (result == 1) {
                        createDialog(
                                Alert.AlertType.CONFIRMATION,
                                "Thành công",
                                "", "Đồng chí vất vả rồi!"
                        );
                        // we will do somthing here
                        lichHoatDong = new LichHoatDong(Integer.valueOf(maHoatDong), tenHoatDong, convertDateWhenAddLichHD(starttime), convertDateWhenAddLichHD(endtime), status, thoiGianTao, selected.getID());
                        System.out.println(maHoatDong);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Hãy đưa ra lựa chọn");
                        alert.setHeaderText("Đồng chí có muốn chọn đồ dùng muốn mượn luôn không?");
                        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                        Optional<ButtonType> ketqua = alert.showAndWait();
                        if (ketqua.get() == ButtonType.YES) {
                            // Code for Option 1
                            addCSVC(event);
                        } else {
                            viewUtils.switchToSoHoKhau_Admin_view(event);
                        }
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
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                viewUtils.switchToLichHoatDong_Admin_view(event);

            }
        }
    }

    public void hide_add_btn() {
        add_btn.setVisible(false);
        tableView.setVisible(false);
    }


    public void hide_update_btn() {
        update_btn.setVisible(false);
        add_btn.setTranslateX(100);
        nguoiTaoTextField.setVisible(false);
        doiNguoiTaoBtn.setVisible(false);
        addCSVCBtn.setVisible(false);
    }

    public void hide_maHoatDongPane() {
        maHoatDongPane.setVisible(false);
    }

    public void hide_statusPane() {
        statusPane.setVisible(false);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusChoiceBox.getItems().add("Chưa duyệt");
        statusChoiceBox.getItems().add("Chấp nhận");
        statusChoiceBox.setValue("Chưa duyệt");
        statusPane.setVisible(userRole.equals("totruong"));

        try {
            ResultSet result = NhanKhauServices.getAllNhanKhau();
            while (result.next()) {
                nhanKhauList.add(new NhanKhau(result.getInt("ID"), result.getString("HoTen"), result.getString("BiDanh"),
                        convertDate(result.getString("NgaySinh")), result.getString("CCCD"), result.getString("NoiSinh"),
                        result.getString("GioiTinh"), result.getString("NguyenQuan"), result.getString("DanToc"),
                        result.getString("NoiThuongTru"), result.getString("TonGiao"), result.getString("QuocTich"),
                        result.getString("DiaChiHienNay"), result.getString("NgheNghiep")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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


    public void doiNguoiTao() {
        tableView.setVisible(true);
        doiNguoiTaoBtn.setVisible(false);
        nguoiTaoTextField.setTranslateX(-200);
        nguoiTaoTextField.setTranslateY(45);
    }
    public void addCSVC(ActionEvent event) throws IOException, SQLException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/quartermanagement/views/add-CSVC.fxml"));
        Parent studentViewParent = loader.load();
        Scene scene = new Scene(studentViewParent);
        AddCSVCController controller = loader.getController();
        controller.init(lichHoatDong);
        stage.setScene(scene);
    }
}
