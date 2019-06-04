package br.com.caelum.jms.topic;

import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EnviaMensagemParaOTopico {

	private final static String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private final static String NOME_TOPICO = "TOPICO.LIVRARIA";
	private final static String USUARIO = "jms";
	private final static String SENHA = "jms2";
	
	public static void main(String[] args) throws NamingException {
		InitialContext ic = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) ic.lookup(CONNECTION_FACTORY);
		Topic topico = (Topic) ic.lookup("jms/" + NOME_TOPICO);
		
		try(JMSContext context = factory.createContext(USUARIO, SENHA)){
			System.out.println("Conexão estabelecida com " + NOME_TOPICO + ", digite sua mensagem.");
			JMSProducer produtor = context.createProducer();
			Scanner scanner = new Scanner(System.in);
			while(scanner.hasNext()){
				String line = scanner.nextLine();
				
				System.out.println("Deseja enviar formato ebook ? (sim/nao)");
				String formato = scanner.nextLine();
				if(formato.equals("sim")){
					produtor.setProperty("formato", "ebook");
				} else{
					produtor.clearProperties();
				}
				
				produtor.send(topico, line);
				System.out.println("Mensagem enviada com sucesso.");
			}
			scanner.close();
		}
	}
}