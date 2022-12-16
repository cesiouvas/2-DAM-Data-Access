package pojos;

/*
 * @author Sergio Castillo Llorens
 */

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.hibernate.*;

public class Act4_2 {
    
    static Scanner tcl = new Scanner(System.in);
    static Scanner txt = new Scanner(System.in);
    
    public static int menu() {
        int opc = 0;
        
        do {
            try {
                System.out.println("Select an option:\n"
                    + "1. Show a department by ID\n"
                    + "2. Show a teacher by ID\n"
                    + "3. Show the teachers in existing department\n"
                    + "4. Create new department\n"
                    + "5. Create new teacher with new department associated\n"
                    + "6. Create new teacher with existing department associated\n"
                    + "7. Delete teacher\n"
                    + "8. Delete department\n"
                    + "9. Set salary of whole department\n"
                    + "10. Rise salary for seniors of department\n"
                    + "11. Quit");
            opc = tcl.nextInt();
            } catch (InputMismatchException e) {
                tcl.next();
                e.printStackTrace();
            }
            switch(opc) {
                case 1:
                    return opc;
                case 2: 
                    return opc;
                case 3: 
                    return opc;
                case 4: 
                    return opc;
                case 5: 
                    return opc;
                case 6: 
                    return opc;
                case 7: 
                    return opc;
                case 8: 
                    return opc; 
                case 9: 
                    return opc; 
                case 10: 
                    return opc;
                case 11: 
                    return opc;
                default:
                    return 0;
            }
        } while (opc != 11);
    }

    public static void showDepartment(Session sesion) {
        int id;
        
        System.out.println("---------------------------------");
        System.out.print("Introduce an ID for the department\n>");
        id = tcl.nextInt();
        Departments dep;
        try {
            dep = (Departments) sesion.get(Departments.class, id);
            System.out.println("\n=============================");
            System.out.println("DATA OF THE DEPARTMENT " + id);
            
            System.out.println("Name: " + dep.getName());
            System.out.println("Office: " + dep.getOffice());
            System.out.println("=============================\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sesion.close();
    }
    
    public static void showTeacher(Session sesion) {
        int id;
        
        System.out.println("---------------------------------");
        System.out.print("Introduce an ID for the teacher\n>");
        id = tcl.nextInt();
        Teachers te;
        try {
            te = (Teachers) sesion.get(Teachers.class, id);
            System.out.println("\n=============================");
            System.out.println("DATA OF THE TEACHER " + id);
            
            System.out.println("Name: " + te.getName());
            System.out.println("Surname: " + te.getSurname());
            System.out.println("Email: " + te.getEmail());
            System.out.println("Start date: " + te.getStartDate());
            System.out.println("Salary: " + te.getSalary());
            System.out.println("Department: " + te.getDepartments().getName());
            System.out.println("=============================\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sesion.close();
    }
    
    public static void showTeachersInDepartment(Session sesion) {
        int id;
        
        System.out.println("---------------------------------");
        System.out.print("Introduce an ID for the department\n>");
        id = tcl.nextByte();
        Departments dep;
        
        try {
            dep = (Departments) sesion.get(Departments.class, id);
            System.out.println("\n=============================");
            System.out.println("DATA OF THE DEPARTMENT " + id);

            System.out.println("Name: " + dep.getName());
            System.out.println("Office: " + dep.getOffice());

            System.out.println("==============================");
            System.out.println("TEACHERS OF THE DEPARTMENT " + id);
            Set<Teachers> listaemple = dep.getTeacherses();
            for (Teachers te : listaemple) {
                System.out.println("ID: "+ te.getId() + " Name: " + te.getName() + ""
                        + "; Surname: " + te.getSurname()
                        + "; Salary: " + te.getSalary());
            }
            System.out.println("==============================\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sesion.close();
    }
    
    public static void createDepartment(Session sesion) {
        Transaction tx = null; 
        int id;
        Departments dep;
        
        try {
            System.out.println("---------------------------------");
            tx = sesion.beginTransaction();
            System.out.print("ID of the department: \n>");
            id = tcl.nextInt();
            dep = new Departments(id);
            System.out.print("Name: \n>");
            dep.setName(txt.nextLine());
            System.out.print("Office: \n>");
            dep.setOffice(txt.nextLine());
            System.out.print("Dept num: \n>");
            dep.setDeptNum(tcl.nextInt());

            System.out.println("(+)DEPARTMENT CREATED");
            sesion.save(dep);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        System.out.println("---------------------------------");
        sesion.close();
    }
    
    public static void createTeacherAndDepartment(Session sesion) {
        Transaction tx = null;
        Departments dep;
        Teachers t;
        int id, tid;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-mm-dd");
        Date fecha;
        
        try {
            System.out.println("---------------------------------");
            tx = sesion.beginTransaction();
            //DEPARTMENT
            System.out.print("Departmentd ID: \n>");
            id = tcl.nextInt();
            dep = new Departments(id);
            System.out.print("Name: \n>");
            dep.setName(txt.nextLine());
            System.out.print("Office: \n>");
            dep.setOffice(txt.nextLine());
            System.out.print("Dept num: \n>");
            dep.setDeptNum(tcl.nextInt());

            System.out.println("(+)DEPARTMENT CREATED");
            sesion.save(dep);

            //TEACHER
            System.out.print("Teacher ID: \n>");
            tid = tcl.nextInt();
            t = new Teachers(tid);
            System.out.print("Name: \n>");
            t.setName(txt.nextLine());
            System.out.print("Surname: \n>");
            t.setSurname(txt.nextLine());
            System.out.print("Email: \n>");
            t.setEmail(txt.nextLine());
            System.out.print("Start date (yyyy-mm-dd): \n>");
            fecha = formato.parse(txt.nextLine());
            System.out.print("Salary: \n>");
            t.setSalary(tcl.nextInt());
            t.setDepartments(dep);

            System.out.println("(+)TEACHER CREATED");
            sesion.save(t);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        System.out.println("---------------------------------");
        sesion.close();
    }
    
    public static void createTeacherInExistingDepartment(Session sesion) {
        Transaction tx = null;
        Departments dep;
        Teachers t;
        int tid, id;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-mm-dd");
        Date fecha;
        
        try {
            System.out.println("---------------------------------");
            tx = sesion.beginTransaction();
            System.out.print("Department ID:\n>");
            id = tcl.nextInt();
            dep = (Departments) sesion.get(Departments.class, id);
            
            //TEACHER
            System.out.print("Teacher ID: \n>");
            tid = tcl.nextInt();
            t = new Teachers(tid);
            System.out.print("Name: \n>");
            t.setName(txt.nextLine());
            System.out.print("Surname: \n>");
            t.setSurname(txt.nextLine());
            System.out.print("Email: \n>");
            t.setEmail(txt.nextLine());
            System.out.print("Start date (yyyy-mm-dd): \n>");
            fecha = formato.parse(txt.nextLine());
            System.out.print("Salary: \n>");
            t.setSalary(tcl.nextInt());
            t.setDepartments(dep);

            System.out.println("(+)TEACHER CREATED");
            sesion.save(t);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        System.out.println("---------------------------------");
        sesion.close();
    }
    
    public static void deleteTeacher(Session sesion) {
        Transaction tx = null;
        Teachers t;
        int id;
        
        try {
            System.out.println("---------------------------------");
            tx = sesion.beginTransaction();
            System.out.print("Teacher ID to delete:\n>");
            id = tcl.nextInt();
            t = (Teachers) sesion.get(Teachers.class, id);
            
            System.out.println("(+)TEACHER " + t.getName() + " HAS BEEN DELETED");
            sesion.delete(t);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("---------------------------------");
        sesion.close();
    }
    
    public static void deleteDepartment(Session sesion) {
        Transaction tx = null;
        Departments dep;
        int id;
        
        try {
            System.out.println("---------------------------------");
            tx = sesion.beginTransaction();
            System.out.print("Department ID to delete:\n>");
            id = tcl.nextInt();
            dep = (Departments) sesion.get(Departments.class, id);
            
            System.out.println("(+)DEPARTMENT " + dep.getName() + " HAS BEEN DELETED");
            sesion.delete(dep);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("---------------------------------");
        sesion.close();
    }
    
    public static void setSalaryOfDepartment(Session sesion) {
        int salary, id;
        
        System.out.println("---------------------------------");
        try {
            System.out.println("Department ID to set new salary");
            id = tcl.nextInt();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        sesion.close();
    }
    
    public static void riseSalaryOfDepartmentSeniors(Session sesion) {
        
    }
    
    public static void main(String[] args) {
        SessionFactory sessionf = NewHibernateUtil.getSessionFactory();
        Session sesion;
        try {
            int opc;
            do {
                sesion = sessionf.openSession();
                opc = menu();
                switch (opc) {
                    case 1:
                        showDepartment(sesion);
                        break;
                    case 2:
                        showTeacher(sesion);
                        break;
                    case 3:
                        showTeachersInDepartment(sesion);
                        break;
                    case 4:
                        createDepartment(sesion);
                        break;
                    case 5:
                        createTeacherAndDepartment(sesion);
                        break;
                    case 6:
                        createTeacherInExistingDepartment(sesion);
                        break;
                    case 7:
                        deleteTeacher(sesion);
                        break;
                    case 8:
                        deleteDepartment(sesion);
                        break;
                    case 9:
                        setSalaryOfDepartment(sesion);
                        break;
                    case 10:
                        //riseSalaryOfDepartmentSeniors(sesion);
                        break;
                    case 11: 
                        System.out.println("bye!");
                        break;
                    default:
                        System.out.println("Select a valid option");
                }
            } while (opc != 11);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sessionf.close();
    }
}
