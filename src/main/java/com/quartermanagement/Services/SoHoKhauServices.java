package com.quartermanagement.Services;

import com.quartermanagement.Model.NhanKhau;
import com.quartermanagement.Model.SoHoKhau;
import com.quartermanagement.Model.ThanhVien;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.quartermanagement.Constants.DBConstants.*;

public class SoHoKhauServices {
    public static ResultSet getSoHoKhauViaMaHoKhau(Connection conn, SoHoKhau soHoKhau) throws SQLException {
        String query = "SELECT * FROM sohokhau, nhankhau, cccd where sohokhau.MaChuHo = nhankhau.ID and nhankhau.id = cccd.idNhankhau and sohokhau.MaHoKhau = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, soHoKhau.getMaHoKhau());
        return preparedStatement.executeQuery();
    }

    public static ResultSet getAllSoHoKhau(Connection conn) throws SQLException {
        String SELECT_QUERY = "select nhankhau.HoTen,sohokhau.DiaChi,sohokhau.MaHoKhau, count(thanhviencuaho.idNhanKhau)+1 as 'SoLuong' from sohokhau\n" +
                "left join thanhviencuaho on thanhviencuaho.idHoKhau = sohokhau.ID\n" +
                "inner join nhankhau on sohokhau.MaChuHo = nhankhau.ID\n" +
                "group by nhankhau.HoTen,sohokhau.DiaChi,sohokhau.MaHoKhau;";
        PreparedStatement preparedStatement = conn.prepareStatement(SELECT_QUERY);
        return preparedStatement.executeQuery();
    }

    public static ResultSet getThanhVienCuaHo(Connection conn, SoHoKhau soHoKhau) throws SQLException {
        String query = "select nhankhau.HoTen, cccd.CCCD, thanhviencuaho.quanHeVoiChuHo from thanhviencuaho, nhankhau, sohokhau, cccd\n" +
                "where thanhviencuaho.idNhanKhau = nhankhau.ID and thanhviencuaho.idHoKhau = sohokhau.ID and cccd.idNhankhau = nhankhau.ID\n" +
                "and sohokhau.MaHoKhau = ?;";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, soHoKhau.getMaHoKhau());
        return preparedStatement.executeQuery();
    }

    public static int deleteSoHoKhau(Connection conn, SoHoKhau selected) throws SQLException {
        String DELETE_QUERY = "DELETE FROM sohokhau WHERE MaHoKhau = ?";
        conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
        preparedStatement.setString(1, selected.getMaHoKhau());
        return preparedStatement.executeUpdate();
    }

    public static ResultSet getChuHo(Connection conn) throws SQLException {
        String SELECT_QUERY = "SELECT nhankhau.*, cccd.CCCD\n" +
                "FROM nhankhau\n" +
                "JOIN cccd\n" +
                "ON nhankhau.ID = cccd.idNhankhau\n" +
                "WHERE nhankhau.ID not in (SELECT idNhanKhau FROM thanhviencuaho) " +
                "and nhankhau.ID not in (SELECT MaChuHo FROM sohokhau)";
        PreparedStatement preparedStatement = conn.prepareStatement(SELECT_QUERY);
        return preparedStatement.executeQuery();
    }

    public static PreparedStatement addSoHoKhau(Connection conn, String maHoKhau, String diaChi, NhanKhau selected) throws SQLException {
        String INSERT_QUERY = "INSERT INTO `sohokhau`(`MaHoKhau`, `DiaChi`, `MaChuHo`,`NgayLap`) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, maHoKhau);
        preparedStatement.setString(2, diaChi);
        preparedStatement.setInt(3, selected.getID());
        preparedStatement.setString(4, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return preparedStatement;
    }

    public static int updateChuHo(Connection conn, int idHoKhau, NhanKhau selected) throws SQLException {
        String UPDATE_QUERY = "UPDATE sohokhau SET MaChuHo = ? WHERE ID = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY);
        preparedStatement.setInt(1, selected.getID());
        preparedStatement.setInt(2, idHoKhau);
        return preparedStatement.executeUpdate();
    }

    public static int update(Connection conn, int idHoKhau, String maHoKhau, String diaChi) throws SQLException {
        String UPDATE_QUERY = "UPDATE sohokhau SET sohokhau.MaHoKhau = ?, sohokhau.DiaChi = ? WHERE ID = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY);
        preparedStatement.setString(1, maHoKhau);
        preparedStatement.setString(2, diaChi);
        preparedStatement.setInt(3, idHoKhau);
        return preparedStatement.executeUpdate();
    }
    public static int deleteThanhVienInSoHoKhau(Connection conn, ThanhVien selected) throws SQLException {
        String DELETE_QUERY = "DELETE thanhviencuaho\n" +
                "FROM thanhviencuaho\n" +
                "JOIN nhankhau\n" +
                "ON thanhviencuaho.idNhanKhau = nhankhau.ID\n" +
                "JOIN cccd\n" +
                "ON nhankhau.ID = cccd.idNhankhau\n" +
                "WHERE cccd.CCCD = ?\n";
        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
        preparedStatement.setString(1, selected.getCCCD());
        return preparedStatement.executeUpdate();
    }

    public static int updateThanhVienInSoHoKhau(Connection conn, String quanHe, ThanhVien selected) throws SQLException {
        String UPDATE_QUERY = "UPDATE thanhviencuaho\n" +
                "SET quanHeVoiChuHo = ?\n" +
                "WHERE idNhanKhau in (SELECT nhankhau.ID FROM nhankhau JOIN cccd ON nhankhau.ID = cccd.idNhankhau WHERE cccd.CCCD = ?)\n";
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY);
        preparedStatement.setString(1, quanHe);
        preparedStatement.setString(2, selected.getCCCD());
        return preparedStatement.executeUpdate();
    }

    public static int getTotalSoHoKhau() {
        int total = 0;
        String GET_QUERY = "SELECT COUNT(*) FROM sohokhau";
        try {
            Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = conn.prepareStatement(GET_QUERY);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                total = result.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return total;
    }
}
