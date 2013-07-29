package com.mycompany.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CAT_ELECTRONIC_PRODUCT")
@XmlRootElement(name="electronicProduct")
public class Electronic extends BaseProduct {

	private static final long serialVersionUID = 8186655362025731213L;
	
	@Column(name = "RESOURCE_URL")
	protected String resourceUrl;

	@XmlElement
	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

}
