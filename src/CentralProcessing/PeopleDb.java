package CentralProcessing;

import java.util.HashMap;
import java.util.List;

import Entities.Person;

public final class PeopleDb {
	private static HashMap<String, Person> people = new HashMap<String, Person>();
	
	public static synchronized void AddPerson(Person p){
		people.put(p.getId(), p);
	}
	
	public static synchronized Person GetPersonById(String id){
		return people.get(id);
	}

	public static synchronized void AddPeople(List<Person> peopleTransformed) {
		
		for(Person p : peopleTransformed){
			if(!people.containsKey(p.getId())){
				people.put(p.getId(), p);
			}
		}
	}
}
