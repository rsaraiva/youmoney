package com.rsaraiva.youmoney.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.Session;

import com.rsaraiva.youmoney.model.CartaoDeCredito;
import com.rsaraiva.youmoney.util.HibernateUtil;

@ManagedBean
@ViewScoped
public class CartaoDeCreditoBean implements Serializable {

	private CartaoDeCredito cartao = new CartaoDeCredito();
	private Collection<CartaoDeCredito> lista;

	public void grava() {
		Session session = new HibernateUtil().getSession();
		session.beginTransaction();
		session.save(cartao);
		session.getTransaction().commit();
		session.close();
		cartao = new CartaoDeCredito();
		atualizaLista();
	}

	public void exclui() {
		Session session = new HibernateUtil().getSession();
		session.beginTransaction();
		session.delete(cartao);
		session.getTransaction().commit();
		session.close();
		cartao = new CartaoDeCredito();
		atualizaLista();
	}

	private void atualizaLista() {
		lista = new ArrayList<CartaoDeCredito>();
		Session session = new HibernateUtil().getSession();
		lista = session.getNamedQuery("findAllCartaoDeCreditos").list();
		session.close();
	}

	public CartaoDeCredito getCartao() {
		return cartao;
	}

	public void setCartao(CartaoDeCredito cartao) {
		this.cartao = cartao;
	}

	public Collection<CartaoDeCredito> getLista() {
		if (lista == null) {
			atualizaLista();
		}
		return lista;
	}

	private static final long serialVersionUID = -5614863366308078364L;

}
