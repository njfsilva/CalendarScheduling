package Main;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import CentralProcessing.CentralProcessingModule;
import CentralProcessing.ClassRoomDb;
import CentralProcessing.PeopleDb;
import CentralProcessing.StudentDB;
import Entities.Availability;
import Entities.ClassRoom;
import Entities.Period;
import Entities.Person;
import Entities.SchedulingWorkUnit;
import Entities.Student;
import Helpers.FileIOHelper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class WorkSubmissionThread extends Thread {
	private List<String> filesToProcess;
	CentralProcessingModule processingUnit;
	
	
	public WorkSubmissionThread(List<String> filesToProcess, CentralProcessingModule processingUnit){
		this.filesToProcess = filesToProcess;
		this.processingUnit = processingUnit;
	}
	
	public void run(){
		
		for (String file : filesToProcess) {
			
			Document doc = GetDoc(file);
			
			List<Student> studentsTrasnformed = GetStudentsFromXml(doc);
			List<Person> peopleTransformed = GetPeopleFromXml(doc);
			List<ClassRoom> classRoomsTransformed = GetClassRoomsFromXml(doc);
			List<SchedulingWorkUnit> workUnits = GetWorkUnitsFromXml(doc);
			
			PeopleDb.AddPeople(peopleTransformed);
			StudentDB.AddStudents(studentsTrasnformed);
			ClassRoomDb.AddClassRooms(classRoomsTransformed);
						
			for(SchedulingWorkUnit wu : workUnits){
				processingUnit.AddWorkUnit(wu);
			}
		}
	}

	private Document GetDoc(String path){
		try {
			return FileIOHelper.Readfile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<SchedulingWorkUnit> GetWorkUnitsFromXml(Document doc) {
		
		List<SchedulingWorkUnit> result = new ArrayList<SchedulingWorkUnit>();
		
		NodeList provas = GetNodesByXpath("//dados//provas//prova", doc);
		
		for(int i=0; i< provas.getLength(); i++){
			
			SchedulingWorkUnit wu = new SchedulingWorkUnit();
			
			Element prova = (Element)provas.item(i);
			Element student = (Element)prova.getElementsByTagName("estudante").item(0);
			Element president = (Element)prova.getElementsByTagName("presidente").item(0);
			Element orienter = (Element)prova.getElementsByTagName("orientador").item(0);
			Element duration = (Element)prova.getElementsByTagName("duracao").item(0);
			NodeList coOrienter = prova.getElementsByTagName("coorientador");
			NodeList arguents = prova.getElementsByTagName("arguente");
			
			wu.setId(prova.getAttribute("id"));
			wu.setStudentId(student.getAttribute("ref"));
			wu.setPresidentId(president.getAttribute("ref"));
			wu.setOrienterId(orienter.getAttribute("ref"));
			wu.setDuration(duration.getTextContent());
			
			if(coOrienter != null && coOrienter.getLength() > 0){
				Element coOrient = (Element)coOrienter.item(0);
				wu.setCoOrienterId(coOrient.getAttribute("ref"));
			}
			
			for(int j=0; j< arguents.getLength(); j++){
				Element arguent = (Element)arguents.item(j);
				wu.AddArguent(arguent.getAttribute("ref"));
			}
			
			result.add(wu);
		}
		
		return result;
	}
	
	private List<ClassRoom> GetClassRoomsFromXml(Document doc) {
		
		List<ClassRoom> result = new ArrayList<ClassRoom>();
		
		NodeList classrooms = GetNodesByXpath("//dados//recursos//sala", doc);
		
		for(int i = 0; i<classrooms.getLength(); i++){
			
			Element e = (Element)classrooms.item(i);
			
			String id = e.getAttribute("id");
			
			NodeList availabilities = e.getElementsByTagName("disponibilidade");
			
			List<Availability> available = SolveAvailabilities(availabilities);
			
			result.add(new ClassRoom(id, available));
		}
		
		return result;
	}
	
	private List<Person> GetPeopleFromXml(Document doc) {
		
		List<Person> result = new ArrayList<Person>();
		
		NodeList people = GetNodesByXpath("//dados//recursos//pessoa", doc);
		
		for(int i = 0; i<people.getLength(); i++){
			Element e = (Element)people.item(i);
			
			String id = e.getAttribute("id");
			String nome = e.getAttribute("nome");
			String email = e.getAttribute("email");
			List<Availability> available = new ArrayList<Availability>();
			
			NodeList availabilities = e.getElementsByTagName("disponibilidade");
			
			available = SolveAvailabilities(availabilities);
			
			Person p = new Person(id, nome, email, available);
			
			result.add(p);
		}
		
		return result;
	}
	
	private List<Student> GetStudentsFromXml(Document doc){
		
		List<Student> result = new ArrayList<Student>();
		
		NodeList students = GetNodesByXpath("//dados//recursos//estudante", doc);
		
		for(int i = 0; i<students.getLength(); i++){
			Element e = (Element)students.item(i);
			
			Student s = new Student(e.getAttribute("id"), e.getAttribute("nome"), e.getAttribute("email"));
			
			result.add(s);
		}
		
		return result;
	}
	
	private NodeList GetNodesByXpath(String expression, Document doc){
		
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression exprStudents = null;
		Object obj = null;
		
		try {
			exprStudents = xpath.compile(expression);
			obj = exprStudents.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
		}
		
		return (NodeList)obj;
	}

	private List<Availability> SolveAvailabilities(NodeList availabilities){
		
		List<Availability> available = new ArrayList<Availability>();
		
		for(int j = 0; j<availabilities.getLength(); j++){
			
			List<Period> periods = new ArrayList<Period>();
			Element a = (Element)availabilities.item(j);
			
			NodeList periodos = a.getElementsByTagName("periodo");
			
			for(int z = 0; z<periodos.getLength(); z++){
				
				Element p = (Element)periodos.item(z);
				String beginning = p.getAttribute("inicio");
				String ending = p.getAttribute("fim");
				
				periods.add((new Period(beginning, ending)));
			}
			
			NodeList data = a.getElementsByTagName("data");
			
			if(data == null || data.getLength()==0){
				String dataInicio = a.getElementsByTagName("datainicio").item(0).getTextContent();
				String dataFim = a.getElementsByTagName("datafim").item(0).getTextContent();
				
				LocalDate initDate = LocalDate.parse(dataInicio);
				LocalDate endDate = LocalDate.parse(dataFim);
				long days = ChronoUnit.DAYS.between(initDate, endDate);
				
				for(int y = 0; y<days; y++){
					available.add(new Availability(initDate.plusDays(y+1), periods));
				}
				
			}else{
				String dataa = a.getElementsByTagName("data").item(0).getTextContent();
				available.add(new Availability(LocalDate.parse(dataa), periods));
			}
		}
		
		return available;
	}

}
