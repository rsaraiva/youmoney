package com.rsaraiva.youmoney.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.hibernate.Session;

import com.rsaraiva.youmoney.model.CartaoDeCredito;
import com.rsaraiva.youmoney.model.Despesa;
import com.rsaraiva.youmoney.model.Lancamento;
import com.rsaraiva.youmoney.model.MeioDePagamento;
import com.rsaraiva.youmoney.model.TipoLancamento;
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
	private Date filtroDataInicial;
	private Date filtroDataFinal;
	
	private Locale defaultLocale = Locale.getDefault();
	
	@PostConstruct
	public void init() {
		atualizaLista();
	}
	
	public void filtrar() {
		atualizaLista();
	}		

	public void grava() {
		
		Session session = new HibernateUtil().getSession();
		session.beginTransaction();
		
		// calcula valor da parcela
		despesa.setValorParcela(despesa.getValor().divide(new BigDecimal(despesa.getQuantidadeParcelas())));
		
		// limpa lancamentos
		if (despesa.getLancamentos() != null) {
			for (Lancamento l : despesa.getLancamentos()) {
				session.delete(l);
			}
		}
		
		// calcular lancamentos
		if (despesa.getQuantidadeParcelas() > 0) {
			
			despesa.setLancamentos(new ArrayList<Lancamento>());
			
			Calendar dataCompra = GregorianCalendar.getInstance();
			dataCompra.setTime(despesa.getData());
			int mesCompra = dataCompra.get(Calendar.MONTH);
			int diaCompra = dataCompra.get(Calendar.DAY_OF_MONTH);
			
			int mesParcela = mesCompra;
			
			Calendar dataParcela = GregorianCalendar.getInstance();
			dataParcela.setTime(despesa.getData());
			
			if (diaCompra > 10) {  // se a compra foi realizada ate o dia 10, entra na fatura do proprio mes
				dataParcela.add(Calendar.MONTH, 1);
			}
			if (despesa.getCartaoDeCredito() != null) {
				dataParcela.set(Calendar.DAY_OF_MONTH, despesa.getCartaoDeCredito().getDiaVencimento());
			}
			
			for (int i = 1; i <= despesa.getQuantidadeParcelas(); i++) {
				Lancamento lancamento = new Lancamento(despesa);
				lancamento.setTipoLancamento(TipoLancamento.SAIDA);
				lancamento.setValor(despesa.getValorParcela());
				lancamento.setData(dataParcela.getTime());
				despesa.getLancamentos().add(lancamento);
				
				System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(dataParcela.getTime()));
				dataParcela.add(Calendar.MONTH, 1);
			}
		}
		
		session.saveOrUpdate(despesa);
		session.getTransaction().commit();
		session.close();

		despesa.setId(null);
		despesa.setDescricao("");
		despesa.setValor(null);
		despesa.getLancamentos().clear();
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
		session.close();
		
		if (filtroDataInicial != null && filtroDataFinal != null) {
			Iterator<Despesa> iterator = lista.iterator();
			search: 
			while(iterator.hasNext()) {
				Despesa d = iterator.next();
				for (Lancamento l : d.getLancamentos()) {
					if (l.getData().after(filtroDataInicial) && l.getData().before(filtroDataFinal)) {
						continue search;
					}
				}
				iterator.remove();
			}
		}
		
		atualizarTotais();
	}
	
	private void atualizarTotais() {
		// atualizar totais
		total = BigDecimal.ZERO;
		totalParcelas = BigDecimal.ZERO;
		for (Despesa l : lista) {
			total = total.add(l.getValor());
			if (l.getValorParcela() != null)
				totalParcelas = totalParcelas.add(l.getValorParcela());
		}
	}

	public Collection<Despesa> getLista() {
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

	public Despesa getDespesa() {
		return despesa;
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public BigDecimal getTotalParcelas() {
		return totalParcelas;
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public Date getFiltroDataInicial() {
		return filtroDataInicial;
	}

	public void setFiltroDataInicial(Date filtroDataInicial) {
		this.filtroDataInicial = filtroDataInicial;
	}

	public Date getFiltroDataFinal() {
		return filtroDataFinal;
	}

	public void setFiltroDataFinal(Date filtroDataFinal) {
		this.filtroDataFinal = filtroDataFinal;
	}

	private static final long serialVersionUID = -5614863366308078364L;

}
