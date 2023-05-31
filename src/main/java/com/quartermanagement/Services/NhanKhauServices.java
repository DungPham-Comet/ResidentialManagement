package com.quartermanagement.Services;

import com.quartermanagement.Model.NhanKhau;

import java.io.PipedReader;
import java.sql.*;

import static com.quartermanagement.Constants.DBConstants.*;
import static com.quartermanagement.Utils.Utils.convertDate;

public class NhanKhauServices {
    public static ResultSet getAllNhanKhau() throws SQLException {
        // Connecting Database
        String SELECT_QUERY = "SELECT nhankhau.*, cccd.CCCD\n" +
                "FROM nhankhau, cccd\n" +
                "WHERE nhankhau.ID = cccd.idNhankhau";
        Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = conn.prepareStatement(SELECT_QUERY);
        return preparedStatement.executeQuery();
    }

    public static int deleteNhanKhauViaCCCD(int ID) throws SQLException {
        String DELETE_QUERY = "DELETE FROM cccd " +
                "WHERE idNhankhau =?";
        Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
        preparedStatement.setInt(1, ID);
        return preparedStatement.executeUpdate();
    }

    public static int deleteNhanKhau(int ID) throws SQLException {
        PreparedStatement preparedStatement = null;
        Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        String DELETE_QUERY =
                "DELETE FROM nhankhau " +
                        "WHERE ID =?";
        preparedStatement = conn.prepareStatement(DELETE_QUERY);
        preparedStatement.setInt(1, ID);
        return preparedStatement.executeUpdate();
    }

    public static int addNhanKhau(Connection conn, String hoVaTen, String biDanh, String ngaySinh, String noiSinh, String gioiTinh, String nguyenQuan,
                                  String danToc, String noiThuongTru, String tonGiao, String quocTich, String diaChiHienNay, String ngheNghiep) throws SQLException {
        String INSERT_QUERY = "INSERT INTO `nhankhau`(`HoTen`, `BiDanh`, `NgaySinh`, `NoiSinh`, `GioiTinh`, `NguyenQuan`, `DanToc`, `NoiThuongTru`, `TonGiao`, `QuocTich`, `DiaChiHienNay`, `NgheNghiep`) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(INSERT_QUERY);
        preparedStatement.setString(1, hoVaTen);
        preparedStatement.setString(2, biDanh);
        preparedStatement.setString(3, ngaySinh);
        preparedStatement.setString(4, noiSinh);
        preparedStatement.setString(5, gioiTinh);
        preparedStatement.setString(6, nguyenQuan);
        preparedStatement.setString(7, danToc);
        preparedStatement.setString(8, noiThuongTru);
        if (tonGiao.equals("")) preparedStatement.setString(9, "Không");
        else preparedStatement.setString(9, tonGiao);
        if (quocTich.equals("")) preparedStatement.setString(10, "Việt Nam");
        else preparedStatement.setString(10, quocTich);
        preparedStatement.setString(11, diaChiHienNay);
        preparedStatement.setString(12, ngheNghiep);
        return preparedStatement.executeUpdate();
    }

    public static int findNhanKhauID(Connection conn) throws SQLException {
        String SELECT_QUERY = "SELECT MAX(ID) AS ID FROM nhankhau";
        PreparedStatement preparedStatement = conn.prepareStatement(SELECT_QUERY);
        ResultSet result = preparedStatement.executeQuery();
        result.next();
        return result.getInt("ID");
    }

    public static int addCCCD(Connection conn, int ID, String cccd) throws SQLException {
        String INSERT_QUERY2 = "INSERT INTO `cccd`(`idNhankhau`,`CCCD`) VALUES (?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(INSERT_QUERY2);
        preparedStatement.setInt(1, ID);
        preparedStatement.setString(2, cccd);
        return preparedStatement.executeUpdate();
    }

    public static int updateNhanKhau(Connection conn, String hoVaTen, String biDanh, String ngaySinh, String noiSinh, String gioiTinh, String nguyenQuan,
                                      String danToc, String noiThuongTru, String tonGiao, String quocTich, String diaChiHienNay, String ngheNghiep, int ID) throws SQLException {
        String UPDATE_QUERY_NHAN_KHAU = "UPDATE `nhankhau` SET `HoTen`=?,`BiDanh`=?,`NgaySinh`=?,`NoiSinh`=?," +
                "`GioiTinh`=?,`NguyenQuan`=?,`DanToc`=?,`NoiThuongTru`=?,`TonGiao`=?,`QuocTich`=?,`DiaChiHienNay`=?," +
                "`NgheNghiep`=? WHERE `ID`=?";
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY_NHAN_KHAU);
        preparedStatement.setString(1, hoVaTen);
        preparedStatement.setString(2, biDanh);
        preparedStatement.setString(3, ngaySinh);
        preparedStatement.setString(4, noiSinh);
        preparedStatement.setString(5, gioiTinh);
        preparedStatement.setString(6, nguyenQuan);
        preparedStatement.setString(7, danToc);
        preparedStatement.setString(8, noiThuongTru);
        preparedStatement.setString(9, tonGiao);
        preparedStatement.setString(10, quocTich);
        preparedStatement.setString(11, diaChiHienNay);
        preparedStatement.setString(12, ngheNghiep);
        preparedStatement.setInt(13, ID);
        preparedStatement.execute();
        return preparedStatement.executeUpdate();
    }

    public static int updateCCCD(Connection conn, String cccd, int ID) throws SQLException {
        String UPDATE_QUERY_CCCD = "UPDATE `cccd` SET `CCCD`=? WHERE `idNhankhau`=?";
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY_CCCD);
        preparedStatement.setString(1, cccd);
        preparedStatement.setInt(2, ID);
        preparedStatement.execute();
        return preparedStatement.executeUpdate();
    }

    public static int getTotalNhanKhau() {
        int total = 0;
        String GET_QUERY = "SELECT COUNT(*) FROM nhankhau";
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
