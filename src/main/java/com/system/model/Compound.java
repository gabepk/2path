package com.system.model;

import java.util.ArrayList;
import java.util.List;

public class Compound {

	private Long id; // TODO: Mandatory
	private String compoundName;
	private String compoundKEGG;
	private String compoundBioCyc;
	private String compoundChebi;
	private List<String> compoundRefs = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompoundName() {
		return compoundName;
	}

	public void setCompoundName(String compoundName) {
		this.compoundName = compoundName;
	}

	public String getCompoundKEGG() {
		return compoundKEGG;
	}

	public void setCompoundKEGG(String compoundKEGG) {
		this.compoundKEGG = compoundKEGG;
	}

	public String getCompoundBioCyc() {
		return compoundBioCyc;
	}

	public void setCompoundBioCyc(String compoundBioCyc) {
		this.compoundBioCyc = compoundBioCyc;
	}

	public String getCompoundChebi() {
		return compoundChebi;
	}

	public void setCompoundChebi(String compoundChebi) {
		this.compoundChebi = compoundChebi;
	}

	public List<String> getCompoundRefs() {
		return compoundRefs;
	}

	public void setCompoundRefs(List<String> compoundRefs) {
		this.compoundRefs = compoundRefs;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Compound[");
        builder.append("id=").append(getId()).append(",");
        builder.append("compoundName=").append(getCompoundName()).append(",");
        builder.append("compoundKEGG=").append(getCompoundKEGG()).append(",");
        builder.append("compoundBioCyc=").append(getCompoundBioCyc()).append(",");
        builder.append("compoundChebi=").append(getCompoundChebi()).append(",");
        builder.append("compoundRefs=[").append(getCompoundRefs()).append("]");
        builder.append("]");

        return builder.toString();
    }
	
}
