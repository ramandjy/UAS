/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas;

/**
 *
 * @author raman
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/db_toko"; // Ganti 'db_toko' dengan nama database Anda
    private static final String USERNAME = "root"; // Ganti sesuai username MySQL Anda
    private static final String PASSWORD = ""; // Ganti sesuai password MySQL Anda

    /**
     * Membuat koneksi ke database.
     *
     * @return Connection object jika berhasil, null jika gagal.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Koneksi ke database berhasil!");
        } catch (SQLException e) {
            System.err.println("Gagal menghubungkan ke database!");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Menutup koneksi database.
     *
     * @param connection Connection object yang akan ditutup.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Koneksi database ditutup.");
            } catch (SQLException e) {
                System.err.println("Gagal menutup koneksi database!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method untuk pengujian koneksi.
     */
    public static void main(String[] args) {
        Connection connection = SQLConnection.getConnection();
        SQLConnection.closeConnection(connection);
    }
}

