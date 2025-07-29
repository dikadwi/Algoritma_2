# 🛒 Point Market - Aplikasi Desktop Java GUI CRUD

**Point Market** adalah aplikasi desktop berbasis **Java Swing** yang dirancang sebagai simulasi sistem **marketplace berbasis poin**. Aplikasi ini memungkinkan pengguna untuk mengelola **data user, produk, dan transaksi penukaran poin** melalui antarmuka grafis yang interaktif.

---

## 🚀 Fitur Utama

- ✅ **Manajemen User**

  - Tambah, lihat, ubah, dan hapus data pengguna

- ✅ **Manajemen Produk**

  - CRUD untuk daftar produk dan pengaturan stok

- ✅ **Manajemen Transaksi**

  - Manajemen transaksi antar user dan produk

- ✅ **Dashboard**

  - Tampilkan ringkasan jumlah user, produk, dan transaksi

- ✅ **Validasi Input**

  - Cek kelengkapan dan kevalidan data input sebelum diproses

- ✅ **Navigasi Antar Tab**

  - Tombol "Detail" antar tab untuk eksplorasi data lanjutan

---

## 🧰 Teknologi yang Digunakan

| Teknologi  | Deskripsi                                   |
| ---------- | ------------------------------------------- |
| 🖥️ Java SE | Swing & AWT untuk GUI                       |
| 🛢️ MySQL   | Sistem manajemen basis data relasional      |
| 🔌 JDBC    | Koneksi antara Java dan MySQL               |
| 🛠️ VS Code | Editor kode utama untuk pengembangan proyek |

---

## 💽 Struktur Basis Data MySQL

```sql
CREATE TABLE user (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  nama VARCHAR(100),
  email VARCHAR(100),
  poin INT
);

CREATE TABLE produk (
  produk_id INT AUTO_INCREMENT PRIMARY KEY,
  nama_produk VARCHAR(100),
  harga_poin INT,
  stok INT
);

CREATE TABLE transaksi (
  id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  produk_id INT,
  jumlah INT,
  total_poin INT,
  tanggal DATE,
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (produk_id) REFERENCES produk(produk_id)
);
```

---

## 📦 Cara Menjalankan Aplikasi

1. **Kompilasi seluruh file `.java`**:

   ```bash
   javac -d bin -cp lib/mysql-connector-java-x.x.x.jar src/**/*.java
   ```

2. **Buat file JAR**:

   ```bash
   jar --create --file=PointMarketApp.jar --main-class=Main -C bin .
   ```

3. **Jalankan aplikasi**:

   ```bash
   java -jar PointMarketApp.jar
   ```

   **Jalankan `Main.java`** sebagai Java Application.

> 🔔 Pastikan file `mysql-connector-java` telah ditambahkan ke classpath agar koneksi ke database berhasil.

---

## 📌 Catatan Tambahan

- Database harus dibuat dan diisi terlebih dahulu sebelum menjalankan aplikasi.
- File konfigurasi koneksi database berada di kelas `DBConnection.java`.
- Seluruh fitur CRUD menggunakan komponen Swing dan `PreparedStatement` untuk keamanan terhadap SQL Injection.

---

## ✍️ Kontributor

- **Andika Dwi Arko** – _Developer & Maintainer_

---
