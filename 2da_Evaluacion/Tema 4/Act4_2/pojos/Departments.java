package pojos;
// Generated 22 nov. 2022 8:14:00 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Departments generated by hbm2java
 */
public class Departments  implements java.io.Serializable {


     private int deptNum;
     private String name;
     private String office;
     private Set teacherses = new HashSet(0);

    public Departments() {
    }

	
    public Departments(int deptNum) {
        this.deptNum = deptNum;
    }
    public Departments(int deptNum, String name, String office, Set teacherses) {
       this.deptNum = deptNum;
       this.name = name;
       this.office = office;
       this.teacherses = teacherses;
    }
   
    public int getDeptNum() {
        return this.deptNum;
    }
    
    public void setDeptNum(int deptNum) {
        this.deptNum = deptNum;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getOffice() {
        return this.office;
    }
    
    public void setOffice(String office) {
        this.office = office;
    }
    public Set getTeacherses() {
        return this.teacherses;
    }
    
    public void setTeacherses(Set teacherses) {
        this.teacherses = teacherses;
    }
}


