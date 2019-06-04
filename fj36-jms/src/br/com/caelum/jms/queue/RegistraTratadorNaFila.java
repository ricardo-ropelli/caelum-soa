package br.com.caelum.jms.queue;

import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RegistraTratadorNaFila {

	private final static String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private final static String NOME_FILA = "FILA.GERADOR";
	private final static String USUARIO = "jms";
	private final static String SENHA = "jms2";
	
	public static void main(String[] args) throws NamingException {
		InitialContext ic = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) ic.lookup(CONNECTION_FACTORY);
		Queue fila = (Queue) ic.lookup("jms/" + NOME_FILA);
		
		try(JMSContext context = factory.createContext(USUARIO, SENHA)){
			JMSConsumer consumidor = context.createConsumer(fila);
			consumidor.setMessageListener(new TratadorDeMensagem());
			context.start();
			System.out.println("Sistema registrado com sucesso à " + NOME_FILA + ".");
			System.out.println("Aguardando novas mensagens.");
			System.out.println("Aperte enter para fechar a conexão");
			
			Scanner teclado = new Scanner(System.in);
			teclado.nextLine();			
			teclado.close();
			context.stop();
		}
	}
}