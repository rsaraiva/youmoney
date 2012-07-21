package com.rsaraiva.youmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
	@NamedQuery(name = "findAllLancamentos", query = "from Lancamento l")
})
public class Lancamento implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String descricao;

	@Temporal(TemporalType.DATE)
	private Date data;

	private BigDecimal valor;
	
	@OneToOne(cascade = CascadeType.ALL)
	private FormaDePagamento formaDePagamento;

	public Lancamento() {
		formaDePagamento = new FormaDePagamento(this);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public FormaDePagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}

	private static final long serialVersionUID = 3819139223671669086L;
}
