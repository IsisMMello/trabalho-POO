package trabalhoFinal;

public class Aluno extends Pessoa{
	private String matricula;
	private int periodo;
	private String turma;
	private float notas; //array[10]
	
	public Aluno (){
        super();
        this.matricula = 0;
        this.periodo = 0;
    }

    public Aluno(String matricula, int periodo) {
        this.matricula = matricula;
        this.periodo = periodo;
    }

    public Aluno(String nome, String data, long cpf, String contato,String matricula, String turma, int periodo) {
        super(nome, data, cpf, contato);
        this.matricula = matricula;
        this.periodo = periodo;
        this.turma = turma;
    }
    
    public void setDados( String nome, String data, long cpf, String contato, String matricula, int periodo) {
       super.SetDados(nome, data, cpf, contato);
       this.matricula = matricula;
       this.periodo = periodo;
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
   
    public void imprimeDados(){
        System.out.println("nome: "+this.getNome());
        System.out.println("cpf: "+this.getCpf());
        System.out.println("Data Nascimento: "+this.getDataNascimento());
        System.out.println("matricula: "+this.matricula);
        System.out.println("periodo: "+this.periodo);
    }
	
}
