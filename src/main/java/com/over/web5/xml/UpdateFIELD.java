package com.over.web5.xml;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "UpdateFIELD")
public class UpdateFIELD {

	String rcmsTicket="";
	String fieldTicket="";
	String date="";
	String time="";
	String comments="";
	String sourceSystem;
	String status="";
	String scheduling="";
	String cause="";
	String defect="";
	String solution="";

	public String getRcmsTicket() {
		return rcmsTicket;
	}
	@XmlElement
	public void setRcmsTicket(String rcmsTicket) {
		this.rcmsTicket = rcmsTicket;
	}

	public String getFieldTicket() {
		return fieldTicket;
	}
	@XmlElement
	public void setFieldTicket(String fieldTicket) {
		this.fieldTicket = fieldTicket;
	}

	public String getDate() {
		return date;
	}
	@XmlElement
	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}
	@XmlElement
	public void setTime(String time) {
		this.time = time;
	}

	public String getComments() {
		return comments;
	}
	@XmlElement
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}
	@XmlElement
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getStatus() {
		return status;
	}
	@XmlElement
	public void setStatus(String status) {
		this.status = status;
	}

	public String getScheduling() {
		return scheduling;
	}
	@XmlElement
	public void setScheduling(String scheduling) {
		this.scheduling = scheduling;
	}

	public String getCause() {
		return cause;
	}
	@XmlElement
	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getDefect() {
		return defect;
	}
	@XmlElement
	public void setDefect(String defect) {
		this.defect = defect;
	}

	public String getSolution() {
		return solution;
	}
	@XmlElement
	public void setSolution(String solution) {
		this.solution = solution;
	}

}
