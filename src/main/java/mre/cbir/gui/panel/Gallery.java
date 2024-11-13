package mre.cbir.gui.panel;

import mre.cbir.core.histogram.FeatureMatrix;
import mre.cbir.core.image.Collection;
import mre.cbir.core.image.Container;
import mre.cbir.core.util.Precondition;
import mre.cbir.gui.util.Layout;

import javax.swing.JPanel;
import java.util.function.Consumer;

public final class Gallery extends JPanel
{
    private Pages         pages;
    private Collection    collection;
    private FeatureMatrix featureMatrix;

    public Gallery()
    {
        this.pages         = null;
        this.collection    = null;
        this.featureMatrix = null;
        load(new Collection());
    }

    public void load(final Collection collection)
    {
        this.removeAll();

        this.collection    = Precondition.nonNull(collection);
        this.featureMatrix = new FeatureMatrix(collection);
        this.pages         = new Pages(collection);

        this.setLayout(Layout.borderLayout());
        this.add(pages, Layout.borderLayoutCenter());

        this.revalidate();
        this.repaint();
    }

    public Collection collection()
    {
        return collection;
    }

    public void onViewClick(final Consumer<Pages.View> action)
    {
        Precondition.nonNull(action);
        pages.onViewClick(action);
    }

    public void showPrevPage()
    {
        pages.showPrev();
    }

    public void showNextPage()
    {
        pages.showNext();
    }

    public void showCheckBox()
    {
        pages.showCheckBox();
    }

    public void hideCheckBox()
    {
        pages.hideCheckBox();
    }

    public void reset()
    {
        pages.sort();
        pages.uncheck();
        pages.showPage(0);
    }

    public void sortByRF(final Container source)
    {
        pages.sort(featureMatrix.rfDistanceMatrix(source).comparator(source));
    }

    public void sortByColorCode(final Container source)
    {
        pages.sort(featureMatrix.colorCodeDistanceMatrix().comparator(source));
    }

    public void sortByIntensity(final Container source)
    {
        pages.sort(featureMatrix.intensityDistanceMatrix().comparator(source));
    }
}
