package com.rsaraiva.youmoney.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.hibernate.Session;

import com.rsaraiva.youmoney.model.CartaoDeCredito;
import com.rsaraiva.youmoney.util.HibernateUtil;

@FacesConverter(value = "cartaoDeCreditoConverter")
public class CartaoDeCreditoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent ui, String id) {
		
		try {
		
			if (id == null || id.isEmpty()) {
				return null;
			}
			
			Session session = new HibernateUtil().getSession();
			CartaoDeCredito cartao = (CartaoDeCredito) session.get(CartaoDeCredito.class, Integer.valueOf(id));
			session.close();
			
			return cartao;
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConverterException("deu erro");
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent ui, Object obj) {
		
		if (obj == null || !(obj instanceof CartaoDeCredito)) {
			return "";
		}
		return ((CartaoDeCredito)obj).getId().toString();
	}
}
