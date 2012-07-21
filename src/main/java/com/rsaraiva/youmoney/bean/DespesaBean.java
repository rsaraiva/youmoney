package com.rsaraiva.youmoney.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.hibernate.Session;

import com.rsaraiva.youmoney.model.CartaoDeCredito;
import com.rsaraiva.youmoney.model.Despesa;
import com.rsaraiva.youmoney.model.MeioDePagamento;
import com.rsaraiva.youmoney.util.HibernateUtil;

@ManagedBean
@ViewScoped
public class DespesaBean implements Serializable {

	private Despesa despesa = new Despesa();
	private Collection<Despesa> lista;
	
	private CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
	
	private Collection<SelectItem> meioPagamentoItems;
	private Collection<SelectItem> cartaoCreditoItems;
	
	private BigDecimal total;
	private BigDecimal totalParcelas;

	public void grava() {
		despesa.setValorParcela(despesa.getValor().divide(new BigDecimal(despesa.getQuantidadeParcelas())));
		Session session = new HibernateUtil().getSession();
		session.beginTransaction();
		session.saveOrUpdate(despesa);
		session.getTransaction().commit();
		session.close();
		//lancamento = new Lancamento();
		despesa.setId(null);
		despesa.setDescricao("");
		despesa.setValor(null);
		atualizaLista();
	}
	
	public void exclui() {
		Session session = new HibernateUtil().getSession();
		session.beginTransaction();
		session.delete(despesa);
		session.getTransaction().commit();
		session.close();
		despesa = new Despesa();
		atualizaLista();
	}
	
	private void atualizaLista() {
		lista = new ArrayList<Despesa>();
		Session session = new HibernateUtil().getSession();
		lista = session.getNamedQuery("findAllLancamentos").list();
		
		// atualizar totais
		total = BigDecimal.ZERO;
		totalParcelas = BigDecimal.ZERO;
		for (Despesa l : lista) {
			total = total.add(l.getValor());
			if (l.getValorParcela() != null)
				totalParcelas = totalParcelas.add(l.getValorParcela());
		}
		
		session.close();
	}

	public Collection<Despesa> getLista() {
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

	public Despesa getLancamento() {
		return despesa;
	}

	public void setLancamento(Despesa lancamento) {
		this.despesa = lancamento;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public BigDecimal getTotalParcelas() {
		return totalParcelas;
	}

	private static final long serialVersionUID = -5614863366308078364L;

}
