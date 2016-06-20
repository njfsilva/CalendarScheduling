package Entities;

import java.util.List;

public class ClassRoom {
	private String id;
	private List<Availability> availabilities;
	
	
	public ClassRoom(String id, List<Availability> availabilities){
		this.id = id;
		this.availabilities = availabilities;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Availability> getAvailabilities() {
		return availabilities;
	}
	public void setAvailabilities(List<Availability> availabilities) {
		this.availabilities = availabilities;
	}

}
