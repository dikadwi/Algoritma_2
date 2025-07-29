package gui;

import java.awt.*;
import javax.swing.*;

public class RiwayatTransaksiPanel extends JPanel {

    public RiwayatTransaksiPanel() {
        setLayout(new BorderLayout());

        // Panel Judul
        JLabel title = new JLabel("RiwayatTransaksi", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Konten utama bisa dilanjutkan di sini
        JPanel content = new JPanel();
        content.add(new JLabel("Konten RiwayatTransaksiPanel"));
        add(content, BorderLayout.CENTER);
    }
}
