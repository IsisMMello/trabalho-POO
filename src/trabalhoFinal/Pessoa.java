package trabalhoFinal;
import java.util.*;

public class Pessoa {
	private String nome;
	private String dataNascimento;
	private String cpf;
	private String telefone;
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	
	public Pessoa(String nome, String data, long cpf, String contato) {
		// TODO Auto-generated constructor stub
	}

	public void SetDados(String nome2, String data, long cpf2, String contato) {
		// TODO Auto-generated method stub
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
}
