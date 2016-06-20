package CentralProcessing;

import java.util.HashMap;
import java.util.List;
import Entities.Student;

public final class StudentDB {
	private static HashMap<String, Student> students = new HashMap<String, Student>();
	
	public static synchronized void AddClassRoom(Student s){
		students.put(s.getId(),  s);
	}
	
	public static synchronized Student GetStudentById(String id){
		return students.get(id);
	}

	public static synchronized void AddStudents(List<Student> studentsTrasnformed) {
		for(Student s : studentsTrasnformed){
			if(!students.containsKey(s.getId())){
				students.put(s.getId(), s);
			}
		}
	}
}
	

