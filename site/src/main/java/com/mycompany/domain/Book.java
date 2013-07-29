package com.mycompany.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Index;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CAT_BOOK")
@XmlRootElement(name="book")
public class Book extends BaseProduct {

	private static final long serialVersionUID = 6956994002719373402L;

	@Column(name = "ISBN_10")
	@Index(name = "IDX_CAT_BOOK_ISBN_10")
	protected String isbn10;

	@XmlElement
	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}
		
}
