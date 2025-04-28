package senai.jandira.atividade.gui;

import senai.jandira.atividade.model.Temperatura;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(null);

        // Label Celsius (mais à esquerda)
        labelCelsius = new JLabel("Temperatura em Celsius:");
        labelCelsius.setBounds(20, 30, 200, 25);
        add(labelCelsius);

        // Campo de texto (ao lado do label)
        textCelsius = new JTextField();
        textCelsius.setBounds(230, 30, 120, 25); // Mais à direita, alinhado
        add(textCelsius);

        // Botão Fahrenheit (centralizado embaixo)
        btnFahrenheit = new JButton("FAHRENHEIT");
        btnFahrenheit.setBounds(50, 80, 130, 40);
        btnFahrenheit.setBackground(new Color(70,130,180)); // azul
        btnFahrenheit.setForeground(Color.WHITE);
        add(btnFahrenheit);

        // Botão Kelvin (ao lado do Fahrenheit)
        btnKelvin = new JButton("KELVIN");
        btnKelvin.setBounds(220, 80, 130, 40);
        btnKelvin.setBackground(new Color(34,139,34)); // verde
        btnKelvin.setForeground(Color.WHITE);
        add(btnKelvin);

        // Label Resultado (centralizado)
        labelResultado = new JLabel("", SwingConstants.CENTER);
        labelResultado.setFont(new Font("Arial", Font.BOLD, 16));
        labelResultado.setBounds(50, 140, 300, 25);
        add(labelResultado);

        // Label Erro (centralizado e embaixo do resultado)
        labelErro = new JLabel("", SwingConstants.CENTER);
        labelErro.setForeground(Color.RED);
        labelErro.setBounds(50, 170, 300, 25);
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

    /*
     Converte para Fahrenheit
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

    /*
     Converte para Kelvin
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
