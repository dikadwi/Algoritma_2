package gui;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UserPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public UserPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Daftar Pengguna", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton tambahBtn = new JButton("Tambah");
        tambahBtn.addActionListener(e -> showUserForm(false, -1, -1));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(tambahBtn, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[] { "User ID", "Nama", "Email", "Poin", "Edit", "Delete" }, 0) {
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
        loadDataUser();
    }

    private void loadDataUser() {
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user")) {

            tableModel.setRowCount(0);
            while (rs.next()) {
                int id = rs.getInt("user_id");
                String nama = rs.getString("nama");
                String email = rs.getString("email");
                int poin = rs.getInt("poin");
                tableModel.addRow(new Object[] { id, nama, email, poin, "Edit", "Delete" });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data user:\n" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showUserForm(boolean isEdit, int userId, int selectedRow) {
        String nama = "", email = "";
        int poin = 0;

        if (isEdit && selectedRow != -1) {
            nama = (String) table.getValueAt(selectedRow, 1);
            email = (String) table.getValueAt(selectedRow, 2);
            poin = Integer.parseInt(table.getValueAt(selectedRow, 3).toString());
        }

        JTextField tfNama = new JTextField(nama);
        JTextField tfEmail = new JTextField(email);
        JSpinner spPoin = new JSpinner(new SpinnerNumberModel(poin, 0, Integer.MAX_VALUE, 1));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.add(new JLabel("Nama:"));
        formPanel.add(tfNama);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(tfEmail);
        formPanel.add(new JLabel("Poin:"));
        formPanel.add(spPoin);

        JDialog dialog = new JDialog((Frame) null, isEdit ? "Edit Data User" : "Tambah Data User", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton simpanBtn = new JButton(isEdit ? "Update" : "Simpan");
        JButton batalBtn = new JButton("Batal");

        simpanBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                String newNama = tfNama.getText();
                String newEmail = tfEmail.getText();
                int newPoin = (Integer) spPoin.getValue();

                if (isEdit) {
                    PreparedStatement ps = conn
                            .prepareStatement("UPDATE user SET nama=?, email=?, poin=? WHERE user_id=?");
                    ps.setString(1, newNama);
                    ps.setString(2, newEmail);
                    ps.setInt(3, newPoin);
                    ps.setInt(4, userId);
                    ps.executeUpdate();
                } else {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO user(nama,email,poin) VALUES(?,?,?)");
                    ps.setString(1, newNama);
                    ps.setString(2, newEmail);
                    ps.setInt(3, newPoin);
                    ps.executeUpdate();
                }
                dialog.dispose();
                loadDataUser();
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

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
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

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            int userId = (int) table.getValueAt(row, 0);
            if (label.equals("Edit")) {
                showUserForm(true, userId, row);
            } else if (label.equals("Delete")) {
                int confirm = JOptionPane.showConfirmDialog(null, "Yakin hapus user ini?", "Hapus Data User !",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement("DELETE FROM user WHERE user_id=?");
                        ps.setInt(1, userId);
                        ps.executeUpdate();
                        loadDataUser();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Gagal menghapus user: " + ex.getMessage());
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
