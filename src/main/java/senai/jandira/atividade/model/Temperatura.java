package senai.jandira.atividade.model;

public class Temperatura {

    private double celsius;

    public Temperatura(double celsius) {
        this.celsius = celsius;
    }

    public double getCelsius() {
        return celsius;
    }

    public void setCelsius(double celsius) {
        this.celsius = celsius;
    }

     /**
     Converte de Celsius para Fahrenheit.
     Fórmula: (°C × 9/5) + 32
     */
    public double toFahrenheit() {
        return (celsius * 9.0 / 5.0) + 32.0;
    }

     /**
     Converte de Celsius para Kelvin.
     Fórmula: °C + 273.15
     */
    public double toKelvin() {
        return celsius + 273.15;
    }
}
