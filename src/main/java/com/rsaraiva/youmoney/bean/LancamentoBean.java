package com.rsaraiva.youmoney.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.Session;

import com.rsaraiva.youmoney.model.Lancamento;
import com.rsaraiva.youmoney.util.HibernateUtil;

@ManagedBean
@ViewScoped
public class LancamentoBean implements Serializable {

	private Lancamento lancamento = new Lancamento();

	public void grava() {
		Session session = new HibernateUtil().getSession();
		session.beginTransaction();
		session.save(lancamento);
		session.getTransaction().commit();
		session.close();
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	private static final long serialVersionUID = -5614863366308078364L;

}
