package backtrack_only;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import generals.Exam;
import generals.Reserve;
import generals.Room;
import generals.Termin;

public class BackTrack {
	private List<Exam> exams;
	private List<List<Reserve>> domains;
	public static Reserve solution[];

	String termins[] = { "08:00", "11:30", "15:00", "18:30" };
	
	public BackTrack(List<Exam> exams, int size) {
		solution = new Reserve[size];
		this.exams = exams;
	}
	
	public List<List<Reserve>> initializeDomains(List<Room> rooms, List<Exam> exams, int duration) {
		domains = new ArrayList<>();
		
		int number_of_exams = exams.size();
		
		for (int i = 0; i < number_of_exams; ++i) {
			domains.add(new ArrayList<>());
		}
		
		for (int i = 0; i < number_of_exams; ++i) {
			
			List<Room> rooms_with_PC = new ArrayList<>(rooms);
			
			if (exams.get(i).getIsOnPCs() == 1) {
				for (Room room : rooms) {
					if (room.getHasPCs() == 1) {
						rooms_with_PC.remove(room);
					}
				}
			}
			
			List<Room> rooms_without_enough_capacity = new ArrayList<>();
			
			for (Room room : rooms_with_PC) {
				if(room.getCapacity() < exams.get(i).getNumberOfSignedStudents()) {
					rooms_without_enough_capacity.add(room);
				}
				else {
					for(int day = 1; day <= duration; ++day) {
						List<Room> reservationRooms = new ArrayList<>();
						
						reservationRooms.add(room);
						
						for(int index=0; index < 4; ++index) {
							domains.get(i).add(new Reserve(exams.get(i), day, termins[index], reservationRooms));
						}
					}
				}
			}
			
			long numberOfSubsets = (long) Math.pow(2, rooms_without_enough_capacity.size());
			
			for (long n = 1; n < numberOfSubsets; ++n) {
				List<Room> reservationRooms = new ArrayList<>();
				int total_space = 0;
				
				for (int mask = 0; mask < rooms_without_enough_capacity.size(); mask++) {
					if ((n & (1 << mask)) != 0) {
						total_space += rooms_without_enough_capacity.get(mask).getCapacity();
						reservationRooms.add(rooms_without_enough_capacity.get(mask));
					}
				}
				
				if (total_space >= exams.get(i).getNumberOfSignedStudents())
					for (int day = 1; day <= duration; day++)
						for(int index=0; index < 4; ++index) {
							domains.get(i).add(new Reserve(exams.get(i), day, termins[index], reservationRooms));
						}
			}
		}
		return domains;
	}

	public boolean algorithm(List<List<Reserve>> domains, int level) {
		if (level == exams.size())
			return true;
		for (Reserve reservation : domains.get(level)) {
			solution[level] = reservation;
			
			List<List<Reserve>> copy_of_all_domains = new ArrayList<>();
			for (int i = 0; i < domains.size(); i++)
				copy_of_all_domains.add(new ArrayList<Reserve>(domains.get(i)));
			
			List<Reserve> newDomain = new ArrayList<Reserve>();
			newDomain.add(reservation);
			copy_of_all_domains.set(level, newDomain);
			
			remove_for_same_day_same_time_same_room(copy_of_all_domains, level, reservation);
			remove_for_same_day_same_year_same_moduo(copy_of_all_domains,level, reservation.getExam().getModules(), reservation.getDay(), reservation.getExam().getExamCode().getGodinaStudija(), reservation.getTime());
			
			if (algorithm(copy_of_all_domains, level + 1) == true)
				return true;
			
			solution[level] = null;
		}
		return false;
	}
	
	private void remove_for_same_day_same_time_same_room(List<List<Reserve>> domain, int level, Reserve reservation) {
		for (int remover_alfa = 0; remover_alfa < domain.size(); ++remover_alfa) {
			if (remover_alfa == level || solution[remover_alfa] != null) continue;
			
			Set<Integer> toBeRemoved = new HashSet<>();
			int remover_alfa_size = domain.get(remover_alfa).size();
			
			for (int remover_beta = 0; remover_beta < remover_alfa_size; ++remover_beta) {
				
				Reserve r1 = domain.get(remover_alfa).get(remover_beta);
				
				if (r1.getDay() != reservation.getDay() || !r1.getTime().equals(reservation.getTime())) break; 
			
				for (Room room1 : r1.getRooms())
					for (Room room2 : reservation.getRooms())
						if (room1.getName().equals(room2.getName())) {
							toBeRemoved.add(remover_beta);
							break;
						}
			}
			
			List<Reserve> newDomain = new ArrayList<>();
			
			for (int i = 0; i < remover_alfa_size; i++)
				if (toBeRemoved.contains(i) == false)
					newDomain.add(domain.get(remover_alfa).get(i));
			
			domain.set(remover_alfa, newDomain);
		}
	}

	private void remove_for_same_day_same_year_same_moduo(List<List<Reserve>> domain, int level, List<String> modules, int day, int yearOfStudy, String time) {
		for (int remover_alfa = 0; remover_alfa < domain.size(); ++remover_alfa) {
			int yearOfStudy2 = exams.get(remover_alfa).getExamCode().getGodinaStudija();
			
			if (remover_alfa == level || solution[remover_alfa] != null || yearOfStudy != yearOfStudy2 || ((Math.abs(yearOfStudy - yearOfStudy2) != 1)))
				continue;
			
			Set<Integer> toBeRemoved = new HashSet<>();
			int remover_alfa_size = domain.get(remover_alfa).size();
			
			for (int remover_beta = 0; remover_beta < remover_alfa_size; ++remover_beta) {
				if (day != domain.get(remover_alfa).get(remover_beta).getDay() || !time.equals(domain.get(remover_alfa).get(remover_beta).getTime()))
					continue;
				
				for (String modules_remover_alfa : modules)
					for (String modules_remover_beta : exams.get(remover_alfa).getModules())
						if (modules_remover_alfa.equals(modules_remover_beta) == true) {
							toBeRemoved.add(remover_beta);
							break;
						}
			}
			
			List<Reserve> newDomain = new ArrayList<>();
			
			for (int i = 0; i < remover_alfa_size; ++i)
				if (toBeRemoved.contains(i) == false)
					newDomain.add(domain.get(remover_alfa).get(i));
			
			domain.set(remover_alfa, newDomain);
		}
	}
	
	public static HashMap<Exam, List<Termin>> transform(){
		
		HashMap<Exam, List<Termin>> final_solution = new HashMap<>();
		
		for(Reserve reserve1 : solution) {
			List<Termin> list = new ArrayList<>();
			reserve1.getExam().setDayOfTrial(reserve1.getDay());
			for(Room r : reserve1.getRooms()) {
				Termin t = new Termin(r, reserve1.getTime(), reserve1.getDay());
				list.add(t);
			}
			final_solution.put(reserve1.getExam(), list);
		}
		
		return final_solution;
	}
}
