package com.rsaraiva.youmoney.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.hibernate.Session;

import com.rsaraiva.youmoney.model.CartaoDeCredito;
import com.rsaraiva.youmoney.model.Lancamento;
import com.rsaraiva.youmoney.model.MeioDePagamento;
import com.rsaraiva.youmoney.util.HibernateUtil;

@ManagedBean
@ViewScoped
public class LancamentoBean implements Serializable {

	private Lancamento lancamento = new Lancamento();
	private Collection<Lancamento> lista;
	
	private CartaoDeCredito cartaoDeCredito;
	
	private Collection<SelectItem> meioPagamentoItems;
	private Collection<SelectItem> cartaoCreditoItems;

	public void grava() {
		Session session = new HibernateUtil().getSession();
		session.beginTransaction();
		session.save(lancamento);
		session.getTransaction().commit();
		session.close();
		atualizaLista();
	}
	
	public void exclui() {
		Session session = new HibernateUtil().getSession();
		session.beginTransaction();
		session.delete(lancamento);
		session.getTransaction().commit();
		session.close();
		lancamento = new Lancamento();
		atualizaLista();
	}
	
	private void atualizaLista() {
		lista = new ArrayList<Lancamento>();
		Session session = new HibernateUtil().getSession();
		lista = session.getNamedQuery("findAllLancamentos").list();
		session.close();
	}

	public Collection<Lancamento> getLista() {
		if (lista == null) {
			atualizaLista();
		}
		return lista;
	}

	public Collection<SelectItem> getMeioPagamentoItems() {
		if (meioPagamentoItems == null) {
			meioPagamentoItems = new ArrayList<SelectItem>();
			for (MeioDePagamento meio_ : MeioDePagamento.values()) {
				meioPagamentoItems.add(new SelectItem(meio_, meio_.getDescricao()));
			}
		}
		return meioPagamentoItems;
	}
	
	public Collection<SelectItem> getCartaoCreditoItems() {
		if (cartaoCreditoItems == null) {
			cartaoCreditoItems = new ArrayList<SelectItem>();
			
			Session session = new HibernateUtil().getSession();
			Collection<CartaoDeCredito> cartoes = session.getNamedQuery("findAllCartaoDeCreditos").list();
			session.close();
			
			for (CartaoDeCredito cartao_ : cartoes) {
				cartaoCreditoItems.add(new SelectItem(cartao_, cartao_.getDescricao()));
			}
		}
		return cartaoCreditoItems;
	}

	public CartaoDeCredito getCartaoDeCredito() {
		return cartaoDeCredito;
	}

	public void setCartaoDeCredito(CartaoDeCredito cartaoDeCredito) {
		this.cartaoDeCredito = cartaoDeCredito;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	private static final long serialVersionUID = -5614863366308078364L;

}
