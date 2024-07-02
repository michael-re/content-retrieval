package mre.cbir.gui.panel;

import mre.cbir.core.image.Container;
import mre.cbir.core.util.Precondition;
import mre.cbir.gui.util.Assets;
import mre.cbir.gui.util.Border;
import mre.cbir.gui.util.Layout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import java.util.HashMap;
import java.util.Map;

public final class Control extends JPanel
{
    private Gallery   gallery;
    private Container current;

    private final Selected selected;
    private final Controls controls;

    public Control(final Gallery gallery)
    {
        this.gallery  = Precondition.nonNull(gallery);
        this.current  = null;
        this.selected = new Selected();
        this.controls = new Controls();

        this.setLayout(Layout.borderLayout());
        this.add(selected, Layout.borderLayoutCenter());
        this.add(controls, Layout.borderLayoutSouth());
    }

    public void load(final Gallery gallery)
    {
        this.gallery = Precondition.nonNull(gallery);
        this.selected.load();
        this.controls.load();
    }

    private final class Selected extends JLabel
    {
        private final JLabel       label  = new JLabel();
        private final TitledBorder border = Border.titleBorder(" ");

        private Selected()
        {
            this.setLayout(Layout.borderLayout());
            this.add(label, Layout.borderLayoutCenter());
            label.setHorizontalAlignment(0);
        }

        private void load()
        {
            reset();
            gallery.onViewClick(view ->
            {
                reset();
                current = Precondition.nonNull(view.container());
                label.setIcon(current.largeIcon());
                border.setTitle(current.name());
            });
        }

        private void reset()
        {
            this.setBorder(border);
            border.setTitle(" ");
            label.setIcon(null);
            current = null;

            this.revalidate();
            this.repaint();
        }
    }

    private final class Controls extends JPanel
    {
        private final Map<String, JButton> buttons = new HashMap<>();

        private void load()
        {
            this.removeAll();

            this.setLayout(Layout.flowLayout());
            this.setBorder(Border.emptyBorder());

            addButton("rf-analysis", "dot-grid")   .addActionListener(_ -> sortByRF());
            addButton("color-code",  "color-code") .addActionListener(_ -> sortByColorCode());
            addButton("intensity",   "intensity")  .addActionListener(_ -> sortByIntensity());
            addButton("reset",       "arrow-reset").addActionListener(_ -> reset());
            addButton("prev",        "arrow-left") .addActionListener(_ -> gallery.showPrevPage());
            addButton("next",        "arrow-right").addActionListener(_ -> gallery.showNextPage());

            buttons.get("prev").setHorizontalTextPosition(JButton.TRAILING);
            buttons.get("next").setHorizontalTextPosition(JButton.LEADING);

            this.revalidate();
            this.repaint();
        }

        private void sortByRF()
        {
            if (current == null)
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select an image to sort collection\nusing the rf-analysis method",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            gallery.sortByRF(current);
        }

        private void sortByColorCode()
        {
            if (current == null)
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select an image to sort collection\nusing the color-code method",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            gallery.sortByColorCode(current);
        }

        private void sortByIntensity()
        {
            if (current == null)
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select an image to sort collection\nusing the intensity method",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            gallery.sortByIntensity(current);
        }

        private void reset()
        {
            gallery.reset();
            selected.reset();
        }

        private JButton addButton(final String name, final String icon)
        {
            if (!buttons.containsKey(name))
            {
                buttons.put(name, new JButton(name));
                buttons.get(name).setIcon(Assets.icon(icon));
                buttons.get(name).setFocusable(false);
            }

            this.add(buttons.get(name));
            return buttons.get(name);
        }
    }
}
