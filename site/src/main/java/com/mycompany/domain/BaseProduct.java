package com.mycompany.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.ProductImpl;
import org.hibernate.annotations.Index;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CAT_PRODUCT")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class BaseProduct extends ProductImpl implements Product {
	
	private static final long serialVersionUID = -4053107047812521349L;
	
	@Column(name = "SAGE_PRODUCT_ID", nullable = false, unique = true)
	@Index(name = "IDX_CAT_PRODUCT_ID")
	private String productId;
	
	
	@Column(name = "TITLE")
	private String title;
	
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	
}
