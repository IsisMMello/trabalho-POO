package trabalhoFinal;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TelaCadastroAluno extends JFrame {
    private JProgressBar barraProgresso;

    public TelaCadastroAluno() {
        setTitle("Central de Cadastros e Edição - eMentor");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JButton btnVoltar = new JButton("Voltar ao Menu");
        add(btnVoltar, BorderLayout.NORTH);
        btnVoltar.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        barraProgresso = new JProgressBar(0, 100);
        barraProgresso.setStringPainted(true);
        add(barraProgresso, BorderLayout.SOUTH);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("1. Alunos", criarAbaAluno());
        abas.addTab("2. Egressos", criarAbaEgresso());
        abas.addTab("3. Professores", criarAbaProfessor());
        abas.addTab("4. Turmas", criarAbaTurma());

        add(abas, BorderLayout.CENTER);
    }

    // =================================================================
    // ABA 1: ALUNOS (Busca estrita pela PK: Matrícula)
    // =================================================================
    private JPanel criarAbaAluno() {
        JPanel painelBase = new JPanel(new BorderLayout());
        JPanel painelCampos = new JPanel(new GridLayout(11, 2, 5, 5));

        JTextField txtMatricula = new JTextField(); JTextField txtCpf = new JTextField(); 
        JTextField txtNome = new JTextField(); JTextField txtData = new JTextField(); 
        JTextField txtTelefone = new JTextField(); JTextField txtPeriodo = new JTextField();
        JTextField txtTurma = new JTextField(); JTextField txtRua = new JTextField();
        JTextField txtBairro = new JTextField(); JTextField txtCidade = new JTextField();
        JTextField txtEstado = new JTextField();

        // Destacando a Primary Key para o usuário
        painelCampos.add(new JLabel("  Matrícula (Primary Key / Busca):")); painelCampos.add(txtMatricula);
        painelCampos.add(new JLabel("  CPF (Herdado de Pessoa):")); painelCampos.add(txtCpf);
        painelCampos.add(new JLabel("  Nome:")); painelCampos.add(txtNome);
        painelCampos.add(new JLabel("  Nascimento (YYYY-MM-DD):")); painelCampos.add(txtData);
        painelCampos.add(new JLabel("  Telefone:")); painelCampos.add(txtTelefone);
        painelCampos.add(new JLabel("  Período:")); painelCampos.add(txtPeriodo);
        painelCampos.add(new JLabel("  Cód. Turma Vinculada:")); painelCampos.add(txtTurma);
        painelCampos.add(new JLabel("  Rua:")); painelCampos.add(txtRua);
        painelCampos.add(new JLabel("  Bairro:")); painelCampos.add(txtBairro);
        painelCampos.add(new JLabel("  Cidade:")); painelCampos.add(txtCidade);
        painelCampos.add(new JLabel("  Estado (UF):")); painelCampos.add(txtEstado);

        JPanel painelBotoes = new JPanel();
        JButton btnBuscar = new JButton("Buscar p/ Alterar");
        JButton btnSalvar = new JButton("Salvar / Alterar");
        painelBotoes.add(btnBuscar); painelBotoes.add(btnSalvar);

        painelBase.add(painelCampos, BorderLayout.CENTER);
        painelBase.add(painelBotoes, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            String pkBusca = txtMatricula.getText().trim();
            if(pkBusca.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a Matrícula (Primary Key) para realizar a busca.");
                return;
            }
            try {
                Connection con = new ConectorBanco().conectar();
                // Busca a partir da PK de Aluno e junta com Pessoa
                String sql = "SELECT p.*, a.Matricula, a.Periodo, a.CodigoTurma FROM Aluno a JOIN Pessoa p ON a.CPF_Pessoa = p.CPF WHERE a.Matricula = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, pkBusca);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    txtCpf.setText(rs.getString("CPF")); txtNome.setText(rs.getString("Nome")); 
                    txtData.setText(rs.getString("DataNascimento")); txtTelefone.setText(rs.getString("Telefone")); 
                    txtRua.setText(rs.getString("Rua")); txtBairro.setText(rs.getString("Bairro")); 
                    txtCidade.setText(rs.getString("Cidade")); txtEstado.setText(rs.getString("Estado")); 
                    txtPeriodo.setText(rs.getString("Periodo")); txtTurma.setText(rs.getString("CodigoTurma"));
                    JOptionPane.showMessageDialog(this, "Aluno encontrado. Modifique os dados e clique em Salvar/Alterar.");
                } else {
                    JOptionPane.showMessageDialog(this, "Aluno não encontrado com esta Matrícula.");
                }
                con.close();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
        });

        btnSalvar.addActionListener(e -> {
            if (txtEstado.getText().length() > 2) {
                JOptionPane.showMessageDialog(this, "Erro: O Estado deve conter apenas 2 letras."); return;
            }
            processarDados("Aluno", txtCpf.getText(), txtNome.getText(), txtData.getText(), txtTelefone.getText(), txtRua.getText(), txtBairro.getText(), txtCidade.getText(), txtEstado.getText(), txtMatricula.getText(), txtPeriodo.getText(), txtTurma.getText(), null, null);
        });
        return painelBase;
    }

    // =================================================================
    // ABA 2: EGRESSOS (Busca estrita pela PK: Matrícula_Aluno)
    // =================================================================
    private JPanel criarAbaEgresso() {
        JPanel painelBase = new JPanel(new BorderLayout());
        JPanel painelCampos = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField txtCpf = new JTextField(); 
        JTextField txtMatricula = new JTextField(); 
        JTextField txtProfissao = new JTextField();

        painelCampos.add(new JLabel("  Matrícula (Primary Key / Busca):")); painelCampos.add(txtMatricula);
        painelCampos.add(new JLabel("  CPF (Apenas para novo cadastro):")); painelCampos.add(txtCpf);
        painelCampos.add(new JLabel("  Profissão Atual:")); painelCampos.add(txtProfissao);
        painelCampos.add(new JLabel("  (O Egresso já deve existir como Aluno)"));

        JPanel painelBotoes = new JPanel();
        JButton btnBuscar = new JButton("Buscar p/ Alterar");
        JButton btnSalvar = new JButton("Salvar / Alterar");
        painelBotoes.add(btnBuscar); painelBotoes.add(btnSalvar);

        painelBase.add(painelCampos, BorderLayout.NORTH);
        painelBase.add(painelBotoes, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            String pkBusca = txtMatricula.getText().trim();
            if(pkBusca.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a Matrícula (Primary Key) para realizar a busca.");
                return;
            }
            try {
                Connection con = new ConectorBanco().conectar();
                String sql = "SELECT ProfissaoAtual FROM Egresso WHERE Matricula_Aluno = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, pkBusca);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    txtProfissao.setText(rs.getString("ProfissaoAtual"));
                    JOptionPane.showMessageDialog(this, "Egresso encontrado.");
                } else {
                    JOptionPane.showMessageDialog(this, "Egresso não encontrado com esta Matrícula.");
                }
                con.close();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
        });

        btnSalvar.addActionListener(e -> {
            processarDados("Egresso", txtCpf.getText(), null, null, null, null, null, null, null, txtMatricula.getText(), null, null, null, txtProfissao.getText());
        });
        return painelBase;
    }

    // =================================================================
    // ABA 3: PROFESSORES (Busca estrita pela PK: CPF_Pessoa)
    // =================================================================
    private JPanel criarAbaProfessor() {
        JPanel painelBase = new JPanel(new BorderLayout());
        JPanel painelCampos = new JPanel(new GridLayout(9, 2, 5, 5));

        JTextField txtCpf = new JTextField(); JTextField txtNome = new JTextField();
        JTextField txtData = new JTextField(); JTextField txtTelefone = new JTextField();
        JTextField txtSalario = new JTextField(); JTextField txtRua = new JTextField();
        JTextField txtBairro = new JTextField(); JTextField txtCidade = new JTextField();
        JTextField txtEstado = new JTextField();

        painelCampos.add(new JLabel("  CPF (Primary Key / Busca):")); painelCampos.add(txtCpf);
        painelCampos.add(new JLabel("  Nome:")); painelCampos.add(txtNome);
        painelCampos.add(new JLabel("  Nascimento (YYYY-MM-DD):")); painelCampos.add(txtData);
        painelCampos.add(new JLabel("  Telefone:")); painelCampos.add(txtTelefone);
        painelCampos.add(new JLabel("  Salário Bruto:")); painelCampos.add(txtSalario);
        painelCampos.add(new JLabel("  Rua:")); painelCampos.add(txtRua);
        painelCampos.add(new JLabel("  Bairro:")); painelCampos.add(txtBairro);
        painelCampos.add(new JLabel("  Cidade:")); painelCampos.add(txtCidade);
        painelCampos.add(new JLabel("  Estado (UF):")); painelCampos.add(txtEstado);

        JPanel painelBotoes = new JPanel();
        JButton btnBuscar = new JButton("Buscar p/ Alterar");
        JButton btnSalvar = new JButton("Salvar / Alterar");
        painelBotoes.add(btnBuscar); painelBotoes.add(btnSalvar);

        painelBase.add(painelCampos, BorderLayout.CENTER);
        painelBase.add(painelBotoes, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            String pkBusca = txtCpf.getText().trim();
            if(pkBusca.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o CPF (Primary Key) para realizar a busca.");
                return;
            }
            try {
                Connection con = new ConectorBanco().conectar();
                String sql = "SELECT p.*, pr.SalarioBruto FROM Pessoa p JOIN Professor pr ON p.CPF = pr.CPF_Pessoa WHERE pr.CPF_Pessoa = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, pkBusca);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    txtNome.setText(rs.getString("Nome")); txtData.setText(rs.getString("DataNascimento"));
                    txtTelefone.setText(rs.getString("Telefone")); txtRua.setText(rs.getString("Rua"));
                    txtBairro.setText(rs.getString("Bairro")); txtCidade.setText(rs.getString("Cidade"));
                    txtEstado.setText(rs.getString("Estado")); txtSalario.setText(rs.getString("SalarioBruto"));
                    JOptionPane.showMessageDialog(this, "Professor encontrado.");
                } else {
                    JOptionPane.showMessageDialog(this, "Professor não encontrado com este CPF.");
                }
                con.close();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
        });

        btnSalvar.addActionListener(e -> {
            if (txtEstado.getText().length() > 2) {
                JOptionPane.showMessageDialog(this, "Erro: O Estado deve conter 2 letras."); return;
            }
            processarDados("Professor", txtCpf.getText(), txtNome.getText(), txtData.getText(), txtTelefone.getText(), txtRua.getText(), txtBairro.getText(), txtCidade.getText(), txtEstado.getText(), null, null, null, txtSalario.getText(), null);
        });
        return painelBase;
    }

    // =================================================================
    // ABA 4: TURMAS (Busca estrita pela PK: CodigoTurma)
    // =================================================================
    private JPanel criarAbaTurma() {
        JPanel painelBase = new JPanel(new BorderLayout());
        JPanel painelCampos = new JPanel(new GridLayout(2, 2, 10, 20));
        
        JTextField txtCodigo = new JTextField();
        JTextField txtNomeTurma = new JTextField();

        painelCampos.add(new JLabel("  Cód. Turma (Primary Key / Busca):")); painelCampos.add(txtCodigo);
        painelCampos.add(new JLabel("  Nome da Coorte (Ex: 2024/02):")); painelCampos.add(txtNomeTurma);

        JPanel painelBotoes = new JPanel();
        JButton btnBuscar = new JButton("Buscar p/ Alterar");
        JButton btnSalvar = new JButton("Salvar / Alterar");
        painelBotoes.add(btnBuscar); painelBotoes.add(btnSalvar);

        painelBase.add(painelCampos, BorderLayout.NORTH);
        painelBase.add(painelBotoes, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            String pkBusca = txtCodigo.getText().trim();
            if(pkBusca.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o Código da Turma (Primary Key) para buscar.");
                return;
            }
            try {
                Connection con = new ConectorBanco().conectar();
                String sql = "SELECT NomeTurma FROM Turma WHERE CodigoTurma = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(pkBusca));
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    txtNomeTurma.setText(rs.getString("NomeTurma"));
                    JOptionPane.showMessageDialog(this, "Turma encontrada.");
                } else {
                    JOptionPane.showMessageDialog(this, "Turma não encontrada com este Código.");
                }
                con.close();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
        });

        btnSalvar.addActionListener(e -> {
            processarTurma(txtCodigo.getText(), txtNomeTurma.getText());
        });
        return painelBase;
    }

    // =================================================================
    // LÓGICA DE PROCESSAMENTO (INSERT/UPDATE DINÂMICO)
    // =================================================================
    private void processarDados(String tipo, String cpf, String nome, String data, String tel, String rua, String bairro, String cidade, String est, String mat, String per, String tur, String sal, String prof) {
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i += 25) {
                    final int p = i; SwingUtilities.invokeLater(() -> barraProgresso.setValue(p));
                    Thread.sleep(80);
                }

                Connection conexao = new ConectorBanco().conectar();

                // Lógica de Pessoa (Para Aluno e Professor)
                if (!tipo.equals("Egresso")) {
                    PreparedStatement chk = conexao.prepareStatement("SELECT CPF FROM Pessoa WHERE CPF = ?");
                    chk.setString(1, cpf);
                    if (chk.executeQuery().next()) {
                        PreparedStatement up = conexao.prepareStatement("UPDATE Pessoa SET Nome=?, DataNascimento=?, Telefone=?, Rua=?, Bairro=?, Cidade=?, Estado=? WHERE CPF=?");
                        up.setString(1, nome); up.setString(2, data); up.setString(3, tel); up.setString(4, rua); up.setString(5, bairro); up.setString(6, cidade); up.setString(7, est); up.setString(8, cpf);
                        up.executeUpdate();
                    } else {
                        PreparedStatement in = conexao.prepareStatement("INSERT INTO Pessoa (CPF, Nome, DataNascimento, Telefone, Rua, Bairro, Cidade, Estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                        in.setString(1, cpf); in.setString(2, nome); in.setString(3, data); in.setString(4, tel); in.setString(5, rua); in.setString(6, bairro); in.setString(7, cidade); in.setString(8, est);
                        in.executeUpdate();
                    }
                }

                // Lógica Específica por Papel
                if (tipo.equals("Aluno")) {
                    PreparedStatement chka = conexao.prepareStatement("SELECT Matricula FROM Aluno WHERE Matricula = ?");
                    chka.setString(1, mat);
                    if (chka.executeQuery().next()) {
                        PreparedStatement ua = conexao.prepareStatement("UPDATE Aluno SET Periodo=?, CodigoTurma=? WHERE Matricula=?");
                        ua.setInt(1, Integer.parseInt(per)); ua.setInt(2, Integer.parseInt(tur)); ua.setString(3, mat);
                        ua.executeUpdate();
                    } else {
                        PreparedStatement ia = conexao.prepareStatement("INSERT INTO Aluno (Matricula, CPF_Pessoa, Periodo, CodigoTurma) VALUES (?, ?, ?, ?)");
                        ia.setString(1, mat); ia.setString(2, cpf); ia.setInt(3, Integer.parseInt(per)); ia.setInt(4, Integer.parseInt(tur));
                        ia.executeUpdate();
                    }
                } else if (tipo.equals("Professor")) {
                    PreparedStatement chkp = conexao.prepareStatement("SELECT CPF_Pessoa FROM Professor WHERE CPF_Pessoa = ?");
                    chkp.setString(1, cpf);
                    if (chkp.executeQuery().next()) {
                        PreparedStatement up = conexao.prepareStatement("UPDATE Professor SET SalarioBruto=? WHERE CPF_Pessoa=?");
                        up.setDouble(1, Double.parseDouble(sal.replace(",", "."))); up.setString(2, cpf);
                        up.executeUpdate();
                    } else {
                        PreparedStatement ip = conexao.prepareStatement("INSERT INTO Professor (CPF_Pessoa, SalarioBruto) VALUES (?, ?)");
                        ip.setString(1, cpf); ip.setDouble(2, Double.parseDouble(sal.replace(",", ".")));
                        ip.executeUpdate();
                    }
                } else if (tipo.equals("Egresso")) {
                    PreparedStatement chke = conexao.prepareStatement("SELECT Matricula_Aluno FROM Egresso WHERE Matricula_Aluno = ?");
                    chke.setString(1, mat);
                    if (chke.executeQuery().next()) {
                        PreparedStatement ue = conexao.prepareStatement("UPDATE Egresso SET ProfissaoAtual=? WHERE Matricula_Aluno=?");
                        ue.setString(1, prof); ue.setString(2, mat);
                        ue.executeUpdate();
                    } else {
                        PreparedStatement ie = conexao.prepareStatement("INSERT INTO Egresso (Matricula_Aluno, ProfissaoAtual) VALUES (?, ?)");
                        ie.setString(1, mat); ie.setString(2, prof);
                        ie.executeUpdate();
                    }
                }

                conexao.close();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, tipo + " salvo/alterado com sucesso!");
                    barraProgresso.setValue(0);
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                    barraProgresso.setValue(0);
                });
            }
        }).start();
    }

    private void processarTurma(String codigo, String nomeTurma) {
        new Thread(() -> {
            try {
                Connection conexao = new ConectorBanco().conectar();
                PreparedStatement chk = conexao.prepareStatement("SELECT CodigoTurma FROM Turma WHERE CodigoTurma = ?");
                chk.setInt(1, Integer.parseInt(codigo));
                
                if (chk.executeQuery().next()) {
                    PreparedStatement up = conexao.prepareStatement("UPDATE Turma SET NomeTurma=? WHERE CodigoTurma=?");
                    up.setString(1, nomeTurma); up.setInt(2, Integer.parseInt(codigo));
                    up.executeUpdate();
                } else {
                    PreparedStatement in = conexao.prepareStatement("INSERT INTO Turma (CodigoTurma, NomeTurma) VALUES (?, ?)");
                    in.setInt(1, Integer.parseInt(codigo)); in.setString(2, nomeTurma);
                    in.executeUpdate();
                }
                conexao.close();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Turma salva/alterada com sucesso!"));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()));
            }
        }).start();
    }
}