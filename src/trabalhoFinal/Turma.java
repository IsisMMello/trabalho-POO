package trabalhoFinal;

import java.util.ArrayList;
import java.util.List;

public class Turma {
    private int codigoTurma;
    private String nomeTurma; // Aqui entrará o valor "2024/02", por exemplo
    private List<Aluno> alunosVinculados; // Associação direta com a classe Aluno

    public Turma(int codigoTurma, String nomeTurma) {
        this.codigoTurma = codigoTurma;
        this.nomeTurma = nomeTurma;
        this.alunosVinculados = new ArrayList<>();
    }

    // Método exigido para gerenciar a associação de alunos
    public void vincularAluno(Aluno aluno) {
        if (!alunosVinculados.contains(aluno)) {
            alunosVinculados.add(aluno);
        }
    }

    // Retorna a lista de todos os alunos daquela coorte
    public List<Aluno> getAlunosVinculados() {
        return alunosVinculados;
    }

    public int getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(int codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }
}