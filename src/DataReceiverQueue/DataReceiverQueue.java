package DataReceiverQueue;


import java.util.concurrent.*;

import Entities.SchedulingWorkUnit;

import java.util.*;

public class DataReceiverQueue extends Thread{

	private static ConcurrentLinkedQueue<SchedulingWorkUnit> workUnitQueue = new ConcurrentLinkedQueue<SchedulingWorkUnit>();
	private boolean canTerminate = false;
	private static final int emptyLimiter = 100;
	private static int emptyCounter = emptyLimiter;
	

	public synchronized void pushWorkUnit(SchedulingWorkUnit unitOfWOrk) 
	{
		try
		{
			workUnitQueue.add(unitOfWOrk);
			//System.out.println("Queued work unit");
		}
		catch(Exception ex)
		{
			System.out.println("Error adding work to queue: " + ex.getMessage());
		}
	}
	
	public synchronized SchedulingWorkUnit pullWorkUnit() 
	{
		SchedulingWorkUnit unit = null;
		
		try
		{
			unit =  workUnitQueue.remove();
			
			//System.out.println("Pulled work unit!");
		}
		catch(NoSuchElementException e)
		{
			emptyCounter--;
			
			if(workUnitQueue.isEmpty() && emptyCounter <= 0)
			{
				canTerminate = true;
			}
		}
		return unit;
	}

	public synchronized int getQueueSize()
	{
		return workUnitQueue.size();
	}
	
	public boolean getCanTerminate() {
		return this.canTerminate;
	}
	
public void setCanTerminate(boolean canTerminate) {
		this.canTerminate = canTerminate;
	}	
}