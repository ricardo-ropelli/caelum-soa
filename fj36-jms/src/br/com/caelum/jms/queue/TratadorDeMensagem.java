package br.com.caelum.jms.queue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TratadorDeMensagem implements MessageListener{

	@Override
	public void onMessage(Message msg) {
		TextMessage message = (TextMessage) msg;
		try{
			System.out.println("Destino " + message.getJMSDestination() + " recebeu a mensagem: " + message.getText());
		} catch(JMSException e){
			e.printStackTrace();
		}
	}	
}
