package com.mycompany.activity3_4;

import java.sql.*;

/*
 * @author 7J
 */
public class Activity3_4 {

    public static void main(String[] args) {
        Connection con = null;
        
        try {
            con = DriverManager.getConnection("jdbc:hsqldb:.\\db\\hsqldb\\act2.3");
            System.out.println("+ Connection established");
            
            //procedure1(con);
            //suma(con);
            //totalDept(con);
            //deptId(con);
            veteran(con);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void totalDept(Connection con) {
        String sql;
        try {
            Statement stt = con.createStatement();
            
            //DELETE PROCEDURE
            sql = "DROP PROCEDURE totalDept IF EXISTS";
            stt.executeUpdate(sql);
            
            sql = "CREATE PROCEDURE totalDept(OUT c INT) "
                    + "reads sql data "
                    + "BEGIN ATOMIC "
                    + "SET c = SELECT COUNT(*) FROM departments; "
                    + "END";
            stt.executeUpdate(sql);
            
            sql = "SELECT * FROM departments";
            JDBCHelper.showResultSet(stt.executeQuery(sql));
            
            CallableStatement call = con.prepareCall("call totalDept(?)");
            call.registerOutParameter(1, Types.INTEGER);
            call.execute();
            
            System.out.println("Numero departamentos " + call.getInt(1));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void deptId(Connection con) {
        String sql;
        try {
            Statement stt = con.createStatement();
            
            //DELETE PROCEDURE
            sql = "DROP PROCEDURE deptId IF EXISTS";
            stt.executeUpdate(sql);
            
            sql = "CREATE PROCEDURE deptId(OUT id INT, IN dept_name VARCHAR(20)) "
                    + "reads sql data "
                    + "BEGIN ATOMIC "
                    + "SET id = SELECT dept_num FROM departments "
                    + "WHERE dept_name = name; "
                    + "END";
            stt.executeUpdate(sql);
            
            sql = "SELECT * FROM departments";
            JDBCHelper.showResultSet(stt.executeQuery(sql));
            
            CallableStatement call = con.prepareCall("call deptId(?,?)");
            call.setString(2, "INFORMATICA");
            call.registerOutParameter(1, Types.INTEGER);
            call.execute();
            
            System.out.println("Numero departamentos " + call.getInt(1));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void veteran(Connection con) {
        String sql;
        try {
            Statement stt = con.createStatement();
            
            //DELETE PROCEDURE
            sql = "DROP PROCEDURE veteran IF EXISTS";
            stt.executeUpdate(sql);
            
            sql = "CREATE PROCEDURE veteran(OUT name VARCHAR(20)) "
                    + "reads sql data "
                    + "BEGIN ATOMIC "
                    + "SET name = SELECT name FROM teachers "
                    + "WHERE start_date IS NOT NULL "
                    + "ORDER BY start_date asc "
                    + "LIMIT 1; "
                    + "END";
            stt.executeUpdate(sql);
            
            sql = "SELECT * FROM teachers";
            JDBCHelper.showResultSet(stt.executeQuery(sql));
            
            CallableStatement call = con.prepareCall("call veteran(?)");
            call.registerOutParameter(1, Types.VARCHAR);
            call.execute();
            
            System.out.println("Veterana " + call.getString(1));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void procedure1(Connection con) {
        String sql;
        try {
            Statement stt = con.createStatement();

            //DELETE THE PROCEDURE
            sql = "DROP PROCEDURE changeOffice IF EXISTS";
            stt.executeUpdate(sql);

            sql = "CREATE PROCEDURE changeOffice() "
                    + "modifies sql data "
                    + "UPDATE departments SET office = 'OFFICE';";
            stt.executeUpdate(sql);

            sql = "SELECT * FROM departments";
            JDBCHelper.showResultSet(stt.executeQuery(sql));

            CallableStatement call = con.prepareCall("call changeOffice()");
            call.execute();
            JDBCHelper.showResultSet(stt.executeQuery(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void suma(Connection con) {
        String sql;
        try {
            Statement stt = con.createStatement();
            sql = "DROP PROCEDURE suma IF EXISTS";
            stt.executeUpdate(sql);

            sql = "CREATE PROCEDURE suma (in a int, in b int, out res int)"
                    + "set res = a+b;";
            stt.executeUpdate(sql);

            CallableStatement call = con.prepareCall("call suma(?,?,?)");
            call.setInt(1, 1);
            call.setInt(2, 1);

            call.registerOutParameter(3, Types.INTEGER);
            call.execute();

            System.out.println("proc: " + call.getInt(3));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
