package com.mycompany.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Index;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CAT_JOURNAL")
@XmlRootElement(name="journal")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Journal extends BaseProduct {

	private static final long serialVersionUID = -5084468357220952664L;

	@Column(name = "ISSN")
	@Index(name = "IDX_CAT_JOURNAL_ISSN")
	protected String issn;

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}
	
	
	
}
