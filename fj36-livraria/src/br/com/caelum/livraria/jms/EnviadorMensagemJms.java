package br.com.caelum.livraria.jms;

import java.io.Serializable;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import br.com.caelum.livraria.modelo.Pedido;

@Component
@Lazy(true)
public class EnviadorMensagemJms implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ConnectionFactory factory;
	
	@Autowired
	private Topic topico;
	
	public void enviar(Pedido pedido) {
		System.out.println("Enviando pedido: " + pedido);
		try(JMSContext context = factory.createContext("jms", "jms2")){
			JMSProducer produtor = context.createProducer();
			produtor.setProperty("formato", pedido.getFormato());
			produtor.send(topico, pedido.toString());
		}
	}
}
