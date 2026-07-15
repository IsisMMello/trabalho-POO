package trabalhoFinal;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConectorBanco {
    private Connection conexao;

    public Connection conectar() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/ementor";
        String usuario = "ementor_app";
        String senha = "1234"; 
        
        return DriverManager.getConnection(url, usuario, senha);
    }

    public void desconectar() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            gerarLogErro("002", "Falha ao desconectar: " + e.getMessage());
        }
    }

    public void gravarAluno(Aluno aluno) {
        String sqlPessoa = "INSERT INTO Pessoa (CPF, Nome, DataNascimento, Telefone, Rua, Bairro, Cidade, Estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlAluno = "INSERT INTO Aluno (Matricula, CPF_Pessoa, Periodo, CodigoTurma) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmtPessoa = conexao.prepareStatement(sqlPessoa);
             PreparedStatement stmtAluno = conexao.prepareStatement(sqlAluno)) {
            
            stmtPessoa.setString(1, String.valueOf(aluno.getCpf()));
            stmtPessoa.setString(2, aluno.getNome());
            stmtPessoa.setString(3, aluno.getDataNascimento());
            stmtPessoa.setString(4, aluno.getTelefone());
            stmtPessoa.setString(5, aluno.getRua());
            stmtPessoa.setString(6, aluno.getBairro());
            stmtPessoa.setString(7, aluno.getCidade());
            stmtPessoa.setString(8, aluno.getEstado());
            stmtPessoa.executeUpdate();

            stmtAluno.setString(1, aluno.getmatricula());
            stmtAluno.setString(2, String.valueOf(aluno.getCpf()));
            stmtAluno.setInt(3, aluno.getperiodo());
            stmtAluno.setString(4, aluno.getturma());
            stmtAluno.executeUpdate();

        } catch (SQLException e) {
            gerarLogErro("003", "Falha ao gravar aluno: " + e.getMessage());
        }
    }

    public void alterarAluno(Aluno aluno) {
        String sql = "UPDATE Pessoa SET Nome = ?, Telefone = ? WHERE CPF = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getTelefone());
            stmt.setString(3, String.valueOf(aluno.getCpf()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            gerarLogErro("004", "Falha ao alterar aluno: " + e.getMessage());
        }
    }

    public List<Aluno> recuperarAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT a.Matricula, a.Periodo, p.Nome, p.CPF FROM Aluno a JOIN Pessoa p ON a.CPF_Pessoa = p.CPF";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setmatricula(rs.getString("Matricula"));
                aluno.setperiodo(rs.getInt("Periodo"));
                aluno.setNome(rs.getString("Nome"));
                aluno.setCpf(Long.parseLong(rs.getString("CPF")));
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            gerarLogErro("005", "Falha ao recuperar alunos: " + e.getMessage());
        }
        return alunos;
    }

    private void gerarLogErro(String codigo, String descricao) {
        try (FileWriter fw = new FileWriter("erros.dat", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println("Código: " + codigo + " - Descrição: " + descricao);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}