package Entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Availability {
	private LocalDate dates = null;
	private List<Period> periods = new ArrayList<Period>();
	
	public Availability(LocalDate date, List<Period> periods){
		this.dates = date;
		this.periods = periods;
	}
	
	public LocalDate getDate() {
		return dates;
	}
	public void setDate(LocalDate date) {
		this.dates = date;
	}
	public List<Period> getPeriods() {
		return periods;
	}
	
	public void AddPeriod(Period p){
		this.periods.add(p);
	}
}


