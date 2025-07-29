package gui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DashboardPanel extends JPanel {

    private final JTabbedPane tabbedPane;
    private JTable table;
    private DefaultTableModel tableModel;

    public DashboardPanel(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Judul Dashboard
        JLabel title = new JLabel("Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Panel untuk 3 box info kecil
        JPanel boxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        boxPanel.setBackground(Color.WHITE);
        boxPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boxPanel.add(createSmallBox("User", getCount("user"), new Color(204, 229, 255), 1));
        boxPanel.add(createSmallBox("Produk", getCount("produk"), new Color(204, 255, 229), 2));
        boxPanel.add(createSmallBox("Transaksi", getCount("transaksi"), new Color(255, 230, 204), 3));

        // Panel untuk tabel riwayat
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Riwayat Transaksi Terbaru"));

        // Buat tabel
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[] { "User", "Produk", "Tanggal", "Poin" });
        table = new JTable(tableModel);
        table.setEnabled(false);
        table.setRowHeight(24);

        // Batasi tinggi scroll pane agar tidak mendominasi
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 120));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Gabungkan boxPanel dan tablePanel dalam satu centerPanel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(boxPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(tablePanel);

        add(centerPanel, BorderLayout.CENTER);

        // Isi data riwayat
        loadLatestTransaksi();
    }

    private JPanel createSmallBox(String title, int count, Color bgColor, int tabIndex) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bgColor);
        panel.setPreferredSize(new Dimension(150, 100));
        panel.setMaximumSize(new Dimension(150, 100));
        panel.setMinimumSize(new Dimension(150, 100));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.DARK_GRAY);

        JLabel countLabel = new JLabel(String.valueOf(count), SwingConstants.CENTER);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        countLabel.setForeground(Color.BLACK);

        JButton detailBtn = new JButton("Detail");
        detailBtn.setFocusPainted(false);
        detailBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailBtn.setBackground(Color.WHITE);
        detailBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        detailBtn.addActionListener(e -> tabbedPane.setSelectedIndex(tabIndex));

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(countLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(detailBtn);

        return panel;
    }

    private int getCount(String tableName) {
        String query = "SELECT COUNT(*) AS total FROM " + tableName;
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mengambil jumlah dari tabel " + tableName + ":\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    private void loadLatestTransaksi() {
        String query = "SELECT u.nama AS user, p.nama_produk AS produk, t.tanggal, t.total_poin " +
                "FROM transaksi t " +
                "JOIN user u ON t.user_id = u.user_id " +
                "JOIN produk p ON t.produk_id = p.produk_id " +
                "ORDER BY t.tanggal DESC LIMIT 5";

        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String user = rs.getString("user");
                String produk = rs.getString("produk");
                Date tanggal = rs.getDate("tanggal");
                int poin = rs.getInt("total_poin");
                tableModel.addRow(new Object[] { user, produk, tanggal, poin });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mengambil riwayat transaksi:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
