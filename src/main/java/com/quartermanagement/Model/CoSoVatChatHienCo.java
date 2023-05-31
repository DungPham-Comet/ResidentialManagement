package com.quartermanagement.Model;

public class CoSoVatChatHienCo {
    private String TenDoDung;
    private int SoLuong;

    public CoSoVatChatHienCo(String tenDoDung, int soLuong) {
        this.TenDoDung = tenDoDung;
        this.SoLuong = soLuong;
    }

    public String getTenDoDung() {
        return TenDoDung;
    }

    public void setTenDoDung(String tenDoDung) {
        TenDoDung = tenDoDung;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }
}
