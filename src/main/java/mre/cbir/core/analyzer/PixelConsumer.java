package mre.cbir.core.analyzer;

@FunctionalInterface
public interface PixelConsumer
{
    void accept(final int r, final int g, final int b);

    default void accept(final int pixel)
    {
        final var r = ((pixel >> 16) & 0xff);
        final var g = ((pixel >>  8) & 0xff);
        final var b = ((pixel >>  0) & 0xff);
        accept(r, g, b);
    }
}
