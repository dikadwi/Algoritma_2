
import javax.swing.*;

import gui.*;
import db.DBConnection;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Aplikasi Pengelolaan Data Produk Point Market");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null); // center window

            // Tab Panel sebagai navigasi
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Dashboard", new DashboardPanel(tabbedPane)); // index 0
            tabbedPane.addTab("User", new UserPanel()); // index 1
            tabbedPane.addTab("Produk", new ProdukPanel()); // index 2
            tabbedPane.addTab("Transaksi", new TransaksiPanel()); // index 3
            // tabbedPane.addTab("Riwayat", new RiwayatTransaksiPanel()); // index 4 (jika
            // ada)

            frame.add(tabbedPane);

            // Latest
            // JTabbedPane tabbedPane = new JTabbedPane();
            // tabbedPane.addTab("Dashboard", new DashboardPanel(tabbedPane));
            // tabbedPane.addTab("User", new UserPanel());
            // tabbedPane.addTab("Produk", new ProdukPanel());
            // tabbedPane.addTab("Transaksi", new TransaksiPanel());
            // tabbedPane.addTab("Riwayat", new RiwayatTransaksiPanel());

            // frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}
