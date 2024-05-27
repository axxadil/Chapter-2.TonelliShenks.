package tonellishenks;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LegendreSymbol {

    private JFrame frame;
    private JTextField aField;
    private JTextField pField;
    private JButton calculateButton;
    private JLabel resultLabel;

    public LegendreSymbol() {
        frame = new JFrame("Legendre Symbol");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(4, 2));

        frame.add(new JLabel("Enter value a:"));
        aField = new JTextField();
        frame.add(aField);

        frame.add(new JLabel("Enter prime number p:"));
        pField = new JTextField();
        frame.add(pField);

        calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateLegendreSymbol();
            }
        });
        frame.add(calculateButton);

        resultLabel = new JLabel("Result:");
        frame.add(resultLabel);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void calculateLegendreSymbol() {
        try {
            int a = Integer.parseInt(aField.getText());
            int p = Integer.parseInt(pField.getText());
            int symbol = legendreSymbol(a, p);
            resultLabel.setText("Result: " + symbol);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please, enter valid numeric values.");
        }
    }

    private int legendreSymbol(int a, int p) {
        if (a % p == 0) {
            return 0;
        }
        int ls = power(a, (p - 1) / 2, p);
        return (ls == p - 1) ? -1 : ls;
    }

    private int power(int a, int b, int p) {
        int res = 1;
        a = a % p;
        while (b > 0) {
            if ((b & 1) == 1) {
                res = (res * a) % p;
            }
            b = b >> 1;
            a = (a * a) % p;
        }
        return res;
    }

    public static void main(String[] args) {
        LegendreSymbol app = new LegendreSymbol();
        app.show();
    }
}