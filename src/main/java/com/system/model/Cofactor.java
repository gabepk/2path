package com.system.model;

import java.util.ArrayList;
import java.util.List;

public class Cofactor {
	
	private Long id; // TODO: Mandatory
	private String cofactorName;
	private String cofactorType;
	private List<String> cofactorRefs = new ArrayList<>();
	
	public Cofactor () {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCofactorName() {
		return cofactorName;
	}

	public void setCofactorName(String cofactorName) {
		this.cofactorName = cofactorName;
	}

	public String getCofactorType() {
		return cofactorType;
	}

	public void setCofactorType(String cofactorType) {
		this.cofactorType = cofactorType;
	}

	public List<String> getCofactorRefs() {
		return cofactorRefs;
	}

	public void setCofactorRefs(List<String> cofactorRefs) {
		this.cofactorRefs = cofactorRefs;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Cofactor[");
        builder.append("id=").append(getId()).append(",");
        builder.append("cofactorName=").append(getCofactorName()).append(",");
        builder.append("cofactorType=").append(getCofactorType()).append(",");
        builder.append("cofactorRefs=[").append(getCofactorRefs()).append("]");
        builder.append("]");

        return builder.toString();
    }
	
}
