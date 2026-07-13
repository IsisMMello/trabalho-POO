package trabalhoFinal;

public class Aluno extends Pessoa{
	private String matricula;
	private int periodo;
	private String turma;
	private float notas; //array[10]
	
	public Aluno() {
	}
	
	public Aluno(String nome, String dataNascimento, long cpf, String telefone, String rua, String bairro, String cidade, String estado, String matricula, int periodo, String codigoTurma) {
        super(nome, dataNascimento, cpf, telefone, rua, bairro, cidade, estado);
        this.matricula = matricula;
        this.periodo = periodo;
        this.turma = codigoTurma;
    }
	
    public Aluno(String nome, String data, long cpf, String contato,String matricula, String turma, int periodo, String bairro, String cidade, String estado) {
        super(nome, data, cpf, contato, bairro, cidade, estado);
        this.matricula = matricula;
        this.periodo = periodo;
        this.turma = turma;
    }
    
    public void setDados(String nome, String dataNascimento, long cpf, String telefone, String rua, String bairro, String cidade, String estado, String matricula, int periodo, String codigoTurma) {
        super.setDados(nome, dataNascimento, cpf, telefone, rua, bairro, cidade, estado);
        this.matricula = matricula;
        this.periodo = periodo;
        this.turma = codigoTurma;
    }
    
    public void setmatricula(String matricula){
       this.matricula = matricula;
    }
    
    public void setperiodo(int periodo){
       this.periodo=periodo;
    }
    
    public String getmatricula(){
        return this.matricula;
    }
    
    public int getperiodo(){
        return this.periodo;
    }
    
    public String getturma(){
    	return this.turma;
    }
    
    public void setturma(String turma) {
    	this.turma = turma;
    }
   
    public void imprimeDados(){
        System.out.println("nome: "+this.getNome());
        System.out.println("cpf: "+this.getCpf());
        System.out.println("Data Nascimento: "+this.getDataNascimento());
        System.out.println("matricula: "+this.matricula);
        System.out.println("periodo: "+this.periodo);
    }

	public String getTelefone() {
		return this.telefone;
	}
	
}
