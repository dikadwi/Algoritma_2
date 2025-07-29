package gui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ProdukPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public ProdukPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Daftar Produk", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton tambahBtn = new JButton("Tambah");
        tambahBtn.addActionListener(e -> showProdukForm(false, -1, -1));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(tambahBtn, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[] { "Produk ID", "Nama Produk", "Harga Poin", "Stok", "Edit", "Delete" },
                0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };

        table = new JTable(tableModel);
        table.getColumn("Edit").setCellRenderer(new ButtonRenderer("Edit"));
        table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));
        table.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));
        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete"));

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadDataProduk();
    }

    private void loadDataProduk() {
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM produk")) {

            tableModel.setRowCount(0);
            while (rs.next()) {
                int id = rs.getInt("produk_id");
                String nama = rs.getString("nama_produk");
                int harga = rs.getInt("harga_poin");
                int stok = rs.getInt("stok");
                tableModel.addRow(new Object[] { id, nama, harga, stok, "Edit", "Delete" });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data produk:\n" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showProdukForm(boolean isEdit, int produkId, int selectedRow) {
        String nama = "";
        int harga = 0, stok = 0;

        if (isEdit && selectedRow != -1) {
            nama = (String) table.getValueAt(selectedRow, 1);
            harga = Integer.parseInt(table.getValueAt(selectedRow, 2).toString());
            stok = Integer.parseInt(table.getValueAt(selectedRow, 3).toString());
        }

        JTextField tfNama = new JTextField(nama);
        JSpinner spHarga = new JSpinner(new SpinnerNumberModel(harga, 0, Integer.MAX_VALUE, 1));
        JSpinner spStok = new JSpinner(new SpinnerNumberModel(stok, 0, Integer.MAX_VALUE, 1));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.add(new JLabel("Nama Produk:"));
        formPanel.add(tfNama);
        formPanel.add(new JLabel("Harga Poin:"));
        formPanel.add(spHarga);
        formPanel.add(new JLabel("Stok:"));
        formPanel.add(spStok);

        JDialog dialog = new JDialog((Frame) null, isEdit ? "Edit Data Produk" : "Tambah Data Produk", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton simpanBtn = new JButton(isEdit ? "Update" : "Simpan");
        JButton batalBtn = new JButton("Batal");

        simpanBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                String newNama = tfNama.getText();
                int newHarga = (Integer) spHarga.getValue();
                int newStok = (Integer) spStok.getValue();

                if (isEdit) {
                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE produk SET nama_produk=?, harga_poin=?, stok=? WHERE produk_id=?");
                    ps.setString(1, newNama);
                    ps.setInt(2, newHarga);
                    ps.setInt(3, newStok);
                    ps.setInt(4, produkId);
                    ps.executeUpdate();
                } else {
                    PreparedStatement ps = conn
                            .prepareStatement("INSERT INTO produk (nama_produk, harga_poin, stok) VALUES (?, ?, ?)");
                    ps.setString(1, newNama);
                    ps.setInt(2, newHarga);
                    ps.setInt(3, newStok);
                    ps.executeUpdate();
                }

                dialog.dispose();
                loadDataProduk();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Gagal menyimpan data: " + ex.getMessage());
            }
        });

        batalBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(simpanBtn);
        buttonPanel.add(batalBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

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
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            int produkId = (int) table.getValueAt(row, 0);
            if (label.equals("Edit")) {
                showProdukForm(true, produkId, row);
            } else if (label.equals("Delete")) {
                int confirm = JOptionPane.showConfirmDialog(null, "Yakin hapus produk ini?", "Konfirmasi",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement("DELETE FROM produk WHERE produk_id=?");
                        ps.setInt(1, produkId);
                        ps.executeUpdate();
                        loadDataProduk();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Gagal menghapus produk: " + ex.getMessage());
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
