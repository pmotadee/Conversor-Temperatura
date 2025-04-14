package senai.jandira.atividade.gui;

import senai.jandira.atividade.model.Temperatura;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe que representa a interface gráfica para converter
 * o valor de temperatura de Celsius para Fahrenheit ou Kelvin.
 */
public class TelaConversor extends JFrame {

    private JLabel labelCelsius;
    private JTextField textCelsius;
    private JButton btnFahrenheit;
    private JButton btnKelvin;
    private JLabel labelResultado;
    private JLabel labelErro;

    public TelaConversor() {
        super("Conversor de Temperatura");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(null);  // Layout nulo para posicionar os componentes manualmente

        // Label: "Temperatura em graus celsius"
        labelCelsius = new JLabel("Temperatura em graus celsius:");
        labelCelsius.setBounds(20, 20, 200, 25);
        add(labelCelsius);

        // Campo de texto
        textCelsius = new JTextField();
        textCelsius.setBounds(220, 20, 50, 25);
        add(textCelsius);

        // Botão Fahrenheit
        btnFahrenheit = new JButton("FAHRENHEIT");
        btnFahrenheit.setBounds(20, 60, 120, 30);
        add(btnFahrenheit);

        // Botão Kelvin
        btnKelvin = new JButton("KELVIN");
        btnKelvin.setBounds(150, 60, 80, 30);
        add(btnKelvin);

        // Label de resultado (exibirá algo como "78,80 FAHRENHEIT")
        labelResultado = new JLabel("");
        labelResultado.setFont(new Font("Arial", Font.BOLD, 16));
        labelResultado.setBounds(20, 110, 300, 25);
        add(labelResultado);

        // Label de erro (vermelho)
        labelErro = new JLabel("");
        labelErro.setForeground(Color.RED);
        labelErro.setBounds(20, 140, 350, 25);
        add(labelErro);

        // Ações dos botões
        btnFahrenheit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                converterParaFahrenheit();
            }
        });

        btnKelvin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                converterParaKelvin();
            }
        });
    }

    /**
     * Converte para Fahrenheit
     */
    private void converterParaFahrenheit() {
        String texto = textCelsius.getText().trim();
        labelErro.setText("");
        labelResultado.setText("");

        try {
            double celsius = Double.parseDouble(texto.replace(',', '.'));
            Temperatura temp = new Temperatura(celsius);
            double fahrenheit = temp.toFahrenheit();
            labelResultado.setText(String.format("%.2f FAHRENHEIT", fahrenheit));
        } catch (NumberFormatException ex) {
            labelErro.setText("Valor inválido! Digite um número.");
        }
    }

    /**
     * Converte para Kelvin
     */
    private void converterParaKelvin() {
        String texto = textCelsius.getText().trim();
        labelErro.setText("");
        labelResultado.setText("");

        try {
            double celsius = Double.parseDouble(texto.replace(',', '.'));
            Temperatura temp = new Temperatura(celsius);
            double kelvin = temp.toKelvin();
            labelResultado.setText(String.format("%.2f KELVIN", kelvin));
        } catch (NumberFormatException ex) {
            labelErro.setText("Valor inválido! Digite um número.");
        }
    }
}
