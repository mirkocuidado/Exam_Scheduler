package main;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import backtrack_only.Algorithm;
import backtrack_plus_forward.CSP_Algorithm;
import generals.Exam;
import generals.Reserve;
import generals.Room;
import generals.Termin;
import readfile.ReadFileRok;
import readfile.ReadFileRooms;

public class Main {

	public static void main(String[] argv) {
		String roomss;
		String rok;
		String resenjeFileName = "result_no_cost1.csv";
		
		FileWriter myWriter = null;
		
		if (argv.length < 2 || argv.length > 3) {
			System.out.println("Invalid number of parameters!");
			return;
		}
		
		roomss = argv[0];
		rok = argv[1];
		
		ReadFileRooms readFileRooms = new ReadFileRooms(roomss);
		ReadFileRok readFileRok = new ReadFileRok(rok);
		
		List<Room> rooms = readFileRooms.getRooms();
		List<Exam> exams = readFileRok.getExams();
		
		String termins[] = { "08:00", "11:30", "15:00", "18:30" };
		int duration = readFileRok.getDuration();
		
		CSP_Algorithm csp = new CSP_Algorithm();
		
		csp.initialDomain(exams, rooms, termins, duration);
		
		int level = 0;
		
		exams = Exam.sortExamsByStudentsApplied(exams);
		
		int size = exams.size();
		
		Algorithm algorithm = new Algorithm(exams, size);
		
		List<List<Reserve>> domains = algorithm.initializeDomains(rooms, exams, duration);
		
		boolean solution_exists = algorithm.algorithm(domains, level);
		
		Reserve[] solution = (solution_exists == true) ? Algorithm.solution : null;
		
		if(solution == null) {
			System.out.println("Solution does not exist!");
			System.exit(1);
		}
		else {
			
			HashMap<Exam, List<Termin>> final_solution = Algorithm.transform();
			
			try {
				myWriter = new FileWriter(resenjeFileName);
				
				for(int day = 1; day <= duration; ++day) {
					
					String [][] matrix = new String[5][rooms.size()+1];
						
					matrix[0][0] = "Dan" + day;
						
					for(int i = 1; i < 5; i++) {
						matrix[i][0] = termins[i-1]; 
					}
						
					for(int j=1; j<rooms.size()+1; j++) {
						matrix[0][j] = rooms.get(j-1).getName();
					}
						
					Set<Exam> exams_output = final_solution.keySet();
			
					for(int row = 1; row < 5; ++row) {
						for(int column = 1; column < rooms.size()+1; ++column) {
								
							for(Exam e : exams_output) {
									
								if(e.getDayOfTrial() == day) {
										
									for(Termin t : final_solution.get(e)) {
											
										if(t.getRoom().getName() == matrix[0][column] && t.getTime().equals(matrix[row][0])) {
											
											matrix[row][column] = e.getExamCodee();
										}
									}
								}
							}
						}
					}
						
					for(int i = 0; i < 5; i++) {
						for(int j = 0; j < rooms.size()+1; j++) {
							if(j!=rooms.size()) {
								if(matrix[i][j]==null) {
									System.out.print("X,");
									myWriter.write("X,");
								}
								else {
									System.out.print(matrix[i][j] + ",");
									myWriter.write(matrix[i][j] + ",");
								}
							}
							else {
								if(matrix[i][j]==null) {
									System.out.print("X");
									myWriter.write("X");
								}
								else {
									System.out.print(matrix[i][j]);
									myWriter.write(matrix[i][j]);
								}
							}
						}
						System.out.println();
						myWriter.write("\n");;
					}
				myWriter.write("\n");
			}
			myWriter.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
		
	return;
	}
}
