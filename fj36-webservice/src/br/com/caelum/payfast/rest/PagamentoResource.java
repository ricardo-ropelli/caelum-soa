package br.com.caelum.payfast.rest;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.caelum.anotacoes.PATCH;
import br.com.caelum.payfast.modelo.Pagamento;
import br.com.caelum.payfast.modelo.Transacao;

@Path("/pagamentos")
@Singleton
public class PagamentoResource {

	private Map<Integer, Pagamento> repositorio = new HashMap<Integer, Pagamento>();
	private Integer idPagamento = 1;
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Pagamento buscaPagamento(@PathParam("id") Integer idPagamento){
		verificaExistenciaPagamento(idPagamento);
		return repositorio.get(idPagamento);
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response criarPagamento(Transacao transacao) throws URISyntaxException{
		Pagamento pagamento = criaNovoPagamentoERetorna(transacao.getValor());
		pagamento.comStatusCriado();
		adicionaNoRepositorio(pagamento);
		System.out.println("Pagamento criado com sucesso: " + pagamento);
		return Response.created(new URI("/pagamentos/" + pagamento.getId()))
					   .entity(pagamento)
				       .type(MediaType.APPLICATION_JSON_TYPE)
				       .build();
	}
	
	@PUT
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Pagamento confirmarPagamento(@PathParam("id") Integer idPagamento){
		if (verificaExistenciaPagamento(idPagamento)){
			Pagamento pagamento = repositorio.get(idPagamento);
			pagamento.comStatusConfirmado();
			System.out.println("Pagamento confirmado com sucesso.");
			return pagamento;
		}
		return null;
	}
	
	@PATCH
	@Path("/{id}")
	public void cancelarPagamento(@PathParam("id") Integer idPagamento){
		if(verificaExistenciaPagamento(idPagamento)){
			repositorio.remove(idPagamento);
			System.out.println("Pagamento cancelado com sucesso.");
		}
	}
	
	public PagamentoResource(){
		Pagamento pagamento = criaNovoPagamentoERetorna(BigDecimal.TEN);
		adicionaNoRepositorio(pagamento);
	}
	
	private Pagamento criaNovoPagamentoERetorna(BigDecimal valor) {
		Pagamento pagamento = new Pagamento();
		pagamento.setId(idPagamento++);
		pagamento.setValor(valor);
		pagamento.comStatusCriado();
		return pagamento;
	}
	
	private void adicionaNoRepositorio(Pagamento pagamento) {
		repositorio.put(pagamento.getId(), pagamento);
	}
	
	private boolean verificaExistenciaPagamento(Integer idPagamento){
		if(!repositorio.containsKey(idPagamento)){
			System.out.println("Não existe nenhum pagamento com esse ID.");
			return false;
		}
		return true;
	}
}
