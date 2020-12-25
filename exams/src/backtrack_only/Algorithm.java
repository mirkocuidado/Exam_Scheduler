package backtrack_only;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import generals.Exam;
import generals.Reserve;
import generals.Room;
import generals.Termin;

public class Algorithm {
	private List<Exam> exams;
	private List<List<Reserve>> domains;
	public static Reserve solution[];

	String termins[] = { "08:00", "11:30", "15:00", "18:30" };
	
	 /********** CONSTRUCTOR **********/
	public Algorithm(List<Exam> exams, int size) {
		solution = new Reserve[size];
		this.exams = exams;
		
		domains = new ArrayList<>();
		for(int i=0; i < size; ++i)
			domains.add(new ArrayList<>());
	}
	
	 /********** INITIALIZATION **********/
	public List<List<Reserve>> initializeDomains(List<Room> rooms, List<Exam> exams, int duration) {
		int number_of_exams = exams.size();
		
		/********** FOR EACH EXAM IN THE LIST **********/
		for (int i = 0; i < number_of_exams; ++i) {
			
			/********** EXAMS WHICH REQUIRE PC => ROOMS WITH PC! EXAMS WHICH DON'T REQUIRE PC => ROOMS WITH AND WITHOUT PC!**********/
			List<Room> rooms_with_PC = new ArrayList<>(rooms);
			
			if (exams.get(i).getIsOnPCs() == 1) {
				for (Room room : rooms) {
					if (room.getHasPCs() == 1) {
						rooms_with_PC.remove(room);
					}
				}
			}
			
			/**********
			 DIVIDE THE ROOMS INTO TWO GROUPS:
			   1) can fit all the students required for the exam => directly to domain
			   2) can not fit all the students required for the exam => process
			 **********/
			
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
			
			/********** Algorithm from the Internet. Make all subsets of a current set for a given number N as a restriction **********/
			long total = (long) Math.pow(2, rooms_without_enough_capacity.size());
			
			for (long n = 1; n < total; ++n) {
				List<Room> reservationRooms = new ArrayList<>();
				int total_space = 0;
				
				for (int mask = 0; mask < rooms_without_enough_capacity.size(); mask++) {
					if ((n & (1 << mask)) != 0) {
						total_space += rooms_without_enough_capacity.get(mask).getCapacity();
						reservationRooms.add(rooms_without_enough_capacity.get(mask));
					}
				}
				
				if (total_space >= exams.get(i).getNumberOfSignedStudents()) {
					for (int day = 1; day <= duration; day++) {
						for(int index=0; index < 4; ++index) {
							domains.get(i).add(new Reserve(exams.get(i), day, termins[index], reservationRooms));
						}
					}
				}
			}
		}
		return domains;
	}

	/********** ALGORITHM FROM CLASS **********/
	public boolean algorithm(List<List<Reserve>> domains, int level) {
		if (level == exams.size())
			return true;
		
		List<Reserve> current_list = domains.get(level);
		
		//current_list = sortDomainForCurrentTermin(current_list);
		
		for (Reserve reservation : current_list) {
			solution[level] = reservation;
			
			/********** COPY ABSOLUTELY EVERYTHING **********/
			List<List<Reserve>> copy_of_all_domains = new ArrayList<>();
			for (int i = 0; i < domains.size(); i++)
				copy_of_all_domains.add(new ArrayList<Reserve>(domains.get(i)));
			
			List<Reserve> newDomain = new ArrayList<Reserve>();
			newDomain.add(reservation);
			copy_of_all_domains.set(level, newDomain);
			
			/********** REMOVE FROM THE COPY JUST MADE ALL INVALID RESERVATIONS **********/
			remove_unnecessary(copy_of_all_domains, level, reservation);
			
			if (algorithm(copy_of_all_domains, level + 1) == true)
				return true;
			
			solution[level] = null;
		}
		return false;
	}
	
	/********** REMOVING **********/
	private void remove_unnecessary(List<List<Reserve>> domain, int level, Reserve reservation) {
		for (int variable1 = 0; variable1 < domain.size(); ++variable1) {
			
			/********** FOR ALL VARIABLES EXCEPT THE "FINISHED" ONES AND THE CURRENT ONE **********/
			if (variable1 == level || solution[variable1] != null) 
				continue;
			
			Set<Integer> toBeRemoved = new HashSet<>();
			
			for (int variable2 = 0; variable2 < domain.get(variable1).size(); ++variable2) {
				
				Reserve reservation2 = domain.get(variable1).get(variable2);
				
				Exam exam1 = reservation.getExam();
				Exam exam2 = reservation2.getExam();
				
				int day1 = reservation.getDay();
				int day2 = reservation2.getDay();
				
				String time1 = reservation.getTime();
				String time2 = reservation2.getTime();
				
				List<String> modules1 = exam1.getModules();
				List<String> modules2 = exam2.getModules();
				
				int year1 = exam1.getExamCode().getGodinaStudija();
				int year2 = exam2.getExamCode().getGodinaStudija();
				
				/********** REMOVE THE ONES WITH THE SAME DAY, SAME TIME AND SAME ROOM **********/
				if(day1 == day2 && time1.equals(time2)) {
					for (Room room1 : reservation2.getRooms()) {
						for (Room room2 : reservation.getRooms()) {
							if (room1.getName().equals(room2.getName())) {
								toBeRemoved.add(variable2);
								continue;
							}
						}
					}
				}
				
				/********** REMOVE THE ONES WITH THE SAME DAY, SAME YEAR AND SAME MODULE **********/
				if (day1 == day2 && year1 == year2) {
					for (String module1 : modules1) {
						for (String module2 : modules2) {
							if (module1.equals(module2) == true) {
								toBeRemoved.add(variable2);
								continue;
							}
						}
					}
				}
				
				/*** +++++++++++++++++ ***/
				/*** ADDITIONAL POINTS ***/
				/*** +++++++++++++++++ ***/
					
				/********** REMOVE THE ONES WITH THE SAME DAY, SAME TIME AND ONE YEAR DIFFERENCE **********/
				if (day1 == day2 && time1.equals(time2) && Math.abs(year1 - year2)==1) {
					for (String module1 : modules1) {
						for (String module2 : modules2) {
							if (module1.equals(module2) == true) {
								toBeRemoved.add(variable2);
								continue;
							}
						}
					}
				}
			}
			
			
			/********** REMOVE ALL OF THE ONES WE SELECTED FOR A NEW DOMAIN **********/
			List<Reserve> newDomain = new ArrayList<>();
			for (int i = 0; i < domain.get(variable1).size(); ++i)
				if (!toBeRemoved.contains(i))
					newDomain.add(domain.get(variable1).get(i));
			
			domain.set(variable1, newDomain);
		}
	}
	
	/********** TRANSFORMATION FOR A EASIER OUTPUT **********/
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

	/********** SORTING **********/
	class SortReserves implements Comparator<Reserve> 
	{  
		public int compare(Reserve a, Reserve b) 
	    { 
	        return a.getCostOfReserve() - b.getCostOfReserve(); 
	    } 
	} 
	
	/********** SORTING **********/
	List<Reserve> sortDomainForCurrentTermin(List<Reserve> lista) {

		Collections.sort(lista, new SortReserves());

		return lista;
	}

}