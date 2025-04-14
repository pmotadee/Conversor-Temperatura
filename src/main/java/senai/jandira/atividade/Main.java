package senai.jandira.atividade;

import senai.jandira.atividade.gui.TelaConversor;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaConversor().setVisible(true);
        });
    }
}
