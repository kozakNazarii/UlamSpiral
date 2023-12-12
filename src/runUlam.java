import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class runUlam extends Frame {


    private java.awt.Graphics Graphics;

    public static void main(String[] args) {
        new runUlam();
    }

    Path filePath = Paths.get("primes.bin");

    File file = new File(filePath.toUri());

    int width = 1920;
    int height = 1080;

    public int stepAmount(int width, int height) {
        int totalsteps;
        totalsteps = width * height * 10;
        return totalsteps;
    }
    public runUlam() {
        super();
        this.setVisible(true);
        this.setSize(width, height);

        this.addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        repaint();
                    }
                }
        );
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                dispose();
                System.exit(0);
            }
        });
    }

    public boolean isPrime(int number) {
        if (number == 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++)
            if (number % i == 0) {
                return false;
            }
        return true;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        setBackground(Color.black);
        g.setColor(Color.white);

        drawSpiral(g);

    }

    private void drawSpiral(Graphics g) {
        int rectSize = 1;
        int startX = this.getWidth() / 2;
        int startY = this.getHeight() / 2;
        int direction = 0;
        int totalSteps = stepAmount(width, height);
        int stepToTurn = 1;
        int turnCount = 0;
        int stepToTurnCounter = 1;
        int currentStep = 1;



        for (int i = 0; i < totalSteps; i++) {
            currentStep++;
            stepToTurn--;

            switch (direction) {
                case 0 -> startX += rectSize;
                case 1 -> startY += rectSize;
                case 2 -> startX -= rectSize;
                case 3 -> startY -= rectSize;
            }
            if (stepToTurn == 0) {
                direction = (direction + 3) % 4;
                turnCount++;

                if (turnCount == 2) {
                    stepToTurnCounter++;
                    turnCount = 0;
                }
                stepToTurn = stepToTurnCounter;
            }

            if (isPrime(currentStep)) {
                drawPixel(g, startX, startY, rectSize);
            }
        }

        savePrimesToFile(currentStep);
    }

    private void savePrimesToFile(int currentStep) {
        try {


            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);

            for (int i = 1; i <= currentStep; i++) {
                if (isPrime(i)) {
                    dos.writeInt(i);
                }
            }

            dos.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void printNumbersFromFile(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            DataInputStream dis = new DataInputStream(fis);

            while (dis.available() > 0) {
                int num = dis.readInt();
                System.out.println(num);
            }

            dis.close();
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void drawPixel(Graphics g, int x, int y, int size) {
        g.fillRect(x, y, size, size);
    }

}