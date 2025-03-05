public class Qualidade_Ajuste {

    public double calcularR2(double[] y, double[] yAjustado, int n) {

        double somaY = 0, somaY2 = 0, somaDiferenca = 0;

        // Calcula a média de y
        for (double valor : y) {
            somaY += valor;
        }
        double mediaY = somaY / n;

        for (int i = 0; i < n; i++) {
            somaY2 += Math.pow(y[i] - mediaY, 2);
            somaDiferenca += Math.pow(y[i] - yAjustado[i], 2);
        }

        // Cálculo do coeficiente de determinação R²
        return 1 - (somaDiferenca / somaY2);
    }
}