package mre.cbir.core.analyzer;

public final class IntensityAnalyzer implements Analyzer
{
    public static final int   BIN_COUNT   = 25;
    public static final float R_INTENSITY = 0.299f;
    public static final float G_INTENSITY = 0.587f;
    public static final float B_INTENSITY = 0.114f;

    @Override
    public int binCount()
    {
        return BIN_COUNT;
    }

    @Override
    public int binPixel(final int r, final int g, final int b)
    {
        final var intensity = (r * R_INTENSITY)
                            + (g * G_INTENSITY)
                            + (b * B_INTENSITY);
        return Math.min(240, (int) intensity) / 10;
    }
}
