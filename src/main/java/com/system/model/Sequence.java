package com.system.model;

public class Sequence {
	
	private Long id;
	private String taxId;
	private String ecNumber;
	private String seqFasta;
	
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
	public String getEcNumber() {
		return ecNumber;
	}
	public void setEcNumber(String ecNumber) {
		this.ecNumber = ecNumber;
	}
	public String getSeqFasta() {
		return seqFasta;
	}
	public void setSeqFasta(String seqFasta) {
		this.seqFasta = seqFasta;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Sequence[");
        builder.append("id=").append(getId().toString()).append(",");
        builder.append("taxId=").append(getTaxId()).append(",");
        builder.append("ecNumber=").append(getEcNumber()).append(",");
        builder.append("seqFasta=").append(getSeqFasta()).append("]");

        return builder.toString();
    }

}
