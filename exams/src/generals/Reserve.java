package generals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Reserve {
	
	private Exam exam;
	private int day;
	private String time;
	private List<Room> rooms = new ArrayList<>();
	
	private int costOfReserve;

	public Reserve(Exam exam, int day, String time, List<Room> rooms) {
		this.exam = exam;
		this.day = day;
		this.time = time;
		this.rooms = rooms;
		for(Room r : rooms)
			costOfReserve = r.getIsOnETF()*10 + r.getNumberOfPeople()*12;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public int getCostOfReserve() {
		return costOfReserve;
	}

	public void setCostOfReserve(int costOfReserve) {
		this.costOfReserve = costOfReserve;
	}
}
