/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas;

/**
 *
 * @author raman
 */
// FrameTransaksi.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FrameTransaksi extends JFrame {
    private JTextField txtIdTransaksi, txtIdKonsumen, txtIdBarang, txtJumlah, txtTotalHarga;
    private JButton btnSimpan, btnUpdate, btnHapus, btnBersih;
    private JTable tblTransaksi;
    private DefaultTableModel tableModel;

    public FrameTransaksi() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Data Transaksi");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Input
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Data Transaksi"));

        inputPanel.add(new JLabel("ID Transaksi:"));
        txtIdTransaksi = new JTextField();
        inputPanel.add(txtIdTransaksi);

        inputPanel.add(new JLabel("ID Konsumen:"));
        txtIdKonsumen = new JTextField();
        inputPanel.add(txtIdKonsumen);

        inputPanel.add(new JLabel("ID Barang:"));
        txtIdBarang = new JTextField();
        inputPanel.add(txtIdBarang);

        inputPanel.add(new JLabel("Jumlah:"));
        txtJumlah = new JTextField();
        inputPanel.add(txtJumlah);

        inputPanel.add(new JLabel("Total Harga:"));
        txtTotalHarga = new JTextField();
        inputPanel.add(txtTotalHarga);

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
        String[] columns = {"ID Transaksi", "ID Konsumen", "ID Barang", "Jumlah", "Total Harga"};
        tableModel = new DefaultTableModel(columns, 0);
        tblTransaksi = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblTransaksi);

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
        
        tblTransaksi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tableSelectionChanged();
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_toko", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM transaksi");

            while (rs.next()) {
                Object[] row = {
                    rs.getString("id_transaksi"),
                    rs.getString("id_konsumen"),
                    rs.getString("id_barang"),
                    rs.getInt("jumlah"),
                    rs.getDouble("total_harga")
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

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_toko", "root", "");
            String sql = "INSERT INTO transaksi (id_transaksi, id_konsumen, id_barang, jumlah, total_harga) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, txtIdTransaksi.getText());
            pstmt.setString(2, txtIdKonsumen.getText());
            pstmt.setString(3, txtIdBarang.getText());
            pstmt.setInt(4, Integer.parseInt(txtJumlah.getText()));
            pstmt.setDouble(5, Double.parseDouble(txtTotalHarga.getText()));

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            loadData();
            bersihForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateData() {
        try {
            if (!validateInput()) return;

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_toko", "root", "");
            String sql = "UPDATE transaksi SET id_konsumen=?, id_barang=?, jumlah=?, total_harga=? WHERE id_transaksi=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, txtIdKonsumen.getText());
            pstmt.setString(2, txtIdBarang.getText());
            pstmt.setInt(3, Integer.parseInt(txtJumlah.getText()));
            pstmt.setDouble(4, Double.parseDouble(txtTotalHarga.getText()));
            pstmt.setString(5, txtIdTransaksi.getText());

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate");
            loadData();
            bersihForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void hapusData() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_toko", "root", "");
            String sql = "DELETE FROM transaksi WHERE id_transaksi=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtIdTransaksi.getText());

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
            loadData();
            bersihForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void bersihForm() {
        txtIdTransaksi.setText("");
        txtIdKonsumen.setText("");
        txtIdBarang.setText("");
        txtJumlah.setText("");
        txtTotalHarga.setText("");
        txtIdTransaksi.requestFocus();
    }

    private void tableSelectionChanged() {
        int selectedRow = tblTransaksi.getSelectedRow();
        if (selectedRow != -1) {
            txtIdTransaksi.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtIdKonsumen.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtIdBarang.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtJumlah.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtTotalHarga.setText(tableModel.getValueAt(selectedRow, 4).toString());
        }
    }

    private boolean validateInput() {
        if (txtIdTransaksi.getText().isEmpty() || 
            txtIdKonsumen.getText().isEmpty() || 
            txtIdBarang.getText().isEmpty() || 
            txtJumlah.getText().isEmpty() || 
            txtTotalHarga.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return false;
        }
        try {
            Integer.parseInt(txtJumlah.getText());
            Double.parseDouble(txtTotalHarga.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah dan Total Harga harus berupa angka!");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrameTransaksi().setVisible(true));
    }
}
