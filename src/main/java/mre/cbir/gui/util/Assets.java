package mre.cbir.gui.util;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import mre.cbir.core.util.Nullable;
import mre.cbir.core.util.Precondition;

import javax.swing.Icon;

import java.util.HashMap;
import java.util.Map;

public final class Assets
{
    private static final Map<String, Icon> ICON_CACHE = new HashMap<>();

    private Assets()
    {
    }

    public static Icon icon(final String name)
    {
        Precondition.nonNull(name);
        if (!ICON_CACHE.containsKey(name))
        {
            final var file = ClassLoader.getSystemResourceAsStream("assets/icons/" + name + ".svg");
            Precondition.nonNull(file);

            final var icon = Nullable.value(() -> new FlatSVGIcon(file));
            Precondition.nonNull(icon);

            ICON_CACHE.put(name, icon);
        }

        return ICON_CACHE.get(name);
    }
}
