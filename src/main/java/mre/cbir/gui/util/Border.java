package mre.cbir.gui.util;

import javax.swing.BorderFactory;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

public final class Border
{
    public static TitledBorder titleBorder(final String title)
    {
        final var border = BorderFactory.createTitledBorder(title);
        border.setTitlePosition(TitledBorder.BELOW_BOTTOM);
        return border;
    }

    public static CompoundBorder compoundBorder(final javax.swing.border.Border border)
    {
        return new CompoundBorder(border, emptyBorder());
    }

    public static javax.swing.border.Border emptyBorder()
    {
        return BorderFactory.createEmptyBorder();
    }
}
