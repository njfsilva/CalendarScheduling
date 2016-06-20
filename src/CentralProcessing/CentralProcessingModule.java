package CentralProcessing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import DataReceiverQueue.DataReceiverQueue;
import Entities.Availability;
import Entities.ClassRoom;
import Entities.Period;
import Entities.Person;
import Entities.Schedule;
import Entities.SchedulingWorkUnit;
import Entities.Student;
import Helpers.FileIOHelper;

public class CentralProcessingModule extends Thread{
	private DataReceiverQueue processingQueue;
	private Boolean canStart = false;
	private String outputFileName = "horario.xml";
	
	public CentralProcessingModule(){
		this.processingQueue = new DataReceiverQueue();
	}
	
	public void run(){
		System.out.println("Processing module Started!");
		
		while(!canStart){
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
				
		System.out.println("Started Processing!");
		
		while(processingQueue.getQueueSize() > 0){
			
			SchedulingWorkUnit wu = processingQueue.pullWorkUnit();
			
			CompletableFuture.runAsync(() -> {
				SolveWorkUnit(wu);
			}); 
		}
		
		ForkJoinPool.commonPool().awaitQuiescence(60, TimeUnit.SECONDS);
	}
	
	private void SolveWorkUnit(SchedulingWorkUnit wu){
		
		Student student = StudentDB.GetStudentById(wu.getStudentId());
		Person president = PeopleDb.GetPersonById(wu.getPresidentId());
		Person orienter = PeopleDb.GetPersonById(wu.getOrienterId());
		Person coOrienter = PeopleDb.GetPersonById(wu.getCoOrienterId()); //might be null
		List<Person> arguents = new ArrayList<Person>();
		List<ClassRoom> classRooms = ClassRoomDb.GetAllClassRooms();
		
		for(String id : wu.GetArguents()){
			arguents.add(PeopleDb.GetPersonById(id));
		}
		
		
		List<LocalDate> matchingDatesForEveryone = new ArrayList<LocalDate>();
		
		Availability matchedClassRoom = null;
		Availability matchedOrienter = null;
		Availability matchedCoOrienter = null;
		Availability matchedArguent = null;
		Availability matchedPresident = null;
		ClassRoom classroomFound = null;
		Person arguentFound = null;
		
		//does not matter who's availability you start with because they all have to be compatible
		for(Availability a : president.getAvailabilities()){
			
			boolean matchOrienter = false;
			boolean matchCoOrienter = false;
			boolean matchArguents = false;
			boolean matchClassRooms = false;
			
			for(Availability oa : orienter.getAvailabilities()){
				if(oa.getDate().equals(a.getDate())){
					matchOrienter = true;
					matchedOrienter = oa;
				}
			}
			
			if(coOrienter != null){
				for(Availability coa : coOrienter.getAvailabilities()){
					if(coa.getDate().equals(a.getDate())){
						matchCoOrienter = true;
						matchedCoOrienter = coa;
					}
				}
			}
			else{
				matchCoOrienter = true;
			}
			
			if(arguents.size() == 0){
				matchArguents = true;
			}
			
			for(Person arguent : arguents){
				for(Availability arg : arguent.getAvailabilities()){
					if(arg.getDate().equals(a.getDate())){
						matchArguents = true;
						matchedArguent = arg;
						arguentFound = arguent;
					}
				}
			}
			
			for(ClassRoom classRoom : classRooms){
				for(Availability cra: classRoom.getAvailabilities()){
					if(cra.getDate().equals(a.getDate())){
						matchClassRooms = true;
						matchedClassRoom = cra;
						classroomFound = classRoom;
					}
				}
			}
			
			
			if(matchOrienter && matchCoOrienter && matchArguents && matchClassRooms){
				matchingDatesForEveryone.add(a.getDate());
				matchedPresident = a;
			}
		}
		
		
		LocalDateTime earliesStartTime = null;
		
		for(LocalDate date : matchingDatesForEveryone){
			
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			//Will start with classroom because periods will be deducted according to usage
			for(Period classRoomPeriod : matchedClassRoom.getPeriods()){
				
				LocalDateTime startDateCR = LocalDateTime.from(f.parse(date + " " + classRoomPeriod.getBeginning()));
				LocalDateTime EndDateCR = LocalDateTime.from(f.parse(date + " " + classRoomPeriod.getEnd()));
				
				earliesStartTime = startDateCR;
				
				for(Period presidentPeriod : matchedPresident.getPeriods()){
					LocalDateTime startDateP = LocalDateTime.from(f.parse(date + " " + presidentPeriod.getBeginning()));
					//LocalDateTime endDateP = LocalDateTime.from(f.parse(date + " " + presidentPeriod.getEnd()));
					
					if(startDateP.compareTo(earliesStartTime) > 0 && startDateP.compareTo(EndDateCR) < 0){
						earliesStartTime = startDateP;
						break;
					}
				}
				
				for(Period orienterPeriod : matchedOrienter.getPeriods()){
					LocalDateTime startDateO = LocalDateTime.from(f.parse(date + " " + orienterPeriod.getBeginning()));
					
					if(startDateO.compareTo(earliesStartTime) > 0 && startDateO.compareTo(EndDateCR) < 0){
						earliesStartTime = startDateO;
						break;
					}
				}
				
				if(matchedCoOrienter != null){
					for(Period CoOrienterPeriod : matchedOrienter.getPeriods()){
						LocalDateTime startDateCoO = LocalDateTime.from(f.parse(date + " " + CoOrienterPeriod.getBeginning()));
						
						if(startDateCoO.compareTo(earliesStartTime) > 0 && startDateCoO.compareTo(EndDateCR) < 0){
							earliesStartTime = startDateCoO;
							break;
						}
					}
				}
				
				if(matchedArguent != null){
					for(Period arguentPeriodo : matchedArguent.getPeriods()){
						LocalDateTime startDateArguent = LocalDateTime.from(f.parse(date + " " + arguentPeriodo.getBeginning()));
						
						if(startDateArguent.compareTo(earliesStartTime) > 0 && startDateArguent.compareTo(EndDateCR) < 0){
							earliesStartTime = startDateArguent;
							break;
						}
					}
				}
				
				
				
				if(earliesStartTime != null){
					String[] duration = wu.getDuration().split("h");
					String[] periodStartTime = classRoomPeriod.getBeginning().split(":");
					
					int durationHour = Integer.parseInt(duration[0]);
					int durationMinutes = Integer.parseInt(duration[1]);
					int startTimeHour = Integer.parseInt(periodStartTime[0]);
					int startTImeMinutes = Integer.parseInt(periodStartTime[1]);
					int newHour = startTimeHour + durationHour;
					int newMinutes = startTImeMinutes + durationMinutes;
					
					periodStartTime[0] = Integer.toString(newHour);
					periodStartTime[1] = Integer.toString(newMinutes);
					
					classRoomPeriod.setBeginning(periodStartTime[0] + ":" +periodStartTime[1] + ":" + periodStartTime[2]);
					break;
				}
			}		
		}
		
		String[] duration = wu.getDuration().split("h");
		
		LocalDateTime endDate = earliesStartTime.plusHours(Long.parseLong(duration[0]));
		endDate = endDate.plusMinutes(Long.parseLong(duration[1]));
		
		Schedule schedule = new Schedule(
				student.getId(),
				student.getNome(),
				president.getId(), 
				president.getNome(), 
				orienter.getId(),
				orienter.getNome(), 
				coOrienter != null ? coOrienter.getId() : null,
				coOrienter != null ? coOrienter.getNome() : null,
				arguentFound != null ? arguentFound.getId() : null,
				arguentFound != null ? arguentFound.getNome() : null,
				classroomFound.getId(), 
				earliesStartTime.toString(),
				endDate.toString());
		
		try {
			FileIOHelper.WriteFile(outputFileName, schedule);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		        

		System.out.println("Finished!");
	}
	
	public void AddWorkUnit(SchedulingWorkUnit untiOfWork){
		processingQueue.pushWorkUnit(untiOfWork);
	}

	public Boolean getCanStart() {
		return canStart;
	}

	public void setCanStart(Boolean canStart) {
		this.canStart = canStart;
	}

}

