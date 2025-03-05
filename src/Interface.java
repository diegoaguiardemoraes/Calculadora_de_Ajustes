import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Interface extends Application {

    private TextField campoX;
    private TextField campoY;
    private TextArea resultadoAjuste;
    private List<Double> listaX;
    private List<Double> listaY;
    private Canvas planoCartesiano;  // Canvas para desenhar o gráfico
    private GraphicsContext gc;

    public Interface() {
        listaX = new ArrayList<>();
        listaY = new ArrayList<>();
    }

    @Override
    public void start(Stage primaryStage) {
        // Tamanho da janela
        primaryStage.setTitle("Calculadora de Ajuste");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(600);

        // Criando o GridPane (layout)
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(15);

        // Fonte maior para melhorar a visibilidade
        Font fonteGrande = Font.font("Arial", 22);

        // Labels com fonte maior
        Label labelX = new Label("Valor de X:");
        labelX.setFont(fonteGrande);

        Label labelY = new Label("Valor de Y:");
        labelY.setFont(fonteGrande);

        // Campos de entrada maiores
        campoX = new TextField();
        campoX.setFont(fonteGrande);
        campoX.setPrefWidth(300);

        campoY = new TextField();
        campoY.setFont(fonteGrande);
        campoY.setPrefWidth(300);

        // Botões maiores
        Button enviar = new Button("Enviar");
        enviar.setFont(fonteGrande);
        enviar.setPrefWidth(250);

        Button calcular = new Button("Calcular");
        calcular.setFont(fonteGrande);
        calcular.setPrefWidth(250);

        Button limpar = new Button("Limpar");
        limpar.setFont(fonteGrande);
        limpar.setPrefWidth(250);  // Ajuste no tamanho do botão Limpar

        // Área de texto para mostrar o resultado
        resultadoAjuste = new TextArea();
        resultadoAjuste.setFont(fonteGrande);
        resultadoAjuste.setEditable(false);
        resultadoAjuste.setWrapText(true);
        resultadoAjuste.setPrefWidth(600);
        resultadoAjuste.setPrefHeight(300);

        // Adicionando o Canvas para o gráfico
        planoCartesiano = new Canvas(600, 300);  // Tamanho do plano cartesiano
        gc = planoCartesiano.getGraphicsContext2D();

        // Adicionando os componentes no GridPane
        grid.add(labelX, 0, 0);
        grid.add(campoX, 1, 0);

        grid.add(labelY, 0, 1);
        grid.add(campoY, 1, 1);

        grid.add(enviar, 0, 2);
        grid.add(calcular, 1, 2);

        grid.add(resultadoAjuste, 0, 3, 2, 1);

        grid.add(planoCartesiano, 0, 4, 2, 1); // Colocando o Canvas abaixo dos outros componentes
        grid.add(limpar, 0, 5, 2, 1); // Botão "Limpar" colocado abaixo do gráfico

        // Ação do botão "Enviar"
        enviar.setOnAction(e -> enviarDados());

        // Configuração para capturar o evento de tecla "Enter" nos campos
        campoX.setOnAction(e -> enviarDados());  // Ao pressionar Enter no campoX
        campoY.setOnAction(e -> enviarDados());  // Ao pressionar Enter no campoY

        // Ação do botão "Calcular"
        calcular.setOnAction(e -> {
            if (listaX.isEmpty() || listaY.isEmpty()) {
                showError("Adicione valores antes de calcular!");
                return;
            }

            // Chama a classe Ajustes para calcular os resultados
            String resultado = Main.calcularAjustes(listaX, listaY);
            resultadoAjuste.setText(resultado);

            // Limpa as listas após o cálculo
            desenharPlanoCartesiano();  // Redesenha o gráfico após o cálculo
        });

        // Ação do botão "Limpar" - Limpa os valores de X e Y, e o gráfico
        limpar.setOnAction(e -> {
            listaX.clear();  // Limpa os valores de X
            listaY.clear();  // Limpa os valores de Y
            resultadoAjuste.clear();  // Limpa o resultado do cálculo
            desenharPlanoCartesiano();  // Limpa o gráfico desenhado
        });

        // Carregando o arquivo CSS para a interface
        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    // Método para mostrar mensagem de erro
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para desenhar o plano cartesiano, a linha e os pontos
    private void desenharPlanoCartesiano() {
        // Limpar o canvas
        gc.clearRect(0, 0, planoCartesiano.getWidth(), planoCartesiano.getHeight());

        // Desenhar a grade de fundo com base no espaçamento
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1.5);

        // Definindo o espaçamento das linhas (1 pixel)
        int espacamento = 20;  // Espaçamento entre as linhas da grade

        // Linhas verticais para o eixo X (grade)
        for (int i = 0; i <= (planoCartesiano.getWidth() - 50); i += espacamento) {
            gc.strokeLine(50 + i, 50, 50 + i, planoCartesiano.getHeight() - 50);  // Linhas verticais
        }

        // Linhas horizontais para o eixo Y (grade)
        for (int i = 0; i <= (planoCartesiano.getHeight() - 100); i += espacamento) {
            gc.strokeLine(50, 50 + i, planoCartesiano.getWidth() - 10, 50 + i);  // Linhas horizontais
        }


        // Desenhar eixos X e Y
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(50, planoCartesiano.getHeight() - 50, 590, planoCartesiano.getHeight() - 50);  // Eixo X com comprimento de 400px

        gc.strokeLine(50, 50, 50, planoCartesiano.getHeight() - 50);  // Eixo Y

        // Adicionar setas aos eixo X
        gc.strokeLine(590, planoCartesiano.getHeight() - 50, 580, planoCartesiano.getHeight() - 60);  // Seta superior
        gc.strokeLine(590, planoCartesiano.getHeight() - 50, 580, planoCartesiano.getHeight() - 40);  // Seta inferior

        gc.strokeLine(50, 50, 40, 60);  // Setas do eixo Y (parte esquerda)
        gc.strokeLine(50, 50, 60, 60);  // Setas do eixo Y (parte direita)

        // Adicionar numeração aos eixos X e Y de 1 a 9 no eixo X e de 9 a 1 no eixo Y
        gc.setFont(Font.font(15));

        // Eixo X:
        for (int i = 1; i <= 12; i++) {
            gc.fillText(String.valueOf(i), 46 + i * 40, planoCartesiano.getHeight() - 30);  // Ajustando a posição para o eixo X
        }

        // Eixo Y:
        for (int i = 5; i >= 1; i--) {
            gc.fillText(String.valueOf(i), 10, 95 + (4 - i) * 40);  // Ajuste para o eixo Y
        }

        // Desenhar os pontos (x, y)
        gc.setFill(Color.RED);
        for (int i = 0; i < listaX.size(); i++) {
            double x = listaX.get(i);
            double y = listaY.get(i);

            // Transformar os valores de x e y para caber na tela (normalização)
            double canvasX = 50 + x * 40;  // Ajustando o espaçamento de x para coincidir com os valores no eixo X
            double canvasY = planoCartesiano.getHeight() - 50 - y * 40;  // Ajuste da escala do eixo Y

            // Desenhar o ponto
            gc.fillOval(canvasX - 5, canvasY - 5, 10, 10);  // Desenhando o ponto como um círculo

            // Desenhar a linha conectando os pontos (se houver mais de um ponto)
            if (i > 0) {
                double prevX = listaX.get(i - 1);
                double prevY = listaY.get(i - 1);
                double prevCanvasX = 50 + prevX * 40;
                double prevCanvasY = planoCartesiano.getHeight() - 50 - prevY * 40;

                gc.setStroke(Color.BLUE);  // Cor da linha
                gc.setLineWidth(2);  // Espessura da linha
                gc.strokeLine(prevCanvasX, prevCanvasY, canvasX, canvasY);  // Desenha a linha entre os pontos
            }
        }
    }

        // Método para enviar os dados para as listas
    private void enviarDados() {
        try {
            double x = Double.parseDouble(campoX.getText());
            double y = Double.parseDouble(campoY.getText());
            listaX.add(x);
            listaY.add(y);
            campoX.clear();
            campoY.clear();

            // Redesenha o gráfico com o ponto inserido
            desenharPlanoCartesiano();
        } catch (NumberFormatException ex) {
            showError("Digite valores numéricos válidos!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
