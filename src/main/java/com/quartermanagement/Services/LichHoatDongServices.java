package com.quartermanagement.Services;

import com.quartermanagement.Model.LichHoatDong;
import com.quartermanagement.Model.NhanKhau;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LichHoatDongServices {
    public static String getNamebyID(Connection conn, int id) throws SQLException {
        String query = "SELECT HoTen FROM nhankhau WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getString("HoTen");
    }

    public static ResultSet getAllPeople(Connection conn) throws SQLException {
        String query = "SELECT HoTen FROM nhankhau";
        PreparedStatement ps = conn.prepareStatement(query);
        return ps.executeQuery();
    }

    public static ResultSet getCoSoVatChatFromLichHoatDong(Connection conn, LichHoatDong lichHoatDong) throws SQLException {
        String query = "SELECT csvc.TenDoDung, hdcsvc.SoLuong " +
                "FROM cosovatchat csvc, hoatdong_cosovatchat hdcsvc " +
                "WHERE csvc.MaDoDung = hdcsvc.MaDoDung AND hdcsvc.MaHoatDong = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1,lichHoatDong.getMaHoatDong());
        System.out.println(ps);
        return ps.executeQuery();
    }


    public static ResultSet getCheck(Connection conn, LichHoatDong lichHoatDong) throws SQLException {
        String CHECK_QUERY = "SELECT hdcsvc.SoLuong, csvc.SoLuongKhaDung " +
                "FROM hoatdong_cosovatchat hdcsvc, cosovatchat csvc " +
                "WHERE hdcsvc.MaDoDung = csvc.MaDoDung AND hdcsvc.MaHoatDong = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(CHECK_QUERY);
        preparedStatement.setInt(1, lichHoatDong.getMaHoatDong());
        return preparedStatement.executeQuery();
    }


    public static int updateLichHoatDong(Connection conn, String maHoatDong, String tenHoatDong, String starttime, String endtime, String status, String maNguoiTao) throws SQLException {
        String UPDATE_QUERY = "UPDATE lichhoatdong SET `MaHoatDong`=?, `TenHoatDong`=?, `ThoiGianBatDau`=?, `ThoiGianKetThuc`=?, `DuocDuyet`=?, `MaNguoiTao`=? WHERE `MaHoatDong`=?";
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY);
        preparedStatement.setString(1, maHoatDong);
        preparedStatement.setString(2, tenHoatDong);
        preparedStatement.setString(3, starttime);
        preparedStatement.setString(4, endtime);
        preparedStatement.setString(5, status);
        preparedStatement.setString(6, maNguoiTao);
        preparedStatement.setString(7, maHoatDong);
        return preparedStatement.executeUpdate();
    }

    public static void updateTruSoLuongKhaDung(Connection conn, LichHoatDong lichHoatDong) throws SQLException {
        String UPDATE_SOLUONG_QUERY = "UPDATE cosovatchat c JOIN hoatdong_cosovatchat s ON c.MaDoDung = s.MaDoDung SET c.SoLuongKhaDung = c.SoLuongKhaDung - s.SoLuong " +
                "WHERE s.MaHoatDong = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SOLUONG_QUERY);
        preparedStatement.setInt(1, lichHoatDong.getMaHoatDong());
        preparedStatement.executeUpdate();
    }

    public static void updateCongSoLuongKhaDung(Connection conn, LichHoatDong lichHoatDong) throws SQLException {
        String UPDATE_SOLUONG_QUERY = "UPDATE cosovatchat c JOIN hoatdong_cosovatchat s ON c.MaDoDung = s.MaDoDung SET c.SoLuongKhaDung = c.SoLuongKhaDung + s.SoLuong " +
                "WHERE s.MaHoatDong = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SOLUONG_QUERY);
        preparedStatement.setInt(1, lichHoatDong.getMaHoatDong());
        preparedStatement.executeUpdate();
    }



    public static int insertLichHoatDong(Connection conn, String maHoatDong, String tenHoatDong, String starttime, String endtime, String status, String thoiGianTao, NhanKhau selected) throws SQLException {
        String INSERT_QUERY = "INSERT INTO lichhoatdong VALUES(?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement((INSERT_QUERY));
        preparedStatement.setString(1, maHoatDong);
        preparedStatement.setString(2, tenHoatDong);
        preparedStatement.setString(3, starttime);
        preparedStatement.setString(4, endtime);
        preparedStatement.setString(5, status);
        preparedStatement.setString(6, thoiGianTao);
        preparedStatement.setString(7, String.valueOf(selected.getID()));
        return preparedStatement.executeUpdate();
    }

    public static int deleteLichHoatDong(Connection conn, LichHoatDong selected) throws SQLException {
        String DELETE_QUERY = "DELETE FROM lichhoatdong WHERE `MaHoatDong`= ?";
        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
        preparedStatement.setString(1, String.valueOf(selected.getMaHoatDong()));
        return preparedStatement.executeUpdate();
    }

    public static ResultSet getLichHoatDongGanNhat(Connection conn) throws SQLException {
        String SELECT_QUERY = "SELECT TenHoatDong, ThoiGianBatDau FROM `lichhoatdong` WHERE ThoiGianBatDau > NOW() ORDER BY ThoiGianBatDau ASC";
        PreparedStatement preparedStatement = conn.prepareStatement(SELECT_QUERY);
        return preparedStatement.executeQuery();
    }
}
