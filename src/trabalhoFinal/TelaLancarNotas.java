package trabalhoFinal;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TelaLancarNotas extends JFrame {
    private JTextField txtMatricula, txtPosicao, txtNota;
    private JButton btnBuscar, btnSalvarNota, btnVoltar;

    public TelaLancarNotas() {
    	setBackground(new Color(241, 233, 209));
    	getContentPane().setBackground(new Color(61, 73, 119));
        setTitle("Lançamento de Notas no Vetor");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new GridLayout(5, 2, 10, 10));

        JLabel label = new JLabel("  Matrícula do Aluno:");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setForeground(new Color(255, 255, 255));
        getContentPane().add(label);
        txtMatricula = new JTextField();
        txtMatricula.setBackground(new Color(241, 233, 209));
        getContentPane().add(txtMatricula);
        getContentPane().add(new JLabel("")); // Espaço para manter o alinhamento do grid
        
                btnBuscar = new JButton("Buscar Aluno");
                btnBuscar.setBackground(new Color(209, 179, 111));
                btnBuscar.setForeground(new Color(0, 0, 0));
                getContentPane().add(btnBuscar);
                
                        // Lógica de Busca
                        btnBuscar.addActionListener(e -> {
                            String matricula = txtMatricula.getText().trim();
                            
                            if (matricula.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Por favor, insira a matrícula do aluno.", "Aviso", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                
                            try {
                                ConectorBanco conector = new ConectorBanco();
                                Connection conexao = conector.conectar();
                                
                                String sql = "SELECT Matricula FROM Aluno WHERE Matricula = ?";
                                PreparedStatement stmt = conexao.prepareStatement(sql);
                                stmt.setString(1, matricula);
                                
                                ResultSet rs = stmt.executeQuery();
                
                                if (rs.next()) {
                                    String matriculaEncontrada = rs.getString("Matricula");
                                    if (matriculaEncontrada.equals(matricula)) {
                                        JOptionPane.showMessageDialog(this, "Aluno Vinculado Encontrado com Sucesso!");
                                        txtPosicao.requestFocus(); 
                                    } else {
                                        JOptionPane.showMessageDialog(this, "Aluno não encontrado no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(this, "Aluno não encontrado no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                                }
                                
                                rs.close();
                                stmt.close();
                                conexao.close();
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(this, "Erro ao buscar aluno: " + ex.getMessage());
                            }
                        });

        JLabel label_1 = new JLabel("  Nota número:");
        label_1.setHorizontalAlignment(SwingConstants.RIGHT);
        label_1.setForeground(new Color(255, 255, 255));
        getContentPane().add(label_1);
        txtPosicao = new JTextField();
        txtPosicao.setBackground(new Color(241, 233, 209));
        getContentPane().add(txtPosicao);

        JLabel label_2 = new JLabel("  Nota (Ex: 8.5):");
        label_2.setHorizontalAlignment(SwingConstants.RIGHT);
        label_2.setForeground(new Color(255, 255, 255));
        getContentPane().add(label_2);
        txtNota = new JTextField();
        txtNota.setBackground(new Color(241, 233, 209));
        getContentPane().add(txtNota);

        btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.setBackground(new Color(209, 179, 111));
        btnVoltar.setForeground(new Color(0, 0, 0));
        btnSalvarNota = new JButton("Gravar Nota");
        btnSalvarNota.setBackground(new Color(209, 179, 111));
        btnSalvarNota.setForeground(new Color(0, 0, 0));
        getContentPane().add(btnVoltar);
        getContentPane().add(btnSalvarNota);

        btnVoltar.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            this.dispose();
        });

        //Gravar a Nota
        btnSalvarNota.addActionListener(e -> {
            String matricula = txtMatricula.getText().trim();
            String posicaoStr = txtPosicao.getText().trim();
            String notaStr = txtNota.getText().trim();

            if (matricula.isEmpty() || posicaoStr.isEmpty() || notaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos antes de gravar!");
                return;
            }

            try {
                int posicao = Integer.parseInt(posicaoStr);
                double nota = Double.parseDouble(notaStr.replace(",", "."));

                if (posicao < 0 || posicao > 9) {
                    JOptionPane.showMessageDialog(this, "A posição do vetor deve ser um número entre 0 e 9.");
                    return;
                }

                ConectorBanco conector = new ConectorBanco();
                Connection conexao = conector.conectar();
                
                String sql = "INSERT INTO Notas_Aluno (Matricula_Aluno, Posicao_Vetor, Valor_Nota) VALUES (?, ?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, matricula);
                stmt.setInt(2, posicao);
                stmt.setDouble(3, nota);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Nota adicionada com sucesso no vetor do aluno!");

                // Limpa apenas a nota e a posição para facilitar lançamentos contínuos
                txtPosicao.setText("");
                txtNota.setText("");
                
                stmt.close();
                conexao.close();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "A posição deve ser um número inteiro e a nota deve ser um número válido.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gravar nota: " + ex.getMessage());
            }
        });
    }
}