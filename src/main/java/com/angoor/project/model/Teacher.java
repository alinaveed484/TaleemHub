package com.angoor.project.model;

public class Teacher {
	String ID;
	String Name;
	
	
	
	
	
	
  @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Teacher teacher = (Teacher) obj;
        return ID.equals(teacher.ID);
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
