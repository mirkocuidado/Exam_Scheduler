package main;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import generals.Exam;
import generals.Room;
import generals.Termin;
import readfile.ReadFileRok;
import readfile.ReadFileRooms;

public class Main {

	public static void main(String[] argv) {
		String roomss;
		String rok;
		String resenjeFileName = "result.csv";
		
		FileWriter myWriter = null;
		
		if (argv.length < 2 || argv.length > 3) {
			System.out.println("Neodgovarajuci broj ulaznih parametara");
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
		
		boolean solution = false;
		
		solution = csp.algorithm(exams, exams.size(), level, null, "f0");
		
		if(solution==false) {
			System.out.println("NOT POSSIBLE!");
			System.exit(1);
		}
		
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
			
				HashMap<Exam, List<Termin>> final_solution = CSP_Algorithm.solution;
				
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
		
	return;
	}
}
