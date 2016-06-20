package Entities;

import java.util.ArrayList;
import java.util.List;

public class SchedulingWorkUnit {
	private String id;
	private String StudentId;
	private String PresidentId;
	private String OrienterId;
	private String CoOrienterId;
	private String Duration;
	private List<String> arguentsIds = new ArrayList<String>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStudentId() {
		return StudentId;
	}
	public void setStudentId(String studentId) {
		StudentId = studentId;
	}
	public String getPresidentId() {
		return PresidentId;
	}
	public void setPresidentId(String presidentId) {
		PresidentId = presidentId;
	}
	public String getOrienterId() {
		return OrienterId;
	}
	public void setOrienterId(String orienterId) {
		OrienterId = orienterId;
	}
	public String getCoOrienterId() {
		return CoOrienterId;
	}
	public void setCoOrienterId(String coOrienterId) {
		CoOrienterId = coOrienterId;
	}
	public String getDuration() {
		return Duration;
	}
	public void setDuration(String duration) {
		Duration = duration;
	}
	
	public void AddArguent(String arguentId){
		this.arguentsIds.add(arguentId);
	}
	
	public List<String> GetArguents(){
		return arguentsIds;
	}
}
