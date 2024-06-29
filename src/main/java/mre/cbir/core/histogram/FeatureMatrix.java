package mre.cbir.core.histogram;

import mre.cbir.core.analyzer.Analyzer;
import mre.cbir.core.analyzer.ColorCodeAnalyzer;
import mre.cbir.core.analyzer.IntensityAnalyzer;
import mre.cbir.core.image.Collection;
import mre.cbir.core.util.Precondition;

public final class FeatureMatrix
{
    private final Collection     collection;
    private final DistanceMatrix colorCodeDistanceMatrix;
    private final DistanceMatrix intensityDistanceMatrix;

    public FeatureMatrix(final Collection collection)
    {
        final var colorCode = Compute.histograms(collection, new ColorCodeAnalyzer());
        final var intensity = Compute.histograms(collection, new IntensityAnalyzer());

        this.collection              = Precondition.nonNull(collection);
        this.colorCodeDistanceMatrix = new DistanceMatrix(collection, colorCode);
        this.intensityDistanceMatrix = new DistanceMatrix(collection, intensity);
    }

    public Collection collection()
    {
        return collection;
    }

    public DistanceMatrix colorCodeDistanceMatrix()
    {
        return colorCodeDistanceMatrix;
    }

    public DistanceMatrix intensityDistanceMatrix()
    {
        return intensityDistanceMatrix;
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
