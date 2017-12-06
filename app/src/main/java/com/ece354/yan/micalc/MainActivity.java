package com.ece354.yan.micalc;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Stack;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void performCalculation(View view) {
        EditText op1Text = findViewById(R.id.editText);
        EditText op2Text = findViewById(R.id.editText2);
        EditText Answer = findViewById(R.id.editText3);
        String op1Str = op1Text.getText().toString();
        String op2Str = op2Text.getText().toString();
        //double op1 = Double.parseDouble(op1Str);
        double op1 = evaluate(op1Str);               //expression evaluation
        //double op2 = Double.parseDouble(op2Str);
        double op2 = evaluate(op2Str) ;
        String result;
        Context context = getApplicationContext();

        switch (view.getId()) {
            case R.id.addButton:
                result = Double.toString(op1 + op2);
                Answer.setText(result.toCharArray(), 0, result.length());
                break;
            case R.id.minusButton:
                result = Double.toString(op1 - op2);
                Answer.setText(result.toCharArray(), 0, result.length());
                break;
            case R.id.multiplyButton:
                result = Double.toString(op1 * op2);
                Answer.setText(result.toCharArray(), 0, result.length());
                break;
            case R.id.divideButton:
                if (op2 == 0) {
                    /*new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Alert").setMessage("Cannot divide by zero.")
                            .setPositiveButton("Ok", null).show();*/
                    //CharSequence text = String.valueOf(result) ;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "Cannot divide by zero.", duration);
                    toast.show();
                } else {
                    result = Double.toString(op1 / op2);
                    Answer.setText(result.toCharArray(), 0, result.length());
                    break;
                }
            case R.id.powerButton:
                result = Double.toString((Math.pow(op1, op2)));
                Answer.setText(result.toCharArray(), 0, result.length());
                break;
            case R.id.sqrtButton:
                if (op2 == 2 && op1 < 0) {
                    /*new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Alert").setMessage("Cannot sqrt negative number by 2.")
                            .setPositiveButton("Ok", null).show();*/
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "Cannot sqrt negative number by 2.", duration);
                    toast.show();

                } else {
                    result = Double.toString((Math.pow(op1, 1d / op2)));
                    Answer.setText(result.toCharArray(), 0, result.length());
                    break;
                }
            case R.id.clearButton:
                op1Text.setText("");
                op2Text.setText("");
                break;
            default:
                break;
        }


    }


    public double evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        //char[] tokens = op2.toCharArray();

        // Stack for numbers: 'values'
        Stack<Double> values = new Stack<Double>();

        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++) {
            // Current token is a whitespace, skip it
            if (tokens[i] == ' ')
                continue;

            // Current token is a number, push it to stack for numbers
            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuffer sbuf = new StringBuffer();
                // There may be more than one digits in number
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
                    sbuf.append(tokens[i++]);
                values.push(Double.parseDouble(sbuf.toString()));
            }

            // Current token is an opening brace, push it to 'ops'
            else if (tokens[i] == '(')
                ops.push(tokens[i]);

                // Closing brace encountered, solve entire brace
            else if (tokens[i] == ')') {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.pop();
            }

            // Current token is an operator.
            else if (tokens[i] == '+' || tokens[i] == '-' ||
                    tokens[i] == '*' || tokens[i] == '/') {
                // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));

                // Push current token to 'ops'.
                ops.push(tokens[i]);
            }
        }

        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));

        // Top of 'values' contains result, return it
        return values.pop();
    }

    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    // A utility method to apply an operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public static double applyOp(char op, double b, double a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }


    /* A Java program to evaluate a given expression where tokens are separated
by space.
Test Cases:
	"10 + 2 * 6"		 ---> 22
	"100 * 2 + 12"		 ---> 212
	"100 * ( 2 + 12 )"	 ---> 1400
	"100 * ( 2 + 12 ) / 14" ---> 100
*/}






