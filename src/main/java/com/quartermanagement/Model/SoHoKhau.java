package com.quartermanagement.Model;

public class SoHoKhau {
    private String MaHoKhau;
    private String tenChuHo, DiaChi;
    private int soLuongThanhVien;

    // constructor
    public SoHoKhau(String tenChuHo, String diaChi, String maHoKhau, int soLuongThanhVien) {
        MaHoKhau = maHoKhau;
        this.tenChuHo = tenChuHo;
        DiaChi = diaChi;
        this.soLuongThanhVien = soLuongThanhVien;
    }

    // Getter and setter


    public String getMaHoKhau() {
        return MaHoKhau;
    }

    public void setMaHoKhau(String maHoKhau) {
        MaHoKhau = maHoKhau;
    }

    public String getTenChuHo() {
        return tenChuHo;
    }

    public void setTenChuHo(String tenChuHo) {
        this.tenChuHo = tenChuHo;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public int getSoLuongThanhVien() {
        return soLuongThanhVien;
    }

    public void setSoLuongThanhVien(int soLuongThanhVien) {
        this.soLuongThanhVien = soLuongThanhVien;
    }
}
