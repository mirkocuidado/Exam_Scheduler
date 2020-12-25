package generals;

import java.util.List;

public class Domain {

	private List<Termin> domain;

	public Domain(List<Termin> domain) {
		super();
		this.domain = domain;
	}

	public List<Termin> getDomain() {
		return domain;
	}

	public void setDomain(List<Termin> domain) {
		this.domain = domain;
	}
}
	