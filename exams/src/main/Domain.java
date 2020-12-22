package main;

import java.util.ArrayList;
import java.util.List;

import generals.Termin;

public class Domain {

	private List<Termin> domain;

	
	public Domain(List<Termin> domain) {
		super();
		this.domain = domain;
	}


	@Override
	protected Domain clone() throws CloneNotSupportedException {
		List<Termin> domain_copy = new ArrayList<>();
		domain.forEach((Termin tI)->{
			domain_copy.add(tI.clone());
		});
		return new Domain(domain_copy);		
		
	}
	
}
	