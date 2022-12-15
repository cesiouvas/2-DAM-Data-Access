package com.mycompany.pruebota;

import java.io.IOException;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.logging.*;
import java.util.*;

/*
 * @author 7J
 */
public class Act3 {

public static int menu() throws IOException {
        Scanner tcl = new Scanner(System.in);
        int opt = 0;

        do {
            System.out.println("\nChoose a database:\n1. SQLite Database\n2. Derby Database\n3. HSQLDB Database\n4. Exit\n");
            try {
                opt = tcl.nextInt();
            } catch (InputMismatchException e) {
                tcl.next();
                System.out.println("Introduce a correct number");
            }
            if (opt < 1 || opt > 4) {
                System.out.println("Select a valid option");
            }
            switch (opt) {
                case 2:
                    return opt;
                case 1:
                    return opt;
                case 3:
                    return opt;
                case 4:
                    return opt;
                default:
                    System.out.println("Invalid option");
            }
        } while (opt != 4);
        return 0;
    }

    public static void management(int opt) throws IOException {
        int opc;
        Connection con = null;
        ResultSet resul = null;
        String route = "";

        try {
            if (opt == 1) {
                System.out.println("You are in the DataBase SQLite\n");
                route = "jdbc:sqlite:.\\db\\sqlite\\act2.1";
            } else if (opt == 2) {
                System.out.println("You are in the DataBase Derby\n");
                route = "jdbc:derby:.\\db\\derby\\act2.2";
            } else if (opt == 3) {
                System.out.println("You are in the DataBase HSQLDB\n");
                route =  "jdbc:hsqldb:.\\db\\hsqldb\\act2.3";
            }

            con = DriverManager.getConnection(route);
            System.out.println(con.getSchema());
            DatabaseMetaData dbmd = con.getMetaData();

            System.out.println("----------------------------------------");
            System.out.println("DATABASE INFORMATION");
            System.out.println("----------------------------------------");

            System.out.println("Name: " + dbmd.getDatabaseProductName());
            System.out.println("Driver: " + dbmd.getDriverName());
            System.out.println("URL: " + dbmd.getURL());
            System.out.println("User: " + dbmd.getUserName());

            System.out.println("----------------------------------------");
            System.out.println("TABLES INFORMATION");

            if (opt == 1) {
                resul = dbmd.getTables(null, null, null, null); //all tables
            } else if (opt == 2) {
                resul = dbmd.getTables(null, "APP", null, null); //all tables
            } else if (opt == 3) {
                resul = dbmd.getTables(null, "PUBLIC", null, null); //all tables
            }

            while (resul.next()) {
                String catalogo = resul.getString(1); // column 1: TABLE_CAT
                String esquema = resul.getString(2); // column 2: TABLE_SCHEM
                String tabla = resul.getString(3); // column 3: TABLE_NAME
                String tipo = resul.getString(4); // column 4: TABLE_TYPE

                System.out.println("----------------------------------------");
                System.out.println("Table name: " + tabla + " Catalog: " + catalogo
                        + " Schema: " + esquema + " Type: " + tipo);
                System.out.println("***   COLUMNS OF TABLE " + tabla + "   ***");

                ResultSet columnas = dbmd.getColumns(null, null, tabla, null);
                while (columnas.next()) {
                    String nombreCol = columnas.getString("COLUMN_NAME");
                    String tipoCol = columnas.getString("TYPE_NAME");
                    String nula = columnas.getString("IS_NULLABLE");

                    System.out.println("Columna: " + nombreCol + " tipo: "
                            + tipoCol + " Admite null: " + nula);
                }
            }
            con.close();
        } catch (SQLException e) {
            Logger.getLogger(Act3.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void main(String[] args) throws IOException {
        int opt;
        do {
            opt = menu();
            switch (opt) {
                case 1:
                    management(1);
                    break;
                case 2:
                    management(2);
                    break;
                case 3:
                    management(3);
                    break;
                case 4:
                    System.out.println("Bye");
                    break;
            }
        } while (opt != 4);
    }
}