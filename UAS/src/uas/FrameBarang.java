/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas;

/**
 *
 * @author raman
 */
// FrameBarang.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FrameBarang extends JFrame {
    private JTextField txtIdBarang, txtNamaBarang, txtHarga, txtStok;
    private JButton btnSimpan, btnUpdate, btnHapus, btnBersih;
    private JTable tblBarang;
    private DefaultTableModel tableModel;

    public FrameBarang() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Data Barang");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Input
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Data Barang"));

        inputPanel.add(new JLabel("ID Barang:"));
        txtIdBarang = new JTextField();
        inputPanel.add(txtIdBarang);

        inputPanel.add(new JLabel("Nama Barang:"));
        txtNamaBarang = new JTextField();
        inputPanel.add(txtNamaBarang);

        inputPanel.add(new JLabel("Harga:"));
        txtHarga = new JTextField();
        inputPanel.add(txtHarga);

        inputPanel.add(new JLabel("Stok:"));
        txtStok = new JTextField();
        inputPanel.add(txtStok);

        // Panel Button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSimpan = new JButton("Simpan");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnBersih = new JButton("Bersih");

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnBersih);

        // Table
        String[] columns = {"ID Barang", "Nama Barang", "Harga", "Stok"};
        tableModel = new DefaultTableModel(columns, 0);
        tblBarang = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblBarang);

        // Layout
        setLayout(new BorderLayout(5, 5));
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Action Listeners
        btnSimpan.addActionListener(e -> simpanData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> hapusData());
        btnBersih.addActionListener(e -> bersihForm());

        tblBarang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tableSelectionChanged();
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM barang")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getDouble("harga"),
                    rs.getInt("stok")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void simpanData() {
        try {
            if (!validateInput()) return;

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO barang (id_barang, nama_barang, harga, stok) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, txtIdBarang.getText());
                pstmt.setString(2, txtNamaBarang.getText());
                pstmt.setDouble(3, Double.parseDouble(txtHarga.getText()));
                pstmt.setInt(4, Integer.parseInt(txtStok.getText()));

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
                loadData();
                bersihForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateData() {
        try {
            if (!validateInput()) return;

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE barang SET nama_barang=?, harga=?, stok=? WHERE id_barang=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, txtNamaBarang.getText());
                pstmt.setDouble(2, Double.parseDouble(txtHarga.getText()));
                pstmt.setInt(3, Integer.parseInt(txtStok.getText()));
                pstmt.setString(4, txtIdBarang.getText());

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate");
                loadData();
                bersihForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void hapusData() {
        try {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "DELETE FROM barang WHERE id_barang=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, txtIdBarang.getText());

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
                loadData();
                bersihForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void bersihForm() {
        txtIdBarang.setText("");
        txtNamaBarang.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        txtIdBarang.requestFocus();
    }

    private void tableSelectionChanged() {
        int selectedRow = tblBarang.getSelectedRow();
        if (selectedRow != -1) {
            txtIdBarang.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtNamaBarang.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtHarga.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtStok.setText(tableModel.getValueAt(selectedRow, 3).toString());
        }
    }

    private boolean validateInput() {
        if (txtIdBarang.getText().isEmpty() || 
            txtNamaBarang.getText().isEmpty() || 
            txtHarga.getText().isEmpty() || 
            txtStok.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return false;
        }
        try {
            Double.parseDouble(txtHarga.getText());
            Integer.parseInt(txtStok.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka!");
            return false;
        }
        return true;
    }
}
