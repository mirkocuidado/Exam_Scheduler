package generals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exam {
	
	public static class ExamCode{
		
		private String godinaAkreditacije;
		private Character studijskiProgram;
		private String maticnaKatedra;
		private int godinaStudija;
		private String nazivPredmeta;
		
		public ExamCode(String examCode) {
			Pattern pattern = Pattern.compile("^(\\d{2})(\\w)(\\d{2})(\\d{1})(\\w{1,4})$", Pattern.UNICODE_CHARACTER_CLASS);
			Matcher matcher = pattern.matcher(examCode);
			if(matcher.find()) {
				godinaAkreditacije = matcher.group(1);
				studijskiProgram = matcher.group(2).charAt(0);
				maticnaKatedra = matcher.group(3);
				godinaStudija = Integer.parseInt(matcher.group(4));
				nazivPredmeta = matcher.group(5);
			}
			else {
				godinaAkreditacije = examCode.charAt(0) + examCode.charAt(1) + "";
				studijskiProgram = examCode.charAt(2);
				maticnaKatedra = examCode.charAt(3) + examCode.charAt(4) + "";
				godinaStudija = Integer.parseInt(examCode.charAt(5)+"");
				nazivPredmeta = examCode.charAt(6) + "";
			}
		}

		public String getGodinaAkreditacije() {
			return godinaAkreditacije;
		}

		public void setGodinaAkreditacije(String godinaAkreditacije) {
			this.godinaAkreditacije = godinaAkreditacije;
		}

		public Character getStudijskiProgram() {
			return studijskiProgram;
		}

		public void setStudijskiProgram(Character studijskiProgram) {
			this.studijskiProgram = studijskiProgram;
		}

		public String getMaticnaKatedra() {
			return maticnaKatedra;
		}

		public void setMaticnaKatedra(String maticnaKatedra) {
			this.maticnaKatedra = maticnaKatedra;
		}

		public int getGodinaStudija() {
			return godinaStudija;
		}

		public void setGodinaStudija(int godinaStudija) {
			this.godinaStudija = godinaStudija;
		}

		public String getNazivPredmeta() {
			return nazivPredmeta;
		}

		public void setNazivPredmeta(String nazivPredmeta) {
			this.nazivPredmeta = nazivPredmeta;
		}
	}
	
	private ExamCode examCode;
	private int numberOfSignedStudents;
	private int isOnPCs;
	private List<String> modules;
	private String examCodee;
	
	private int dayOfTrial;
	private int numOfPlaceTaken;
	
	private List<Termin> domain;
	
	public Exam(String examCodee, int numberOfSignedStudents, int isOnPCs, List<String> modules) {
		this.examCode = new ExamCode(examCodee);
		this.numberOfSignedStudents = numberOfSignedStudents;
		this.isOnPCs = isOnPCs;
		this.modules = modules;
		this.examCodee = examCodee;
		this.domain = new ArrayList<>();
	}
	
	public boolean checkIfExamsAreSameYearAndSameModule(Exam e) {
		
		if(examCode.getGodinaStudija() != e.getExamCode().getGodinaStudija()) return false;
		
		List<String> eListOfModules = e.getModules();
		for(String module: modules) {
			for(String m: eListOfModules) {
				if(module.equals(m) == true) return true;
			}
		}
		
		return false;
	}
	
	public static boolean checkIfExamsAreSameYearAndSameModuleS(Exam e1, Exam e2) {
		
		if(e1.getExamCode().getGodinaStudija() != e2.getExamCode().getGodinaStudija()) return false;
		
		List<String> eListOfModules = e1.getModules();
		for(String module: e1.getModules()) {
			for(String m: eListOfModules) {
				if(module.equals(m) == true) return true;
			}
		}
		
		return false;
	}
	
	public boolean checkIfExamsAreSameYearAndSameModuleUpgrade(Exam e) { // useless
		
		if(examCode.getGodinaStudija() != e.getExamCode().getGodinaStudija()) return false;
		
		if(Math.abs(examCode.getGodinaStudija() - e.getExamCode().getGodinaStudija()) > 1) return false;
		
		//System.out.println(examCode.getGodinaStudija() + " " + e.getExamCode().getGodinaStudija());
		List<String> eListOfModules = e.getModules();
		for(String module: modules) {
			for(String m: eListOfModules) {
				if(module.equals(m) == true) return true;
			}
		}
		
		return false;
	}
	
	public boolean checkIfExamsAreSameModule(Exam e) {
		List<String> eListOfModules = e.getModules();
		for(String module: modules) {
			for(String m: eListOfModules) {
				if(module.equals(m) == true) return true;
			}
		}
	
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
	 if(obj instanceof Exam) {
		 Exam exam = (Exam) obj;
		 return exam.examCodee.equals(this.examCodee);
	 }
	 return false;
	}

	public int getDayOfTrial() {
		return dayOfTrial;
	}

	public void setDayOfTrial(int dayOfTrial) {
		this.dayOfTrial = dayOfTrial;
	}
	
	public List<Termin> getDomain() {
		return domain;
	}

	public void setDomain(List<Termin> domain) {
		this.domain = domain;
	}

	public int getNumOfPlaceTaken() {
		return numOfPlaceTaken;
	}

	public void setNumOfPlaceTaken(int numOfPlaceTaken) {
		this.numOfPlaceTaken = numOfPlaceTaken;
	}

	public ExamCode getExamCode() {
		return examCode;
	}

	public void setExamCode(ExamCode examCode) {
		this.examCode = examCode;
	}

	public int getNumberOfSignedStudents() {
		return numberOfSignedStudents;
	}

	public void setNumberOfSignedStudents(int numberOfSignedStudents) {
		this.numberOfSignedStudents = numberOfSignedStudents;
	}

	public int getIsOnPCs() {
		return isOnPCs;
	}

	public void setIsOnPCs(int isOnPCs) {
		this.isOnPCs = isOnPCs;
	}

	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public String getExamCodee() {
		return examCodee;
	}
	
	public void removeTerminFromList(Termin t) {
		for(int i=0; i<domain.size();i++) {
			Termin tt = domain.get(i);
			if(tt.equals(t)) {
				domain.remove(tt);
			}
		}
	}
	
	public void removeTerminFromList(String s) {
		for(int i=0; i<domain.size();i++) {
			Termin tt = domain.get(i);
			String ss = tt.getDay()+tt.getTime();
			if(ss.equals(s)) {
				domain.remove(tt);
			}
		}
	}

	public void setExamCodee(String examCodee) {
		this.examCodee = examCodee;
	}
	
	private static class SortExams implements Comparator<Exam> {

		@Override
		public int compare(Exam e1, Exam e2) {
			return e2.getNumberOfSignedStudents() - e1.getNumberOfSignedStudents();
		}
		
	}
	
	public static List<Exam> sortExamsByStudentsApplied(List<Exam> exams) {
		Collections.sort(exams, new SortExams());
		return exams;
	}
}
