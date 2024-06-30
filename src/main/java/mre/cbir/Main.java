package mre.cbir;

import mre.cbir.gui.Window;

import javax.swing.SwingUtilities;

public final class Main
{
    public static void main(final String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            final var window = new Window();
            window.setVisible(true);
        });
    }
}
