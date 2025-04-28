# 📄 Calculadora de IP - README Técnico
Atividade executada sobre a supervisão do professor Celso.
Parametrôs:
- Feito em 30 minutos
- Script Monolitico devido a ausência de especificações, e dado como orientado na aula, executando o que foi pedido, é facultativa a forma de entrega

## 🛠 Descrição do Projeto

Este projeto consiste em uma aplicação **Swing** em **Java** para **análise de redes IP** baseado em uma entrada no formato `IP/CIDR` (ex.: `192.168.0.0/24`).  
O programa realiza os seguintes cálculos:

- Identifica a **classe** do IP.
- Gera a **máscara de sub-rede** em formato **decimal** e **binário**.
- Calcula a quantidade de **IPs disponíveis**.
- Divide a rede em **sub-redes** e mostra:
  - IP da rede
  - Primeiro IP
  - Último IP
  - Endereço de broadcast

Aqui foi usado um esquema simples de input, output e button:
```java
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
```

---

## 🧠 Lógica e Modelos Matemáticos

### 1. Identificação da Classe do IP

Baseado no **primeiro octeto**:

| Intervalo | Classe | Uso             |
|:----------|:-------|:-----------------|
| 0–127     | A      | Redes grandes    |
| 128–191   | B      | Redes médias     |
| 192–223   | C      | Redes pequenas   |
| 224–239   | D      | Multicast         |
| 240–255   | E      | Experimental     |

**Código:**
```java
private String obterClasse(int primeiroOcteto) {
    if (primeiroOcteto >= 0 && primeiroOcteto <= 127) return "A";
    else if (primeiroOcteto >= 128 && primeiroOcteto <= 191) return "B";
    else if (primeiroOcteto >= 192 && primeiroOcteto <= 223) return "C";
    else if (primeiroOcteto >= 224 && primeiroOcteto <= 239) return "D (Multicast)";
    else return "E (Experimental)";
}

Aqui é basicamente a contagem dentro de um Range, utilizando valores pré-definidos, então um `if-else` dá conta.
```

---

### 2. Cálculo da Máscara de Sub-rede

A partir do **CIDR** informado (`/n`), a máscara é calculada.

> traduzindo, o onjetivo aqui é separar os conjuntos de rede|host, onde se pressupões que inicialmente tudo é rede, e subtrai o conjunto de hosts a serem usados

> Fórmula:
> \[
> \text{Máscara} = (2^{32} - 1) \ll (32 - \text{CIDR})
> \]
> Onde `<<` representa um deslocamento de bits.

**Código:**
```java
private long cidrParaMascaraDecimal(int cidr) {
    return (0xFFFFFFFFL << (32 - cidr)) & 0xFFFFFFFFL;
}
```


**Conversão para notação decimal:**
```java
private String decimalParaIP(long mascara) {
    return String.format("%d.%d.%d.%d",
        (mascara >>> 24) & 0xFF,
        (mascara >>> 16) & 0xFF,
        (mascara >>> 8) & 0xFF,
        mascara & 0xFF
    );
}
```
Traduzindo:
1. O número longo (mascara) é dividido em quatro partes de 8 bits (ou seja, 1 byte cada), que correspondem aos quatro octetos de um endereço IP.
2. O operador de deslocamento lógico à direita (>>>) move os bits para a direita, isolando os blocos de 8 bits desejados.
- (mascara >>> 24) & 0xFF: Obtém os 8 bits mais significativos (primeiro octeto).
- (mascara >>> 16) & 0xFF: Obtém o segundo octeto.
- (mascara >>> 8) & 0xFF: Obtém o terceiro octeto.
- mascara & 0xFF: Obtém os 8 bits menos significativos (quarto octeto).
3. O método String.format organiza esses quatro octetos no formato "x.x.x.x".

**Conversão para notação binária:**
```java
private String decimalParaBinario(long mascara) {
    return String.format("%8s.%8s.%8s.%8s",
        Integer.toBinaryString((int) ((mascara >>> 24) & 0xFF)),
        Integer.toBinaryString((int) ((mascara >>> 16) & 0xFF)),
        Integer.toBinaryString((int) ((mascara >>> 8) & 0xFF)),
        Integer.toBinaryString((int) (mascara & 0xFF))
    ).replace(' ', '0');
}
```
1. Isolamento dos octetos: Usa deslocamento de bits (>>>) para extrair os quatro blocos de 8 bits (1 byte cada) da máscara.
2. Conversão para binário: Utiliza Integer.toBinaryString(int) para transformar cada octeto em uma string binária.
3. Formatação: O método String.format("%8s.%8s.%8s.%8s", ...) coloca os valores binários em grupos de 8 bits separados por pontos.
4. Preenchimento com zeros: Como toBinaryString pode gerar números binários sem os 8 dígitos completos (por exemplo, 101 em vez de 00000101), usamos .replace(' ', '0') para garantir que cada octeto tenha 8 caracteres.
---

### 3. Cálculo de IPs Disponíveis

> Fórmula:
> \[
> \text{IPs disponíveis} = 2^{(32 - \text{CIDR})} - 2
> \]


**Explicação:**  
- Eleva bit a 32 e subtrais do cidr, e assim remove dois itens que seriam rede/broadcast.

**Código:**
```java
resultado.append("IPs disponíveis: ").append((int) (Math.pow(2, 32 - cidr) - 2)).append("\n");
```
---

### 4. Subdivisão em Sub-redes

#### Salto entre redes
> Fórmula:
> \[
> \text{Salto} = 2^{8 - (\text{CIDR} \mod 8)}
> \]

O **mod**, ou **módulo**, é uma operação matemática que calcula o **resto da divisão** de um número por outro. Em outras palavras, quando você divide um número `A` por `B`, o módulo (`A mod B`) retorna o que sobra após a divisão inteira.

No contexto da fórmula:
\[
\text{Salto} = 2^{8 - (\text{CIDR} \mod 8)}
\]
o **módulo (`mod`)** está pegando o valor do CIDR e encontrando o **resto** da divisão por 8. Isso ajuda a determinar quantos bits extras estão sendo usados no último octeto da máscara de sub-rede.

### Exemplo:
- Se **CIDR = 24**, então:
  \[
  24 \mod 8 = 0
  \]
  **Salto** será:
  \[
  2^{8 - 0} = 2^8 = 256
  \]
  O bloco cobre 256 endereços IP.

- Se **CIDR = 26**, então:
  \[
  26 \mod 8 = 2
  \]
  **Salto** será:
  \[
  2^{8 - 2} = 2^6 = 64
  \]
  O bloco cobre apenas 64 endereços IP.

Ou seja, o **mod** nos ajuda a identificar **como a divisão dos endereços muda conforme o CIDR**.

---

#### Cálculo de cada rede

- **IP da rede:** Base + (salto × índice)
- **Primeiro IP:** IP da rede + 1
- **Último IP:** Broadcast - 1
- **Broadcast:** IP da rede + salto - 1

**Código:**
```java
int salto = (int) Math.pow(2, 8 - (cidr % 8));
int quantidadeRedes = 256 / salto;

for (int i = 0; i < quantidadeRedes; i++) {
    int rede = baseIp + (i * salto); 

    int primeiroHost = rede + 1; // Sempre salva a utima rede de referência
    int broadcast = rede + salto - 1;
    int ultimoHost = broadcast - 1;
}
```

---

## Divisão da Rede com Salto

**Exemplo:**
- IP: 192.168.0.0/26
- Salto: 64
- Sub-redes:

| Sub-rede | IP da Rede  | Primeiro IP    | Último IP      | Broadcast      |
|:--------:|:-----------:|:--------------:|:--------------:|:--------------:|
| 1        | 192.168.0.0 | 192.168.0.1    | 192.168.0.62   | 192.168.0.63   |
| 2        | 192.168.0.64| 192.168.0.65   | 192.168.0.126  | 192.168.0.127  |
| ...      | ...         | ...            | ...            | ...            |

