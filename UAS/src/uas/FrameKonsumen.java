/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas;

/**
 *
 * @author raman
 */
// FrameKonsumen.java
// FrameKonsumen.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FrameKonsumen extends JFrame {
    private JTextField txtIdKonsumen, txtNamaKonsumen, txtAlamat, txtNoTelp, txtEmail;
    private JButton btnSimpan, btnUpdate, btnHapus, btnBersih;
    private JTable tblKonsumen;
    private DefaultTableModel tableModel;

    public FrameKonsumen() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Data Konsumen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Input
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Data Konsumen"));

        inputPanel.add(new JLabel("ID Konsumen:"));
        txtIdKonsumen = new JTextField();
        inputPanel.add(txtIdKonsumen);

        inputPanel.add(new JLabel("Nama Konsumen:"));
        txtNamaKonsumen = new JTextField();
        inputPanel.add(txtNamaKonsumen);

        inputPanel.add(new JLabel("Alamat:"));
        txtAlamat = new JTextField();
        inputPanel.add(txtAlamat);

        inputPanel.add(new JLabel("No. Telp:"));
        txtNoTelp = new JTextField();
        inputPanel.add(txtNoTelp);

        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        inputPanel.add(txtEmail);

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
        String[] columns = {"ID Konsumen", "Nama Konsumen", "Alamat", "No. Telp", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        tblKonsumen = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblKonsumen);

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

        tblKonsumen.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tableSelectionChanged();
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM konsumen")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("id_konsumen"),
                    rs.getString("nama_konsumen"),
                    rs.getString("alamat"),
                    rs.getString("no_telp"),
                    rs.getString("email")
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
                String sql = "INSERT INTO konsumen (id_konsumen, nama_konsumen, alamat, no_telp, email) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, txtIdKonsumen.getText());
                pstmt.setString(2, txtNamaKonsumen.getText());
                pstmt.setString(3, txtAlamat.getText());
                pstmt.setString(4, txtNoTelp.getText());
                pstmt.setString(5, txtEmail.getText());

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
                String sql = "UPDATE konsumen SET nama_konsumen=?, alamat=?, no_telp=?, email=? WHERE id_konsumen=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, txtNamaKonsumen.getText());
                pstmt.setString(2, txtAlamat.getText());
                pstmt.setString(3, txtNoTelp.getText());
                pstmt.setString(4, txtEmail.getText());
                pstmt.setString(5, txtIdKonsumen.getText());

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
                String sql = "DELETE FROM konsumen WHERE id_konsumen=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, txtIdKonsumen.getText());

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
        txtIdKonsumen.setText("");
        txtNamaKonsumen.setText("");
        txtAlamat.setText("");
        txtNoTelp.setText("");
        txtEmail.setText("");
        txtIdKonsumen.requestFocus();
    }

    private void tableSelectionChanged() {
        int selectedRow = tblKonsumen.getSelectedRow();
        if (selectedRow != -1) {
            txtIdKonsumen.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtNamaKonsumen.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtAlamat.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtNoTelp.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow, 4).toString());
        }
    }

    private boolean validateInput() {
        if (txtIdKonsumen.getText().isEmpty() || 
            txtNamaKonsumen.getText().isEmpty() || 
            txtAlamat.getText().isEmpty() || 
            txtNoTelp.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return false;
        }
        return true;
    }
}
