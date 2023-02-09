package code;

/*
 * @author Sergio Castillo Llorens
 */
import java.util.*;
import org.hibernate.*;
import pojos.*;

public class QueryTeach {

    static SessionFactory sf = NewHibernateUtil.getSessionFactory();

    public static void showTeacher(Teachers teach) {
        Session sesion = sf.openSession();
        sesion.update(teach);
        System.out.println(teach.toString());
        sesion.close();
    }

    public static Teachers[] getAllTeachers() {
        Session sesion = sf.openSession();
        Query q = sesion.createQuery("from Teachers");
        List<Teachers> list = q.list();
        Teachers[] listTeach = new Teachers[list.size()];
        list.toArray(listTeach);

        sesion.close();
        return listTeach;
    }

    public static Teachers getMostVeteranTeacher() {
        Session sesion = sf.openSession();
        Query q = sesion.createQuery("from Teachers te where te.startDate "
                + "= (select min(t.startDate) from Teachers t)");
        Teachers t = (Teachers) q.uniqueResult();

        sesion.close();
        return t;
    }

    public static int setSalary(int newSalary) {
        Session sesion = sf.openSession();
        Transaction tx = sesion.beginTransaction();

        try {
            Query q = sesion.createQuery("update Teachers set salary = :newSalary");
            q.setInteger("newSalary", newSalary);
            int modifiedSalary = q.executeUpdate();

            tx.commit();
            sesion.close();
            return modifiedSalary;
        } catch (Exception e) {
            System.out.println("(!) Error while updating salary");
            tx.rollback();
            sesion.close();
        }
        return 0;
    }

    public static int riseSeniorSalary(int year, int percentage) {
        Session sesion = sf.openSession();
        Transaction tx = sesion.beginTransaction();

        try {
            Query q = sesion.createQuery("update Teachers set salary = salary+((salary*:percentage)/100) "
                    + "where year(current_date()) - year(startDate) > :year");

            q.setInteger("percentage", percentage);
            q.setInteger("year", year);

            int modifiedSalary = q.executeUpdate();

            tx.commit();
            sesion.close();
            return modifiedSalary;
        } catch (Exception e) {
            System.out.println("(!) Error while raising salary");
            tx.rollback();
            sesion.close();
        }
        return 0;
    }
    
    public static int deleteTeachersOfDepartment(String name) {
        Session sesion = sf.openSession();
        Transaction tx = sesion.beginTransaction();
        
        try {
            Query q = sesion.createQuery("delete Teachers te where te.departments.deptNum "
                    + "in (select d.deptNum from Departments d where d.name = :name)");
            q.setString("name", name);
            
            int modifiedRows = q.executeUpdate();
            
            tx.commit();
            sesion.close();
            return modifiedRows;
        } catch (Exception e) {
            System.out.println("(!) Invalid name");
        }
        return 0;        
    }

    public static void closeConnection() {
        sf.close();
    }
}
