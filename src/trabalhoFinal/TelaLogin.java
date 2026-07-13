package trabalhoFinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TelaLogin extends JFrame {

    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;

    public TelaLogin() {
        // Configurações básicas da janela
        setTitle("eMentor-Plus - Acesso ao Sistema");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setLayout(new GridLayout(3, 2, 10, 10)); // Grid com 3 linhas, 2 colunas e espaçamento

        // Instanciando os componentes da tela
        JLabel labelUsuario = new JLabel("  Usuário:");
        campoUsuario = new JTextField();
        
        JLabel labelSenha = new JLabel("  Senha:");
        campoSenha = new JPasswordField(); // Oculta os caracteres digitados
        
        botaoEntrar = new JButton("Entrar");

        // Adicionando os componentes na janela
        add(labelUsuario);
        add(campoUsuario);
        add(labelSenha);
        add(campoSenha);
        add(new JLabel("")); // Espaço vazio para alinhar o botão na direita
        add(botaoEntrar);

        // Ação do botão Entrar
        botaoEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });
    }

    private void validarLogin() {
        String usuarioDigitado = campoUsuario.getText();
        String senhaDigitada = new String(campoSenha.getPassword());

        // Lógica de verificação no Banco de Dados
        try {
            // ATENÇÃO: Ajuste a senha abaixo para a mesma do seu db.properties ou banco local
            Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/ementor", "ementor_app", "1234");
            
            String sql = "SELECT * FROM Usuario WHERE NomeUsuario = ? AND Senha = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuarioDigitado);
            stmt.setString(2, senhaDigitada);
            
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Se achou o usuário no banco, login foi bem sucedido
                JOptionPane.showMessageDialog(this, "Acesso concedido! Bem-vindo, " + usuarioDigitado);
                
                // Fecha a tela de login
                this.dispose(); 
                
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
                
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos!", "Erro de Acesso", JOptionPane.ERROR_MESSAGE);
            }
            
            conexao.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar no banco: " + ex.getMessage());
        }
    }
}