package mre.cbir.gui.panel;

import mre.cbir.core.histogram.FeatureMatrix;
import mre.cbir.core.image.Collection;
import mre.cbir.core.util.Precondition;
import mre.cbir.gui.util.Layout;

import javax.swing.JPanel;

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
}
