package com.mycompany.api.wrapper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.web.api.wrapper.APIUnwrapper;
import org.broadleafcommerce.core.web.api.wrapper.ProductWrapper;
import org.springframework.context.ApplicationContext;

import com.mycompany.domain.BaseProduct;


@XmlRootElement(name = "product")
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class SageProductWrapper extends ProductWrapper implements APIUnwrapper<Product>{

	protected List<SageSkuWrapper> skus;
	protected String productId;
	protected String type;
	protected String title;
	protected String thumb;
	
	public Product unwrap(HttpServletRequest request, ApplicationContext context) {
		// does not work
		return null;
	}
	
	public Product populate(BaseProduct p){
		
		// p.setName(name);
		p.setTitle(title);
		p.setProductId(productId);
		p.setDescription(description);
		p.setLongDescription(longDescripion);
		p.setActiveStartDate(activeStartDate);
		p.setActiveEndDate(activeEndDate);
		p.setManufacturer(manufacturer);
		p.setModel(model);
		p.setPromoMessage(promoMessage);
		
		return p;
	}
	
	
	@XmlElement
	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	@XmlElement
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@XmlElement
	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "sku")
    @XmlElementWrapper(name = "skus")
	public List<SageSkuWrapper> getSkus() {
		return skus;
	}

	public void setSkus(List<SageSkuWrapper> skus) {
		this.skus = skus;
	}

	
	
}
