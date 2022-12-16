package code;

/*
 * @author Sergio Castillo Llorens
 */

import java.util.*;
import org.hibernate.*;
import pojos.*;

public class QueryTeach {

    static SessionFactory sf = NewHibernateUtil.getSessionFactory();

    public static void showTeacher(Teachers dep) {
        Session sesion = sf.openSession();
        sesion.update(dep);
        System.out.println(dep.toString());
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
        Query q = sesion.createQuery("from Teachers where startDate "
                + "= (select min(startDate) from Teachers)");
        Teachers t = (Teachers) q.uniqueResult();
        
        sesion.close();
        return t;
    }
    
}
