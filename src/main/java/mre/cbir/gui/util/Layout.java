package mre.cbir.gui.util;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

public final class Layout
{
    private Layout()
    {
    }

    public static BorderLayout borderLayout()
    {
        return new BorderLayout();
    }

    public static String borderLayoutCenter()
    {
        return BorderLayout.CENTER;
    }

    public static String borderLayoutSouth()
    {
        return BorderLayout.SOUTH;
    }

    public static LayoutManager flowLayout()
    {
        return new FlowLayout(FlowLayout.CENTER);
    }

    public static GridBagLayout gridBagLayout()
    {
        return new GridBagLayout();
    }

    public static GridBagConstraints gridBagConstraints()
    {
        final var constraints = new GridBagConstraints();
        constraints.gridx     = 2;
        constraints.anchor    = GridBagConstraints.SOUTHWEST;
        return constraints;
    }

    public static MigLayout migLayout(final int rows, final int columns)
    {
        return new MigLayout("fill,center,hidemode 3",
                             "[grow,fill]".repeat(rows),
                             "[grow,fill]".repeat(columns));
    }
}
