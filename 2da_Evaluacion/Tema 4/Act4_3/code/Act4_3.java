package code;

/*
 * @author Sergio Castillo Llorens
 */
import java.util.*;
import pojos.*;

public class Act4_3 {

    static Scanner tcl = new Scanner(System.in);
    static Scanner txt = new Scanner(System.in);
    
    public static void getAllDepartments() {
        Departments[] dep = QueryDep.getAllDepartments();
        for (int i = 0; i < dep.length; i++) {
            QueryDep.showDepartment(dep[i]);
        }
    }
    
    public static void depByName() {
        String depName;
        
        System.out.print("Name of the department:\n>");
        depName = txt.nextLine();
        
        QueryDep.showDepartment(QueryDep.getDepartmentByName(depName));
    }
    
    public static void salaryofDepartment() {
        String depName;
        double salary;
        System.out.print("Name of the department:\n>");
        depName = txt.nextLine();
        
        salary = QueryDep.getAverageSalaryofDepartment(depName);
        System.out.println("Salary of the department " + depName + ": " + salary
        + "\n------------------------------------------");
    }
    
    public static void salaryPerDept() {
        HashMap<String,Double> avgSalary = null;
        avgSalary = QueryDep.getAverageSalaryPerDept();
        System.out.println("----AVERAGE SALARY----" + avgSalary.toString());
    }
    
    public static void getAllTeachers() {
        Teachers[] teach = QueryTeach.getAllTeachers();
        for (int i = 0; i < teach.length; i++) {
            QueryTeach.showTeacher(teach[i]);
        }
    }
    
    public static void getMostVeteranTeacher() {
        System.out.println("----MOST VETERAN TEACHER----");
        QueryTeach.showTeacher(QueryTeach.getMostVeteranTeacher());
    }
    
    public static void setSalary() {
        System.out.println("\nSet the new salary: ");
        int salary = tcl.nextInt();
        QueryTeach.setSalary(salary);
    }
    
    public static void main(String[] args) {
        int opc;
        
        do {
            System.out.print("Select an option:\n"
                    + "1. Show all departments\n"
                    + "2. Show department whose name matches a pattern\n"
                    + "3. Get average salary of a department (by name)\n"
                    + "4. Show average salary of each department\n"
                    + "5. Show all teachers\n"
                    + "6. Show most veteran teacher\n"
                    + "7. Set salary\n"
                    + "8. Rise salary of senior teachers\n"
                    + "9. Delete teachers of a department\n"
                    + "10. Quit"
                    + "\n>");
            opc = tcl.nextInt();
            
            switch (opc) {
                case 1:
                    getAllDepartments();
                    break;
                case 2:
                    depByName();
                    break;
                case 3:
                    salaryofDepartment();
                    break;
                case 4:
                    salaryPerDept();
                    break;
                case 5:
                    getAllTeachers();
                    break;
                case 6:
                    getMostVeteranTeacher();
                    break;
                case 7:
                    setSalary();
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    System.out.println("Bye!");
                    QueryTeach.closeConnection();
                    QueryDep.closeConnection();
                    break;
                default:
                    System.out.println("Select a valid option");
                    break;
            }
        } while (opc != 10);
    }
}
