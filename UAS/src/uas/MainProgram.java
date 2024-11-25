/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas;

/**
 *
 * @author raman
 */
// MainProgram.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainProgram extends JFrame {
    private JButton btnKonsumen, btnTransaksi, btnBarang;

    public MainProgram() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Main Program");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create buttons
        btnKonsumen = new JButton("Manage Konsumen");
        btnTransaksi = new JButton("Manage Transaksi");
        btnBarang = new JButton("Manage Barang");

        // Add Action Listeners
        btnKonsumen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open FrameKonsumen
                new FrameKonsumen().setVisible(true);
            }
        });

        btnTransaksi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open FrameTransaksi
                new FrameTransaksi().setVisible(true);
            }
        });

        btnBarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open FrameBarang
                new FrameBarang().setVisible(true);
            }
        });

        // Layout for the main window
        setLayout(new GridLayout(3, 1, 10, 10));
        add(btnKonsumen);
        add(btnTransaksi);
        add(btnBarang);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainProgram().setVisible(true);
            }
        });
    }
}
