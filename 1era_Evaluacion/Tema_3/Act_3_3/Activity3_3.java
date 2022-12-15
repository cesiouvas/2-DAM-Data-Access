package com.mycompany.activity3_3;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.*;

/*
 * @author 7J
 */
public class Activity3_3 {

    public static void deleteDir(File path) {
        File[] fich = path.listFiles();
        if (fich != null) {
            for (File juan : fich) {
                if (juan.isDirectory()) {
                    deleteDir(juan);
                }
                juan.delete();
            }
        }
    }

    public static void createDB(File path) {
        Connection con = null;

        try {
            con = DriverManager.getConnection("jdbc:derby:" + path + "; create = true;");
            Statement stt = con.createStatement();
            System.out.println("+ Database created");
            con.close();
        } catch (SQLException e) {
            Logger.getLogger(Activity3_3.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void createDept(File path) {
        Connection con = null;
        String sql;

        try {
            System.out.println("Creating table departments..");
            con = DriverManager.getConnection("jdbc:derby:" + path);
            Statement stt = con.createStatement();
            sql = "CREATE TABLE departments (dept_num int not null primary key,"
                    + "name VARCHAR(30),"
                    + "office VARCHAR(30))";
            int affectedRows = stt.executeUpdate(sql);
            System.out.println("+ Table created\n");
        } catch (SQLException e) {
            System.out.println("- Error while creating the table");
        }
    }
    
        public static void createTeach(File path) {
        Connection con = null;
        String sql;

        try {
            System.out.println("Creating table teachers..");
            con = DriverManager.getConnection("jdbc:derby:" + path);
            Statement stt = con.createStatement();
            sql = "CREATE TABLE teachers ("
                    + "id int not null primary key,"
                    + "name VARCHAR(30),"
                    + "surname VARCHAR(30),"
                    + "email VARCHAR(50),"
                    + "start_date DATE,"
                    + "dept_num INT,"
                    + "FOREIGN KEY (dept_num)"
                    + "REFERENCES departments (dept_num))";
            int affectedRows = stt.executeUpdate(sql);
            System.out.println("+ Table created\n");
        } catch (SQLException e) {
            System.out.println("- Error while creating the table");
        }
    }

    public static void readFile(File path, int num) {
        File f = null;
        FileReader fr = null;
        BufferedReader br = null;
        String line;
        int cont = 0;

        try {
            if (num == 1) {
                System.out.println("Filling the table departments...");
                f = new File("files\\departments.txt");
            } else if (num == 2) {
                System.out.println("Filling the table teachers...");
                f = new File("files\\teachers.txt");
            }

            fr = new FileReader(f);
            br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {
                word(line, path, num);
                cont++;
            }
            System.out.println("Affected rows: " + cont);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void word(String linea, File path, int num) {
        if (linea == null || linea.length() <= 0) {

        } else {
            String[] valor = linea.split(",");
            insertData(valor, path, num);
        }
    }

    public static void insertData(String[] valor, File path, int num) {
        Connection con = null;
        String sql;
        PreparedStatement stt = null;

        try {
            con = DriverManager.getConnection("jdbc:derby:" + path);

            if (num == 1) {
                sql = "INSERT INTO departments VALUES (?,?,?)";
                stt = con.prepareStatement(sql);

                stt.setInt(1, Integer.parseInt(valor[0]));
                stt.setString(2, valor[1]);
                stt.setString(3, valor[2]);
                stt.executeUpdate();
            } else if (num == 2) {
                sql = "INSERT INTO teachers VALUES (?,?,?,?,?,?)";
                stt = con.prepareStatement(sql);

                stt.setInt(1, Integer.parseInt(valor[0]));
                stt.setString(2, valor[1]);
                stt.setString(3, valor[2]);
                stt.setString(4, valor[3]);
                if (valor[4].equals("")) {
                    stt.setNull(5, Types.DATE);
                } else {
                    try {
                        stt.setDate(5, date(valor[4]));
                    } catch (Exception e) {
                        System.out.println("Date wrong");
                    }
                    //String format2 = formato.format(valor[4]);
                    //java.sql.Date fech = java.sql.Date.valueOf(format2);
                    
                }
                stt.setString(6, valor[5]);
                stt.executeUpdate();
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static java.sql.Date date(String notFormat) {
        java.sql.Date date = null;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-mm-dd");
        
        try {
            java.util.Date dateFormat = formato.parse(notFormat);
            date = new Date(dateFormat.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void select(File path, int num) {
        Connection con = null;
        String sql = "";

        try {
            con = DriverManager.getConnection("jdbc:derby:" + path);
            Statement stt = con.createStatement();
            if (num == 1) {
                sql = "SELECT * FROM departments";
            } else if (num == 2) {
                sql = "SELECT * FROM teachers";
            }

            ResultSet resu = stt.executeQuery(sql);
            JDBCHelper.showResultSet(resu);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String dir = "database\\act3_3.db";
        File f = new File(dir);
        String dir2 = ".\\database";
        File f2 = new File(dir2);
        int nFich;

        //DELETE DATABASE
        deleteDir(f2);
        System.out.println("+ Database deleted\n");

        //CREATE DATABASE
        createDB(f);

        // CREATE AND INSERT DEPARTMENTS
        nFich = 1;
        createDept(f);
        readFile(f, nFich);

        System.out.println("\nSELECT * FROM departments");
        select(f, nFich);

        //CREATE AND INSERT TEACHERS
        nFich = 2;
        createTeach(f);
        readFile(f, nFich);

        System.out.println("\nSELECT * FROM teachers");
        select(f, nFich);
    }
}
