package com.quartermanagement.Model;

public class NhanKhau {
    private String HoTen, BiDanh, NgaySinh, CCCD, NoiSinh,
            GioiTinh, NguyenQuan, DanToc, NoiThuongTru, TonGiao, QuocTich, DiaChiHienNay, NgheNghiep;

    private  int ID;
    // constructor
    public NhanKhau(){
    }

    public NhanKhau(Integer id,String hoTen, String biDanh, String ngaySinh, String CCCD,
                    String noiSinh, String gioiTinh, String nguyenQuan, String danToc, String noiThuongTru,
                    String tonGiao, String quocTich, String diaChiHienNay, String ngheNghiep) {
        ID = id;
        HoTen = hoTen;
        BiDanh = biDanh;
        NgaySinh = ngaySinh;
        this.CCCD = CCCD;
        NoiSinh = noiSinh;
        GioiTinh = gioiTinh;
        NguyenQuan = nguyenQuan;
        DanToc = danToc;
        NoiThuongTru = noiThuongTru;
        TonGiao = tonGiao;
        QuocTich = quocTich;
        DiaChiHienNay = diaChiHienNay;
        NgheNghiep = ngheNghiep;
    }

    // Getter and setter


    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getBiDanh() {
        return BiDanh;
    }

    public void setBiDanh(String biDanh) {
        BiDanh = biDanh;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getNoiSinh() {
        return NoiSinh;
    }

    public void setNoiSinh(String noiSinh) {
        NoiSinh = noiSinh;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public String getNguyenQuan() {
        return NguyenQuan;
    }

    public void setNguyenQuan(String nguyenQuan) {
        NguyenQuan = nguyenQuan;
    }

    public String getDanToc() {
        return DanToc;
    }

    public void setDanToc(String danToc) {
        DanToc = danToc;
    }

    public String getNoiThuongTru() {
        return NoiThuongTru;
    }

    public void setNoiThuongTru(String noiThuongTru) {
        NoiThuongTru = noiThuongTru;
    }

    public String getTonGiao() {
        return TonGiao;
    }

    public void setTonGiao(String tonGiao) {
        TonGiao = tonGiao;
    }

    public String getQuocTich() {
        return QuocTich;
    }

    public void setQuocTich(String quocTich) {
        QuocTich = quocTich;
    }

    public String getDiaChiHienNay() {
        return DiaChiHienNay;
    }

    public void setDiaChiHienNay(String diaChiHienNay) {
        DiaChiHienNay = diaChiHienNay;
    }

    public String getNgheNghiep() {
        return NgheNghiep;
    }

    public void setNgheNghiep(String ngheNghiep) {
        NgheNghiep = ngheNghiep;
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
