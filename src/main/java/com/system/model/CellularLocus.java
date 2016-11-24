package com.system.model;

public class CellularLocus {

	private Long id; // TODO: Mandatory
	private String cellularLocus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCellularLocus() {
		return cellularLocus;
	}

	public void setCellularLocus(String cellularLocus) {
		this.cellularLocus = cellularLocus;
	}
	
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("CellularLocus[");
        builder.append("id=").append(getId()).append(",");
        builder.append("cellularLocus=").append(getCellularLocus()).append(",");
        builder.append("]");

        return builder.toString();
    }
	
}
