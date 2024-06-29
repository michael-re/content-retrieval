package mre.cbir.core.histogram;

import mre.cbir.core.image.Collection;
import mre.cbir.core.image.Container;
import mre.cbir.core.util.Precondition;

import java.util.Comparator;

public final class DistanceMatrix
{
    private final Collection collection;
    private final float[][]  matrix;

    public DistanceMatrix(final Collection  collection,
                          final Histogram[] histograms)
    {
        Precondition.nonNull(collection);
        Precondition.nonNull(histograms);
        Precondition.validArg(collection.size() == histograms.length);

        this.collection = collection;
        this.matrix     = new float[collection.size()][collection.size()];

        for (var i = 0; i < collection.size(); i++)
        {
            for (var j = 0; j <= i; j++)
            {
                final var hi = histograms[i];
                final var hj = histograms[j];
                matrix[i][j] = manhattanDistance(hi, hj);
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    public DistanceMatrix(final Collection  collection,
                          final Histogram[] histograms,
                          final float[]     weights)
    {
        Precondition.nonNull(collection);
        Precondition.nonNull(histograms);
        Precondition.validArg(collection.size() == histograms.length);

        this.collection = collection;
        this.matrix     = new float[collection.size()][collection.size()];

        for (var i = 0; i < collection.size(); i++)
        {
            for (var j = 0; j <= i; j++)
            {
                final var hi = histograms[i];
                final var hj = histograms[j];
                matrix[i][j] = manhattanDistance(hi, hj, weights);
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    public float distance(final Container a, final Container b)
    {
        validContainer(a);
        validContainer(b);
        return matrix[a.index()][b.index()];
    }

    public Comparator<Container> comparator(final Container source)
    {
        return new Comparator<Container>()
        {
            @Override
            public int compare(final Container a, final Container b)
            {
                validContainer(source);
                validContainer(a);
                validContainer(b);

                final var distanceA = distance(source, a);
                final var distanceB = distance(source, b);
                final var distance  = distanceA - distanceB;

                if (distance > 0) return  1;
                if (distance < 0) return -1;
                return 0;
            }
        };
    }

    private void validContainer(final Container container)
    {
        final var error = "container does not belong to this collection";
        Precondition.nonNull(container);
        Precondition.validArg(collection.contains(container), error);
    }

    private static float manhattanDistance(final Histogram a,
                                           final Histogram b)
    {
        Precondition.nonNull(a);
        Precondition.nonNull(b);

        var size     = Math.max(a.size(), b.size());
        var distance = 0.0f;

        for (var i = 0; i < size; i++)
            distance += Math.abs((a.bin(i) / ((float) a.container().size())) -
                                 (b.bin(i) / ((float) b.container().size())));

        return distance;
    }

    private static float manhattanDistance(final Histogram a,
                                           final Histogram b,
                                           final float[]   weights)
    {
        Precondition.nonNull(a);
        Precondition.nonNull(b);

        var size     = Math.max(a.size(), b.size());
        var distance = 0.0f;

        Precondition.validArg(weights.length == size);
        for (var i = 0; i < size; i++)
            distance += weights[i] * Math.abs(a.bin(i) - b.bin(i));

        return distance;
    }
}
