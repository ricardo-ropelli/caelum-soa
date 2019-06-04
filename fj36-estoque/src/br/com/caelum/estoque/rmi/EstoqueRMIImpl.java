package br.com.caelum.estoque.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class EstoqueRMIImpl extends UnicastRemoteObject implements EstoqueRMI {

	private static final long serialVersionUID = 1L;

	private Map<String, ItemEstoque> repositorio = new HashMap<String, ItemEstoque>();
	
	public EstoqueRMIImpl() throws RemoteException {
		super();
		adicionaLivroNoEstoque("ARQ", 5);
		adicionaLivroNoEstoque("SOA", 2);
		adicionaLivroNoEstoque("TDD", 3);
		adicionaLivroNoEstoque("RES", 4);
		adicionaLivroNoEstoque("LOG", 3);
		adicionaLivroNoEstoque("WEB", 4);
	}

	@Override
	public ItemEstoque getItemEstoque(String codigo) throws RemoteException {
		return repositorio.get(codigo);
	}

	private void adicionaLivroNoEstoque(String codigo, int quantidade){
		repositorio.put(codigo, new ItemEstoque(codigo, quantidade));
	}
}
