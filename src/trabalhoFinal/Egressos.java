package trabalhoFinal;

import javax.swing.JOptionPane;

public class Egressos extends Aluno {
    public Egressos(String nome, String data, long cpf, String contato, String matricula, String turma, int periodo,
			String bairro, String cidade, String estado, String profissao, double salario, String cursoAnteriro, String cursoAtual, String cursoAnterior) {
		super(nome, data, cpf, contato, matricula, turma, periodo, bairro, cidade, estado);
		this.profissao = profissao;
		this.salario = salario;
		this.cursoAnterior =  cursoAnterior;
		this.cursoAtual = cursoAtual;
	}

	private String profissao;
    private double salario;
    private String cursoAnterior;
    private String cursoAtual;

 

    public String getProfissaoAtual() { return profissao; }
    public void setProfissaoAtual(String profissaoAtual) { this.profissao = profissaoAtual; }
    public double getFaixaSalarial() { return salario; }
    public void setFaixaSalarial(double faixaSalarial) { this.salario = faixaSalarial; }
    public String getCursoAnterior() { return cursoAnterior; }
    public void setCursoAnterior(String cursoAnterior) { this.cursoAnterior = cursoAnterior; }
    public String getCursoAtual() { return cursoAtual; }
    public void setCursoAtual(String cursoAtual) { this.cursoAtual = cursoAtual; }

    @Override
    public void imprimeDados() {
        JOptionPane.showMessageDialog(null, "Nome: " + getNome() + "\nMatrícula: " + getmatricula() + "\nProfissão Atual: " + profissao);
    }
}