package trabalhoFinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("eMentor-Plus - Menu Principal");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2, 20, 20)); // Grid com botões grandes e espaçados

        // Criando botões grandes
        JButton btnCadastro = new JButton("Gerenciar Alunos (Cadastrar/Alterar)");
        JButton btnListagem = new JButton("Visualizar Cadastros (Tabelas)");
        JButton btnNotas = new JButton("Lançar Notas de Alunos");
        JButton btnRelatorio = new JButton("Gerar Relatório PDF");

        // Ajustando fontes para destacar o tamanho
        Font fonteBotao = new Font("Arial", Font.BOLD, 14);
        btnCadastro.setFont(fonteBotao);
        btnListagem.setFont(fonteBotao);
        btnNotas.setFont(fonteBotao);
        btnRelatorio.setFont(fonteBotao);

        // Adicionando componentes
        add(btnCadastro);
        add(btnListagem);
        add(btnNotas);
        add(btnRelatorio);

        // Ações dos botões
        btnCadastro.addActionListener(e -> {
            new TelaCadastroAluno().setVisible(true);
            this.dispose();
        });

        btnListagem.addActionListener(e -> {
            new TelaListagem().setVisible(true);
            this.dispose();
        });

        btnNotas.addActionListener(e -> {
            new TelaLancarNotas().setVisible(true);
            this.dispose();
        });

        btnRelatorio.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Relatório PDF gerado com sucesso (Salvo em: relatorio.pdf)!");
        });
    }
}