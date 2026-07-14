package trabalhoFinal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
    	setBackground(new Color(241, 233, 209));

        setTitle("eMentor-Plus - Menu Principal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(
                new GridLayout(2, 2, 20, 20)
        );
        painelPrincipal.setBackground(new Color(241, 233, 209));

        painelPrincipal.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        setContentPane(painelPrincipal);

        Font fonteBotao = new Font(
                "Arial",
                Font.BOLD,
                15
        );

        // Carrega as imagens.
        ImageIcon iconeCadastro = carregarImagem(
                "/trabalhoFinal/imagens/alunos.png",
                90,
                90
        );

        ImageIcon iconeListagem = carregarImagem(
                "/trabalhoFinal/imagens/listagem.png",
                90,
                90
        );

        ImageIcon iconeNotas = carregarImagem(
                "/trabalhoFinal/imagens/notas.png",
                90,
                90
        );

        ImageIcon iconeRelatorio = carregarImagem(
                "/trabalhoFinal/imagens/relatorio.png",
                90,
                90
        );

        // Criação direta dos quatro botões.
        JButton btnCadastro = new JButton(
                "<html><center>Cadastrar Pessoas<br>"
                        + "(Cadastrar/Alterar)</center></html>",
                iconeCadastro
        );

        JButton btnListagem = new JButton(
                "<html><center>Visualizar Cadastros<br>"
                        + "(Tabelas)</center></html>",
                iconeListagem
        );

        JButton btnNotas = new JButton(
                "<html><center>Lançar Notas<br>"
                        + "de Alunos</center></html>",
                iconeNotas
        );

        JButton btnRelatorio = new JButton(
                "<html><center>Gerar Relatório<br>"
                        + "PDF</center></html>",
                iconeRelatorio
        );

        // Configura os quatro botões.
        JButton[] botoes = {
                btnCadastro,
                btnListagem,
                btnNotas,
                btnRelatorio
        };

        for (JButton botao : botoes) {

            botao.setFont(fonteBotao);

            // Texto embaixo da imagem.
            botao.setVerticalTextPosition(
                    SwingConstants.BOTTOM
            );

            // Texto centralizado em relação à imagem.
            botao.setHorizontalTextPosition(
                    SwingConstants.CENTER
            );

            // Centraliza imagem e texto dentro do botão.
            botao.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            botao.setVerticalAlignment(
                    SwingConstants.CENTER
            );

            // Espaço entre a imagem e o texto.
            botao.setIconTextGap(15);

            botao.setFocusPainted(false);

            botao.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );
        }

        /*
         * Adicione diretamente ao painel.
         * Não utilize apenas add(...).
         */
        painelPrincipal.add(btnCadastro);
        painelPrincipal.add(btnListagem);
        painelPrincipal.add(btnNotas);
        painelPrincipal.add(btnRelatorio);

        // Ações dos botões.
        btnCadastro.addActionListener(e -> {

            new TelaCadastro().setVisible(true);
            dispose();
            
            new TelaLogin().setVisible(true);
            this.dispose();
        });

        btnListagem.addActionListener(e -> {
            new TelaListagem().setVisible(true);
            dispose();
        });

        btnNotas.addActionListener(e -> {
            new TelaLancarNotas().setVisible(true);
            dispose();
        });

        btnRelatorio.addActionListener(e -> {
            abrirOpcoesRelatorio();
        });
    }

    /**
     * Carrega e redimensiona uma imagem do projeto.
     */
//    private ImageIcon carregarImagem(
//            String caminho,
//            int largura,
//            int altura
//    ) {
//
//        URL urlImagem =
//                MenuPrincipal.class.getResource(caminho);
//
//        if (urlImagem == null) {
//            System.err.println(
//                    "ERRO: imagem não encontrada: " + caminho
//            );
//
//            return null;
//        }
//
//        ImageIcon iconeOriginal =
//                new ImageIcon(urlImagem);
//
//        Image imagemRedimensionada =
//                iconeOriginal.getImage().getScaledInstance(
//                        largura,
//                        altura,
//                        Image.SCALE_SMOOTH
//                );
//
//        return new ImageIcon(imagemRedimensionada);
//    }
    private ImageIcon carregarImagem(
            String caminho,
            int largura,
            int altura
    ) {
        URL urlImagem = MenuPrincipal.class.getResource(caminho);

        if (urlImagem == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "A imagem não foi encontrada:\n" + caminho,
                    "Erro ao carregar imagem",
                    JOptionPane.ERROR_MESSAGE
            );

            return null;
        }

        ImageIcon iconeOriginal = new ImageIcon(urlImagem);

        Image imagemRedimensionada =
                iconeOriginal.getImage().getScaledInstance(
                        largura,
                        altura,
                        Image.SCALE_SMOOTH
                );

        return new ImageIcon(imagemRedimensionada);
    }

    private void abrirOpcoesRelatorio() {

        String[] opcoes = {
                "Alunos",
                "Egressos",
                "Professores",
                "Turmas",
                "Cancelar"
        };

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

        if (escolha < 0 || escolha >= 4) {
            return;
        }

        String tipo = opcoes[escolha];

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle(
                "Salvar Relatório - " + tipo
        );

        fileChooser.setSelectedFile(
                new java.io.File(
                        "relatorio_"
                                + tipo.toLowerCase()
                                + ".pdf"
                )
        );

        int userSelection =
                fileChooser.showSaveDialog(this);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        java.io.File arquivoDestino =
                fileChooser.getSelectedFile();

        String caminho =
                arquivoDestino.getAbsolutePath();

        if (!caminho.toLowerCase().endsWith(".pdf")) {
            caminho += ".pdf";
        }

        final String caminhoFinal = caminho;

        new Thread(() -> {

            try {

                switch (escolha) {

                    case 0:
                        GeradorRelatorioPDF
                                .gerarRelatorioAlunos(caminhoFinal);
                        break;

                    case 1:
                        GeradorRelatorioPDF
                                .gerarRelatorioEgressos(caminhoFinal);
                        break;

                    case 2:
                        GeradorRelatorioPDF
                                .gerarRelatorioProfessores(caminhoFinal);
                        break;

                    case 3:
                        GeradorRelatorioPDF
                                .gerarRelatorioTurmas(caminhoFinal);
                        break;
                }

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            this,
                            "Relatório PDF de "
                                    + tipo
                                    + " gerado com sucesso!\n"
                                    + "Salvo em: "
                                    + caminhoFinal
                    );
                });

            } catch (Exception ex) {

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            this,
                            "Erro ao gerar relatório: "
                                    + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                });

                try (
                        java.io.FileWriter fw =
                                new java.io.FileWriter(
                                        "erros.dat",
                                        true
                                );

                        java.io.PrintWriter pw =
                                new java.io.PrintWriter(fw)
                ) {

                    pw.println(
                            "Código: 010 - Erro ao gerar "
                                    + "relatório PDF ("
                                    + tipo
                                    + "): "
                                    + ex.getMessage()
                    );

                } catch (Exception logEx) {
                    logEx.printStackTrace();
                }
            }

        }).start();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}