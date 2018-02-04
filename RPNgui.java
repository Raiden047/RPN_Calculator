import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.ArrayList;

class StackArrayList {

	ArrayList<String> arrayList;

	public StackArrayList() {
		arrayList = new ArrayList<String>();
	}

	public String pop() {
		return arrayList.remove(arrayList.size() - 1);
	}

	public void push(String d) {
		arrayList.add(d);
	}

	public char get(int i) {
		return arrayList.get(i).charAt(0);
	}

	public boolean isEmpty() {
		return arrayList.size() == 0;
	}

	public int getSize() {
		return arrayList.size();
	}

	public void displayStack() {
		for (int i = 0; i < arrayList.size(); i++) {
			System.out.println(arrayList.get(i));
		}
	}

}

class RPNcal {

	private String hold1 = "";
	private String hold2 = "";
	StackArrayList stack1 = new StackArrayList();
	StackArrayList stack2 = new StackArrayList();

	public RPNcal(String expression) {
		inFix(expression);
		postFix(hold1);
	}

	public void inFix(String e) {
		for (int i = 0; i < e.length(); i++) {
			if (e.charAt(i) == '(' || e.charAt(i) == ')' || isOperator(e.charAt(i))) {
				if (!stack1.isEmpty()) {
					compare(e.charAt(i));
				} else {
					stack1.push("" + e.charAt(i));
				}

			} else
				hold1 += "" + e.charAt(i);

		}

		for (int i = stack1.getSize() - 1; i >= 0; i--) {
			String str = stack1.pop();
			if (isOperator(str.charAt(0))) {
				hold1 += str;
			}
		}
	}

	public void compare(char i) {

		if (stack1.getSize() == 0) {
			stack1.push("" + i);
		} else {
			char h = stack1.get(stack1.getSize() - 1);

			if (i == '(' || i == ')') {
				if (stack1.get(stack1.getSize() - 2) == '(' && getPri(h) > -1 && i == ')') {
					hold1 += stack1.pop();
					stack1.pop();
				} else
					stack1.push("" + i);
			} else {
				if (getPri(h) == getPri(i)) {
					hold1 += stack1.pop();
					compare(i);
				} else if (getPri(h) > getPri(i)) {
					hold1 += stack1.pop();
					compare(i);
				} else if (getPri(h) < getPri(i)) {
					stack1.push("" + i);
				} else
					stack1.push("" + i);
			}
		}

	}

	public int getPri(char i) {
		if (i == '-' || i == '+')
			return 0;
		else if (i == '%')
			return 1;
		else if (i == '/' || i == '*')
			return 2;
		else
			return -1;
	}

	public void postFix(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!stack2.isEmpty()) {
				stack2.push("" + str.charAt(i));
				if (isOperator(str.charAt(i))) {
					postSolve();
				}
			} else {
				stack2.push("" + str.charAt(i));
			}
		}

		while (!stack2.isEmpty()) {
			for (int i = 0; i < stack2.getSize(); i++) {
				if (isOperator(str.charAt(i))) {
					postSolve();
				}
			}
			if (stack2.getSize() == 1)
				hold2 = stack2.pop();
		}
	}

	public void postSolve() {
		String op = stack2.pop();
		String n2 = stack2.pop();
		String n1 = stack2.pop();
		stack2.push("" + evalExpress(n1, op, n2));
	}

	public int evalExpress(String num1, String op, String num2) {
		int n1 = Integer.parseInt(num1);
		int n2 = Integer.parseInt(num2);
		switch (op) {
		case "+":
			return n1 + n2;
		case "-":
			return n1 - n2;
		case "/":
			return n1 / n2;
		case "*":
			return n1 * n2;
		case "%":
			return n1 % n2;
		default:
			throw new RuntimeException("unknown operator: " + op);
		}
	}

	public boolean isOperator(char i) {
		if (i == '/' || i == '*' || i == '+' || i == '-' || i == '%')
			return true;
		else
			return false;
	}

	public String getRPN() {
		return hold1;
	}

	public String getAns() {
		return hold2;
	}
}

public class RPNgui extends JFrame {

	private JLabel inputL;
	private JTextField inputT;

	private JLabel postL;
	private JTextField postT;

	private JLabel ansL;
	private JTextField ansT;

	private JButton solveB;

	public RPNgui() {

		setLayout(new FlowLayout());

		inputL = new JLabel("Insert Expression: ");
		add(inputL);
		inputT = new JTextField(15);
		add(inputT);

		postL = new JLabel("Reverse Polish Notation: ");
		add(postL);
		postT = new JTextField(15);
		postT.setEditable(false);
		add(postT);

		ansL = new JLabel("Answer: ");
		add(ansL);
		ansT = new JTextField(15);
		ansT.setEditable(false);
		add(ansT);

		solveB = new JButton("Solve");
		add(solveB);
		solveB.addActionListener(new ListenToSolve());

	}

	class ListenToSolve implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String str = inputT.getText();

			RPNcal solve = new RPNcal(str);

			postT.setText(solve.getRPN());

			ansT.setText(solve.getAns());

		}
	}

	public static void main(String[] args) {
		RPNgui gui = new RPNgui();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(200, 220);
		gui.setLocation(500, 200);
		gui.setVisible(true);
		gui.setTitle("RPN");
	}

}
