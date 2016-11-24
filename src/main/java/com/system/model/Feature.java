package com.system.model;

import java.util.ArrayList;
import java.util.List;

public class Feature {

	private Long id; // TODO: Mandatory
	private String featureName;
	private String featureType;
	private String featureSequence;
	private String featureGff;
	private String featureGenbank;
	private List<String> featureRefs = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureSequence() {
		return featureSequence;
	}

	public void setFeatureSequence(String featureSequence) {
		this.featureSequence = featureSequence;
	}

	public String getFeatureGff() {
		return featureGff;
	}

	public void setFeatureGff(String featureGff) {
		this.featureGff = featureGff;
	}

	public String getFeatureGenbank() {
		return featureGenbank;
	}

	public void setFeatureGenbank(String featureGenbank) {
		this.featureGenbank = featureGenbank;
	}

	public List<String> getFeatureRefs() {
		return featureRefs;
	}

	public void setFeatureRefs(List<String> featureRefs) {
		this.featureRefs = featureRefs;
	}
	
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Enzyme[");
        builder.append("id=").append(getId()).append(",");
        builder.append("featureName=").append(getFeatureName()).append(",");
        builder.append("featureType=").append(getFeatureType()).append(",");
        builder.append("featureSequence=").append(getFeatureSequence()).append(",");
        builder.append("featureGff=").append(getFeatureGff()).append(",");
        builder.append("featureGenbank=").append(getFeatureGenbank()).append(",");
        builder.append("featureRefs=[").append(getFeatureRefs()).append("]");
        builder.append("]");

        return builder.toString();
    }

}
