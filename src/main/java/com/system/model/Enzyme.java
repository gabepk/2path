package com.system.model;

import java.util.ArrayList;
import java.util.List;

public class Enzyme {

	private Integer id; // TODO: Mandatory
	private String enzymeAlternativeName;
	private String ecNumber;
	private String enzymeName;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getEnzymeAlternativeName() {
		return enzymeAlternativeName;
	}

	public void setEnzymeAlternativeName(String enzymeAlternativeName) {
		this.enzymeAlternativeName = enzymeAlternativeName;
	}

	public String getEcNumber() {
		return ecNumber;
	}

	public void setEcNumber(String ecNumber) {
		this.ecNumber = ecNumber;
	}

	public String getEnzymeName() {
		return enzymeName;
	}

	public void setEnzymeName(String enzymeName) {
		this.enzymeName = enzymeName;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Enzyme[");
        builder.append("id=").append(getId().toString()).append(",");
        builder.append("enzymeAlternativeName=").append(getEnzymeAlternativeName()).append(",");
        builder.append("ecNumber=").append(getEcNumber()).append(",");
        builder.append("enzymeName=").append(getEnzymeName()).append("]");

        return builder.toString();
    }

}
