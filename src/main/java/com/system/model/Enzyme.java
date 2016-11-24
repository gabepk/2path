package com.system.model;

import java.util.ArrayList;
import java.util.List;

public class Enzyme {

	private Long id; // TODO: Mandatory
	private String enzymeName;
	private String enzymeEC;
	private String enzymeUniprot;
	private String enzymeBrenda;
	private List<String> enzymeRefs = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEnzymeName() {
		return enzymeName;
	}

	public void setEnzymeName(String enzymeName) {
		this.enzymeName = enzymeName;
	}

	public String getEnzymeEC() {
		return enzymeEC;
	}

	public void setEnzymeEC(String enzymeEC) {
		this.enzymeEC = enzymeEC;
	}

	public String getEnzymeUniprot() {
		return enzymeUniprot;
	}

	public void setEnzymeUniprot(String enzymeUniprot) {
		this.enzymeUniprot = enzymeUniprot;
	}

	public String getEnzymeBrenda() {
		return enzymeBrenda;
	}

	public void setEnzymeBrenda(String enzymeBrenda) {
		this.enzymeBrenda = enzymeBrenda;
	}

	public List<String> getEnzymeRefs() {
		return enzymeRefs;
	}

	public void setEnzymeRefs(List<String> enzymeRefs) {
		this.enzymeRefs = enzymeRefs;
	}
	
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Enzyme[");
        builder.append("id=").append(getId()).append(",");
        builder.append("enzymeName=").append(getEnzymeName()).append(",");
        builder.append("enzymeEC=").append(getEnzymeEC()).append(",");
        builder.append("enzymeUniprot=").append(getEnzymeUniprot()).append(",");
        builder.append("enzymeBrenda=").append(getEnzymeBrenda()).append(",");
        builder.append("enzymeRefs=[").append(getEnzymeRefs()).append("]");
        builder.append("]");

        return builder.toString();
    }

}
