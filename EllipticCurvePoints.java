package tonellishenks;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EllipticCurvePoints extends JFrame {
    private JTextField aField, bField, pField;
    private JTextArea resultArea;

    public EllipticCurvePoints() {
        setTitle("Эллиптические кривые и алгоритм Тонелли-Шенкса");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2));

        JLabel aLabel = new JLabel("Введите a:");
        aField = new JTextField();
        JLabel bLabel = new JLabel("Введите b:");
        bField = new JTextField();
        JLabel pLabel = new JLabel("Введите p:");
        pField = new JTextField();
        JButton calculateButton = new JButton("Вычислить точки");
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculatePoints();
            }
        });

        add(aLabel);
        add(aField);
        add(bLabel);
        add(bField);
        add(pLabel);
        add(pField);
        add(new JLabel());
        add(calculateButton);
        add(new JScrollPane(resultArea));
    }

    private void calculatePoints() {
        try {
            BigInteger a = new BigInteger(aField.getText());
            BigInteger b = new BigInteger(bField.getText());
            BigInteger p = new BigInteger(pField.getText());

            EllipticCurve curve = new EllipticCurve(a, b, p);
            List<EllipticCurve.Point> points = curve.getPoints();
            resultArea.setText("");
            for (EllipticCurve.Point point : points) {
                resultArea.append(point + "\n");
            }
        } catch (Exception e) {
            resultArea.setText("Ошибка ввода данных. Пожалуйста, введите корректные значения.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EllipticCurvePoints().setVisible(true);
            }
        });
    }
}

class EllipticCurve {
    private final BigInteger a;
    private final BigInteger b;
    private final BigInteger p;

    public EllipticCurve(BigInteger a, BigInteger b, BigInteger p) {
        this.a = a;
        this.b = b;
        this.p = p;
    }

    public List<Point> getPoints() {
        List<Point> points = new ArrayList<>();
        for (BigInteger x = BigInteger.ZERO; x.compareTo(p) < 0; x = x.add(BigInteger.ONE)) {
            BigInteger ySquare = x.pow(3).add(a.multiply(x)).add(b).mod(p);
            TonelliShenks.Solution sol = TonelliShenks.ts(ySquare, p);
            if (sol.exists) {
                points.add(new Point(x, sol.root1));
                points.add(new Point(x, sol.root2));
            }
        }
        return points;
    }

    public static class Point {
        private final BigInteger x;
        private final BigInteger y;

        public Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}

class TonelliShenks {
    static Solution ts(BigInteger n, BigInteger p) {
        BigInteger ZERO = BigInteger.ZERO;
        BigInteger ONE = BigInteger.ONE;
        BigInteger TWO = BigInteger.valueOf(2);
        BigInteger FOUR = BigInteger.valueOf(4);

        BiFunction<BigInteger, BigInteger, BigInteger> powModP = (a, e) -> a.modPow(e, p);
        Function<BigInteger, BigInteger> ls = a -> powModP.apply(a, p.subtract(ONE).divide(TWO));

        if (!ls.apply(n).equals(ONE)) return new Solution(ZERO, ZERO, false);

        BigInteger q = p.subtract(ONE);
        BigInteger ss = ZERO;
        while (q.and(ONE).equals(ZERO)) {
            ss = ss.add(ONE);
            q = q.shiftRight(1);
        }

        if (ss.equals(ONE)) {
            BigInteger r1 = powModP.apply(n, p.add(ONE).divide(FOUR));
            return new Solution(r1, p.subtract(r1), true);
        }

        BigInteger z = TWO;
        while (!ls.apply(z).equals(p.subtract(ONE))) z = z.add(ONE);
        BigInteger c = powModP.apply(z, q);
        BigInteger r = powModP.apply(n, q.add(ONE).divide(TWO));
        BigInteger t = powModP.apply(n, q);
        BigInteger m = ss;

        while (true) {
            if (t.equals(ONE)) return new Solution(r, p.subtract(r), true);

            BigInteger i = ZERO;
            BigInteger zz = t;
            while (!zz.equals(BigInteger.ONE) && i.compareTo(m.subtract(ONE)) < 0) {
                zz = zz.multiply(zz).mod(p);
                i = i.add(ONE);
            }
            BigInteger b = c;
            BigInteger e = m.subtract(i).subtract(ONE);
            while (e.compareTo(ZERO) > 0) {
                b = b.multiply(b).mod(p);
                e = e.subtract(ONE);
            }
            r = r.multiply(b).mod(p);
            c = b.multiply(b).mod(p);
            t = t.multiply(c).mod(p);
            m = i;
        }
    }

    static class Solution {
        BigInteger root1;
        BigInteger root2;
        boolean exists;

        Solution(BigInteger root1, BigInteger root2, boolean exists) {
            this.root1 = root1;
            this.root2 = root2;
            this.exists = exists;
        }
    }
}
