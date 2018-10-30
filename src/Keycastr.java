import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Keycastr extends JFrame implements NativeKeyListener, WindowListener {
    private AdvancedJLabel label;
    private Timer timer;
    private int x;
    private int y;

    public Keycastr() {
        GlobalScreen.setEventDispatcher(new SwingDispatchService());
        setUndecorated(true);
        setSize(0, 0);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaltScreen = ge.getDefaultScreenDevice();

        Rectangle rect = defaltScreen.getDefaultConfiguration().getBounds();
        x = (int)rect.getMaxX() - this.getWidth() - 100;
        y = (int)rect.getMaxY() - this.getHeight() - 100;

        setLocation(x, y);
        setBounds(x, y, 0, 0);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(this);
        setVisible(true);
        setAlwaysOnTop(true);
        label = new AdvancedJLabel("Keycastr");
        Font f = new Font("Arial", Font.PLAIN, 20);
        label.setFont(f);

        label.setAlpha(0.5f);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBorder(new EmptyBorder(5, 10, 5, 10));


        getContentPane().add(label);

        timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("");
                setSize(0, 0);
            }
        });

        this.pack();
        timer.start();

    }

    @Override
    public void windowOpened(WindowEvent e) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e1) {
            e1.printStackTrace();
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e1) {
            e1.printStackTrace();
        }
        System.runFinalization();
       System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        timer.restart();
        this.pack();
        setLocation(x, y);
        revalidate();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        timer.restart();
        String text = label.getText();
        text += NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());
//        text += String.valueOf((char)nativeKeyEvent.getRawCode());
        System.out.println(nativeKeyEvent.getRawCode());
        System.out.println(text);
        label.setText(text);
        this.pack();
        setLocation(x, y);
        revalidate();
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        timer.restart();
        this.pack();
        setLocation(x, y);
        revalidate();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Keycastr();
            }
        });
    }
}
class AdvancedJLabel extends JLabel {
    private AlphaComposite cmp;
    private float alpha;

    public AdvancedJLabel() {
        cmp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
        alpha = 1;
    }

    public AdvancedJLabel(String s) {
        cmp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
        alpha = 1;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        if (isVisible()) {
            paintImmediately(getBounds());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        g2d.setComposite(cmp.derive(alpha));
        super.paintComponent(g2d);
    }
}

class KeyEvent {
    public static String getKeyText(int rawKeyCode) {
        switch (rawKeyCode) {
            case 0x45:
                return "esc";

            case 0x50:
                return "f1";

            case 0x51:
                return "f2";

            case 0x52:
                return "f3";

            case 0x53:
                return "f4";

            case 0x54:
                return "f5";

            case 0x55:
                return "f6";

            case 0x56:
                return "f7";

            case 0x57:
                return "f8";

            case 0x58:
                return "f9";

            case 0x59:
                return "f10";

                default:
                return String.valueOf((char)rawKeyCode);
        }
    }
}



