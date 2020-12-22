package generals;

public class Room {
	private String name;
	private int capacity;
	private int numberOfPeople;
	private int isOnETF;
	private int hasPCs;
	
	public Room(String n, int c, int numPeople, int onETF, int hasPCs) {
		this.name = n;
		this.capacity = c;
		this.numberOfPeople = numPeople;
		this.isOnETF = onETF;
		this.hasPCs = hasPCs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	public int getIsOnETF() {
		return isOnETF;
	}

	public void setIsOnETF(int isOnETF) {
		this.isOnETF = isOnETF;
	}

	public int getHasPCs() {
		return hasPCs;
	}

	public void setHasPCs(int hasPCs) {
		this.hasPCs = hasPCs;
	}
}
