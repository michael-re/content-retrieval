package mre.cbir.gui.panel;

import mre.cbir.core.histogram.FeatureMatrix;
import mre.cbir.core.image.Collection;
import mre.cbir.core.util.Precondition;

import javax.swing.JPanel;

public final class Gallery extends JPanel
{
    private Collection    collection;
    private FeatureMatrix featureMatrix;

    public Gallery()
    {
        this.collection    = null;
        this.featureMatrix = null;
        load(new Collection());
    }

    public void load(final Collection collection)
    {
        this.removeAll();

        this.collection    = Precondition.nonNull(collection);
        this.featureMatrix = new FeatureMatrix(collection);

        this.revalidate();
        this.repaint();
    }

    public Collection collection()
    {
        return collection;
    }
}
