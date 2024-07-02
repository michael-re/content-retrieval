package mre.cbir.gui;

import mre.cbir.core.image.Collection;
import mre.cbir.core.util.Nullable;
import mre.cbir.gui.panel.Control;
import mre.cbir.gui.panel.Gallery;
import mre.cbir.gui.panel.Menu;
import mre.cbir.gui.util.Assets;
import mre.cbir.gui.util.Layout;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import java.util.Objects;

public final class Window extends JFrame
{
    private final int WINDOW_WIDTH  = 1_000;
    private final int WINDOW_HEIGHT = 1_000;

    private final Menu    menu;
    private final Control control;
    private final Gallery gallery;

    public Window()
    {
        this.menu    = new Menu();
        this.gallery = new Gallery();
        this.control = new Control(gallery);

        this.initFrame();
        this.initMenu();
        this.initPanels();
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

        menu.getItem("Open Folder").setIcon(Assets.icon("folder-open"));
        menu.getItem("Exit")       .setIcon(Assets.icon("exit"));

        menu.getItem("Open Folder")       .addActionListener(_ -> openAction());
        menu.getItem("Exit")              .addActionListener(_ -> exitAction());
        menu.getItem("Relevance Feedback").addActionListener(_ -> rfAction());

        this.setJMenuBar(menu);
    }

    private void initPanels()
    {
        this.setLayout(Layout.migLayout(10, 10));
        this.getContentPane().add(control, "cell 0 0 10 5");
        this.getContentPane().add(gallery, "cell 0 5 10 5");
    }

    private void openAction()
    {
        final var fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            final var file = fileChooser.getSelectedFile();
            if (file.getAbsolutePath().isEmpty() || !file.exists() || !file.isDirectory())
            {
                JOptionPane.showMessageDialog(
                    this,
                    "No folder selected",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (Objects.equals(gallery.collection().directory(), file))
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Folder already open",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            final var collection = Nullable.value(() -> new Collection(file));
            if (collection == null)
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to open directory",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (collection.size() == 0)
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to find any supported images",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            gallery.load(collection);
            control.load(gallery);
            this.revalidate();
            this.repaint();
            System.gc();
        }
    }

    private void exitAction()
    {
        this.dispose();
        System.exit(0);
    }

    private void rfAction()
    {
        final var item = menu.getCheckBoxItem("Relevance Feedback");
        if (item.isSelected())
            gallery.showCheckBox();
        else
            gallery.hideCheckBox();
    }
}
