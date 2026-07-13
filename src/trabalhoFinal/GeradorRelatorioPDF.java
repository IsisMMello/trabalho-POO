package trabalhoFinal;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GeradorRelatorioPDF {

    // Paleta de Cores e Fontes Profissionais
    private static final BaseColor COR_PRIMARIA = new BaseColor(31, 78, 121); // Azul Escuro / Marinho
    private static final BaseColor COR_TEXTO_CABECALHO = BaseColor.WHITE;
    private static final BaseColor COR_LINHA_PAR = new BaseColor(240, 244, 248); // Cinza Azulado Claro
    private static final BaseColor COR_LINHA_IMPAR = BaseColor.WHITE;
    private static final BaseColor COR_BORDA = new BaseColor(218, 223, 230);

    private static final Font FONTE_TITULO = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, COR_PRIMARIA);
    private static final Font FONTE_SUBTITULO = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.DARK_GRAY);
    private static final Font FONTE_SECAO = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COR_PRIMARIA);
    private static final Font FONTE_CABECALHO_TABELA = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, COR_TEXTO_CABECALHO);
    private static final Font FONTE_CELULA = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    private static final Font FONTE_CELULA_NEGRITO = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);

    private static final NumberFormat FORMATO_MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private static final SimpleDateFormat FORMATO_DATA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    // Método auxiliar para criar células padronizadas
    private static PdfPCell criarCelula(String texto, Font fonte, BaseColor corFundo, int alinhamento, float padding) {
        PdfPCell celula = new PdfPCell(new Paragraph(texto, fonte));
        if (corFundo != null) {
            celula.setBackgroundColor(corFundo);
        }
        celula.setBorderColor(COR_BORDA);
        celula.setHorizontalAlignment(alinhamento);
        celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celula.setPadding(padding);
        return celula;
    }

    // Método para adicionar o Cabeçalho Padrão do Documento
    private static void adicionarCabecalhoDocumento(Document document, String tituloRelatorio) throws DocumentException {
        Paragraph titulo = new Paragraph("eMentor-Plus - Sistema de Mentoria", FONTE_TITULO);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        Paragraph subtitulo = new Paragraph(tituloRelatorio + " | Gerado em: " + FORMATO_DATA_HORA.format(new Date()), FONTE_SUBTITULO);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(20);
        document.add(subtitulo);
    }

    // Método auxiliar para recuperar as notas e calcular a média do aluno direto do banco de dados
    private static List<Double> obterNotasAluno(String matricula) {
        List<Double> notas = new ArrayList<>();
        String sql = "SELECT Valor_Nota FROM Notas_Aluno WHERE Matricula_Aluno = ? ORDER BY Posicao_Vetor ASC";
        try {
            ConectorBanco conector = new ConectorBanco();
            try (Connection conn = conector.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, matricula);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        notas.add(rs.getDouble("Valor_Nota"));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar notas do aluno " + matricula + ": " + e.getMessage());
        }
        return notas;
    }

    private static double calcularMedia(List<Double> notas) {
        if (notas.isEmpty()) {
            return 0.0;
        }
        double soma = 0.0;
        for (double nota : notas) {
            soma += nota;
        }
        return soma / notas.size();
    }

    private static String formatarNotas(List<Double> notas) {
        if (notas.isEmpty()) {
            return "Sem notas lançadas";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < notas.size(); i++) {
            sb.append(String.format("%.1f", notas.get(i)));
            if (i < notas.size() - 1) {
                sb.append(" | ");
            }
        }
        return sb.toString();
    }

    // 1. Relatório de Alunos: Tabela com os dados pessoais e acadêmicos.
    public static void gerarRelatorioAlunos(List<Aluno> alunos, String caminhoArquivo) throws Exception {
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36); // Paisagem para caber dados
        PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
        document.open();

        adicionarCabecalhoDocumento(document, "Relatório Geral de Alunos");

        // Colunas: Matrícula, Nome, CPF, Telefone, Período, Código Turma, Cidade/Estado
        float[] largurasColunas = {2.5f, 5.0f, 2.5f, 2.5f, 1.5f, 2.0f, 3.0f};
        PdfPTable tabela = new PdfPTable(largurasColunas);
        tabela.setWidthPercentage(100);

        // Cabeçalhos
        String[] cabecalhos = {"Matrícula", "Nome do Aluno", "CPF", "Telefone", "Período", "Turma", "Cidade/UF"};
        for (String cab : cabecalhos) {
            tabela.addCell(criarCelula(cab, FONTE_CABECALHO_TABELA, COR_PRIMARIA, Element.ALIGN_CENTER, 6f));
        }

        // Linhas
        int count = 0;
        for (Aluno aluno : alunos) {
            BaseColor corFundo = (count++ % 2 == 0) ? COR_LINHA_PAR : COR_LINHA_IMPAR;
            
            tabela.addCell(criarCelula(aluno.getmatricula(), FONTE_CELULA, corFundo, Element.ALIGN_CENTER, 5f));
            tabela.addCell(criarCelula(aluno.getNome(), FONTE_CELULA, corFundo, Element.ALIGN_LEFT, 5f));
            tabela.addCell(criarCelula(String.valueOf(aluno.getCpf()), FONTE_CELULA, corFundo, Element.ALIGN_CENTER, 5f));
            tabela.addCell(criarCelula(aluno.getTelefone() != null ? aluno.getTelefone() : "Não informado", FONTE_CELULA, corFundo, Element.ALIGN_CENTER, 5f));
            tabela.addCell(criarCelula(String.valueOf(aluno.getperiodo()), FONTE_CELULA, corFundo, Element.ALIGN_CENTER, 5f));
            tabela.addCell(criarCelula(aluno.getturma() != null ? aluno.getturma() : "Não informada", FONTE_CELULA, corFundo, Element.ALIGN_CENTER, 5f));
            
            String cidadeEstado = "Não informada";
            if (aluno.getCidade() != null && aluno.getEstado() != null) {
                cidadeEstado = aluno.getCidade() + "/" + aluno.getEstado();
            } else if (aluno.getCidade() != null) {
                cidadeEstado = aluno.getCidade();
            }
            tabela.addCell(criarCelula(cidadeEstado, FONTE_CELULA, corFundo, Element.ALIGN_LEFT, 5f));
        }

        document.add(tabela);
        document.close();
    }

    // 2. Relatório de Egressos: Tabela com os dados acadêmicos mais Profissão Atual, Faixa Salarial, Curso Anterior e Atual.
    public static void gerarRelatorioEgressos(List<Egressos> egressos, String caminhoArquivo) throws Exception {
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
        document.open();

        adicionarCabecalhoDocumento(document, "Relatório Geral de Egressos");

        // Colunas: Matrícula, Nome, Curso Anterior, Curso Atual, Profissão Atual, Salário/Faixa Salarial
        float[] largurasColunas = {2.5f, 4.5f, 3.5f, 3.5f, 3.5f, 2.5f};
        PdfPTable tabela = new PdfPTable(largurasColunas);
        tabela.setWidthPercentage(100);

        // Cabeçalhos
        String[] cabecalhos = {"Matrícula", "Nome do Egresso", "Curso Anterior", "Curso Atual", "Profissão Atual", "Faixa Salarial"};
        for (String cab : cabecalhos) {
            tabela.addCell(criarCelula(cab, FONTE_CABECALHO_TABELA, COR_PRIMARIA, Element.ALIGN_CENTER, 6f));
        }

        // Linhas
        int count = 0;
        for (Egressos egresso : egressos) {
            BaseColor corFundo = (count++ % 2 == 0) ? COR_LINHA_PAR : COR_LINHA_IMPAR;

            tabela.addCell(criarCelula(egresso.getmatricula(), FONTE_CELULA, corFundo, Element.ALIGN_CENTER, 5f));
            tabela.addCell(criarCelula(egresso.getNome(), FONTE_CELULA, corFundo, Element.ALIGN_LEFT, 5f));
            tabela.addCell(criarCelula(egresso.getCursoAnterior() != null ? egresso.getCursoAnterior() : "Não informado", FONTE_CELULA, corFundo, Element.ALIGN_LEFT, 5f));
            tabela.addCell(criarCelula(egresso.getCursoAtual() != null ? egresso.getCursoAtual() : "Não informado", FONTE_CELULA, corFundo, Element.ALIGN_LEFT, 5f));
            tabela.addCell(criarCelula(egresso.getProfissaoAtual() != null ? egresso.getProfissaoAtual() : "Não informada", FONTE_CELULA, corFundo, Element.ALIGN_LEFT, 5f));
            
            String salarioStr = FORMATO_MOEDA.format(egresso.getFaixaSalarial());
            tabela.addCell(criarCelula(salarioStr, FONTE_CELULA, corFundo, Element.ALIGN_RIGHT, 5f));
        }

        document.add(tabela);
        document.close();
    }

    // 3. Relatório de Professores: Tabela com dados gerais, salário bruto e salário líquido calculado.
    public static void gerarRelatorioProfessores(List<Professor> professores, String caminhoArquivo) throws Exception {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
        document.open();

        adicionarCabecalhoDocumento(document, "Relatório de Professores e Folha Salarial");

        // Colunas: CPF, Nome, Telefone, Data Admissão, Salário Bruto Base, Salário Bruto Total, Salário Líquido
        float[] largurasColunas = {2.5f, 4.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f};
        PdfPTable tabela = new PdfPTable(largurasColunas);
        tabela.setWidthPercentage(100);

        // Cabeçalhos
        String[] cabecalhos = {"CPF", "Nome do Professor", "Telefone", "Admissão", "Salário Bruto (Base)", "Salário Bruto (Total)", "Salário Líquido"};
        for (String cab : cabecalhos) {
            tabela.addCell(criarCelula(cab, FONTE_CABECALHO_TABELA, COR_PRIMARIA, Element.ALIGN_CENTER, 6f));
        }

        // Linhas
        int count = 0;
        for (Professor prof : professores) {
            BaseColor corFundo = (count++ % 2 == 0) ? COR_LINHA_PAR : COR_LINHA_IMPAR;

            tabela.addCell(criarCelula(String.valueOf(prof.getCpf()), FONTE_CELULA, corFundo, Element.ALIGN_CENTER, 5f));
            tabela.addCell(criarCelula(prof.getNome(), FONTE_CELULA, corFundo, Element.ALIGN_LEFT, 5f));
            tabela.addCell(criarCelula(prof.getTelefone() != null ? prof.getTelefone() : "Não informado", FONTE_CELULA, corFundo, Element.ALIGN_CENTER, 5f));
            tabela.addCell(criarCelula(prof.getDataAdmissao() != null ? prof.getDataAdmissao() : "Não informada", FONTE_CELULA, corFundo, Element.ALIGN_CENTER, 5f));
            
            tabela.addCell(criarCelula(FORMATO_MOEDA.format(prof.getSalarioBruto()), FONTE_CELULA, corFundo, Element.ALIGN_RIGHT, 5f));
            tabela.addCell(criarCelula(FORMATO_MOEDA.format(prof.calcularSalarioBrutoTotal()), FONTE_CELULA, corFundo, Element.ALIGN_RIGHT, 5f));
            tabela.addCell(criarCelula(FORMATO_MOEDA.format(prof.calcularSalarioLiquido()), FONTE_CELULA_NEGRITO, corFundo, Element.ALIGN_RIGHT, 5f));
        }

        document.add(tabela);
        document.close();
    }

    // 4. Relatório de Turmas (Crucial): Tabela contendo o código e nome da turma, listando os alunos vinculados, e exibindo explicitamente as notas e a média final de cada aluno.
    public static void gerarRelatorioTurmas(List<Turma> turmas, String caminhoArquivo) throws Exception {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
        document.open();

        adicionarCabecalhoDocumento(document, "Relatório de Turmas com Notas e Médias");

        if (turmas.isEmpty()) {
            Paragraph p = new Paragraph("Nenhuma turma cadastrada para exibição.", FONTE_CELULA);
            document.add(p);
        } else {
            for (Turma turma : turmas) {
                // Título da Turma
                Paragraph tituloTurma = new Paragraph("Turma: " + turma.getCodigoTurma() + " - Coorte: " + turma.getNomeTurma(), FONTE_SECAO);
                tituloTurma.setSpacingBefore(15);
                tituloTurma.setSpacingAfter(8);
                document.add(tituloTurma);

                List<Aluno> alunos = turma.getAlunosVinculados();
                if (alunos == null || alunos.isEmpty()) {
                    Paragraph semAlunos = new Paragraph("Nenhum aluno vinculado a esta turma.", FONTE_CELULA);
                    semAlunos.setSpacingAfter(15);
                    document.add(semAlunos);
                } else {
                    // Tabela de Alunos da Turma
                    // Colunas: Matrícula, Nome do Aluno, Notas Lançadas, Média Final
                    float[] largurasColunas = {3.0f, 6.0f, 7.0f, 2.5f};
                    PdfPTable tabelaAlunos = new PdfPTable(largurasColunas);
                    tabelaAlunos.setWidthPercentage(100);
                    tabelaAlunos.setSpacingAfter(15);

                    // Cabeçalho da Tabela
                    tabelaAlunos.addCell(criarCelula("Matrícula", FONTE_CABECALHO_TABELA, COR_PRIMARIA, Element.ALIGN_CENTER, 6f));
                    tabelaAlunos.addCell(criarCelula("Nome do Aluno", FONTE_CABECALHO_TABELA, COR_PRIMARIA, Element.ALIGN_LEFT, 6f));
                    tabelaAlunos.addCell(criarCelula("Histórico de Notas", FONTE_CABECALHO_TABELA, COR_PRIMARIA, Element.ALIGN_CENTER, 6f));
                    tabelaAlunos.addCell(criarCelula("Média Final", FONTE_CABECALHO_TABELA, COR_PRIMARIA, Element.ALIGN_CENTER, 6f));

                    int count = 0;
                    for (Aluno aluno : alunos) {
                        BaseColor corFundo = (count++ % 2 == 0) ? COR_LINHA_PAR : COR_LINHA_IMPAR;

                        tabelaAlunos.addCell(criarCelula(aluno.getmatricula(), FONTE_CELULA, corFundo, Element.ALIGN_CENTER, 5f));
                        tabelaAlunos.addCell(criarCelula(aluno.getNome(), FONTE_CELULA, corFundo, Element.ALIGN_LEFT, 5f));

                        // Recupera dinamicamente as notas salvas no BD para preencher o PDF
                        List<Double> notas = obterNotasAluno(aluno.getmatricula());
                        String notasFormatadas = formatarNotas(notas);
                        double media = calcularMedia(notas);

                        tabelaAlunos.addCell(criarCelula(notasFormatadas, FONTE_CELULA, corFundo, Element.ALIGN_CENTER, 5f));
                        
                        String mediaFormatada = String.format("%.2f", media);
                        tabelaAlunos.addCell(criarCelula(mediaFormatada, FONTE_CELULA_NEGRITO, corFundo, Element.ALIGN_CENTER, 5f));
                    }
                    document.add(tabelaAlunos);
                }
            }
        }

        document.close();
    }

    // Métodos sobrecarregados para gerar os relatórios buscando dados do Banco de Dados
    public static void gerarRelatorioAlunos(String caminhoArquivo) throws Exception {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT p.CPF, p.Nome, p.DataNascimento, p.Telefone, p.Rua, p.Bairro, p.Cidade, p.Estado, a.Matricula, a.Periodo, a.CodigoTurma FROM Aluno a JOIN Pessoa p ON a.CPF_Pessoa = p.CPF";
        try (Connection conn = new ConectorBanco().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                long cpf = 0;
                try {
                    cpf = Long.parseLong(rs.getString("CPF"));
                } catch (Exception ignored) {}
                int periodo = rs.getInt("Periodo");
                alunos.add(new Aluno(
                    rs.getString("Nome"),
                    rs.getString("DataNascimento"),
                    cpf,
                    rs.getString("Telefone"),
                    rs.getString("Rua"),
                    rs.getString("Bairro"),
                    rs.getString("Cidade"),
                    rs.getString("Estado"),
                    rs.getString("Matricula"),
                    periodo,
                    rs.getString("CodigoTurma")
                ));
            }
        }
        gerarRelatorioAlunos(alunos, caminhoArquivo);
    }

    public static void gerarRelatorioEgressos(String caminhoArquivo) throws Exception {
        List<Egressos> egressos = new ArrayList<>();
        String sql = "SELECT p.CPF, p.Nome, p.DataNascimento, p.Telefone, p.Rua, p.Bairro, p.Cidade, p.Estado, a.Matricula, a.Periodo, a.CodigoTurma, e.ProfissaoAtual, e.FaixaSalarial, e.CursoAnterior, e.CursoAtual FROM Egresso e JOIN Aluno a ON e.Matricula_Aluno = a.Matricula JOIN Pessoa p ON a.CPF_Pessoa = p.CPF";
        try (Connection conn = new ConectorBanco().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                long cpf = 0;
                try {
                    cpf = Long.parseLong(rs.getString("CPF"));
                } catch (Exception ignored) {}
                int periodo = rs.getInt("Periodo");
                double salario = rs.getDouble("FaixaSalarial");
                egressos.add(new Egressos(
                    rs.getString("Nome"),
                    rs.getString("DataNascimento"),
                    cpf,
                    rs.getString("Telefone"),
                    rs.getString("Matricula"),
                    rs.getString("CodigoTurma"),
                    periodo,
                    rs.getString("Bairro"),
                    rs.getString("Cidade"),
                    rs.getString("Estado"),
                    rs.getString("ProfissaoAtual"),
                    salario,
                    rs.getString("CursoAnterior"),
                    rs.getString("CursoAtual"),
                    rs.getString("CursoAnterior")
                ));
            }
        }
        gerarRelatorioEgressos(egressos, caminhoArquivo);
    }

    public static void gerarRelatorioProfessores(String caminhoArquivo) throws Exception {
        List<Professor> professores = new ArrayList<>();
        String sql = "SELECT p.CPF, p.Nome, p.DataNascimento, p.Telefone, p.Rua, p.Bairro, p.Cidade, p.Estado, pr.DataAdmissao, pr.CargoChefia, pr.CargoCoordenacao, pr.SalarioBruto FROM Professor pr JOIN Pessoa p ON pr.CPF_Pessoa = p.CPF";
        try (Connection conn = new ConectorBanco().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                long cpf = 0;
                try {
                    cpf = Long.parseLong(rs.getString("CPF"));
                } catch (Exception ignored) {}
                boolean chefia = rs.getBoolean("CargoChefia");
                boolean coord = rs.getBoolean("CargoCoordenacao");
                professores.add(new Professor(
                    rs.getString("Nome"),
                    rs.getString("DataNascimento"),
                    cpf,
                    rs.getString("Telefone"),
                    rs.getString("Rua"),
                    rs.getString("Bairro"),
                    rs.getString("Cidade"),
                    rs.getString("Estado"),
                    rs.getString("DataAdmissao"),
                    chefia,
                    coord,
                    rs.getDouble("SalarioBruto")
                ));
            }
        }
        gerarRelatorioProfessores(professores, caminhoArquivo);
    }

    public static void gerarRelatorioTurmas(String caminhoArquivo) throws Exception {
        List<Turma> turmas = new ArrayList<>();
        String sqlTurmas = "SELECT CodigoTurma, NomeTurma FROM Turma";
        String sqlAlunos = "SELECT p.CPF, p.Nome, p.DataNascimento, p.Telefone, p.Rua, p.Bairro, p.Cidade, p.Estado, a.Matricula, a.Periodo, a.CodigoTurma FROM Aluno a JOIN Pessoa p ON a.CPF_Pessoa = p.CPF WHERE a.CodigoTurma = ?";
        try (Connection conn = new ConectorBanco().conectar();
             PreparedStatement stmtT = conn.prepareStatement(sqlTurmas);
             ResultSet rsT = stmtT.executeQuery()) {
            while (rsT.next()) {
                int cod = rsT.getInt("CodigoTurma");
                Turma turma = new Turma(cod, rsT.getString("NomeTurma"));
                try (PreparedStatement stmtA = conn.prepareStatement(sqlAlunos)) {
                    stmtA.setInt(1, cod);
                    try (ResultSet rsA = stmtA.executeQuery()) {
                        while (rsA.next()) {
                            long cpf = 0;
                            try {
                                cpf = Long.parseLong(rsA.getString("CPF"));
                            } catch (Exception ignored) {}
                            Aluno aluno = new Aluno(
                                rsA.getString("Nome"),
                                rsA.getString("DataNascimento"),
                                cpf,
                                rsA.getString("Telefone"),
                                rsA.getString("Rua"),
                                rsA.getString("Bairro"),
                                rsA.getString("Cidade"),
                                rsA.getString("Estado"),
                                rsA.getString("Matricula"),
                                rsA.getInt("Periodo"),
                                String.valueOf(cod)
                            );
                            turma.vincularAluno(aluno);
                        }
                    }
                }
                turmas.add(turma);
            }
        }
        gerarRelatorioTurmas(turmas, caminhoArquivo);
    }
}
