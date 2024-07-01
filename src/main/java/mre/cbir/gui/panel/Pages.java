package mre.cbir.gui.panel;

import mre.cbir.core.image.Container;
import mre.cbir.core.image.Collection;
import mre.cbir.core.util.Precondition;
import mre.cbir.gui.util.Border;
import mre.cbir.gui.util.Layout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class Pages extends JPanel
{
    private static final int IMAGE_ROW_COUNT = 5;
    private static final int IMAGE_COL_COUNT = 4;
    private static final int IMAGES_PER_PAGE = IMAGE_ROW_COUNT * IMAGE_COL_COUNT;

    private final AtomicInteger currentPage;
    private final int           pageCount;
    private final List<View>    views;

    public Pages(final Collection collection)
    {
        Precondition.nonNull(collection);
        final var size        = (float) (collection.size());
        final var pageCount   = (int) Math.ceil(size /  (float) IMAGES_PER_PAGE);
        final var viewCount   = pageCount * IMAGES_PER_PAGE;
        final var views       = new ArrayList<View>(viewCount);

        collection.forEach(c -> views.add(new View.Filled(c)));
        while (views.size() < viewCount) views.add(new View.Empty());

        this.currentPage = new AtomicInteger(-1);
        this.pageCount   = pageCount;
        this.views       = views;
        this.showPage(0);
    }

    public void showPage(final int page)
    {
        if ((currentPage.get() != page) && validPage(page))
        {
            this.removeAll();
            this.setLayout(Layout.migLayout(IMAGE_ROW_COUNT, IMAGE_COL_COUNT));

            for (var row = 0; row < IMAGE_ROW_COUNT; row++)
            {
                for (var col = 0; col < IMAGE_COL_COUNT; col++)
                {
                    final var index = (page * IMAGES_PER_PAGE) + (col * IMAGE_ROW_COUNT) + row;
                    final var view  = views.get(index);
                    this.add(view, "cell " + row + " " + col + " 1 1");
                }
            }

            currentPage.set(page);
            this.revalidate();
            this.repaint();
        }
    }

    public void showNext()
    {
        showPage(currentPage.get() + 1);
    }

    public void showPrev()
    {
        showPage(currentPage.get() - 1);
    }

    private boolean validPage(final int pageIndex)
    {
        return !views.isEmpty() && (pageIndex >= 0 && pageIndex < pageCount);
    }

    private static abstract class View extends JLabel
    {
        protected abstract void showCheckBox();
        protected abstract void hideCheckBox();

        protected abstract void check();
        protected abstract void uncheck();

        private static final class Filled extends View
        {
            private final Container container;
            private final JButton   button;
            private final JCheckBox checkBox;

            private Filled(final Container container)
            {
                this.container = Precondition.nonNull(container);
                this.button    = new JButton(container.smallIcon());
                this.checkBox  = new JCheckBox();

                final var border = Border.titleBorder(container.name());
                this.setBorder(Border.compoundBorder(border));
                this.setLayout(Layout.gridBagLayout());

                button.setFocusCycleRoot(false);
                button.setMargin(new Insets(5, 5, 5, 5));
                this.add(button);

                checkBox.setVisible(false);
                checkBox.setFocusable(false);
                this.add(checkBox, Layout.gridBagConstraints());
            }

            @Override
            protected void showCheckBox()
            {
                checkBox.setVisible(true);
            }

            @Override
            protected void hideCheckBox()
            {
                checkBox.setVisible(false);
            }

            @Override
            protected void check()
            {
                checkBox.setSelected(true);
                container.relevant(true);
            }

            @Override
            protected void uncheck()
            {
                checkBox.setSelected(false);
                container.relevant(false);
            }
        }

        private static final class Empty extends View
        {
            @Override protected void showCheckBox() {}
            @Override protected void hideCheckBox() {}
            @Override protected void check()        {}
            @Override protected void uncheck()      {}
        }
    }
}
