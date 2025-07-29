package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/gui";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // ganti jika pakai password
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // WAJIB untuk load Driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Koneksi database berhasil.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC MySQL tidak ditemukan.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Gagal koneksi ke database.");
            e.printStackTrace();
        }

        return connection;
    }

}
