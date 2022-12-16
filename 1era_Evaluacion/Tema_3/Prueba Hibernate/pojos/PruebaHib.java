package pojos;

import java.util.*;
import org.hibernate.*;

/*
 * @author 7J
 */
public class PruebaHib {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SessionFactory sessionf = NewHibernateUtil.getSessionFactory();
        Session sesion = sessionf.openSession();
        Transaction tx = sesion.beginTransaction();

        Empleados emp = (Empleados) sesion.get(Empleados.class, 2);
        System.out.println("El salario antiguo era: " + emp.getSalario());
        System.out.println("La comision antigua era: " + emp.getComision());
        emp.setSalario((float) 300);
        emp.setComision((float) 1);
        tx.commit();
        
        sesion.close();
        
        sesion = sessionf.openSession();
        emp = (Empleados) sesion.get(Empleados.class, 2);
        System.out.println("El salario nuevo es: " + emp.getSalario());
        System.out.println("La comision nueva era: " + emp.getComision());
        
        sesion.close();
        sessionf.close();
    }

    public static void juan(Session sesion, Transaction tx) {

        try {
            System.out.println("=============================");
            System.out.println("DATOS DEL DEPARTAMENTO 1");
            Departamentos dep;
            dep = (Departamentos) sesion.get(Departamentos.class, 1);
            System.out.println("Nombre: " + dep.getDnombre());
            System.out.println("Localidad: " + dep.getLoc());

            dep.setDnombre("ASDF");
            dep.setLoc("Oficina");

            Empleados emp = new Empleados(718);
            emp.setApellido("Soriano");
            emp.setDir(69);
            emp.setOficio("Vendedor");
            emp.setSalario(1500f);
            emp.setComision(10f);
            emp.setDepartamentos(dep);
            System.out.println("Guardando empleado. ID: ;" + sesion.save(emp));

            tx.commit();

            System.out.println("==============================");
            System.out.println("EMPLEADOS DEL DEPARTAMENTO 1");
            Set<Empleados> listaemple = dep.getEmpleadoses();
            for (Empleados emple : listaemple) {
                System.out.println("Apellido: " + emple.getApellido() + ""
                        + "; Salario: " + emple.getSalario());
            }
            System.out.println("Fin de empleados");
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }
}
