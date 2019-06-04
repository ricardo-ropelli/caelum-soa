package br.com.caelum.jms.queue;

import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EnviadorParaFila {
	
	private final static String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private final static String NOME_FILA = "FILA.GERADOR";
	private final static String USUARIO = "jms";
	private final static String SENHA = "jms2";
	
	public static void main (String[] args) throws NamingException{
		InitialContext ic = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) ic.lookup(CONNECTION_FACTORY);
		Queue fila = (Queue) ic.lookup("jms/" + NOME_FILA);
		
		try(JMSContext context = factory.createContext(USUARIO, SENHA)){
			System.out.println("Conexão estabelecida com " + NOME_FILA + ", digite sua mensagem.");
			JMSProducer produtor = context.createProducer();
			Scanner scanner = new Scanner(System.in);
			while(scanner.hasNext()){
				String line = scanner.nextLine();
				produtor.send(fila, line);
				System.out.println("Mensagem enviada com sucesso.");
			}
			scanner.close();
		}		
	}
}