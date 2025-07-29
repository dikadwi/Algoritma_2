package model;

public class User {

    private int id;
    private String nama;
    private String email;
    private int poin;

    public User(int id, String nama, String email, int poin) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.poin = poin;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public int getPoin() {
        return poin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPoin(int poin) {
        this.poin = poin;
    }
}
