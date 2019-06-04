package br.com.caelum.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig={
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/TOPICO.LIVRARIA"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "formato='ebook'")
})
public class GeradorEbookMessageDrivenBean implements MessageListener {

	@Override
	public void onMessage(Message message) {		
		try {
			TextMessage textMessage = (TextMessage) message;
			System.out.println("Ebook message driven bean recebeu a mensagem: " + textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
