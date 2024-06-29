package mre.cbir.core.histogram;

import mre.cbir.core.analyzer.Analyzer;
import mre.cbir.core.analyzer.ColorCodeAnalyzer;
import mre.cbir.core.analyzer.IntensityAnalyzer;
import mre.cbir.core.image.Collection;
import mre.cbir.core.util.Precondition;

public final class FeatureMatrix
{
    private final Collection  collection;
    private final Histogram[] colorCodeHistograms;
    private final Histogram[] intensityHistograms;

    public FeatureMatrix(final Collection collection)
    {
        this.collection          = Precondition.nonNull(collection);
        this.colorCodeHistograms = Compute.histograms(collection, new ColorCodeAnalyzer());
        this.intensityHistograms = Compute.histograms(collection, new IntensityAnalyzer());
    }

    public Collection collection()
    {
        return collection;
    }

    public Histogram[] colorCodeHistograms()
    {
        return colorCodeHistograms;
    }

    public Histogram[] intensityHistograms()
    {
        return intensityHistograms;
    }

    private static final class Compute
    {
        private static Histogram[] histograms(final Collection collection,
                                              final Analyzer   analyzer)
        {
            Precondition.nonNull(collection);
            Precondition.nonNull(analyzer);

            final var histograms = new Histogram[collection.size()];
            collection.forEach(c -> histograms[c.index()] = new Histogram(c, analyzer));

            return histograms;
        }
    }
}
