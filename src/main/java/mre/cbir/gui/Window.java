package mre.cbir.gui;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public final class Window extends JFrame
{
    private final int WINDOW_WIDTH  = 1_000;
    private final int WINDOW_HEIGHT = 1_000;

    public Window()
    {
        this.initFrame();
    }

    private void initFrame()
    {
        this.setTitle("Content Based Image Retrieval System");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
