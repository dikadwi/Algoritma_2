````markdown
---

# 🛒 Point Market - Java GUI CRUD Application

**Point Market** adalah aplikasi desktop berbasis Java Swing dengan fitur CRUD untuk mengelola pengguna, produk, dan transaksi penukaran poin. Aplikasi ini menggunakan MySQL sebagai basis data dan dirancang sebagai simulasi sistem marketplace berbasis poin.

---

## 🚀 Fitur Utama

- ✅ **Manajemen User** (Create, Read, Update, Delete)
- ✅ **Manajemen Produk** (Create, Read, Update, Delete)
- ✅ **Manajemen Transaksi** (Create, Read, Update, Delete)
- ✅ **Dashboard** (Ringkasan jumlah dan riwayat transaksi)
- ✅ **Validasi Input dan Tampilan Menggunakan Dialog**
- ✅ **Interaksi antar tab melalui tombol Detail**

---

## 🧰 Teknologi yang Digunakan

- **Java SE** (Swing, AWT)
- **MySQL** (Database)
- **JDBC** (Koneksi database)
- **VS Code**

---

## 💽 Struktur Tabel MySQL

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
````

---

## ⚙️ Cara Menjalankan

1. **Klon repositori ini**:

   ```bash
   git clone https://github.com/dikadwi/Algoritma_2
   ```

2. **Import project** ke IDE Java kamu.

3. **Siapkan database** `gui` di MySQL:

   - Buat database `gui`
   - Jalankan SQL di atas

4. **Pastikan MySQL Connector** (`mysql-connector-java-x.x.x.jar`) sudah ada di classpath/project library.

5. **Jalankan `Main.java`** sebagai Java Application.

---


## ✍️ Kontributor

- **Andika Dwi Arko** – _Developer & Maintainer_

---
