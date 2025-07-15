package com.example.calculadorahp12c;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private EditText display;
    private Stack<Double> stack = new Stack<>(); // Pilha notação polonesa reversa
    private Double n, i, pv, fv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Associa a interface XML à lógica Java

        display = findViewById(R.id.display); // Localiza o TextView da tela para atualizações do número atual
        display.setKeyListener(null);

        setupNumericButtons();
        setupOperatorButtons();
        setupFinancialButtons();
    }

    private void setupNumericButtons() {
        int[] ids = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
                     R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot};

        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            display.append(b.getText());
        };

        for (int id : ids)
            findViewById(id).setOnClickListener(listener);
    }

    private void setupOperatorButtons() {
        findViewById(R.id.btnEnter).setOnClickListener(v -> {
            try {
                double value = Double.parseDouble(display.getText().toString());
                stack.push(value);
                display.setText("");
            } catch (Exception e) {
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
            }
        });
        // Envia a operação selecionada
        findViewById(R.id.btnAdd).setOnClickListener(v -> applyOperation((a, b) -> a + b));
        findViewById(R.id.btnSub).setOnClickListener(v -> applyOperation((a, b) -> a - b));
        findViewById(R.id.btnMul).setOnClickListener(v -> applyOperation((a, b) -> a * b));
        findViewById(R.id.btnDiv).setOnClickListener(v -> applyOperation((a, b) -> a / b));
    }

    private void applyOperation(Operation op) { // efetua a operação
        if (stack.size() < 2) {
            Toast.makeText(this, "Insira dois operandos", Toast.LENGTH_SHORT).show();
            return;
        }
        double b = stack.pop();
        double a = stack.pop();
        double result = op.apply(a, b);
        stack.push(result);
        display.setText(String.valueOf(result));
    }

    private interface Operation {
        double apply(double a, double b); // Como a operação é enviada para ser calculada no applyOperation
    }

    private void setupFinancialButtons() {
        findViewById(R.id.btnN).setOnClickListener(v -> n = getDisplayValue());
        findViewById(R.id.btnI).setOnClickListener(v -> i = getDisplayValue() / 100.0);
        findViewById(R.id.btnPV).setOnClickListener(v -> pv = getDisplayValue());
        findViewById(R.id.btnFV).setOnClickListener(v -> {
            if (n != null && i != null && pv != null) { // Calcula os juros
                fv = pv * Math.pow(1 + i, n);
                display.setText(String.valueOf(fv));
            } else { // Faltou preencher alguma parte da formula
                Toast.makeText(this, "Defina n, i e PV", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Double getDisplayValue() {
        try {
            double val = Double.parseDouble(display.getText().toString()); // Armazena o valor no display
            display.setText("");
            return val;
        } catch (Exception e) {
            Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
