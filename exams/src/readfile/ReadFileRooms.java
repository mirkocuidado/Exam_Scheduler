package readfile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import generals.Room;

import java.util.List;
import java.util.ArrayList;

public class ReadFileRooms {

	private String fileName;
	
	List<Room> rooms;
	
	public ReadFileRooms(String fileName) {
		super();
		this.rooms = null;
		this.fileName = fileName;
		this.readRooms();
	}
	
	public final void readRooms() {
		JSONParser jsonParser = new JSONParser();
		
		try(FileReader fileReader = new FileReader(fileName)){
			Object obj = jsonParser.parse(fileReader);
			
			if(obj == null) return;
			
			rooms  = new ArrayList<>();
			JSONArray listeSala = (JSONArray) obj;
			for (Object object : listeSala) {
				rooms.add(readRoomsFromJSON( (JSONObject) object));			
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
	
	private Room readRoomsFromJSON(JSONObject obj) {
		String name = (String)obj.get("naziv");
		Integer capacity = ((Long)obj.get("kapacitet")).intValue();
		Integer pcs = ((Long)obj.get("racunari")).intValue();
		Integer people = ((Long)obj.get("dezurni")).intValue();
		Integer etf = ((Long)obj.get("etf")).intValue();
		
		return new Room(name, capacity, people,  etf, pcs);
	}
	
	public List<Room> getRooms(){
		if(rooms == null) {
			System.out.println("NO ROOMS!");
			return null;
		}
		else return rooms;
	}
	
}
