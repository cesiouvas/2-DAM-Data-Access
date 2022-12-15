package com.mycompany.activity3_2;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

/*
 * @author 7J
 */
public class Activity3_2 {

    static Scanner tcl = new Scanner(System.in);
    static Scanner txt = new Scanner(System.in);

    public static int menu() throws IOException {
        int opt = 0;

        do {
            System.out.println("\nSelect an option:\n1. Show all teachers\n"
                    + "2. Show all departments\n3. Add new teacher\n"
                    + "4. Add new department\n5. Add salary column to teachers\n"
                    + "6. Evaluate custom query\n7. Quit");
            try {
                opt = tcl.nextInt();
            } catch (InputMismatchException e) {
                tcl.next();
                e.printStackTrace();
            }
            if (opt < 1 || opt > 7) {
                System.out.println("Select a valid option");
            }
            switch (opt) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
                case 4:
                    return 4;
                case 5:
                    return 5;
                case 6:
                    return 6;
                case 7:
                    return 7;
                default:
                    System.out.println("Invalid option");
            }
        } while (opt != 7);
        return 0;
    }

    public static void show(int opt) {
        String select = "";
        try {
            Connection con = null;
            con = DriverManager.getConnection("jdbc:sqlite:.\\db\\DB");
            Statement stt = con.createStatement();

            if (opt == 1) {
                select = "Select * from teachers";
            } else if (opt == 2) {
                select = "Select * from departments";
            }

            ResultSet resu = stt.executeQuery(select);
            JDBCHelper.showResultSet(resu);
            con.close();
        } catch (SQLException e) {
            Logger.getLogger(Activity3_2.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void insertT() {
        int id, dept_num, salary;
        String name, surname, email, date, sql, col = "salary";

        try {
            Connection con = null;
            con = DriverManager.getConnection("jdbc:sqlite:.\\db\\DB");
            Statement stt = con.createStatement();

            try {

                //INSERT VALUES
                System.out.println("Insert the following values");
                System.out.print("ID:\n>");
                id = tcl.nextInt();
                System.out.print("Name:\n>");
                name = txt.nextLine();
                System.out.print("Surname:\n>");
                surname = txt.nextLine();
                System.out.print("Email:\n>");
                email = txt.nextLine();
                System.out.print("Date (yyy-mm-dd):\n>");
                date = txt.nextLine();
                System.out.print("Department number:\n>");
                dept_num = tcl.nextInt();

                ResultSet resu = stt.executeQuery("SELECT * FROM teachers");

                //IF COLUMN SALARY EXISTS
                if (JDBCHelper.contains(resu, col) == true) {
                    System.out.print("Salary:\n>");
                    salary = tcl.nextInt();
                    //INSERT QUERY WITH SALARY
                    sql = String.format("INSERT INTO teachers VALUES('%d','%s','%s',"
                            + "'%s','%s','%d','%d')", id, name, surname, email, date, dept_num, salary);
                    int affectedRows = stt.executeUpdate(sql);
                    System.out.println("Affected rows: " + affectedRows);
                } else {
                    //INSERT QUERY
                    sql = String.format("INSERT INTO teachers VALUES('%d','%s','%s',"
                            + "'%s','%s','%d')", id, name, surname, email, date, dept_num);
                    int affectedRows = stt.executeUpdate(sql);
                    System.out.println("Affected rows: " + affectedRows);
                }
            } catch (InputMismatchException em) {
                System.out.println("Incorrect format");
            }
            tcl.nextLine();
            con.close();

        } catch (SQLException e) {
            Logger.getLogger(Activity3_2.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void insertD() {
        int dept_num;
        String name, office, sql;

        try {
            Connection con = null;
            con = DriverManager.getConnection("jdbc:sqlite:.\\db\\DB");
            Statement stt = con.createStatement();

            try {
                //INSERT VALUES
                System.out.println("Insert the following values");
                System.out.print("Department number:\n>");
                dept_num = tcl.nextInt();
                System.out.print("Department name:\n>");
                name = txt.nextLine().toUpperCase();
                System.out.print("Office:\n>");
                office = txt.nextLine();

                //INSERT QUERY
                sql = String.format("INSERT INTO departments VALUES('%d','%s','%s')"
                        + "", dept_num, name, office);
                int affectedRows = stt.executeUpdate(sql);
                System.out.println("Affected rows: " + affectedRows);

            } catch (InputMismatchException em) {
                System.out.println("Incorrect format");
            }
            tcl.nextLine();
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(Activity3_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void column() {
        String col = "salary";

        try {
            Connection con = null;
            con = DriverManager.getConnection("jdbc:sqlite:.\\db\\DB");
            Statement stt = con.createStatement();
            ResultSet resu = stt.executeQuery("SELECT * FROM teachers");

            if (JDBCHelper.contains(resu, col) == true) {
                System.out.println("The column is already defined in the table");
            } else {
                System.out.println("Adding the column SALARY in the table teachers");
                int affectedRows = stt.executeUpdate("ALTER TABLE teachers ADD COLUMN salary decimal");
                System.out.println("Affected rows: " + affectedRows);
            }
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Activity3_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void customQuery() {
        String query;

        try {
            Connection con = null;
            con = DriverManager.getConnection("jdbc:sqlite:.\\db\\DB");
            Statement stt = con.createStatement();

            System.out.println("Introduce a SELECT query: ");
            query = txt.nextLine();

            if (query.contains("SELECT")) {
                ResultSet resu = stt.executeQuery(query);
                JDBCHelper.showResultSet(resu);
            } else {
                System.out.println("ERROR: The sentence is not a SELECT query");
            }
            con.close();

        } catch (SQLException e) {
            Logger.getLogger(Activity3_2.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void main(String[] args) {
        try {
            int opt;
            do {
                opt = menu();
                switch (opt) {
                    case 1:
                        show(1);
                        break;
                    case 2:
                        show(2);
                        break;
                    case 3:
                        insertT();
                        break;
                    case 4:
                        insertD();
                        break;
                    case 5:
                        column();
                        break;
                    case 6:
                        customQuery();
                        break;
                    case 7:
                        System.out.println("Bye!");
                        break;
                }
            } while (opt != 7);
        } catch (IOException ex) {
            Logger.getLogger(Activity3_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
