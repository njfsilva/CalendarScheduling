package Entities;

public class Schedule {
	private String studentId;
	private String studentName;
	private String presidentId;
	private String presidentName;
	private String orienterId;
	private String orienterName;
	private String coOrienterId;
	private String coOrienterName;
	private String arguentId;
	private String arguentName;
	private String classRoomId;
	private String startTime;
	private String endTime;
	
	public Schedule(String studentId, String studentName, String presidentId, String presidentName, String orienterId,
			String orienterName, String coOrienterId, String coOrienterName, String arguentId, String arguentName,
			String classRoomId, String startTime, String endTime) {
		this.studentId = studentId;
		this.studentName = studentName;
		this.presidentId = presidentId;
		this.presidentName = presidentName;
		this.orienterId = orienterId;
		this.orienterName = orienterName;
		this.coOrienterId = coOrienterId;
		this.coOrienterName = coOrienterName;
		this.arguentId = arguentId;
		this.arguentName = arguentName;
		this.classRoomId = classRoomId;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public Schedule(){
		
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getPresidentId() {
		return presidentId;
	}

	public void setPresidentId(String presidentId) {
		this.presidentId = presidentId;
	}

	public String getPresidentName() {
		return presidentName;
	}

	public void setPresidentName(String presidentName) {
		this.presidentName = presidentName;
	}

	public String getOrienterId() {
		return orienterId;
	}

	public void setOrienterId(String orienterId) {
		this.orienterId = orienterId;
	}

	public String getOrienterName() {
		return orienterName;
	}

	public void setOrienterName(String orienterName) {
		this.orienterName = orienterName;
	}

	public String getCoOrienterId() {
		return coOrienterId;
	}

	public void setCoOrienterId(String coOrienterId) {
		this.coOrienterId = coOrienterId;
	}

	public String getCoOrienterName() {
		return coOrienterName;
	}

	public void setCoOrienterName(String coOrienterName) {
		this.coOrienterName = coOrienterName;
	}

	public String getArguentId() {
		return arguentId;
	}

	public void setArguentId(String arguentId) {
		this.arguentId = arguentId;
	}

	public String getArguentName() {
		return arguentName;
	}

	public void setArguentName(String arguentName) {
		this.arguentName = arguentName;
	}

	public String getClassRoomId() {
		return classRoomId;
	}

	public void setClassRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String time) {
		this.startTime = time;
	}
	
	public String getendTime() {
		return endTime;
	}

	public void setendTime(String time) {
		this.endTime = time;
	}

}
