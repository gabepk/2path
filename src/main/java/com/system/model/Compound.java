package com.system.model;

public class Compound {

	private Integer id; // TODO: Mandatory
	private String keggID_compound;
	private String compoundName;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getKeggID_compound() {
		return keggID_compound;
	}

	public void setKeggID_compound(String keggID_compound) {
		this.keggID_compound = keggID_compound;
	}
	
	public String getCompoundName() {
		return compoundName;
	}

	public void setCompoundName(String compoundName) {
		this.compoundName = compoundName;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Compound[");
        builder.append("id=").append(getId().toString()).append(",");
        builder.append("keggID_compound=").append(getCompoundName()).append(",");
        builder.append("compoundName=").append(getKeggID_compound()).append("]");

        return builder.toString();
    }
	
}
