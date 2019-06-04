package br.com.caelum.jms.topic;

import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.caelum.jms.queue.TratadorDeMensagem;

public class RegistraFinanceiroNoTopico {
	
	private final static String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private final static String NOME_TOPICO = "TOPICO.LIVRARIA";
	private final static String USUARIO = "jms";
	private final static String SENHA = "jms2";
	private final static String ID_CLIENTE = "Financeiro";
	private final static String ASSINATURA = "AssinaturaNotas";
	
	public static void main(String[] args) throws NamingException {
		InitialContext ic = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) ic.lookup(CONNECTION_FACTORY);
		Topic topico = (Topic) ic.lookup("jms/" + NOME_TOPICO);
		
		try(JMSContext context = factory.createContext(USUARIO, SENHA)){
			context.setClientID(ID_CLIENTE);
			JMSConsumer consumidor = context.createDurableConsumer(topico, ASSINATURA);
			consumidor.setMessageListener(new TratadorDeMensagem());
			context.start();
			System.out.println("Sistema " + ID_CLIENTE + " registrado com sucesso à " + NOME_TOPICO + ".");
			System.out.println("Aguardando novas mensagens.");
			System.out.println("Aperte enter para fechar a conexão");
			
			Scanner teclado = new Scanner(System.in);
			teclado.nextLine();
			teclado.close();
			context.stop();
		}
	}
}