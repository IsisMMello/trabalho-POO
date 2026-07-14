package trabalhoFinal;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TelaCadastro extends JFrame {
    private JProgressBar barraProgresso;

    public TelaCadastro() {
        setTitle("Central de Cadastros e Edição - eMentor");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.setForeground(new Color(0, 0, 0));
        btnVoltar.setBackground(new Color(209, 179, 111));
        getContentPane().add(btnVoltar, BorderLayout.NORTH);
        btnVoltar.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        barraProgresso = new JProgressBar(0, 100);
        barraProgresso.setStringPainted(true);
        getContentPane().add(barraProgresso, BorderLayout.SOUTH);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Alunos", criarAbaAluno());
        abas.addTab("Egressos", criarAbaEgresso());
        abas.addTab("Professores", criarAbaProfessor());
        abas.addTab("Turmas", criarAbaTurma());

        getContentPane().add(abas, BorderLayout.CENTER);
    }

    // ABA 1: ALUNOS (Busca estrita pela PK: Matrícula)
    
    private JPanel criarAbaAluno() {
        JPanel painelBase = new JPanel(new BorderLayout());
        JPanel painelCampos = new JPanel(new GridLayout(11, 2, 5, 5));
        painelCampos.setBackground(new Color(46, 56, 95));

        JTextField txtMatricula = new JTextField(); 
        txtMatricula.setBackground(new Color(154, 153, 150));JTextField txtCpf = new JTextField(); 
txtCpf.setBackground(new Color(154, 153, 150));
        JTextField txtNome = new JTextField(); 
        txtNome.setBackground(new Color(154, 153, 150));JTextField txtData = new JTextField(); 
txtData.setBackground(new Color(154, 153, 150));
        JTextField txtTelefone = new JTextField(); 
        txtTelefone.setBackground(new Color(154, 153, 150));JTextField txtPeriodo = new JTextField();
txtPeriodo.setBackground(new Color(154, 153, 150));
        JTextField txtTurma = new JTextField(); 
        txtTurma.setBackground(new Color(154, 153, 150));JTextField txtRua = new JTextField();
txtRua.setBackground(new Color(154, 153, 150));
        JTextField txtBairro = new JTextField(); 
        txtBairro.setBackground(new Color(154, 153, 150));JTextField txtCidade = new JTextField();
txtCidade.setBackground(new Color(154, 153, 150));
        JTextField txtEstado = new JTextField();
        txtEstado.setBackground(new Color(154, 153, 150));

        JLabel label = new JLabel("  Matrícula:");
        label.setForeground(new Color(255, 255, 255));
        painelCampos.add(label); painelCampos.add(txtMatricula);
        JLabel label_1 = new JLabel("  CPF:");
        label_1.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_1); painelCampos.add(txtCpf);
        JLabel label_2 = new JLabel("  Nome:");
        label_2.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_2); painelCampos.add(txtNome);
        JLabel label_3 = new JLabel("  Nascimento:");
        label_3.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_3); painelCampos.add(txtData);
        JLabel label_4 = new JLabel("  Telefone:");
        label_4.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_4); painelCampos.add(txtTelefone);
        JLabel label_5 = new JLabel("  Período:");
        label_5.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_5); painelCampos.add(txtPeriodo);
        JLabel label_6 = new JLabel("  Turma:");
        label_6.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_6); painelCampos.add(txtTurma);
        JLabel label_7 = new JLabel("  Rua:");
        label_7.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_7); painelCampos.add(txtRua);
        JLabel label_8 = new JLabel("  Bairro:");
        label_8.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_8); painelCampos.add(txtBairro);
        JLabel label_9 = new JLabel("  Cidade:");
        label_9.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_9); painelCampos.add(txtCidade);
        JLabel label_10 = new JLabel("  Estado (UF):");
        label_10.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_10); painelCampos.add(txtEstado);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(46, 56, 95));
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setForeground(new Color(0, 0, 0));
        btnBuscar.setBackground(new Color(209, 179, 111));
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setForeground(new Color(0, 0, 0));
        btnSalvar.setBackground(new Color(209, 179, 111));
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

    // ABA 2: EGRESSOS (Busca estrita pela PK: Matrícula_Aluno)

    private JPanel criarAbaEgresso() {
        JPanel painelBase = new JPanel(new BorderLayout());
        painelBase.setBackground(new Color(61, 73, 119));

        JPanel painelCampos = new JPanel(
                new GridLayout(9, 2, 5, 5)
        );

        painelCampos.setBackground(new Color(61, 73, 119));

        JTextField txtMatricula = new JTextField();
        txtMatricula.setBackground(new Color(154, 153, 150));

        JTextField txtCpf = new JTextField();
        txtCpf.setBackground(new Color(154, 153, 150));

        JTextField txtProfissao = new JTextField();
        txtProfissao.setBackground(new Color(154, 153, 150));

        JLabel labelMatricula = new JLabel("  Matrícula:");
        labelMatricula.setForeground(Color.WHITE);

        JLabel labelCpf = new JLabel("  CPF:");
        labelCpf.setForeground(Color.WHITE);

        JLabel labelProfissao = new JLabel("  Profissão Atual:");
        labelProfissao.setForeground(Color.WHITE);

        JLabel labelInformacao = new JLabel(
                "  O egresso já deve existir como aluno"
        );
        labelInformacao.setForeground(Color.WHITE);

        // Linha 1
        painelCampos.add(labelMatricula);
        painelCampos.add(txtMatricula);

        // Linha 2
        painelCampos.add(labelCpf);
        painelCampos.add(txtCpf);

        // Linha 3
        painelCampos.add(labelProfissao);
        painelCampos.add(txtProfissao);

        // Linha 4
        painelCampos.add(labelInformacao);
        painelCampos.add(new JLabel());

        // Linhas vazias para completar as 9 linhas
        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(61, 73, 119));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setBackground(new Color(209, 179, 111));

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.setBackground(new Color(209, 179, 111));

        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnSalvar);

        painelBase.add(painelCampos, BorderLayout.CENTER);
        painelBase.add(painelBotoes, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            String pkBusca = txtMatricula.getText().trim();

            if (pkBusca.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Informe a Matrícula para realizar a busca."
                );
                return;
            }

            try {
                Connection con = new ConectorBanco().conectar();

                String sql =
                        "SELECT ProfissaoAtual "
                        + "FROM Egresso "
                        + "WHERE Matricula_Aluno = ?";

                PreparedStatement stmt =
                        con.prepareStatement(sql);

                stmt.setString(1, pkBusca);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    txtProfissao.setText(
                            rs.getString("ProfissaoAtual")
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Egresso encontrado."
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Egresso não encontrado com esta Matrícula."
                    );
                }

                rs.close();
                stmt.close();
                con.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Erro: " + ex.getMessage()
                );
            }
        });

        btnSalvar.addActionListener(e -> {
            processarDados(
                    "Egresso",
                    txtCpf.getText(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    txtMatricula.getText(),
                    null,
                    null,
                    null,
                    txtProfissao.getText()
            );
        });

        return painelBase;
    }

    // ABA 3: PROFESSORES (Busca estrita pela PK: CPF_Pessoa)

    private JPanel criarAbaProfessor() {
        JPanel painelBase = new JPanel(new BorderLayout());
        JPanel painelCampos = new JPanel(new GridLayout(9, 2, 5, 5));
        painelCampos.setBackground(new Color(61, 73, 119));

        JTextField txtCpf = new JTextField(); 
        txtCpf.setBackground(new Color(154, 153, 150));JTextField txtNome = new JTextField();
txtNome.setBackground(new Color(154, 153, 150));
        JTextField txtData = new JTextField(); 
        txtData.setBackground(new Color(154, 153, 150));JTextField txtTelefone = new JTextField();
 txtTelefone.setBackground(new Color(154, 153, 150));
        JTextField txtSalario = new JTextField(); 
        txtSalario.setBackground(new Color(154, 153, 150));JTextField txtRua = new JTextField();
 txtRua.setBackground(new Color(154, 153, 150));
        JTextField txtBairro = new JTextField(); 
        txtBairro.setBackground(new Color(154, 153, 150));JTextField txtCidade = new JTextField();
 txtCidade.setBackground(new Color(154, 153, 150));
        JTextField txtEstado = new JTextField();
        txtEstado.setBackground(new Color(154, 153, 150));

        JLabel label_8 = new JLabel("  CPF:");
        label_8.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_8); painelCampos.add(txtCpf);
        JLabel label_7 = new JLabel("  Nome:");
        label_7.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_7); painelCampos.add(txtNome);
        JLabel label_6 = new JLabel("  Nascimento:");
        label_6.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_6); painelCampos.add(txtData);
        JLabel label_5 = new JLabel("  Telefone:");
        label_5.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_5); painelCampos.add(txtTelefone);
        JLabel label_4 = new JLabel("  Salário Bruto:");
        label_4.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_4); painelCampos.add(txtSalario);
        JLabel label_3 = new JLabel("  Rua:");
        label_3.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_3); painelCampos.add(txtRua);
        JLabel label_2 = new JLabel("  Bairro:");
        label_2.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_2); painelCampos.add(txtBairro);
        JLabel label_1 = new JLabel("  Cidade:");
        label_1.setForeground(new Color(255, 255, 255));
        painelCampos.add(label_1); painelCampos.add(txtCidade);
        JLabel label = new JLabel("  Estado (UF):");
        label.setForeground(new Color(255, 255, 255));
        painelCampos.add(label); painelCampos.add(txtEstado);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(61, 73, 119));
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setForeground(new Color(0, 0, 0));
        btnBuscar.setBackground(new Color(209, 179, 111));
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setForeground(new Color(0, 0, 0));
        btnSalvar.setBackground(new Color(209, 179, 111));
        painelBotoes.add(btnBuscar); painelBotoes.add(btnSalvar);

        painelBase.add(painelCampos, BorderLayout.CENTER);
        painelBase.add(painelBotoes, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            String pkBusca = txtCpf.getText().trim();
            if(pkBusca.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o CPF para realizar a busca.");
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

    // ABA 4: TURMAS (Busca estrita pela PK: CodigoTurma)

    private JPanel criarAbaTurma() {
        JPanel painelBase = new JPanel(new BorderLayout());
        painelBase.setBackground(new Color(61, 73, 119));

        JPanel painelCampos = new JPanel(
                new GridLayout(9, 2, 5, 5)
        );

        painelCampos.setBackground(new Color(61, 73, 119));

        JTextField txtCodigo = new JTextField();
        txtCodigo.setBackground(new Color(154, 153, 150));

        JTextField txtNomeTurma = new JTextField();
        txtNomeTurma.setBackground(new Color(154, 153, 150));

        JLabel labelCodigo = new JLabel("  Turma:");
        labelCodigo.setForeground(Color.WHITE);

        JLabel labelNomeTurma = new JLabel(
                "  Nome da Coorte (Ex: 2024/2):"
        );
        labelNomeTurma.setForeground(Color.WHITE);

        // Linha 1
        painelCampos.add(labelCodigo);
        painelCampos.add(txtCodigo);

        // Linha 2
        painelCampos.add(labelNomeTurma);
        painelCampos.add(txtNomeTurma);

        // Linhas vazias para completar as 9 linhas
        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        painelCampos.add(new JLabel());
        painelCampos.add(new JLabel());

        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(61, 73, 119));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setBackground(new Color(209, 179, 111));

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.setBackground(new Color(209, 179, 111));

        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnSalvar);

        painelBase.add(painelCampos, BorderLayout.CENTER);
        painelBase.add(painelBotoes, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            String pkBusca = txtCodigo.getText().trim();

            if (pkBusca.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Informe o Código da Turma para buscar."
                );
                return;
            }

            try {
                Connection con = new ConectorBanco().conectar();

                String sql =
                        "SELECT NomeTurma "
                        + "FROM Turma "
                        + "WHERE CodigoTurma = ?";

                PreparedStatement stmt =
                        con.prepareStatement(sql);

                stmt.setInt(
                        1,
                        Integer.parseInt(pkBusca)
                );

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    txtNomeTurma.setText(
                            rs.getString("NomeTurma")
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Turma encontrada."
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Turma não encontrada com este Código."
                    );
                }

                rs.close();
                stmt.close();
                con.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Erro: " + ex.getMessage()
                );
            }
        });

        btnSalvar.addActionListener(e -> {
            processarTurma(
                    txtCodigo.getText(),
                    txtNomeTurma.getText()
            );
        });

        return painelBase;
    }


    // LÓGICA DE PROCESSAMENTO (INSERT/UPDATE DINÂMICO)

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