package mre.cbir.core.util;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Pattern;

public final class NaturalOrder<T> implements Comparator<T>
{
    private static final String  NUM_REGEX   = "(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)";
    private static final Pattern NUM_PATTERN = Pattern.compile(NUM_REGEX);

    @Override
    public int compare(final T a, final T b)
    {
        final var stringA = (a == null) ? "" : a.toString();
        final var stringB = (b == null) ? "" : b.toString();
        if (Objects.equals(stringA, stringB)) return 0;

        final var segmentA = NUM_PATTERN.split(stringA);
        final var segmentB = NUM_PATTERN.split(stringB);
        final var length = Math.min(segmentA.length, segmentB.length);

        for (int i = 0, result = 0; i < length; i++)
        {
            final var charA = segmentA[i].charAt(0);
            final var charB = segmentB[i].charAt(0);

            // sort numerically
            if (Character.isDigit(charA) && Character.isDigit(charB))
                result = new BigInteger(segmentA[i]).compareTo(new BigInteger(segmentB[i]));

            // sort lexicographically
            if (result == 0) result = segmentA[i].compareTo(segmentB[i]);

            // short circuit if the two strings are no longer equal
            if (result != 0) return result;
        }

        return segmentA.length - segmentB.length;
    }

    @Override
    public int hashCode()
    {
        return NaturalOrder.class.hashCode();
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object) return true;
        return object instanceof NaturalOrder<?>;
    }

    @Override
    public String toString()
    {
        return "NaturalOrder";
    }
}
