package trabalhoFinal;

public class Usuario {
    private String nomeUsuario;
    private String senha;
    private int nivelAcesso;

    public Usuario() {
    }

    public void setDados(String nomeUsuario, String senha, int nivelAcesso) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.nivelAcesso = nivelAcesso;
    }

    public String getNomeUsuario() { return nomeUsuario; }
    public String getSenha() { return senha; }
    public int getNivelAcesso() { return nivelAcesso; }
}