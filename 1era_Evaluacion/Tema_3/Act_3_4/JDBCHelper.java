package com.mycompany.activity3_4;

import java.sql.*;
import java.util.logging.*;

/**
 *
 * @author 7J
 */
public class JDBCHelper {
        public static void showResultSet(ResultSet res) {
        try {
            String aux = "";
            ResultSetMetaData meta = res.getMetaData();
            int numC = meta.getColumnCount();
            System.out.println("----------------------------------------");

            while (res.next()) {
                for (int i = 1; i <= numC; i++) {
                    aux += meta.getColumnName(i) + ": " + res.getString(i) + " ";
                }
                aux += "\n";
            }
            System.out.println(aux);
            System.out.println("----------------------------------------");

        } catch (SQLException ex) {
            Logger.getLogger(JDBCHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
