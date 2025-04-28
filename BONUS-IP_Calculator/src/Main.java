package senai.jandira.atividade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IPAddressSwingApp().setVisible(true));
    }

    public static class IPAddressSwingApp extends JFrame {

        private JTextField inputField;
        private JTextArea outputArea;

        public IPAddressSwingApp() {
            setTitle("Calculadora de IP");
            setSize(700, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            inicializarComponentes();
        }

        private void inicializarComponentes() {
            JPanel painel = new JPanel();
            painel.setLayout(new BorderLayout());

            JLabel label = new JLabel("Digite o IP/CIDR (ex: 192.168.0.0/24):");
            inputField = new JTextField();
            JButton botaoCalcular = new JButton("Calcular");
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(outputArea);

            botaoCalcular.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    calcularIP();
                }
            });

            JPanel inputPanel = new JPanel(new BorderLayout());
            inputPanel.add(label, BorderLayout.NORTH);
            inputPanel.add(inputField, BorderLayout.CENTER);
            inputPanel.add(botaoCalcular, BorderLayout.EAST);

            painel.add(inputPanel, BorderLayout.NORTH);
            painel.add(scrollPane, BorderLayout.CENTER);

            add(painel);
        }

        private void calcularIP() {
            try {
                String entrada = inputField.getText().trim();
                String[] partes = entrada.split("/");
                String ip = partes[0];
                int cidr = Integer.parseInt(partes[1]);

                String[] octetos = ip.split("\\.");
                int[] ipNums = new int[4];
                for (int i = 0; i < 4; i++) {
                    ipNums[i] = Integer.parseInt(octetos[i]);
                }

                StringBuilder resultado = new StringBuilder();
                resultado.append("Classe do IP: ").append(obterClasse(ipNums[0])).append("\n");
                long mascaraDecimal = cidrParaMascaraDecimal(cidr);
                resultado.append("Máscara decimal: ").append(decimalParaIP(mascaraDecimal)).append("\n");
                resultado.append("Máscara binária: ").append(decimalParaBinario(mascaraDecimal)).append("\n");
                resultado.append("IPs disponíveis: ").append((int) (Math.pow(2, 32 - cidr) - 2)).append("\n");

                resultado.append("\nInformações de Sub-redes:\n");
                resultado.append(calcularSubredes(ipNums, cidr));

                outputArea.setText(resultado.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: Verifique a entrada. Exemplo correto: 192.168.0.0/24", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

        private String obterClasse(int primeiroOcteto) {
            if (primeiroOcteto >= 0 && primeiroOcteto <= 127) return "A";
            else if (primeiroOcteto >= 128 && primeiroOcteto <= 191) return "B";
            else if (primeiroOcteto >= 192 && primeiroOcteto <= 223) return "C";
            else if (primeiroOcteto >= 224 && primeiroOcteto <= 239) return "D (Multicast)";
            else return "E (Experimental)";
        }

        private long cidrParaMascaraDecimal(int cidr) {
            return (0xFFFFFFFFL << (32 - cidr)) & 0xFFFFFFFFL;
        }

        private String decimalParaIP(long mascara) {
            return String.format("%d.%d.%d.%d",
                    (mascara >>> 24) & 0xFF,
                    (mascara >>> 16) & 0xFF,
                    (mascara >>> 8) & 0xFF,
                    mascara & 0xFF
            );
        }

        private String decimalParaBinario(long mascara) {
            return String.format("%8s.%8s.%8s.%8s",
                    Integer.toBinaryString((int) ((mascara >>> 24) & 0xFF)),
                    Integer.toBinaryString((int) ((mascara >>> 16) & 0xFF)),
                    Integer.toBinaryString((int) ((mascara >>> 8) & 0xFF)),
                    Integer.toBinaryString((int) (mascara & 0xFF))
            ).replace(' ', '0');
        }

        private String calcularSubredes(int[] ip, int cidr) {
            StringBuilder sb = new StringBuilder();

            int totalHosts = (int) (Math.pow(2, 32 - cidr));
            int hostsPorRede = totalHosts - 2;

            int salto = (int) Math.pow(2, 8 - (cidr % 8));

            sb.append("Salto entre redes: ").append(salto).append("\n");
            sb.append("Número de Sub-redes: ").append((256 / salto)).append("\n");
            sb.append("Hosts por Sub-rede: ").append(hostsPorRede).append("\n\n");

            int baseIp = (ip[0] << 24) | (ip[1] << 16) | (ip[2] << 8) | ip[3];

            int quantidadeRedes = 256 / salto;

            for (int i = 0; i < quantidadeRedes; i++) {
                int rede = baseIp + (i * salto);

                int primeiroHost = rede + 1;
                int broadcast = rede + salto - 1;
                int ultimoHost = broadcast - 1;

                sb.append("Rede ").append(i + 1).append(":\n");
                sb.append("IP da rede: ").append(converterParaIP(rede)).append("\n");
                sb.append("Primeiro IP: ").append(converterParaIP(primeiroHost)).append("\n");
                sb.append("Último IP: ").append(converterParaIP(ultimoHost)).append("\n");
                sb.append("Broadcast: ").append(converterParaIP(broadcast)).append("\n\n");
            }

            return sb.toString();
        }

        private String converterParaIP(int valor) {
            return ((valor >> 24) & 0xFF) + "." +
                    ((valor >> 16) & 0xFF) + "." +
                    ((valor >> 8) & 0xFF) + "." +
                    (valor & 0xFF);
        }
    }
}
