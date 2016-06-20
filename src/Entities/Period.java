package Entities;

public class Period{
	private String beginning;
	private String end;
	
	public Period(String start, String end){
		this.beginning = start;
		this.end = end;
	}

	public synchronized String getBeginning() {
		return beginning;
	}

	public synchronized void setBeginning(String beginning) {
		this.beginning = beginning;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
}