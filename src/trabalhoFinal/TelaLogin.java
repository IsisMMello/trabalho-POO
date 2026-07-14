package trabalhoFinal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelaLogin extends JFrame {

    private static final Color AZUL_PRINCIPAL =
            new Color(31, 49, 98);

    private static final Color AZUL_ESCURO =
            new Color(23, 38, 78);

    private static final Color FUNDO =
            new Color(241, 244, 249);

    private static final Color DOURADO =
            new Color(209, 179, 111);

    private static final Color TEXTO =
            new Color(45, 50, 60);

    private static final Color TEXTO_SECUNDARIO =
            new Color(105, 110, 120);

    private static final Color BORDA =
            new Color(205, 210, 220);

    private static final String URL_BANCO =
            "jdbc:mysql://localhost:3306/ementor";

    private static final String USUARIO_BANCO =
            "ementor_app";

    private static final String SENHA_BANCO =
            "1234";

    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;

    public TelaLogin() {
        configurarJanela();
        criarInterface();
        configurarEventos();
    }

    private void configurarJanela() {
        setTitle("eMentor-Plus - Acesso ao Sistema");
        setSize(460, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void criarInterface() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(FUNDO);

        painelPrincipal.add(
                criarCabecalho(),
                BorderLayout.NORTH
        );

        painelPrincipal.add(
                criarAreaCentral(),
                BorderLayout.CENTER
        );

        setContentPane(painelPrincipal);
    }

    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel();

        cabecalho.setLayout(
                new BoxLayout(
                        cabecalho,
                        BoxLayout.Y_AXIS
                )
        );

        cabecalho.setBackground(AZUL_PRINCIPAL);

        cabecalho.setBorder(
                new EmptyBorder(35, 20, 35, 20)
        );

        JLabel titulo = new JLabel("eMentor-Plus");

        titulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Sistema de gerenciamento acadêmico"
        );

        subtitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        subtitulo.setForeground(
                new Color(220, 226, 240)
        );

        subtitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        cabecalho.add(titulo);
        cabecalho.add(Box.createVerticalStrut(6));
        cabecalho.add(subtitulo);

        return cabecalho;
    }

    private JPanel criarAreaCentral() {
        JPanel painelExterno = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        0,
                        25
                )
        );

        painelExterno.setBackground(FUNDO);

        JPanel formulario = new JPanel();

        formulario.setLayout(
                new BoxLayout(
                        formulario,
                        BoxLayout.Y_AXIS
                )
        );

        formulario.setBackground(Color.WHITE);

        formulario.setPreferredSize(
                new Dimension(370, 365)
        );

        formulario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDA),
                        new EmptyBorder(25, 28, 28, 28)
                )
        );

        JLabel tituloFormulario =
                new JLabel("Acesse sua conta");

        tituloFormulario.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        21
                )
        );

        tituloFormulario.setForeground(TEXTO);

        tituloFormulario.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel descricao = new JLabel(
                "Informe seu usuário e sua senha para continuar"
        );

        descricao.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        descricao.setForeground(TEXTO_SECUNDARIO);

        descricao.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

         //Componentes do usuário.

        JLabel labelUsuario = new JLabel("Usuário");

        configurarLabel(labelUsuario);

        campoUsuario = new JTextField();

        configurarCampo(campoUsuario);

         //Componentes da senha.

        JLabel labelSenha = new JLabel("Senha");

        configurarLabel(labelSenha);

        campoSenha = new JPasswordField();

        configurarCampo(campoSenha);

        botaoEntrar = criarBotaoEntrar();

         //Ordem dos componentes no formulário.

        formulario.add(tituloFormulario);
        formulario.add(Box.createVerticalStrut(5));

        formulario.add(descricao);
        formulario.add(Box.createVerticalStrut(22));

        formulario.add(labelUsuario);
        formulario.add(Box.createVerticalStrut(7));
        formulario.add(campoUsuario);

        formulario.add(Box.createVerticalStrut(17));

        formulario.add(labelSenha);
        formulario.add(Box.createVerticalStrut(7));
        formulario.add(campoSenha);

        formulario.add(Box.createVerticalStrut(25));

        formulario.add(botaoEntrar);

        painelExterno.add(formulario);

        return painelExterno;
    }

    private void configurarLabel(JLabel label) {
        Dimension tamanhoLabel =
                new Dimension(310, 18);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(TEXTO);

        label.setPreferredSize(tamanhoLabel);
        label.setMinimumSize(tamanhoLabel);
        label.setMaximumSize(tamanhoLabel);

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );
    }

    private void configurarCampo(JTextField campo) {
        Dimension tamanhoCampo =
                new Dimension(310, 44);

        campo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );

        campo.setForeground(TEXTO);
        campo.setBackground(Color.WHITE);
        campo.setCaretColor(AZUL_PRINCIPAL);

        campo.setPreferredSize(new Dimension(310, 50));
        campo.setMinimumSize(new Dimension(310, 50));
        campo.setMaximumSize(new Dimension(310, 50));

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDA),
                        new EmptyBorder(9, 11, 9, 11)
                )
        );

        campo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );
    }

    private JButton criarBotaoEntrar() {
        JButton botao = new JButton("Entrar");

        Dimension tamanhoBotao =
                new Dimension(310, 46);

        botao.setPreferredSize(tamanhoBotao);
        botao.setMinimumSize(tamanhoBotao);
        botao.setMaximumSize(tamanhoBotao);

        botao.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        botao.setForeground(new Color(0, 0, 0));
        botao.setBackground(DOURADO);

        botao.setFocusPainted(false);
        botao.setBorderPainted(false);

        botao.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        botao.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return botao;
    }

    private void configurarEventos() {
        botaoEntrar.addActionListener(
                evento -> validarLogin()
        );

        campoSenha.addActionListener(
                evento -> validarLogin()
        );

        campoUsuario.addActionListener(
                evento -> campoSenha.requestFocusInWindow()
        );
    }

    private void validarLogin() {
        String usuarioDigitado =
                campoUsuario.getText().trim();

        String senhaDigitada =
                new String(campoSenha.getPassword());

        if (usuarioDigitado.isEmpty()
                || senhaDigitada.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha o usuário e a senha.",
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String sql = """
                SELECT 1
                FROM Usuario
                WHERE NomeUsuario = ?
                  AND Senha = ?
                """;

        try (
                Connection conexao =
                        DriverManager.getConnection(
                                URL_BANCO,
                                USUARIO_BANCO,
                                SENHA_BANCO
                        );

                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {
            comando.setString(
                    1,
                    usuarioDigitado
            );

            comando.setString(
                    2,
                    senhaDigitada
            );

            try (
                    ResultSet resultado =
                            comando.executeQuery()
            ) {
                if (resultado.next()) {
                    abrirMenuPrincipal(usuarioDigitado);
                } else {
                    informarLoginIncorreto();
                }
            }

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao conectar ao banco de dados:\n"
                            + erro.getMessage(),
                    "Erro de conexão",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void abrirMenuPrincipal(String usuario) {
        JOptionPane.showMessageDialog(
                this,
                "Acesso concedido!\nBem-vindo, "
                        + usuario + ".",
                "Login realizado",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();

        new MenuPrincipal().setVisible(true);
    }

    private void informarLoginIncorreto() {
        JOptionPane.showMessageDialog(
                this,
                "Usuário ou senha incorretos.",
                "Erro de acesso",
                JOptionPane.ERROR_MESSAGE
        );

        campoSenha.setText("");
        campoSenha.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaLogin tela = new TelaLogin();
            tela.setVisible(true);
        });
    }
}