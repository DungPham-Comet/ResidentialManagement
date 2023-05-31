package com.quartermanagement.Model;

public class CoSoVatChat {
    private int maDoDung, soLuong, soLuongKhaDung;

    private String tenDoDung;

    public CoSoVatChat() {
    }

    public CoSoVatChat(int maDoDung, String tenDoDung, int soLuong, int soLuongKhaDung) {
        this.maDoDung = maDoDung;
        this.tenDoDung = tenDoDung;
        this.soLuong = soLuong;
        this.soLuongKhaDung = soLuongKhaDung;
    }

    public int getMaDoDung() {
        return maDoDung;
    }

    public void setMaDoDung(int maDoDung) {
        this.maDoDung = maDoDung;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getSoLuongKhaDung() {
        return soLuongKhaDung;
    }

    public void setSoLuongKhaDung(int soLuongKhaDung) {
        this.soLuongKhaDung = soLuongKhaDung;
    }

    public String getTenDoDung() {
        return tenDoDung;
    }

    public void setTenDoDung(String tenDoDung) {
        this.tenDoDung = tenDoDung;
    }
}
