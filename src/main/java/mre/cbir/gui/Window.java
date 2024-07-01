package mre.cbir.gui;

import mre.cbir.core.histogram.FeatureMatrix;
import mre.cbir.core.image.Collection;
import mre.cbir.core.util.Nullable;
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

    private Collection    collection;
    private FeatureMatrix featureMatrix;

    private Menu       menu;

    public Window()
    {
        this.collection = new Collection();
        this.menu       = new Menu();

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

        menu.getItem("Open Folder")       .addActionListener(e -> openAction());
        menu.getItem("Exit")              .addActionListener(e -> exitAction());
        menu.getItem("Relevance Feedback").addActionListener(e -> rfAction());

        this.setJMenuBar(menu);
    }

    private void initPanels()
    {
        this.setLayout(Layout.migLayout(10, 10));
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

            if (Objects.equals(collection.directory(), file))
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

            this.collection    = collection;
            this.featureMatrix = new FeatureMatrix(collection);
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
        System.out.println("rf action");
    }
}
