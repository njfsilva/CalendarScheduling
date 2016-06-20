package CentralProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Entities.ClassRoom;

public final class ClassRoomDb {
	private static HashMap<String, ClassRoom> classrooms = new HashMap<String, ClassRoom>();
	
	public static synchronized void AddClassRoom(ClassRoom c){
		classrooms.put(c.getId(), c);
	}
	
	public static synchronized ClassRoom GetClassRoomById(String id){
		return classrooms.get(id);
	}
	
	public static synchronized List<ClassRoom> GetAllClassRooms(){
		
		return new ArrayList<ClassRoom>(classrooms.values());
	}

	public static synchronized void AddClassRooms(List<ClassRoom> classRoomTransformed) {
		
		for(ClassRoom c : classRoomTransformed){
			if(!classrooms.containsKey(c.getId())){
				classrooms.put(c.getId(), c);
			}
		}
	}
}
