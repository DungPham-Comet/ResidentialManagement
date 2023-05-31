package com.quartermanagement.Model;

public class ThanhVien {
    private String HoTen, CCCD, quanHeVoiChuHo;

    public ThanhVien(String hoTen, String CCCD, String quanHeVoiChuHo) {
        HoTen = hoTen;
        this.CCCD = CCCD;
        this.quanHeVoiChuHo = quanHeVoiChuHo;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getQuanHeVoiChuHo() {
        return quanHeVoiChuHo;
    }

    public void setQuanHeVoiChuHo(String quanHeVoiChuHo) {
        this.quanHeVoiChuHo = quanHeVoiChuHo;
    }
}
