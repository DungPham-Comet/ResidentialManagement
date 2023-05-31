package com.quartermanagement.Services;

import com.quartermanagement.Model.CoSoVatChat;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static com.quartermanagement.Constants.DBConstants.*;

public class CoSoVatChatServices {
    public static ResultSet getAllFacility(Connection conn) throws SQLException {
        String SELECT_QUERY = "SELECT * FROM cosovatchat";
        PreparedStatement preparedStatement = conn.prepareStatement(SELECT_QUERY);
        return preparedStatement.executeQuery();
    }

    public static int deleteFacility(Connection conn, CoSoVatChat selected) throws SQLException {
        String DELETE_QUERY = "DELETE FROM cosovatchat WHERE `MaDoDung`= ?";
        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
        preparedStatement.setString(1, String.valueOf(selected.getMaDoDung()));
        return preparedStatement.executeUpdate();
    }

    public static int addFacility(Connection conn, String maDoDung, String tenDoDung, String soLuong, String soLuongKhaDung) throws SQLException {
        String INSERT_QUERY = "INSERT INTO cosovatchat VALUES(?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement((INSERT_QUERY));
        preparedStatement.setString(1, maDoDung);
        preparedStatement.setString(2, tenDoDung);
        preparedStatement.setString(3, soLuong);
        preparedStatement.setString(4, soLuongKhaDung);
        return preparedStatement.executeUpdate();
    }

    public static int updateFacility(Connection conn, String maDoDung, String tenDoDung, String soLuong, String soLuongKhaDung) throws SQLException {
        String UPDATE_QUERY = "UPDATE cosovatchat SET `MaDoDung`=?, `TenDoDung`=?, `SoLuong`=?, `SoLuongKhaDung`=? WHERE `MaDoDung`=?";
        PreparedStatement preparedStatement = conn.prepareStatement((UPDATE_QUERY));
        preparedStatement.setString(1, maDoDung);
        preparedStatement.setString(2, tenDoDung);
        preparedStatement.setString(3, soLuong);
        preparedStatement.setString(4, soLuongKhaDung);
        preparedStatement.setString(5, maDoDung);
        return preparedStatement.executeUpdate();
    }

    public static Map<String, Integer> getLeastFiveFacility() {
        Map<String, Integer> result = new HashMap<>();
        String GET_QUERY = "SELECT TenDoDung, SoLuongKhaDung FROM `cosovatchat` ORDER BY SoLuongKhaDung ASC LIMIT 5";
        try {
            Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = conn.prepareStatement(GET_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.put(resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
