package tonellishenks;
import java.math.BigInteger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TonelliShanks extends JFrame {

    private JTextField pField, aField;
    private JLabel resultLabel;

    public TonelliShanks() {
        setTitle("Алгоритм Тонелли-Шенкса");
        setSize(750, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2));

        JLabel pLabel = new JLabel("Введите число p:");
        pField = new JTextField();

        JLabel aLabel = new JLabel("Введите число a:");
        aField = new JTextField();

        JButton calculateButton = new JButton("Вычислить");
        resultLabel = new JLabel("Результаты будут отображаться здесь");

        add(pLabel);
        add(pField);
        add(aLabel);
        add(aField);
        add(calculateButton);
        add(resultLabel);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateRoots();
            }
        });
    }

    private void calculateRoots() {
        try {
            BigInteger p = new BigInteger(pField.getText());
            BigInteger a = new BigInteger(aField.getText());

            if (!a.modPow(p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2)), p).equals(BigInteger.ONE)) {
                resultLabel.setText("Нет решения для этих входных данных.");
                return;
            }

            BigInteger[] roots = tonelliShanks(p, a);
            if (roots == null) {
                resultLabel.setText("Решение не найдено.");
            } else {
                resultLabel.setText("Квадратные корни " + a + " по модулю " + p + " равны x1 = " + roots[0] + " и x2 = " + roots[1]);
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Неверный ввод. Пожалуйста, введите корректные числа.");
        }
    }

    private BigInteger[] tonelliShanks(BigInteger p, BigInteger a) {
        BigInteger q = p.subtract(BigInteger.ONE);
        BigInteger ss = BigInteger.ZERO;
        while (q.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            ss = ss.add(BigInteger.ONE);
            q = q.divide(BigInteger.valueOf(2));
        }

        if (ss.equals(BigInteger.ONE)) {
            BigInteger r1 = a.modPow(p.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
            BigInteger r2 = p.subtract(r1);
            return new BigInteger[]{r1, r2};
        }

        BigInteger z = BigInteger.valueOf(2);
        while (!z.modPow(p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2)), p).equals(p.subtract(BigInteger.ONE))) {
            z = z.add(BigInteger.ONE);
        }

        BigInteger c = z.modPow(q, p);
        BigInteger r = a.modPow(q.add(BigInteger.ONE).divide(BigInteger.valueOf(2)), p);
        BigInteger t = a.modPow(q, p);
        BigInteger m = ss;
        BigInteger b, tmp;

        while (!t.equals(BigInteger.ONE)) {
            int i = 0;
            tmp = t;
            while (!tmp.equals(BigInteger.ONE)) {
                tmp = tmp.multiply(tmp).mod(p);
                i++;
            }

            BigInteger exponent = BigInteger.valueOf(2).pow(m.intValue() - i - 1);
            b = c.modPow(exponent, p);
            r = r.multiply(b).mod(p);
            c = b.multiply(b).mod(p);
            t = t.multiply(c).mod(p);
            m = BigInteger.valueOf(i);
        }

        BigInteger r2 = p.subtract(r);
        return new BigInteger[]{r, r2};
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TonelliShanks().setVisible(true);
            }
        });
    }
}
