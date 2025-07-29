package model;

public class Produk {

    private int id;
    private String namaProduk;
    private int hargaPoin;
    private int stok;

    public Produk(int id, String namaProduk, int hargaPoin, int stok) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.hargaPoin = hargaPoin;
        this.stok = stok;
    }

    public int getId() {
        return id;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public int getHargaPoin() {
        return hargaPoin;
    }

    public int getStok() {
        return stok;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public void setHargaPoin(int hargaPoin) {
        this.hargaPoin = hargaPoin;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}
