package Helpers;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Entities.Schedule;

public final class FileIOHelper {
	
	public static synchronized Document Readfile(String filePath) throws Exception{
		
		File file = new File(filePath);
		
		try{
			
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document  = documentBuilder.parse(file);
			return document;
			
		}catch(Exception e){
			throw new Exception("ERROR LOADING FILE: " + e.getMessage());
		}
	}
	
	public static synchronized void WriteFile(String filePath, Schedule schedule) throws Exception{
		
		Document doc = Readfile(filePath);
		
		Element rootElement = doc.getDocumentElement();
		
		if(rootElement == null || !rootElement.getTagName().equals("Schedules")){
			doc.createElement("Schedules");
		}
		else{
			Element newElement = doc.createElement("Schedule");
			
			Element classRoom = doc.createElement("Sala");
			classRoom.setTextContent(schedule.getClassRoomId());
			newElement.appendChild(classRoom);
			
			Element dataInicio = doc.createElement("DataInicio");
			dataInicio.setTextContent(schedule.getStartTime());
			newElement.appendChild(dataInicio);
			
			Element dataFim =doc.createElement("DataFim");
			dataFim.setTextContent(schedule.getendTime());
			newElement.appendChild(dataFim);
			
			Element student = doc.createElement("Estudante");
			student.setAttribute("Ïd", schedule.getStudentId());
			student.setTextContent(schedule.getStudentName());
			newElement.appendChild(student);
			
			Element president = doc.createElement("Presidente");
			president.setAttribute("Id", schedule.getPresidentId());
			president.setTextContent(schedule.getPresidentName());
			newElement.appendChild(president);
			
			Element orienter = doc.createElement("Orientador");
			orienter.setAttribute("Id", schedule.getOrienterId());
			orienter.setTextContent(schedule.getOrienterName());
			newElement.appendChild(orienter);
			
			if(schedule.getCoOrienterId() != null){
				Element coOrienter = doc.createElement("CoOrientador");
				coOrienter.setAttribute("Id", schedule.getCoOrienterId());
				coOrienter.setTextContent(schedule.getCoOrienterName());
				newElement.appendChild(coOrienter);
			}
			
			if(schedule.getArguentId() != null){
				Element arguent = doc.createElement("Arguente");
				arguent.setAttribute("Id", schedule.getArguentId());
				arguent.setTextContent(schedule.getArguentName());
				newElement.appendChild(arguent);
			}
			
			rootElement.insertBefore(newElement, rootElement.getFirstChild());
			
			DOMSource source = new DOMSource(doc);

	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	        StreamResult result = new StreamResult(filePath);
	        transformer.transform(source, result);
		}
		
		
	}
}
