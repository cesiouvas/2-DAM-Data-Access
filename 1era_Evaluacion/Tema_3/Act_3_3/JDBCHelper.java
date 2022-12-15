package com.mycompany.activity3_3;

import java.sql.*;
import java.util.logging.*;

/*
 * @author 7J
 */
public class JDBCHelper {

    public static void showResultSet(ResultSet res) {
        try {
            String aux = "";
            ResultSetMetaData meta = res.getMetaData();
            int numC = meta.getColumnCount();

            while (res.next()) {
                aux += "--> ";
                for (int i = 1; i <= numC; i++) {
                    aux += meta.getColumnName(i) + ": " + res.getString(i) + " ";
                }
                aux += "\n";
            }
            System.out.println(aux);

        } catch (SQLException ex) {
            Logger.getLogger(JDBCHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
