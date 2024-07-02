package mre.cbir.gui.panel;

import mre.cbir.core.util.Precondition;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.util.HashMap;
import java.util.Map;

public final class Menu extends JMenuBar
{
    @FunctionalInterface
    private interface Supplier
    {
        JMenuItem get();
    }

    private final Map<String, JMenu>     menus = new HashMap<>();
    private final Map<String, JMenuItem> items = new HashMap<>();

    public JMenuItem getItem(final String item)
    {
        Precondition.nonNull(item);
        return Precondition.nonNull(items.get(item));
    }

    public JCheckBoxMenuItem getCheckBoxItem(final String item)
    {
        Precondition.nonNull(item);
        return (JCheckBoxMenuItem) getItem(item);
    }

    public JMenuItem addItem(final String menu, final String item)
    {
        Precondition.nonNull(menu);
        Precondition.nonNull(item);
        return getItem(menu, item, JMenuItem::new);
    }

    public JMenuItem addCheckbox(final String menu, final String item)
    {
        Precondition.nonNull(menu);
        Precondition.nonNull(item);
        return getItem(menu, item, JCheckBoxMenuItem::new);
    }

    private JMenuItem getItem(final String   menu,
                              final String   item,
                              final Supplier supplier)
    {
        Precondition.nonNull(menu);
        Precondition.nonNull(item);
        Precondition.nonNull(supplier);

        if (!menus.containsKey(menu))
        {
            menus.put(menu, new JMenu(menu));
            this.add(menus.get(menu));
        }

        if (!items.containsKey(item))
        {
            items.put(item, Precondition.nonNull(supplier.get()));
            items.get(item).setText(item);
            menus.get(menu).add(items.get(item));
        }

        return items.get(item);
    }
}
