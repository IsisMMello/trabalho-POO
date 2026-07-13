package trabalhoFinal;
import java.util.*;

public class Pessoa {
	protected String nome;
	protected String dataNascimento;
	protected long cpf;
	protected String telefone;
	protected String rua;
	protected String bairro;
	protected String cidade;
	protected String estado;
	
	public Pessoa(){
	}
	
	public Pessoa(String nome, String data, long cpf, String telefone, String bairro, String cidade, String estado) {
		this.nome = nome;
		this.dataNascimento = data;
		this.cpf = cpf;
		this.telefone = telefone;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
	}

	public Pessoa(String nome, String dataNascimento, long cpf, String telefone, String rua, String bairro, String cidade, String estado) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.telefone = telefone;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    public void setDados(String nome, String dataNascimento, long cpf, String telefone, String rua, String bairro, String cidade, String estado) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.telefone = telefone;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public long getCpf() {
		return cpf;
	}

	public void setCpf(long cpf) {
		this.cpf = cpf;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public String getTelefone() {
		return this.telefone;
	}
	
	public String getCidade() {
		return this.cidade;
	}
	
	public String getBairro() {
		return this.bairro;
	}
	
	public String getRua() {
		return this.rua;
	}
	
	public String getEstado() {
		return this.estado;
	}
}
