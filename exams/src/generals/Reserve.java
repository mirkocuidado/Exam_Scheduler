package generals;

import java.util.ArrayList;
import java.util.List;

public class Reserve {
	
	private Exam exam;
	private int day;
	private String time;
	private List<Room> rooms = new ArrayList<>();

	public Reserve(Exam exam, int day, String time, List<Room> rooms) {
		this.exam = exam;
		this.day = day;
		this.time = time;
		this.rooms = rooms;
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
}
