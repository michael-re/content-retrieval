package mre.cbir.core.histogram;

import mre.cbir.core.analyzer.Analyzer;
import mre.cbir.core.image.Container;
import mre.cbir.core.util.Precondition;

public final class Histogram
{
    private final Container container;
    private final float[]   bins;

    public Histogram(final Container container, final Analyzer analyzer)
    {
        Precondition.nonNull(container);
        Precondition.nonNull(analyzer);

        this.container = container;
        this.bins      = new float[analyzer.binCount()];

        container.forEachPixel((r, g, b) -> bins[analyzer.binPixel(r, g, b)]++);
    }

    public int size()
    {
        return bins.length;
    }

    public float bin(final int index)
    {
        return (index >= 0) && (index < bins.length)
             ? bins[index]
             : 0.0f;
    }

    public Container container()
    {
        return container;
    }
}
