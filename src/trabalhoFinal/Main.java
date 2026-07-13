package trabalhoFinal;

import javax.swing.SwingUtilities;

public class Main {
    
    public static void main(String[] args) {
        // O padrão correto no Java Swing é iniciar a interface gráfica
        // dentro de uma thread especial (Event Dispatch Thread) para evitar travamentos.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Instancia e torna a tela de login visível
                TelaLogin telaLogin = new TelaLogin();
                telaLogin.setVisible(true);
            }
        });
    }
}