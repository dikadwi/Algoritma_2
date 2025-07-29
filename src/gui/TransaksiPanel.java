package gui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class TransaksiPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private Map<Integer, Integer> produkHargaMap = new HashMap<>();
    private Map<Integer, String> userMap = new HashMap<>();
    private Map<Integer, String> produkMap = new HashMap<>();

    public TransaksiPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Daftar Transaksi", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton tambahBtn = new JButton("Tambah");
        tambahBtn.addActionListener(e -> showForm(false, -1));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(tambahBtn, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[] { "ID", "User", "Produk", "Jumlah", "Total Poin", "Tanggal", "Edit", "Delete" }, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 6 || column == 7;
            }
        };

        table = new JTable(tableModel);
        table.getColumn("Edit").setCellRenderer(new ButtonRenderer("Edit"));
        table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));
        table.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));
        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete"));

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        userMap.clear();
        produkMap.clear();
        produkHargaMap.clear();

        try (Connection conn = DBConnection.getConnection()) {
            // load user
            ResultSet ru = conn.createStatement().executeQuery("SELECT * FROM user");
            while (ru.next()) {
                userMap.put(ru.getInt("user_id"), ru.getString("nama"));
            }

            // load produk
            ResultSet rp = conn.createStatement().executeQuery("SELECT * FROM produk");
            while (rp.next()) {
                int id = rp.getInt("produk_id");
                produkMap.put(id, rp.getString("nama_produk"));
                produkHargaMap.put(id, rp.getInt("harga_poin"));
            }

            String sql = "SELECT * FROM transaksi ORDER BY tanggal DESC";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id_transaksi");
                int userId = rs.getInt("user_id");
                int produkId = rs.getInt("produk_id");
                int jumlah = rs.getInt("jumlah");
                int totalPoin = rs.getInt("total_poin");
                Date tanggal = rs.getDate("tanggal");

                tableModel.addRow(new Object[] {
                        id,
                        userMap.get(userId),
                        produkMap.get(produkId),
                        jumlah,
                        totalPoin,
                        new SimpleDateFormat("yyyy-MM-dd").format(tanggal),
                        "Edit", "Delete"
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data transaksi: " + e.getMessage());
        }
    }

    private void showForm(boolean isEdit, int rowIndex) {
        JComboBox<String> cbUser = new JComboBox<>(userMap.values().toArray(new String[0]));
        JComboBox<String> cbProduk = new JComboBox<>(produkMap.values().toArray(new String[0]));
        JSpinner spJumlah = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JSpinner spTanggal = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spTanggal, "yyyy-MM-dd");
        spTanggal.setEditor(dateEditor);

        if (isEdit && rowIndex != -1) {
            cbUser.setSelectedItem(table.getValueAt(rowIndex, 1));
            cbProduk.setSelectedItem(table.getValueAt(rowIndex, 2));
            spJumlah.setValue(table.getValueAt(rowIndex, 3));
            try {
                Date tgl = new SimpleDateFormat("yyyy-MM-dd").parse((String) table.getValueAt(rowIndex, 5));
                spTanggal.setValue(tgl);
            } catch (Exception ignored) {
            }
        }

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("User:"));
        panel.add(cbUser);
        panel.add(new JLabel("Produk:"));
        panel.add(cbProduk);
        panel.add(new JLabel("Jumlah:"));
        panel.add(spJumlah);
        panel.add(new JLabel("Tanggal:"));
        panel.add(spTanggal);

        JDialog dialog = new JDialog((Frame) null, isEdit ? "Edit Transaksi" : "Tambah Transaksi", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnSimpan = new JButton(isEdit ? "Update" : "Simpan");
        JButton btnBatal = new JButton("Batal");
        btnPanel.add(btnSimpan);
        btnPanel.add(btnBatal);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        btnSimpan.addActionListener(e -> {
            int userId = getKeyByValue(userMap, (String) cbUser.getSelectedItem());
            int produkId = getKeyByValue(produkMap, (String) cbProduk.getSelectedItem());
            int jumlah = (int) spJumlah.getValue();
            Date tanggal = (Date) spTanggal.getValue();
            int totalPoin = produkHargaMap.get(produkId) * jumlah;

            try (Connection conn = DBConnection.getConnection()) {
                if (isEdit) {
                    int transaksiId = (int) table.getValueAt(rowIndex, 0);
                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE transaksi SET user_id=?, produk_id=?, jumlah=?, total_poin=?, tanggal=? WHERE id_transaksi=?");
                    ps.setInt(1, userId);
                    ps.setInt(2, produkId);
                    ps.setInt(3, jumlah);
                    ps.setInt(4, totalPoin);
                    ps.setDate(5, new java.sql.Date(tanggal.getTime()));
                    ps.setInt(6, transaksiId);
                    ps.executeUpdate();
                } else {
                    PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO transaksi (user_id, produk_id, jumlah, total_poin, tanggal) VALUES (?, ?, ?, ?, ?)");
                    ps.setInt(1, userId);
                    ps.setInt(2, produkId);
                    ps.setInt(3, jumlah);
                    ps.setInt(4, totalPoin);
                    ps.setDate(5, new java.sql.Date(tanggal.getTime()));
                    ps.executeUpdate();
                }
                dialog.dispose();
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Gagal menyimpan: " + ex.getMessage());
            }
        });

        btnBatal.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // Button renderer dan editor sama seperti panel lain
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;

        public ButtonEditor(JCheckBox checkBox, String label) {
            super(checkBox);
            this.label = label;
            button = new JButton(label);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (label.equals("Edit")) {
                showForm(true, row);
            } else if (label.equals("Delete")) {
                int id = (int) table.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(null, "Hapus transaksi ini?", "Konfirmasi",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement("DELETE FROM transaksi WHERE id_transaksi=?");
                        ps.setInt(1, id);
                        ps.executeUpdate();
                        loadData();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Gagal menghapus transaksi: " + ex.getMessage());
                    }
                }
            }
            return button;
        }

        public Object getCellEditorValue() {
            return label;
        }
    }
}
