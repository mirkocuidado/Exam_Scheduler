package generals;

import java.util.List;

public class Rok {
	private int duration;
	private List<Exam> exams;
	
	public Rok(int duration, List<Exam> exams) {
		super();
		this.duration = duration;
		this.exams = exams;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public List<Exam> getExams() {
		return exams;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}	
	
}
