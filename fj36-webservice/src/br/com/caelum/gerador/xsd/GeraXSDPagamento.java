package br.com.caelum.gerador.xsd;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import br.com.caelum.payfast.modelo.Pagamento;

public class GeraXSDPagamento {

	public static void main(String[] args) throws Exception {
		JAXBContext ctx = JAXBContext.newInstance(Pagamento.class);
		ctx.generateSchema(new SchemaOutputResolver() {			
			@Override
			public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
				return new StreamResult(new File("pagamento.xsd"));
			}
		});
	}
}
