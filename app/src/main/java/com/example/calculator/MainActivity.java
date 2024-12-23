package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private boolean hasError = false;
    private boolean isCalculated=false;
    private char currentSymbol;
    private double firstValue = Double.NaN;
    private double secondValue;
    private TextView inputDisplay, outputDisplay;
    private DecimalFormat decimalFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        decimalFormat = new DecimalFormat("#.##########");
        inputDisplay = findViewById(R.id.input);
        outputDisplay = findViewById(R.id.output);
        inputDisplay.setText("");
        outputDisplay.setText("");

    }

    public void calculateResult() {
        switch (currentSymbol) {
            case '+':
                firstValue += secondValue;
                break;
            case '-':
                firstValue -= secondValue;
                break;
            case 'X':
                firstValue *= secondValue;
                break;
            case '÷':
                if (secondValue != 0) {
                    firstValue /= secondValue;
                } else {
                    outputDisplay.setText("Cannot divide by 0");
                    hasError = true;
                    return;
                }
                break;
            case '%':
                firstValue = firstValue % secondValue;
            default:
                firstValue = secondValue;
                break;
        }
        //outputDisplay.setText(decimalFormat.format(firstValue));
        outputDisplay.setText(String.valueOf(firstValue));

    }

    public void numFunc(View view) {
        resetError();
        Button btn = (Button) view;
        String currText=btn.getText().toString();
        if(isCalculated){
            inputDisplay.setText("");
            outputDisplay.setText("");
            isCalculated=false;
        }
        outputDisplay.append(currText);
    }

    public void operationFunction(View view) {
        if (hasError) return;
        String currText = outputDisplay.getText().toString();
        if (currText.isEmpty()) {
            outputDisplay.setText("Enter a number");
            hasError = true;
            return;
        }
        // Save the first number
        try {
            firstValue = Double.parseDouble(currText);
        } catch (NumberFormatException e) {
            outputDisplay.setText("Invalid Number");
            hasError = true;
            return;
        }
        currentSymbol =((Button) view).getText().toString().charAt(0);
        inputDisplay.setText(currText+currentSymbol);
        // Reset the view after reciving the first number
        outputDisplay.setText(null);
        isCalculated=false;
    }

    public void equalsFunction(View view) {
        String currText = outputDisplay.getText().toString();
        if (currText.isEmpty()||hasError||currentSymbol=='\0') {
            outputDisplay.setText("No number to calaculate");
            hasError = true;
            return;
        }
        String inputDis=inputDisplay.getText().toString();
        if (inputDis.charAt(inputDis.length()-1)=='=') {
            inputDisplay.setText(decimalFormat.format(firstValue) + currentSymbol + decimalFormat.format(secondValue) + "=");
            calculateResult();
            return;
        }

        try {
            secondValue = Double.parseDouble(currText);
            calculateResult();
            inputDisplay.append(currText+'=');
            isCalculated=true;
        } catch (NumberFormatException e) {
            outputDisplay.setText("Invalid Number");
            hasError = true;
            return;
        }
    }
        private void resetError () {
            if (hasError) {
                inputDisplay.setText("");
                outputDisplay.setText("");
                hasError = false;
            }
        }
    public void deleteFunction(View view) {
        firstValue=0;
        secondValue=0;
        currentSymbol='\0';
        inputDisplay.setText("");
        outputDisplay.setText("");
        isCalculated=false;
        resetError();
    }
    public void deleteCharFunction(View view){
        if(hasError||outputDisplay==null)
            return;
        String currText=outputDisplay.getText().toString();
        if(!currText.isEmpty()){
            currText=currText.substring(0,currText.length()-1);
            outputDisplay.setText(currText);
        }
        else{
            outputDisplay.setText("");
        }
    }
}