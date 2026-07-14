package trabalhoFinal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TelaListagem extends JFrame {
    private JTable tabelaAlunos, tabelaEgressos, tabelaProfessores;
    private DefaultTableModel modeloAlunos, modeloEgressos, modeloProfessores;

    public TelaListagem() {
    	setBackground(new Color(241, 233, 209));
        setTitle("Listagem de Cadastros - Sistema eMentor");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Botão Voltar 
        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.setForeground(new Color(0, 0, 0));
        btnVoltar.setBackground(new Color(209, 179, 111));
        getContentPane().add(btnVoltar, BorderLayout.NORTH);
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuPrincipal().setVisible(true);
                dispose();
            }
        });

        // Configuração das colunas de cada tabela
        modeloAlunos = new DefaultTableModel(new String[]{"Matrícula", "Nome do Aluno", "Período"}, 0);
        tabelaAlunos = new JTable(modeloAlunos);

        modeloEgressos = new DefaultTableModel(new String[]{"Matrícula", "Nome do Egresso", "Profissão Atual"}, 0);
        tabelaEgressos = new JTable(modeloEgressos);

        modeloProfessores = new DefaultTableModel(new String[]{"CPF", "Nome do Professor", "Salário Bruto (R$)"}, 0);
        tabelaProfessores = new JTable(modeloProfessores);

        // Organizando as 3 tabelas em abas
        JTabbedPane abas = new JTabbedPane();
        abas.setForeground(new Color(255, 255, 255));
        abas.setBackground(new Color(61, 73, 119));
        abas.addTab("Lista de Alunos", new JScrollPane(tabelaAlunos));
        abas.addTab("Lista de Egressos", new JScrollPane(tabelaEgressos));
        abas.addTab("Lista de Professores", new JScrollPane(tabelaProfessores));

        getContentPane().add(abas, BorderLayout.CENTER);

        // Dispara as consultas ao banco assim que a tela é desenhada
        carregarAlunos();
        carregarEgressos();
        carregarProfessores();
    }

    private void carregarAlunos() {
        modeloAlunos.setRowCount(0); // Limpa linhas residuais
        try {
            ConectorBanco conector = new ConectorBanco();
            Connection conexao = conector.conectar();
            
            // O comando JOIN une a tabela Aluno com a tabela Pessoa baseando-se no CPF
            String sql = "SELECT a.Matricula, p.Nome, a.Periodo FROM Aluno a JOIN Pessoa p ON a.CPF_Pessoa = p.CPF";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Laço de repetição que cria as linhas na tabela da interface
            while (rs.next()) {
                String matricula = rs.getString("Matricula");
                String nome = rs.getString("Nome");
                String periodo = rs.getString("Periodo");
                
                modeloAlunos.addRow(new Object[]{matricula, nome, periodo});
            }
            conexao.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar a lista de alunos: " + e.getMessage());
        }
    }

    private void carregarEgressos() {
        modeloEgressos.setRowCount(0);
        try {
            ConectorBanco conector = new ConectorBanco();
            Connection conexao = conector.conectar();
            
            // JOIN triplo: Une Egresso -> Aluno -> Pessoa
            String sql = "SELECT e.Matricula_Aluno, p.Nome, e.ProfissaoAtual FROM Egresso e JOIN Aluno a ON e.Matricula_Aluno = a.Matricula JOIN Pessoa p ON a.CPF_Pessoa = p.CPF";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modeloEgressos.addRow(new Object[]{rs.getString("Matricula_Aluno"), rs.getString("Nome"), rs.getString("ProfissaoAtual")});
            }
            conexao.close();
        } catch (Exception e) {
            // Se a tabela Egressos não tiver sido criada ainda
            System.out.println("Tabela Egressos vazia ou não criada no MySQL: " + e.getMessage());
        }
    }

    private void carregarProfessores() {
        modeloProfessores.setRowCount(0);
        try {
            ConectorBanco conector = new ConectorBanco();
            Connection conexao = conector.conectar();
            
            String sql = "SELECT pr.CPF_Pessoa, p.Nome, pr.SalarioBruto FROM Professor pr JOIN Pessoa p ON pr.CPF_Pessoa = p.CPF";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modeloProfessores.addRow(new Object[]{rs.getString("CPF_Pessoa"), rs.getString("Nome"), rs.getString("SalarioBruto")});
            }
            conexao.close();
        } catch (Exception e) {
            // Se a tabela Professor não tiver sido criada ainda
            System.out.println("Tabela Professor vazia ou não criada no MySQL: " + e.getMessage());
        }
    }
}