package com.mycompany.ejercicio1;

/*
 * @author 7J
 */
import java.sql.*;
import java.util.*;

public class Ejercicio1 {

    //ESCANER PARA NUMEROS
    static Scanner tcl = new Scanner(System.in);
    //ESCANER PARA TEXTO
    static Scanner txt = new Scanner(System.in);

    //METODO PARA CONECTAR A LA BASE DE DATOS
    public static void connection(int opt) {
        Connection con = null;
        ResultSet resul = null;
        String route = "";

        try {
            if (opt == 1) {
                System.out.println("Estás en la base de datos SQLite");
                route = "jdbc:sqlite:.\\examen\\sqlite\\database";
            } else if (opt == 2) {
                System.out.println("Estás en la base de datos Derby");
                route = "jdbc:derby:.\\examen\\derby\\database";
            } else if (opt == 3) {
                System.out.println("Estás en la base de datos HSQLDB");
                route = "jdbc:hsqldb:.\\examen\\hsqldb\\database";
            }

            con = DriverManager.getConnection(route);
            DatabaseMetaData dbmd = con.getMetaData();
            System.out.println("Driver: " + dbmd.getDriverName());

            menu(con);

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //METODO DEL SEGUNDO MENU Y NUEVAS FUNCIONES
    public static void menu(Connection con) {
        int opt;
        do {
            System.out.println("¿Qué operación desea realizar?\n"
                    + "1. Mostrar un listado de alumnos\n"
                    + "2. Mostrar un listado de municipios\n"
                    + "3. Introducir un nuevo alumno (PreparedStatement)\n"
                    + "4. Introducir un nuevo municipio (Procedimiento)\n5. Salir");
            opt = tcl.nextInt();
            switch (opt) {
                case 1:
                    mostrar_alumnos(con);
                    break;
                case 2:
                    mostrar_municipios(con);
                    break;
                case 3:
                    introduce_alumno(con);
                    break;
                case 4:
                    introduce_municipio(con);
                    break;
                case 5:
                    System.out.println("Adios!!");
                    break;
                default:
                    System.out.println("Selecciona una opción válida!!");
                    break;
            }
        } while (opt != 5);
    }

    //MOSTRAR ALUMNOS
    public static void mostrar_alumnos(Connection con) {
        String sql;

        try {
            Statement stt = con.createStatement();
            sql = "SELECT nombre, apellidos, id_municipio FROM alumnos";
            ResultSet resu = stt.executeQuery(sql);
            JDBCHelper.showResultSet(resu);

            resu.close();
            stt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //MOSTRAR MUNICIPIOS
    public static void mostrar_municipios(Connection con) {
        String sql;

        try {
            Statement stt = con.createStatement();
            sql = "SELECT * FROM municipios";
            ResultSet resu = stt.executeQuery(sql);
            JDBCHelper.showResultSet(resu);

            resu.close();
            stt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //INTRODUCIR ALUMNO EN LA TABLA
    public static void introduce_alumno(Connection con) {
        String sql, nombre, apellido;
        int nia, id_municipio;
        PreparedStatement stt = null;

        try {
            System.out.println("Introduce los siguientes valores:\n");
            System.out.print("NIA:\n>");
            nia = tcl.nextInt();
            System.out.print("Nombre:\n>");
            nombre = txt.nextLine();
            System.out.print("Apellido\n>");
            apellido = txt.nextLine();
            System.out.print("ID municipio:\n>");
            id_municipio = tcl.nextInt();

            sql = "INSERT INTO alumnos VALUES(?,?,?,?);";
            stt = con.prepareStatement(sql);

            stt.setInt(1, nia);
            stt.setString(2, nombre);
            stt.setString(3, apellido);
            stt.setInt(4, id_municipio);
            stt.executeUpdate();
            
            stt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //INTRODUCIR MUNICIPIO SOLO EN HSQL
    public static void introduce_municipio(Connection con) {
        String cod, nombre, sql;
        int n_habitantes;
        PreparedStatement stt = null;
        CallableStatement call;

        try {
            DatabaseMetaData dbmd = con.getMetaData();
            if (dbmd.getDriverName().equals("HSQL Database Engine Driver")) {
                crear_procedimiento(con);

                System.out.println("Introduce los siguientes valores:");
                System.out.print("Codigo:\n>");
                cod = txt.nextLine();
                System.out.print("Nombre:\n>");
                nombre = txt.nextLine();
                System.out.print("Numero habitantes:\n>");
                n_habitantes = tcl.nextInt();

                //sql = "INSERT INTO municipios VALUES(?,?,?);";
                //stt = con.prepareStatement(sql);
                
                call = con.prepareCall("call nuevo_municipio (?,?,?)");

                call.setString(1, cod);
                call.setString(2, nombre);
                call.setInt(3, n_habitantes);
                call.executeUpdate();
                
                stt.close();
            } else {
                System.out.println("Esta función solo se puede realizar en la base de datos HSQL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void crear_procedimiento(Connection con) {
        String sql;

        try {
            Statement stt = con.createStatement();

            //DELETE PROCEDURE
            sql = "DROP PROCEDURE nuevo_municipio IF EXISTS";
            stt.executeUpdate(sql);

            sql = "CREATE PROCEDURE nuevo_municipio(IN cod VARCHAR(5), IN nombre VARCHAR(10), "
                    + "IN n_habitantes INT) "
                    + "modifies sql data "
                    + "BEGIN ATOMIC "
                    + "INSERT INTO municipios VALUES(cod, nombre, n_habitantes); "
                    + "END";
            stt.executeUpdate(sql);
            
            stt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\t...Procedimiento creado: nuevo_municipio");
    }

    public static void main(String[] args) {
        int opt;
        Connection con = null;

        do {
            System.out.println("¿Con qué SGBD quiere conectar?\n1. SQLite\n"
                    + "2. Derby\n3. HSQLDB\n4. Salir");
            opt = tcl.nextInt();
            switch (opt) {
                case 1:
                    connection(1);
                    break;
                case 2:
                    connection(2);
                    break;
                case 3:
                    connection(3);
                    break;
                case 4:
                    System.out.println("Adios!!");
                    break;
                default:
                    System.out.println("Selecciona una opción válida!!");
                    break;
            }
        } while (opt != 4);
    }
}
