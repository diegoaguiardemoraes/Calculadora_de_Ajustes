import java.util.List;
import org.apache.commons.math3.linear.*;


public class Main {
    public static String calcularAjustes(List<Double> listaX, List<Double> listaY) {
        int n = listaX.size();
        if (n == 0) {
            return "Nenhum dado foi inserido para cálculo.";
        }

        double[] x = listaX.stream().mapToDouble(Double::doubleValue).toArray();
        double[] y = listaY.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yAjustado = new double[n];

        // Somatorio Ajusta Parabolico
        double Sx = 0, Sx2 = 0, Sx3 = 0, Sx4 = 0, Sy = 0, Sxy = 0, Sx2y = 0;

        for (int i = 0; i < n; i++) {
            Sx += x[i];
            Sx2 += x[i] * x[i];
            Sx3 += x[i] * x[i] * x[i];
            Sx4 += x[i] * x[i] * x[i] * x[i];
            Sy += y[i];
            Sxy += x[i] * y[i];
            Sx2y += x[i] * x[i] * y[i];
        }
        // matriz A
        double[][] A = {
                {n, Sx, Sx2},
                {Sx, Sx2, Sx3},
                {Sx2, Sx3, Sx4}
        };

        // matriz B
        double[] B = {Sy, Sxy, Sx2y};


        // resolver sistema
        double[] coef = resolverSistema(A, B);

        // yAjustado
        for (int i = 0; i < n; i++) {
            yAjustado[i] = coef[2] * x[i] * x[i] + coef[1] * x[i] + coef[0];
        }

        Qualidade_Ajuste objeto = new Qualidade_Ajuste();
        double parabolico = objeto.calcularR2(y, yAjustado, n);


        // Somatório Ajuste Linear
        double somaX = 0, somaY = 0, somaXY = 0, somaX2 = 0;
        for (int i = 0; i < n; i++) {
            somaX += x[i];
            somaY += y[i];
            somaXY += x[i] * y[i];
            somaX2 += x[i] * x[i];
        }

        double a1_linear = (n * somaXY - somaX * somaY) / (n * somaX2 - somaX * somaX);
        double a0_linear = (somaX2 * somaY - somaX * somaXY) / (n * somaX2 - somaX * somaX);

        // Somatório Ajuste Exponencial
        double soma_x = 0, soma_ln_y = 0, soma_x_ln_y = 0, soma_x2 = 0;

        for (int i = 0; i < n; i++) {
            double ln_y = Math.log(y[i]);
            soma_x += x[i];
            soma_ln_y += ln_y;
            soma_x_ln_y += x[i] * ln_y;
            soma_x2 += x[i] * x[i];
        }

        double a1_exponencial = (n * soma_x_ln_y - soma_x * soma_ln_y) / (n * soma_x2 - soma_x * soma_x);
        double a0_exponencial = Math.exp((soma_ln_y - a1_exponencial * soma_x) / n);

        // Somatório Ajuste Logarítmico
        double soma_ln_x = 0, soma_Y = 0, soma_ln_x_y = 0, soma_ln_x2 = 0;
        for (int i = 0; i < n; i++) {
            double ln_x = Math.log(x[i]);
            soma_ln_x += ln_x;
            soma_Y += y[i];
            soma_ln_x_y += ln_x * y[i];
            soma_ln_x2 += ln_x * ln_x;
        }

        double a1_logaritmico = (n * soma_ln_x_y - soma_ln_x * soma_Y) / (n * soma_ln_x2 - soma_ln_x * soma_ln_x);
        double a0_logaritmico = (soma_Y - a1_logaritmico * soma_ln_x) / n;

        // Somatório Ajuste Potencial
        double soma_ln_X = 0, soma_ln_Y = 0, soma_ln_x_ln_y = 0, soma_ln_x2_potencial = 0;
        for (int i = 0; i < n; i++) {
            double[] ln_x = new double[n];
            double[] ln_y = new double[n];
            ln_x[i] = Math.log(x[i]);
            ln_y[i] = Math.log(y[i]);
            soma_ln_X += ln_x[i];
            soma_ln_Y += ln_y[i];
            soma_ln_x_ln_y += ln_x[i] * ln_y[i];
            soma_ln_x2_potencial += ln_x[i] * ln_x[i];
        }

        double a1_potencial = (n * soma_ln_x_ln_y - soma_ln_X * soma_ln_Y) / (n * soma_ln_x2_potencial - soma_ln_X * soma_ln_X);
        double a0_potencial = (soma_ln_Y - a1_potencial * soma_ln_X) / n;
        //----------------------------------------------------------------------------------------------------------

        //Somatorio Ajuste Hiperbolico
        double soma_1_x = 0, soma_y = 0, soma_1_x_y = 0, soma_1_x2 = 0;

        double[] inv_x = new double[n];
        for (int i = 0; i < n; i++) {
            inv_x[i] = 1.0 / x[i];
            soma_1_x += inv_x[i];
            soma_y += y[i];
            soma_1_x_y += inv_x[i] * y[i];
            soma_1_x2 += inv_x[i] * inv_x[i];
        }

        double a1_hiperbolico = (n * soma_1_x_y - soma_1_x * soma_y) / (n * soma_1_x2 - soma_1_x * soma_1_x);
        double a0_hiperbolico = (soma_y - a1_hiperbolico * soma_1_x) / n;

        // Montar resultado formatado


        // Ajuste Linear
        double[] YaJLinear = new double[n];
        for (int i = 0; i < n; i++) {
            YaJLinear[i] = a1_linear * x[i] + a0_linear;
        }
        double r2_linear = objeto.calcularR2(y, YaJLinear, n);

        // Ajuste Exponencial
        double[] YaJExponencial = new double[n];
        for (int i = 0; i < n; i++) {
            YaJExponencial[i] = a0_exponencial * Math.exp(a1_exponencial * x[i]);
        }
        double r2_exponencial = objeto.calcularR2(y, YaJExponencial, n);

        // Ajuste Logarítmico
        double[] YaJLogaritmico = new double[n];
        for (int i = 0; i < n; i++) {
            YaJLogaritmico[i] = a0_logaritmico + a1_logaritmico * Math.log(x[i]);
        }
        double r2_logaritmico = objeto.calcularR2(y, YaJLogaritmico, n);

        // Ajuste Potencial
        double[] YaJPotencial = new double[n];
        for (int i = 0; i < n; i++) {
            YaJPotencial[i] = Math.exp(a0_potencial) * Math.pow(x[i], a1_potencial);  // Ajuste correto
        }
        double r2_potencial = objeto.calcularR2(y, YaJPotencial, n);

        // Ajuste Hiperbolico
        double[] YaJHiperbolico = new double[n];
        for (int i = 0; i < n; i++) {
            YaJHiperbolico[i] = a0_hiperbolico + a1_hiperbolico / x[i];
        }
        double r2_hiperbolico = objeto.calcularR2(y, YaJHiperbolico, n);

        return String.format(
                "Ajuste Linear: y = %.6f x + %.6f  | R² é: %.6f\n" +
                        "Ajuste Exponencial: y = %.6f * exp(%.6f * x)  | R² é: %.6f\n" +
                        "Ajuste Logarítmico: y = %.6f + %.6f * ln(x)  | R² é: %.6f\n" +
                        "Ajuste Potencial: y = %.6f * x^%.6f  | R² é: %.6f\n" +
                        "Ajuste Hiperbólico: y = %.6f + %.6f / x  | R² = %.6f\n" +
                        "Ajuste Parabólico: y = %.6f x² + %.6f x + %.6f | R² = %.6f",

                a1_linear, a0_linear, r2_linear,
                a0_exponencial, a1_exponencial, r2_exponencial,
                a0_logaritmico, a1_logaritmico, r2_logaritmico,
                Math.exp(a0_potencial), a1_potencial, r2_potencial,
                a0_hiperbolico, a1_hiperbolico, r2_hiperbolico,
                coef[2], coef[1], coef[0], parabolico
        );





    }
    // Ajuste parabolico
    public static double[] resolverSistema ( double[][] A, double[] B){
        // Criando a matriz A
        RealMatrix matrixA = new Array2DRowRealMatrix(A);

        // Criando o vetor B
        RealVector vectorB = new ArrayRealVector(B);

        // Resolvendo o sistema Ax = B
        DecompositionSolver solver = new LUDecomposition(matrixA).getSolver();
        RealVector solution = solver.solve(vectorB);

        return solution.toArray();
    }
}