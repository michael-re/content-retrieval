package mre.cbir.core.histogram;

import mre.cbir.core.analyzer.Analyzer;
import mre.cbir.core.image.Container;
import mre.cbir.core.util.Precondition;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

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

    public Histogram(final Container container, Histogram... histograms)
    {
        Precondition.nonNull(container);
        Precondition.nonNull(histograms);

        final var desPos = new AtomicInteger();
        final var length = Arrays.stream(histograms)
                                 .map(Precondition::nonNull)
                                 .mapToInt(Histogram::size)
                                 .sum();

        this.container = container;
        this.bins      = new float[length];

        Arrays.stream(histograms)
              .map(Histogram::bins)
              .forEach(bins -> {
                    final var len = bins.length;
                    final var pos = desPos.getAndAdd(len);
                    System.arraycopy(bins, 0, this.bins, pos, len);
              });
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

    private float[] bins()
    {
        return bins;
    }

    public Histogram add(int index, float value)
    {
        if ((index >= 0) && (index < bins.length))
            bins[index] += value;
        return this;
    }

    public Histogram sub(int index, float value)
    {
        if ((index >= 0) && (index < bins.length))
            bins[index] -= value;
        return this;
    }

    public Histogram mul(int index, float value)
    {
        if ((index >= 0) && (index < bins.length))
            bins[index] *= value;
        return this;
    }

    public Histogram div(int index, float value)
    {
        if ((index >= 0) && (index < bins.length) && value != 0)
            bins[index] /= value;
        return this;
    }
}
