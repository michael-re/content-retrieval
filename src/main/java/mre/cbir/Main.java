package mre.cbir;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import mre.cbir.gui.Window;

import javax.swing.SwingUtilities;

public final class Main
{
    public static void main(final String[] args)
    {
        FlatMacLightLaf.setup();
        SwingUtilities.invokeLater(() ->
        {
            final var window = new Window();
            window.setVisible(true);
        });
    }
}
