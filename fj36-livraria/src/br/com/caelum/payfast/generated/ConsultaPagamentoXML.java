package br.com.caelum.payfast.generated;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Com um XSD fornecido por um serviço que não é nosso, conseguimos
 * gerar as classes respectivas já anotadas com JAX-B, basta então
 * gerarmos o client para fazer a requisição.
 * 
 * @author RICARDO
 *
 */
public class ConsultaPagamentoXML {

	private static final String SERVER_URI = "http://localhost:8080/fj36-webservice";
	private static final String ENTRY_POINT = "/pagamentos/";
	
	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		Pagamento pagamento = client.target(SERVER_URI + ENTRY_POINT + "1")
				                    .request()
				                    .buildGet()
				                    .invoke(Pagamento.class);
		System.out.printf("Pagamento %d consultado via XML, possui valor %f e status %s\n",
				          pagamento.getId(),
				          pagamento.getValor(),
				          pagamento.getStatus());
	}
}