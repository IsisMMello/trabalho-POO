package trabalhoFinal;

import javax.swing.JOptionPane;

public class Professor extends Pessoa {
    private String dataAdmissao;
    private boolean cargoChefia;
    private boolean cargoCoordenacao;
    private double salarioBruto;


    public Professor(String nome, String dataNascimento, long cpf, String telefone, String rua, String bairro, String cidade, String estado, String dataAdmissao, boolean cargoChefia, boolean cargoCoordenacao, double salarioBruto) {
        super(nome, dataNascimento, cpf, telefone, rua, bairro, cidade, estado);
        this.dataAdmissao = dataAdmissao;
        this.cargoChefia = cargoChefia;
        this.cargoCoordenacao = cargoCoordenacao;
        this.salarioBruto = salarioBruto;
    }

    public void setDados(String nome, String dataNascimento, long cpf, String telefone, String rua, String bairro, String cidade, String estado, String dataAdmissao, boolean cargoChefia, boolean cargoCoordenacao, double salarioBruto) {
        super.setDados(nome, dataNascimento, cpf, telefone, rua, bairro, cidade, estado);
        this.dataAdmissao = dataAdmissao;
        this.cargoChefia = cargoChefia;
        this.cargoCoordenacao = cargoCoordenacao;
        this.salarioBruto = salarioBruto;
    }

    public String getDataAdmissao() { return dataAdmissao; }
    public double getSalarioBruto() { return salarioBruto; }
    public void setCargoChefia(boolean cargoChefia) { this.cargoChefia = cargoChefia; }
    public void setCargoCoordenacao(boolean cargoCoordenacao) { this.cargoCoordenacao = cargoCoordenacao; }
    public void setSalarioBruto(double salarioBruto) { this.salarioBruto = salarioBruto; }

    public double calcularSalarioBrutoTotal() {
        double salario = salarioBruto;
        if (cargoChefia) salario += 1500.0;
        if (cargoCoordenacao) salario += 2000.0;
        return salario;
    }

    public double calcularSalarioLiquido() {
        double bruto = calcularSalarioBrutoTotal();
        double inss = bruto * 0.14;
        double irpf = (bruto >= 5000.0) ? bruto * 0.225 : 0.0;
        return bruto - inss - irpf;
    }

    public void imprimeDados() {
        JOptionPane.showMessageDialog(null, "Nome: " + getNome() + "\nSalário Líquido: R$ " + calcularSalarioLiquido());
    }
}