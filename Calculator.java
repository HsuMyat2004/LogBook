package com.kmd.uog.logbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calculator extends AppCompatActivity {

    private EditText display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        display= findViewById(R.id.txtInput);
        display.setShowSoftInputOnFocus(false);

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getString(R.string.display).equals(display.getText().toString()))
                {
                    display.setText("");
                }
            }
        });
    }

    private void updateText(String strToAdd)
    {
        String oldStr= display.getText().toString();
        int cursorPos= display.getSelectionStart();
        String leftStr= oldStr.substring(0,cursorPos);
        String rightStr= oldStr.substring(cursorPos);

        if(getString(R.string.display).equals(display.getText().toString()))
        {
            display.setText(strToAdd);
            display.setSelection(cursorPos + 1);
        }
        else {
            display.setText(String.format("%s%s%s", leftStr, strToAdd, rightStr));
            display.setSelection(cursorPos + 1);
        }


    }
    public void ZeroBTN(View view)
    {
            updateText("0");
    }

    public void OneBTN(View view)
    {
        updateText("1");
    }
    public void TwoBTN(View view)
    {
        updateText("2");
    }
    public void ThreeBTN(View view)
    {
        updateText("3");
    }
    public void FourBTN(View view)
    {
        updateText("4");
    }
    public void FiveBTN(View view)
    {
        updateText("5");
    }
    public void SixBTN(View view)
    {
        updateText("6");
    }
    public void SevenBTN(View view)
    {
        updateText("7");
    }
    public void EightBTN(View view)
    {
        updateText("8");
    }
    public void NineBTN(View view)
    {
        updateText("9");
    }
    public void PlusBTN(View view)
    {
        updateText("+");
    }
    public void MinusBTN(View view)
    {
        updateText("-");
    }
    public void MultiplyBTN(View view)
    {
        updateText("*");
    }
    public void DivideBTN(View view)
    {
        updateText("/");
    }
    public void HyperBTN(View view)
    {
           int cursorPos= display.getSelectionStart();
           int openHyper= 0;
           int closedHyper = 0;
           int textLen= display.getText().length();

           for(int i=0; i <cursorPos; i++)
            {
                if (display.getText().toString().substring(i,i+1).equals("("))
                {
                    openHyper +=1;
                }
                if (display.getText().toString().substring(i,i+1).equals(")"))
                {
                    closedHyper +=1;
                }
            }
           if(openHyper == closedHyper || display.getText().toString().substring(textLen-1, textLen).equals("("))
           {
               updateText("(");

           }
        else if(closedHyper < openHyper && !display.getText().toString().substring(textLen-1, textLen).equals(")"))
        {
            updateText(")");

        }
        display.setSelection(cursorPos + 1);
    }
    public void ExponentBTN(View view)
    {
        updateText("^");
    }
    public void PlusMinusBTN(View view) {
        String currentText = display.getText().toString();
        int cursorPos = display.getSelectionStart();

        // Check if the current text is empty or only contains a negative sign
        if (currentText.isEmpty() || currentText.equals("-")) {
            // If it's empty or just a negative sign, add a negative sign
            updateText("-");
        } else if (cursorPos == 0) {
            // If the cursor is at the beginning of the text, insert a negative sign at the start
            display.setText("-" + currentText);
            display.setSelection(cursorPos + 1);
        } else {
            // Toggle the sign of the number by removing or adding a negative sign
            String leftStr = currentText.substring(0, cursorPos);
            String rightStr = currentText.substring(cursorPos);
            if (leftStr.startsWith("-")) {
                leftStr = leftStr.substring(1); // Remove the negative sign
            } else {
                leftStr = "-" + leftStr; // Add a negative sign
            }
            display.setText(leftStr + rightStr);
            display.setSelection(cursorPos);
        }
    }

    public void PointBTN(View view)
    {
        updateText(".");
    }

    private void evaluateExpression() {
        String expression = display.getText().toString();

        // Replace "^" with "**" for exponentiation
        expression = expression.replaceAll("\\^", "^");

        try {
            // Evaluate the expression using Java's Math library
            double result = evaluateMathExpression(expression);

            // Display the result in the EditText field
            display.setText(String.valueOf(result));
            display.setSelection(display.getText().length()); // Move the cursor to the end
        } catch (Exception e) {
            // Handle any errors here (e.g., invalid expressions)
            display.setText("Error");
        }
    }

    // Function to evaluate the mathematical expression using Java's Math library
    private double evaluateMathExpression(String expression) {
        // Add any necessary safety checks or validation here
        // For simplicity, we assume the expression is valid for this example

        // Use Java's Math library to calculate the result
        return eval(expression);
    }

    // Recursive function to evaluate the mathematical expression
    private double eval(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') {
                    nextChar();
                }
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) {
                        x += parseTerm();
                    } else if (eat('-')) {
                        x -= parseTerm();
                    } else {
                        return x;
                    }
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) {
                        x *= parseFactor();
                    } else if (eat('/')) {
                        x /= parseFactor();
                    } else {
                        return x;
                    }
                }
            }

            double parseFactor() {
                if (eat('+')) {
                    return parseFactor();
                }
                if (eat('-')) {
                    return -parseFactor();
                }

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') {
                        nextChar();
                    }
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') {
                        nextChar();
                    }
                    String func = expression.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) {
                        x = Math.sqrt(x);
                    } else if (func.equals("sin")) {
                        x = Math.sin(Math.toRadians(x));
                    } else if (func.equals("cos")) {
                        x = Math.cos(Math.toRadians(x));
                    } else if (func.equals("tan")) {
                        x = Math.tan(Math.toRadians(x));
                    } else {
                        throw new RuntimeException("Unknown function: " + func);
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) {
                    x = Math.pow(x, parseFactor());
                }

                return x;
            }
        }.parse();
    }



    public void EqualBTN(View view) {
        evaluateExpression();
    }

    public void BackSpaceBTN(View view)
    {
        int cursorPos= display.getSelectionStart();
        int textLen= display.getText().length();

        if(cursorPos != 0 && textLen !=0)
        {
            SpannableStringBuilder selection= (SpannableStringBuilder) display.getText();
            selection.replace(cursorPos - 1, cursorPos, "");
            display.setText(selection);
            display.setSelection(cursorPos - 1);
        }
    }
    public void ClearBTN(View view)
    {
        display.setText("");
    }
}