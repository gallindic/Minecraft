package engineTester;

import org.lwjgl.Sys;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StartPage {
    public static boolean pressed = false;

    public static void StartUp() {

        JFrame frame = new JFrame();

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton play = new JButton("Play");
        JButton exit = new JButton("Exit");
        buttons.setBackground(Color.GRAY);
        play.setSize(100,20);
        buttons.add(play);
        buttons.add(exit);
        ImagePanel panel = new ImagePanel(
                new ImageIcon("assets/textures/background.png").getImage());
        panel.setBackground(Color.green);
        frame.setSize(1024,1024);
        frame.setLocation(200,100);
        frame.setResizable(false);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Minecraft.startGame();
                System.exit(0);
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.add(buttons,BorderLayout.SOUTH);

        while(!pressed){}
    }

    public static void main(String[] args) {
        StartUp();

        System.exit(0);
    }

}

class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(String img) {
        this(new ImageIcon(img).getImage());
    }

    public ImagePanel(Image img) {
        this.img = img;

        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

}