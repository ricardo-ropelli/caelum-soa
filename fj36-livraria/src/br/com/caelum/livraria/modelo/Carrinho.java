package br.com.caelum.livraria.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import br.com.caelum.correios.soap.ConsumidorServicoCorreios;
import br.com.caelum.estoque.rmi.EstoqueRMI;
import br.com.caelum.estoque.rmi.ItemEstoque;
import br.com.caelum.livraria.jms.EnviadorMensagemJms;
import br.com.caelum.livraria.rest.ClienteRest;

@Component
@Scope("session")
public class Carrinho implements Serializable {

	private static final long serialVersionUID = 1L;

	private Set<ItemCompra> itensDeCompra = new LinkedHashSet<>();
	private BigDecimal valorFrete = BigDecimal.ZERO;
	private String cepDestino;
	private String numeroCartao;
	private String titularCartao;
	private Pagamento pagamento;

	@Autowired
	ClienteRest clienteRest;

	@Autowired
	EnviadorMensagemJms enviador;


	public void adicionarOuIncremantarQuantidadeDoItem(Livro livro, Formato formato) {

		ItemCompra item = new ItemCompra(livro, formato);

		if (jaExisteItem(item)) {
			ItemCompra itemCarrinho = this.procurarItem(item);
			itemCarrinho.incrementaQuantidade(item.getQuantidade());
		} else {
			this.itensDeCompra.add(item);
		}

		cancelarPagamento();
	}

	public void removerItemPeloCodigoEFormato(String codigo, Formato formato) {

		ItemCompra itemARemover = procurarItemPelaId(codigo, formato);

		if (itemARemover != null) {
			this.itensDeCompra.remove(itemARemover);
		}

		if (!this.isComLivrosImpressos()) {
			this.valorFrete = BigDecimal.ZERO;
		}

		cancelarPagamento();
	}

	public Pagamento criarPagamento(String numeroCartao, String nomeTitular) {
		Transacao transacao = new Transacao();
		transacao.setNumero(numeroCartao);
		transacao.setTitular(nomeTitular);
		transacao.setValor(this.getTotal());

		this.pagamento = this.clienteRest.criarPagamento(transacao);
		
		return this.pagamento;
	}

	private void cancelarPagamento() {
		this.pagamento = null;
		//poderia ter chamada do WS para cancelar o pagamento
	}

	public Pedido finalizarPedido() {

		Pedido pedido = new Pedido();
		pedido.setData(Calendar.getInstance());
		pedido.setItens(new LinkedHashSet<>(this.itensDeCompra));

		this.pagamento = this.clienteRest.confirmarPagamento(pagamento);
		pedido.setPagamento(pagamento);
		this.enviador.enviar(pedido);

		this.limparCarrinho();

		return pedido;
	}

	public void atualizarFrete(final String novoCepDestino) {
		ConsumidorServicoCorreios servicoCorreios = new ConsumidorServicoCorreios();
		this.valorFrete = servicoCorreios.calculaFrete(novoCepDestino);
	}

	public String getCepDestino() {
		return cepDestino;
	}

	public List<ItemCompra> getItensCompra() {
		return new ArrayList<ItemCompra>(this.itensDeCompra);
	}

	public BigDecimal getTotal() {

		BigDecimal total = BigDecimal.ZERO;

		for (ItemCompra item : this.itensDeCompra) {
			total = total.add(item.getTotal());
		}

		return total.add(valorFrete);
	}

	public Pagamento getPagamento() {
		return this.pagamento;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public boolean isFreteCalculado() {
		return !this.valorFrete.equals(BigDecimal.ZERO) || !this.isComLivrosImpressos();
	}

	public boolean isPagamentoCriado() {
		if (this.pagamento == null) {
			return false;
		}
		return this.pagamento.ehCriado();
	}

	public boolean isProntoParaSerFinalizado() {
		return this.isFreteCalculado() && this.isPagamentoCriado();
	}

	public boolean isComLivrosImpressos() {

		for (ItemCompra itemCompra : this.itensDeCompra) {
			if (itemCompra.isImpresso()) {
				return true;
			}
		}
		return false;
	}

	private void atualizarQuantidadeDisponivelDoItemCompra(final ItemEstoque resposta) {
		ItemCompra item = Iterables.find(this.itensDeCompra, new Predicate<ItemCompra>() {

			public boolean apply(ItemCompra item) {
				return item.temCodigo(resposta.getCodigo());
			}
		});

		item.setQuantidadeNoEstoque(resposta.getQuantidade());
	}

	private void limparCarrinho() {
		this.itensDeCompra = new LinkedHashSet<>();
		this.valorFrete = BigDecimal.ZERO;
	}

	private boolean jaExisteItem(final ItemCompra item) {
		return this.itensDeCompra.contains(item);
	}

	private ItemCompra procurarItem(final ItemCompra itemProcurado) {
		for (ItemCompra item : this.itensDeCompra) {
			if (item.equals(itemProcurado)) {
				return item;
			}
		}
		throw new IllegalStateException("Item não encontrado");
	}

	private ItemCompra procurarItemPelaId(final String codigo, Formato formato) {

		for (ItemCompra item : this.itensDeCompra) {
			if (item.getCodigo().equals(codigo) && item.getFormato().equals(formato)) {
				return item;
			}
		}

		return null;
	}

	@SuppressWarnings("unused")
	private List<String> getCodigosDosItensImpressos() {
		List<String> codigos = new ArrayList<>();

		for (ItemCompra item : this.itensDeCompra) {
			if (item.isImpresso())
				codigos.add(item.getCodigo());
		}
		return codigos;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public String getTitularCartao() {
		return titularCartao;
	}

	public void setTitularCartao(String titularCartao) {
		this.titularCartao = titularCartao;
	}

	public boolean temCartao() {
		return numeroCartao != null && titularCartao != null;
	}

	public void verificarDisponibilidadeDosItensComRmi() throws MalformedURLException, RemoteException, NotBoundException {
		EstoqueRMI estoque = (EstoqueRMI) Naming.lookup("rmi://localhost:1099/estoque");
		for (ItemCompra itemCompra : itensDeCompra) {
			if (itemCompra.isImpresso()){
				ItemEstoque item = estoque.getItemEstoque(itemCompra.getCodigo());
				System.out.println("Livro de codigo " + item.getCodigo() + " possui estoque de " + item.getQuantidade());
				itemCompra.setQuantidadeNoEstoque(item.getQuantidade());
			}
		}
	}
}
