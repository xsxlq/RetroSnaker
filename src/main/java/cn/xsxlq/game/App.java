package cn.xsxlq.game;

import cn.xsxlq.game.panel.SnakePanel;

import javax.swing.*;

/**
 * Main App
 */
public class App {
    public static void main( String[] args ) {
        JFrame jFrame = new JFrame("SnakePanel");
        jFrame.setBounds(400,150,1005,810);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakePanel snakePanel = new SnakePanel();
        jFrame.add(snakePanel);
        jFrame.setVisible(true);
    }
}
