package mre.cbir.gui;

import mre.cbir.gui.panel.Menu;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public final class Window extends JFrame
{
    private final int WINDOW_WIDTH  = 1_000;
    private final int WINDOW_HEIGHT = 1_000;

    private Menu menu;

    public Window()
    {
        this.menu = new Menu();

        this.initFrame();
        this.initMenu();
    }

    private void initFrame()
    {
        this.setTitle("Content Based Image Retrieval System");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initMenu()
    {
        menu.addItem    ("File",    "Open Folder");
        menu.addItem    ("File",    "Exit");
        menu.addCheckbox("Options", "Relevance Feedback");
        this.setJMenuBar(menu);
    }
}
