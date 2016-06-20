package Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import CentralProcessing.CentralProcessingModule;

public class ApplicationStartup {

	private static Scanner scan = new Scanner(System.in);
	private static ExecutorService executor = Executors.newFixedThreadPool(4);
	private static CentralProcessingModule cpu;
	

	public static void main(String[] args) throws InterruptedException {

		cpu = new CentralProcessingModule();
		cpu.start();
				
		List<WorkSubmissionThread> workSubmitters = new ArrayList<WorkSubmissionThread>();
		
		WorkSubmissionThread subThread = new WorkSubmissionThread(Arrays.asList("data.xml"), cpu);
		WorkSubmissionThread subThread2 = new WorkSubmissionThread(Arrays.asList("data2.xml"), cpu);
		WorkSubmissionThread subThread3 = new WorkSubmissionThread(Arrays.asList("data3.xml"), cpu);
		
		workSubmitters.add(subThread);
		workSubmitters.add(subThread2);
		workSubmitters.add(subThread3);
		
		for(WorkSubmissionThread t : workSubmitters){
			executor.execute(t);
		}
		
		executor.shutdown();
		
		try {
			  executor.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println("Fuck your threads dude!");
		}
		
		System.out.println("Work Submission Processes finished, start processing?(y/n)");
		
		String s = scan.next();
		
		while(!s.toLowerCase().equals("y")){
			System.out.println("Start processing?(y/n)");
			s = scan.next();
		}
		
		cpu.setCanStart(true);		
	}
}
