module comz.quartermanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;
	requires javafx.base;
	requires javafx.graphics;

    opens com.quartermanagement to javafx.fxml;
    exports com.quartermanagement;
    exports com.quartermanagement.Model;
    opens com.quartermanagement.Model to javafx.fxml;
    exports com.quartermanagement.Controller;
    opens com.quartermanagement.Controller to javafx.fxml;
    exports com.quartermanagement.Controller.NhanKhau;
    opens com.quartermanagement.Controller.NhanKhau to javafx.fxml;
    exports com.quartermanagement.Controller.SoHoKhau;
    opens com.quartermanagement.Controller.SoHoKhau to javafx.fxml;
    exports com.quartermanagement.Constants;
    opens com.quartermanagement.Constants to javafx.fxml;
    exports com.quartermanagement.Controller.CoSoVatChat;
    opens com.quartermanagement.Controller.CoSoVatChat to javafx.fxml;
    exports com.quartermanagement.Utils;
    opens com.quartermanagement.Utils to javafx.fxml;
    exports com.quartermanagement.Controller.LichHoatDong;
    opens com.quartermanagement.Controller.LichHoatDong to javafx.fxml;
}