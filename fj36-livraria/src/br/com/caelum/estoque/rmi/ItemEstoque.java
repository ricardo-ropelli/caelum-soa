package br.com.caelum.estoque.rmi;

import java.io.Serializable;

public class ItemEstoque implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String codigo;
	private int quantidade;
	
	public ItemEstoque(){
		
	}
	
	public ItemEstoque(String codigo, int quantidade) {
		super();
		this.codigo = codigo;
		this.quantidade = quantidade;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
}
