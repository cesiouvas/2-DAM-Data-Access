package code;

/*
 * @author Sergio Castillo Llorens
 */
import java.util.*;
import org.hibernate.*;
import pojos.*;

public class QueryDep {

    static SessionFactory sf = NewHibernateUtil.getSessionFactory();

    public static void showDepartment(Departments dep) {
        Session sesion = sf.openSession();
        sesion.update(dep);
        System.out.println(dep.toString());
        sesion.close();
    }

    public static Departments[] getAllDepartments() {
        Session sesion = sf.openSession();
        Query q = sesion.createQuery("from Departments");
        List<Departments> list = q.list();
        Departments[] listDep = new Departments[list.size()];
        list.toArray(listDep);

        sesion.close();
        return listDep;
    }

    public static Departments getDepartmentByName(String patternName) {
        Session sesion = sf.openSession();
        Departments dep = null;
        try {
            Query q = sesion.createQuery("from Departments where name like :patternName");
            q.setParameter("patternName", patternName);
            dep = (Departments) q.uniqueResult();

            sesion.close();
            return dep;
        } catch (NonUniqueResultException e) {
            System.out.println("(!) Más de un departamento");
        }
        return dep;
    }

    public static double getAverageSalaryofDepartment(String depName) {
        Session sesion = sf.openSession();
        double salary = 0;
        try {
            Query q = sesion.createQuery("select avg(salary) from Teachers "
                    + "where departments.name = '" + depName + "'");
            salary = (double) q.uniqueResult();
            sesion.close();
            return salary;
        } catch (NullPointerException e) {
            System.out.println("(!) This department doesn't have teachers");
        }
        return salary;
    }

    public static HashMap<String, Double> getAverageSalaryPerDept() {
        Session sesion = sf.openSession();
        
        Query q = sesion.createQuery("select name, avg(salary) "
                + "from Teachers group by departments.name");

        HashMap<String, Double> list = (HashMap<String, Double>) q.list();

        sesion.close();
        return list;
    }
    
    public static void closeConnection() {
        sf.close();
    }
}
