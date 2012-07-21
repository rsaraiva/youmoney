package com.rsaraiva.youmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class FormaDePagamento implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;
	
	@OneToOne
	private Lancamento lancamento;
	
	@Enumerated(EnumType.STRING)
	private MeioDePagamento meioDePagamento;
	
//	private Boolean parcelado;
	
	private Integer quantidadeParcelas;
	
	private BigDecimal valorParcela;
	
	@ManyToOne
	private CartaoDeCredito cartaoDeCredito;

	public FormaDePagamento() {
	}

	public FormaDePagamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public MeioDePagamento getMeioDePagamento() {
		return meioDePagamento;
	}

	public void setMeioDePagamento(MeioDePagamento meioDePagamento) {
		this.meioDePagamento = meioDePagamento;
	}

	public Integer getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public CartaoDeCredito getCartaoDeCredito() {
		return cartaoDeCredito;
	}

	public void setCartaoDeCredito(CartaoDeCredito cartaoDeCredito) {
		this.cartaoDeCredito = cartaoDeCredito;
	}
	
	private static final long serialVersionUID = -2152391307153197040L;
}
