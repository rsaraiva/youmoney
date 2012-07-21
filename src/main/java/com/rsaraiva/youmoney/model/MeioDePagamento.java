package com.rsaraiva.youmoney.model;

public enum MeioDePagamento {

	DI("Dinheiro"), CH("Cheque"), CA("Cartão de Crédito");
	
	private String descricao;

	private MeioDePagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
