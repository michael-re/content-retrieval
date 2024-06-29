package mre.cbir.core.analyzer;

public final class ColorCodeAnalyzer implements Analyzer
{
    public static final int BIN_COUNT = 64;

    @Override
    public int binCount()
    {
        return BIN_COUNT;
    }

    @Override
    public int binPixel(final int r, final int g, final int b)
    {
        return ((r >> 6) << 4)
             | ((g >> 6) << 2)
             | ((b >> 6) << 0);
    }
}
