package com.system.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Organism implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String label; // TODO: Retirar esse atributo
	
	private Long id; // TODO : Mandatory
	private Long taxId;
	private String taxName;
	private List<String> taxRefs = new ArrayList<>();
	
	public Organism() {
		
	}
	
	public Organism(String label) {
		this.label = label;
	}
	
	// TODO: Retirar esse metodo
	public String getLabel() {
		return label;
	}
	
	// TODO: Retirar esse metodo
	public void setLabel(String label) {
		this.label = label;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaxId() {
		return taxId;
	}

	public void setTaxId(Long taxId) {
		this.taxId = taxId;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public List<String> getTaxRefs() {
		return taxRefs;
	}

	public void setTaxRefs(List<String> taxRefs) {
		this.taxRefs = taxRefs;
	}
	
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Organism[");
        builder.append("id=").append(getId()).append(",");
        builder.append("taxId=").append(getTaxId()).append(",");
        builder.append("taxName=").append(getTaxName()).append(",");
        builder.append("taxrefs=[").append(getTaxRefs()).append("]");
        builder.append("]");

        return builder.toString();
    }
	
}
