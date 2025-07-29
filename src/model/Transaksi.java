package model;

import java.time.LocalDateTime;

public class Transaksi {

    private int id;
    private int userId;
    private int produkId;
    private int jumlah;
    private int totalPoin;
    private LocalDateTime tanggal;

    public Transaksi(int id, int userId, int produkId, int jumlah, int totalPoin, LocalDateTime tanggal) {
        this.id = id;
        this.userId = userId;
        this.produkId = produkId;
        this.jumlah = jumlah;
        this.totalPoin = totalPoin;
        this.tanggal = tanggal;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getProdukId() {
        return produkId;
    }

    public int getJumlah() {
        return jumlah;
    }

    public int getTotalPoin() {
        return totalPoin;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProdukId(int produkId) {
        this.produkId = produkId;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public void setTotalPoin(int totalPoin) {
        this.totalPoin = totalPoin;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }
}
