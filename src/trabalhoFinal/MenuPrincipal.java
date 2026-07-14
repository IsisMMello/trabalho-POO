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
            new TelaLogin().setVisible(true);
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
            String[] opcoes = {"Alunos", "Egressos", "Professores", "Turmas", "Cancelar"};
            int escolha = JOptionPane.showOptionDialog(
                this,
                "Selecione o tipo de relatório que deseja gerar:",
                "Gerar Relatório PDF",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
            );

            if (escolha >= 0 && escolha < 4) {
                String tipo = opcoes[escolha];
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Salvar Relatório - " + tipo);
                fileChooser.setSelectedFile(new java.io.File("relatorio_" + tipo.toLowerCase() + ".pdf"));
                
                int userSelection = fileChooser.showSaveDialog(this);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    java.io.File arquivoDestino = fileChooser.getSelectedFile();
                    String caminho = arquivoDestino.getAbsolutePath();
                    if (!caminho.toLowerCase().endsWith(".pdf")) {
                        caminho += ".pdf";
                    }
                    
                    final String caminhoFinal = caminho;
                    
                    // Executa a geração em uma thread separada para não travar a UI
                    new Thread(() -> {
                        try {
                            switch (escolha) {
                                case 0:
                                    GeradorRelatorioPDF.gerarRelatorioAlunos(caminhoFinal);
                                    break;
                                case 1:
                                    GeradorRelatorioPDF.gerarRelatorioEgressos(caminhoFinal);
                                    break;
                                case 2:
                                    GeradorRelatorioPDF.gerarRelatorioProfessores(caminhoFinal);
                                    break;
                                case 3:
                                    GeradorRelatorioPDF.gerarRelatorioTurmas(caminhoFinal);
                                    break;
                            }
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "Relatório PDF de " + tipo + " gerado com sucesso!\nSalvo em: " + caminhoFinal);
                            });
                        } catch (Exception ex) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            });
                            // Registra no log erros.dat conforme Requisito 7
                            try (java.io.FileWriter fw = new java.io.FileWriter("erros.dat", true);
                                 java.io.PrintWriter pw = new java.io.PrintWriter(fw)) {
                                pw.println("Código: 010 - Erro ao gerar relatório PDF (" + tipo + "): " + ex.getMessage());
                            } catch (Exception logEx) {
                                logEx.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }
}