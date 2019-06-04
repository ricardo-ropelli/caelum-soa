package br.com.caelum.estoque.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RegistraERodaServiceTeste {

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		LocateRegistry.createRegistry(1099);
		Naming.rebind("/estoque", new EstoqueRMIImpl());
		System.out.println("Serviço registrado na porta 1099.");
	}
}
