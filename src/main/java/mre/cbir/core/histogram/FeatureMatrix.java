package mre.cbir.core.histogram;

import mre.cbir.core.analyzer.Analyzer;
import mre.cbir.core.analyzer.ColorCodeAnalyzer;
import mre.cbir.core.analyzer.IntensityAnalyzer;
import mre.cbir.core.image.Collection;
import mre.cbir.core.image.Container;
import mre.cbir.core.util.Precondition;

import java.util.Arrays;

public final class FeatureMatrix
{
    private final Collection     collection;
    private final DistanceMatrix colorCodeDistanceMatrix;
    private final DistanceMatrix intensityDistanceMatrix;
    private final Histogram[]    normalizedFeatureMatrix;
    private final int            normalizedFeatureBinCount;

    public FeatureMatrix(final Collection collection)
    {
        final var colorCode = Compute.histograms(collection, new ColorCodeAnalyzer());
        final var intensity = Compute.histograms(collection, new IntensityAnalyzer());

        this.collection                = Precondition.nonNull(collection);
        this.colorCodeDistanceMatrix   = new DistanceMatrix(collection, colorCode);
        this.intensityDistanceMatrix   = new DistanceMatrix(collection, intensity);
        this.normalizedFeatureMatrix   = Compute.histograms(collection, colorCode, intensity);
        this.normalizedFeatureBinCount = Compute.largestBin(normalizedFeatureMatrix);
    }

    public DistanceMatrix colorCodeDistanceMatrix()
    {
        return colorCodeDistanceMatrix;
    }

    public DistanceMatrix intensityDistanceMatrix()
    {
        return intensityDistanceMatrix;
    }

    public DistanceMatrix rfDistanceMatrix(final Container source)
    {
        final var previous   = Precondition.nonNull(source).relevant(true);
        final var histograms = Arrays.stream(normalizedFeatureMatrix)
                                     .map(Histogram::container)
                                     .filter(Container::relevant)
                                     .map(c -> new Histogram(c, normalizedFeatureMatrix[c.index()]))
                                     .toArray(Histogram[]::new);
        final var weights = new float[normalizedFeatureBinCount];

        // first iteration of rf - equal weights
        if (histograms.length == 1)
        {
            Arrays.fill(weights, 1.0f / normalizedFeatureBinCount);
        }
        else
        {
            final var mean   = Compute.binMean(histograms);
            final var stdev  = Compute.binStdev(mean, histograms);
            final var mindev = 2 / Compute.minStdev(stdev);

            var total = 0.0f;
            for (var i = 0; i < weights.length; i++)
            {
                weights[i] = (stdev[i] != 0) ? (1.0f / stdev[i]) : mindev;
                total     += weights[i];
            }

            // normalize weights
            for (var i = 0; total != 0 && i < weights.length; i++)
                weights[i] /= total;
        }

        source.relevant(previous);
        return new DistanceMatrix(collection, normalizedFeatureMatrix, weights);
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

        private static Histogram[] histograms(final Collection  collection,
                                              final Histogram[] colorCodes,
                                              final Histogram[] intensity)
        {
            Precondition.nonNull(collection);
            Precondition.nonNull(colorCodes);
            Precondition.nonNull(intensity);

            final var normalized = new Histogram[collection.size()];
            collection.forEach(c ->
            {
                final var i   = c.index();
                normalized[i] = new Histogram(c, intensity[i], colorCodes[i]);
                for (var bin = 0; bin < normalized[i].size(); bin++)
                    normalized[i].div(bin, c.size());
            });

            final var mean  = binMean(normalized);
            final var stdev = binStdev(mean, normalized);

            // normalize: v = (v - μ) / σ
            for (var bin = 0; bin < mean.length; bin++)
                for (final var histogram : normalized)
                    histogram.sub(bin, mean[bin])
                             .div(bin, stdev[bin]);

            return normalized;
        }

        private static float[] binMean(final Histogram... histograms)
        {
            Precondition.nonNull(histograms);

            final var mean = new float[largestBin(histograms)];
            for (var i = 0; i < mean.length; i++)
            {
                final var bin = i;
                final var sum = Arrays.stream(histograms)
                                      .map(Precondition::nonNull)
                                      .mapToDouble(h -> h.bin(bin))
                                      .sum();

                final var len = (float) histograms.length;
                mean[i] = (float) (sum / len);
            }

            return mean;
        }

        private static float[] binStdev(final float[]      mean,
                                        final Histogram... histograms)
        {
            Precondition.nonNull(mean);
            Precondition.nonNull(histograms);

            final var stdev = new float[mean.length];
            for (var i = 0; i < stdev.length; i++)
            {
                final var bin = i;
                final var ssd = Arrays.stream(histograms)
                                      .map(Precondition::nonNull)
                                      .mapToDouble(h -> Math.pow(h.bin(bin) - mean[bin], 2))
                                      .sum();

                final var len = histograms.length - 1;
                stdev[i] = (float) Math.sqrt(ssd / len);
            }

            return stdev;
        }

        private static float minStdev(final float[] stdev)
        {
            var min = Float.MAX_VALUE;
            for (final var dev : Precondition.nonNull(stdev))
                if (dev != 0.0f) min = Math.min(min, dev);
            return min;
        }

        private static int largestBin(final Histogram... histograms)
        {
            try
            {
                return Arrays.stream(histograms)
                             .mapToInt(Histogram::size)
                             .max()
                             .getAsInt();
            }
            catch (final Exception _)
            {
                return 0;
            }
        }
    }
}
