package readfile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import generals.Exam;
import generals.Rok;

public class ReadFileRok {

	private String fileName;
	
	private int duration;
	private List<Exam> exams;
	
	public ReadFileRok(String fileName) {
		this.duration = -1;
		this.exams = null;
		this.fileName = fileName;
		this.readRok();
	}
	
	public final void readRok() {
		JSONParser jsonParser = new JSONParser();
		
		try(FileReader fileReader = new FileReader(fileName)){
			Object obj = jsonParser.parse(fileReader);
			
			if(obj == null) return;
			
			JSONObject jsonObject = (JSONObject) obj;
			duration = ((Long)jsonObject.get("trajanje_u_danima")).intValue();
			
			exams  = new ArrayList<>();
			JSONArray rooms = (JSONArray) jsonObject.get("ispiti");
			for (Object object : rooms) {
				exams.add(readRokFromJSON( (JSONObject) object));			
			}
			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
			System.err.println("File not found!");
			System.exit(1);
		} catch (IOException e) {		
			e.printStackTrace();
			System.err.println("User unknown error while working with file from ReadFileRooms!");
		} catch (ParseException e) {	
			e.printStackTrace();
			System.err.println("User error while parsing!");
		}
	}
	
	public Exam readRokFromJSON(JSONObject obj) {
		String examCode = (String) obj.get("sifra");
		Integer numberOfSignedStudents = ((Long) obj.get("prijavljeni")).intValue();
		Integer isOnPCs = ((Long) obj.get("racunari")).intValue();
		
		JSONArray jsonModules = (JSONArray)obj.get("odseci");
				
		List<String> modules = new ArrayList<>();	
		for (Object module : jsonModules) {
			modules.add((String)module);
		}	
		
		return new Exam(examCode,numberOfSignedStudents,isOnPCs,modules);			
	}
	
	public Rok getRok() {
		return new Rok(duration, exams);
	}
	
	public List<Exam> getExams(){
		if(exams == null) {
			System.out.println("Exams empty!");
		}
		return exams;
	}
	
	public int getDuration() {
		return duration;
	}
}
