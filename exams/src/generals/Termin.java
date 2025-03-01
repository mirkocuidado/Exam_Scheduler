package generals;

public class Termin {
	private Room room;
	private String time;
	private int day;
	private String s_name;
	
	public Termin(Room room, String time, int day) {
		super();
		this.room = room;
		this.time = time ;
		this.day = day;
		this.s_name = day + time;
	}
	
	@Override
	public boolean equals(Object obj) {
	 if(obj instanceof Termin) {
		 Termin termin = (Termin) obj;
		 return termin.getDay() == day && termin.getRoom().getName() == room.getName() && termin.getTime().equals(time);
	 }
	 return false;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getS_name() {
		return s_name;
	}

	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	
}
