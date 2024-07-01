package mre.cbir.gui.util;

import net.miginfocom.swing.MigLayout;

public final class Layout
{
    private Layout()
    {
    }

    public static MigLayout migLayout(final int rows, final int columns)
    {
        return new MigLayout("fill,center,hidemode 3",
                             "[grow,fill]".repeat(rows),
                             "[grow,fill]".repeat(columns));
    }
}
