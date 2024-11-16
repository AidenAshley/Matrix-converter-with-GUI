import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class MatrixMultiplicatonApp extends Application {

    private TextField file1Field;
    private TextField file2Field;
    private TextField sizeField;
    private Label statusLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Matrix Multiplication");

        // GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // File inputs
        Label file1Label = new Label("Matrix File 1:");
        file1Field = new TextField();
        Label file2Label = new Label("Matrix File 2:");
        file2Field = new TextField();
        grid.add(file1Label, 0, 0);
        grid.add(file1Field, 1, 0);
        grid.add(file2Label, 0, 1);
        grid.add(file2Field, 1, 1);

        // Random matrix generation size input
        Label sizeLabel = new Label("Or Enter Matrix Size:");
        sizeField = new TextField();
        grid.add(sizeLabel, 0, 2);
        grid.add(sizeField, 1, 2);

        // Buttons
        Button multiplyButton = new Button("Multiply Matrices");
        multiplyButton.setOnAction(e -> handleMultiply());
        grid.add(multiplyButton, 1, 3);

        // Status label
        statusLabel = new Label();
        grid.add(statusLabel, 0, 4, 2, 1);

        // Set up scene and show
        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleMultiply() {
        String file1 = file1Field.getText();
        String file2 = file2Field.getText();
        String sizeText = sizeField.getText();

        try {
            if (!sizeText.isEmpty()) {
                // Generate random matrices if size is provided
                int size = Integer.parseInt(sizeText);
                generateRandomMatrices(size);
                file1 = "matrix1.txt";
                file2 = "matrix2.txt";
                statusLabel.setText("Random matrices generated and saved to matrix1.txt and matrix2.txt.");
            }

            // Read matrices from files and multiply
            int[][] matrixA = readMatrixFromFile(file1);
            int[][] matrixB = readMatrixFromFile(file2);
            int[][] result = multiplyMatrices(matrixA, matrixB);

            // Write result to file
            writeMatrixToFile(result, "matrix3.txt");
            statusLabel.setText("Matrix multiplication completed and saved to matrix3.txt.");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int[][] readMatrixFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<int[]> rows = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                int[] row = Arrays.stream(tokens).mapToInt(Integer::parseInt).toArray();
                rows.add(row);
            }
            return rows.toArray(new int[0][]);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filename);
        }
    }

    private void writeMatrixToFile(int[][] matrix, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (int[] row : matrix) {
                for (int value : row) {
                    writer.print(value + " ");
                }
                writer.println();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + filename);
        }
    }

    private void generateRandomMatrices(int size) {
        int[][] matrixA = new int[size][size];
        int[][] matrixB = new int[size][size];
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrixA[i][j] = rand.nextInt(10); // Random integers from 0 to 9
                matrixB[i][j] = rand.nextInt(10);
            }
        }

        writeMatrixToFile(matrixA, "matrix1.txt");
        writeMatrixToFile(matrixB, "matrix2.txt");
    }

    private int[][] multiplyMatrices(int[][] a, int[][] b) {
        if (a[0].length != b.length) {
            throw new IllegalArgumentException("Matrix multiplication not possible. Check dimensions.");
        }

        int[][] result = new int[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }
}
