package br.com.caelum.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig={
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/TOPICO.LIVRARIA"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class FinanceiroMessageDrivenBean implements MessageListener {

	@Override
	public void onMessage(Message message) {		
		try {
			TextMessage textMessage = (TextMessage) message;
			System.out.println("Financeiro message driven bean recebeu a mensagem: " + textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
