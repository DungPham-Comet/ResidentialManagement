package com.quartermanagement.Model;

import com.quartermanagement.Services.LichHoatDongServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.quartermanagement.Constants.DBConstants.*;

public class LichHoatDong {
    private int maHoatDong;
    private String tenHoatDong;
    private String startTime;
    private String endTime;

    private String status;
    private String madeTime;

    private int maNguoiTao;
    private String tenNguoiTao;
    private Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);

    public LichHoatDong() throws SQLException {}
    public LichHoatDong(int maHoatDong, String tenHoatDong, String startTime, String endTime, String status,
    String madeTime, int maNguoiTao) throws SQLException {
        this.maHoatDong = maHoatDong;
        this.tenHoatDong = tenHoatDong;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.madeTime = madeTime;
        this.maNguoiTao = maNguoiTao;
        this.tenNguoiTao = LichHoatDongServices.getNamebyID(conn, maNguoiTao);
    }

    public int getMaHoatDong() {
        return maHoatDong;
    }

    public void setMaHoatDong(int maHoatDong) {
        this.maHoatDong = maHoatDong;
    }

    public String getTenHoatDong() {
        return tenHoatDong;
    }

    public void setTenHoatDong(String tenHoatDong) {
        this.tenHoatDong = tenHoatDong;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMadeTime() {
        return madeTime;
    }

    public void setMadeTime(String madeTime) {
        this.madeTime = madeTime;
    }

    public int getMaNguoiTao() {
        return maNguoiTao;
    }

    public void setMaNguoiTao(int maNguoiTao) {
        this.maNguoiTao = maNguoiTao;
    }

    public String getTenNguoiTao() {
        return tenNguoiTao;
    }

    public void setTenNguoiTao(String tenNguoiTao) {
        this.tenNguoiTao = tenNguoiTao;
    }
}
