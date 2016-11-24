package com.system.model;

import java.io.Serializable;

public class Organism implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String label;
	
	private Long id; // TODO : Mandatory
	private String taxId;
	private String taxName;
	private String taxRefs;
	
	public Organism() {
		
	}
	
	public Organism(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public String getTaxRefs() {
		return taxRefs;
	}

	public void setTaxRefs(String taxRefs) {
		this.taxRefs = taxRefs;
	}
	
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Organism[");
        builder.append("id=").append(getId().toString()).append(",");
        builder.append("taxId=").append(getTaxId()).append(",");
        builder.append("taxName=").append(getTaxName()).append(",");
        builder.append("taxRefs=").append(getTaxRefs()).append("]");

        return builder.toString();
    }
	
}
