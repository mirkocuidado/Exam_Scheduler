package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import generals.Exam;
import generals.Room;
import generals.Termin;

public class CSP_Algorithm {

	/***** FINAL SOLUTION OF THIS ALGORITHM *****/
	public static HashMap<Exam, List<Termin>> solution = new HashMap<>();

	/***** ALL THE EXAMS IN THE CHALLENGE *****/
	public static List<Exam> exams = new ArrayList<>();

	/***** INITIALIZE EACH EXAM WITH A LIST OF POSSIBLE TERMINS *****/
	public void initialDomain(List<Exam> exams, List<Room> rooms, String termins[], int duration) {
		for (Exam exam : exams) {
			List<Termin> listOfTermins = new ArrayList<>();
			for (int day = 1; day <= duration; ++day) {
				for (Room room : rooms) {
					if (room.getHasPCs() == 0 && exam.getIsOnPCs() == 1) {
						// then it is not in the domain!
					} else {
						for (String time : termins) {
							Termin termin = new Termin(room, time, day);
							listOfTermins.add(termin);
						}
					}
				}
			}
			exam.setDomain(listOfTermins);
			listOfTermins = null;
		}
	}

	/***** EXAM SORTING DESCENDING BY NUMBER OF SIGNED UP STUDENTS*****/
	class SortExams implements Comparator<Exam> 
	{  
		public int compare(Exam a, Exam b) 
	    { 
	        return b.getNumberOfSignedStudents() - a.getNumberOfSignedStudents(); 
	    } 
	} 
	
	List<Exam> sortExams(List<Exam> exams) {

		Collections.sort(exams, new SortExams());

		return exams;
	}
	
	/***** EXAM CHOICE *****/
	public Exam getExamWithLeastPossibleOptions(List<Exam> exams) {
		
		/***** ONE WITH THE LEAST POSSIBLE SOLUTIONS *****/
		/*for(Exam e: exams) {
			if(e.getNumOfPlaceTaken() != 0) {
				return e;
			}
		}
		
		int min = 100000;
		String examMinName = "";

		for (Exam e : exams) {
			if (e.getDomain().size() < min) {
				examMinName = e.getExamCodee();
				min = e.getDomain().size();
			}
		}

		for (Exam e : exams) {
			if (e.getExamCodee() == examMinName)
				return e;
		}*/

		/***** ONE WITH THE MOST STUDENTS REQUIRED *****/
		return exams.get(0);
	}

	/***** TERMIN SORTING DESCENDING BY NUMBER OF SIGNED UP STUDENTS*****/
	class SortTermins implements Comparator<Termin> 
	{  
		public int compare(Termin a, Termin b) 
	    { 
	        return b.getRoom().getCapacity() - a.getRoom().getCapacity(); 
	    } 
	} 
	
	List<Termin> sortDomainForCurrentTermin(Exam exam) {

		Collections.sort(exam.getDomain(), new SortTermins());

		return exam.getDomain();
	}

	/***** CHECK IF ASSIGNING ONE TERMIN TO THE SOLUTION IS VALID *****/
	public boolean isValidToAssign(List<Termin> listForThisExam, Termin termin, Exam exam) {
		
		/***** if our list is empty, then we can assign it, since we remove all invalid termins until then *****/
		if (listForThisExam.size() == 0)
			return true;
		
		/***** if we have a termin we want to assign, but the list is not empty -> one termin is already assigned, it is not valid if the
		 * 			1) day is not the same as the one already assigned or
		 *   		2) room is the same as the one already assigned or
		 *   		3) time is not the same as the one already assigned
		 *   *****/
		
		for (Termin t : listForThisExam) {
			if (termin.getDay() != t.getDay() || termin.getRoom().getName().equals(t.getRoom().getName()) || !termin.getTime().equals(t.getTime()))
				return false;
		}
		
		/*****  if the previous for loop is passed, we return true *****/
		return true;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	static int function_name = 0;
	
	public boolean algorithm(List<Exam> exams, int exams_size, int level, Exam exam_param, String func_name) {	
		
		/***** WHEN ALL OF OUR EXAMS ARE SOLVED, WE RETURN TRUE! *****/
		if (level == exams_size) {
			return true;
		}

		/***** SORT THE EXAMS BY THE MOST SIGNED UP STUDENTS *****/
		exams = sortExams(exams);
		
		/***** GET THE FIRST OF THE EXAMS *****/
		Exam exam = getExamWithLeastPossibleOptions(exams);
		
		/***** IF IT IS NULL, THEN EVERYTHING IS DONE *****/
		if (exam == null) {
			return true;
		}

		/*****  WARNING!!! POSSIBLY NOT NEEDED *****/
		if(exam_param!=null)
			exam.setNumOfPlaceTaken(exam_param.getNumOfPlaceTaken());
		
		/*****  IF THE EXAM IS NOT IN THE SOLUTION, THEN WE ADD IT AN EMPTY LIST OF TERMINS *****/
		int flagFound = 0;
		for(Exam e: solution.keySet()) {
			if(e.equals(exam)) {
				flagFound = 1;
				break;
			}
		}
		if (flagFound == 0) {
			solution.put(exam, new ArrayList<>());
		}
		
		/*****  WE NEED TO SORT THE DOMAIN OF THE CURRENT EXAM *****/
		exam.setDomain(sortDomainForCurrentTermin(exam));

		/***** WE GO THROUGH EACH AND EVERY TERMIN *****/
		for (int i = 0; i < exam.getDomain().size(); i++) {
			
			/***** WE TAKE A SINGLE TERMIN FROM THE LIST OF THIS EXAM *****/
			Termin termin = exam.getDomain().get(i);

			/***** WE GET THE LIST OF TERMINS ASSIGNED TO THIS EXAM FROM THE SOLUTION HASHMAP *****/
			List<Termin> solution_termins_for_exam = new ArrayList<>();
			
			for(Exam e: solution.keySet()) {
				if(e.equals(exam)) {
					solution_termins_for_exam = solution.get(e);
					break;
				}
			}
			
			/***** CHECK IF THE ASSIGNEMENT IS VALID *****/
			if (isValidToAssign(solution_termins_for_exam, termin, exam)) {

				/***** IF IT IS VALID, ADD THIS TERMIN TO THE SOLUTION HASHMAP FOR THIS EXAM *****/
				for(Exam e: solution.keySet()) {
					if(e.equals(exam)) {
						solution.get(e).add(termin);
						break;
					}
				}
				
				/***** ADD THE STUDENTS FOR THIS EXAM WHICH HAVE BEEN ADDED *****/
				exam.setNumOfPlaceTaken(termin.getRoom().getCapacity() + exam.getNumOfPlaceTaken());
				
				/***** SET THE DAY FOR THIS EXAM -> will be done every time, but it doesn't matter due to our isValidToAssign function *****/
				exam.setDayOfTrial(termin.getDay());
				
				/***** MAKE A COPY OF ALL THE EXAMS *****/
				List<Exam> copy_exams = new ArrayList<>();
				
				for(Exam e: exams) {
					
					/***** this if was needed before I removed the exams which were finished with assignements *****/
					if(e.getNumOfPlaceTaken() <= e.getNumberOfSignedStudents()) {
						
						/***** COPY A SINGLE EXAM *****/
						Exam copy_exam = new Exam(e.getExamCodee(), e.getNumberOfSignedStudents(), e.getIsOnPCs(), e.getModules());
						
						/***** FOR THIS EXAM SET THE NUMBER OF STUDENTS WHO TOOK PLACE TO CURRENT NUMBER *****/
						if(e.equals(exam)) 
							//copy_exam.setNumOfPlaceTaken(termin.getRoom().getCapacity() + copy_exam.getNumOfPlaceTaken()); radi sigurno
							copy_exam.setNumOfPlaceTaken(exam.getNumOfPlaceTaken());
						
						/***** COPY ALL TERMINS FOR THIS EXAM *****/
						List<Termin> copy_termins = new ArrayList<>();
						for(Termin termin_iter: e.getDomain()) {
							Termin copy_termin = new Termin(termin_iter.getRoom(), termin_iter.getTime(), termin_iter.getDay());
							copy_termins.add(copy_termin);
						}
					
						/***** CHECK IF CURRENT EXAM AND THE CHOSEN ONE ARE THE SAME YEAR AND MODULE *****/
						boolean sameYearAndModule = exam.checkIfExamsAreSameYearAndSameModule(e);
						int dayOfRok = -1; 
						/***** IF THEY ARE THE SAME YEAR AND MODULE, GET THE DAY OF THE ROK -> IF THEY AREN'T dayOfRok IS -1 *****/
						if(sameYearAndModule == true && exam.equals(e)==false) 
							dayOfRok = termin.getDay();
						
						/***** MAKE A SET OF ALL TERMINS TO BE REMOVED FROM THE copy_termins LIST *****/
						Set<Termin> toBeRemoved = new HashSet<Termin>();
						
						/***** REMOVING CERTAIN TERMINS *****/
						for(int j=0; j<copy_termins.size(); ++j) {

							/***** FOR ALL THE EXAMS REMOVE THE CURRENT MEETING WE ARE GOING THROUGH *****/
							if(copy_termins.get(j).equals(termin) == true) {
								toBeRemoved.add(copy_termins.get(j));
							}
							
							/***** IF THE CURRENT EXAM AND THE CHOSEN ONE ARE NOT THE SAME *****/
							if(exam.equals(e) == false) {
								/***** AND THE MODULE AND THE YEAR OF THESE EXAMS ARE THE SAME (dayOfRok!= -1) THEN REMOVE THAT TERMIN FOR THAT EXAM *****/
								if(dayOfRok == copy_termins.get(j).getDay()) {
									toBeRemoved.add(copy_termins.get(j));
								}
								/*** +++++++++++++++++ ***/
								/*** ADDITIONAL POINTS ***/
								/*** +++++++++++++++++ ***/
								if(exam.checkIfExamsAreSameModule(e) ) {
									if(Math.abs(e.getExamCode().getGodinaStudija() - exam.getExamCode().getGodinaStudija()) == 1) {
										if(termin.getDay() == copy_termins.get(j).getDay() && termin.getTime().equals(copy_termins.get(j).getTime())) {
											toBeRemoved.add(copy_termins.get(j));
										}
									}
								}
							}
							/*** IF THE CURRENT EXAM AND THE CHOSEN ONE ARE THE SAME***/
							else {
								/*** IF THE DAYS ARE THE SAME AND IF THE ROOMS ARE THE SAME, THEN WE CAN REMOVE ALL OF THOSE ROOMS FROM THE copy_termins***/
								if(termin.getDay() == copy_termins.get(j).getDay() && termin.getRoom().equals(copy_termins.get(j).getRoom())) {
									toBeRemoved.add(copy_termins.get(j));
								}
								
							}
						}
						
						/*** REMOVING THEM ALL ***/
						for(Termin t: toBeRemoved) {
							copy_termins.remove(t);
						}
						
						copy_exam.setDomain(copy_termins);
						
						copy_exams.add(copy_exam);
					}
				}
				
				/*** IF THE EXAM IS "DONE" THEN WE ADD AN INCREMENT TO THE LEVEL OF THE FUNCTION ***/
				int isDone = 0;
				if(exam.getNumberOfSignedStudents() <= exam.getNumOfPlaceTaken()) {
					copy_exams.remove(exam);
					isDone = 1;
				}
				else {
					isDone = 0;
				}
				
				/*** CALL RECURSIVE FUNCTION WITH EITHER A level+0 OR level+1 AND WITH/WITHOUT THE exam_copy PARAMETER (useless) ***/
				if (algorithm(copy_exams, exams_size, level + isDone, (isDone==1)? null: exam, "f"+function_name++) == true) {
					return true;
				} else {
					/*** BACKTRACKING!!! ***/
					
					//System.out.println("BACKTRACK! " + exam.getExamCodee() + " " + termin.getRoom().getName() + " " + termin.getTime() + " " +termin.getDay()) ;
					
					/*** REMOVE THE CURRENT TERMIN FOR THIS EXAM FROM THE SOLUTION***/
					for(Exam e: solution.keySet()) {
						if(e.equals(exam)) {
							solution.get(e).remove(termin);
							break;
						}
					}
					
					/*** REMOVE THE PEOPLE FROM THE CURRENT TERMIN***/
					exam.setNumOfPlaceTaken(exam.getNumOfPlaceTaken() - termin.getRoom().getCapacity());
					
					/*** RETURN THE CURRENT TERMIN FOR ALL THE EXAMS, EXCEPT THE CHOSEN ONE, SO IT CAN BE CHOSEN AGAIN ***/
					for(Exam e: copy_exams) {
						if(!e.equals(exam)) //dodato
							e.getDomain().add(termin);
					}
				}
			}
		}
		
		/*** IF ALL THE TERMINS ARE FINISHED WE EITHER RETURN TRUE OF FALSE BASED ON "FINISHING" THE EXAM ***/
		if(exam.getNumOfPlaceTaken() >= exam.getNumberOfSignedStudents()) {
			return true;
		}
		else {
			return false;
		}
	}
}