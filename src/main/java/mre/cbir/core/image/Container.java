package mre.cbir.core.image;

import mre.cbir.core.analyzer.PixelConsumer;
import mre.cbir.core.util.Nullable;
import mre.cbir.core.util.Precondition;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class Container
{
    private final File          file;
    private final AtomicInteger index;
    private final AtomicBoolean relevant;
    private final BufferedImage image;
    private final Icon          smallIcon;
    private final Icon          largeIcon;

    private Container(final File           file,
                      final AtomicInteger index,
                      final BufferedImage image)
    {
        this.file      = Precondition.nonNull(file);
        this.index     = Precondition.nonNull(index);
        this.image     = Precondition.nonNull(image);
        this.smallIcon = new ImageIcon(scaled(110, 70));
        this.largeIcon = new ImageIcon(scaled(700, 325));
        this.relevant  = new AtomicBoolean(false);
    }

    public static Container create(final File file, final AtomicInteger index)
    {
        Precondition.nonNull(index);
        final var image = Nullable.value(() -> ImageIO.read(file));
        return (image == null)
             ? null
             : new Container(file, new AtomicInteger(index.getAndIncrement()), image);
    }

    public boolean relevant()
    {
        return relevant.get();
    }

    public boolean relevant(final boolean isRelevant)
    {
        final var previous = relevant();
        relevant.set(isRelevant);
        return previous;
    }

    public int index()
    {
        return index.get();
    }

    public int size()
    {
        return image.getWidth() * image.getHeight();
    }

    public String name()
    {
        return file.getName();
    }

    public Icon smallIcon()
    {
        return smallIcon;
    }

    public Icon largeIcon()
    {
        return largeIcon;
    }

    public Container forEachPixel(final PixelConsumer action)
    {
        Precondition.nonNull(action);
        for (var y = 0; y < image.getHeight(); y++)
            for (var x = 0; x < image.getWidth(); x++)
                action.accept(image.getRGB(x, y));
        return this;
    }

    private Image scaled(final double maxWidth, final double maxHeight)
    {
        Precondition.validArg(maxWidth  > 0, "invalid scaled width");
        Precondition.validArg(maxHeight > 0, "invalid scaled height");

        final var width   = (double) image.getWidth();
        final var height  = (double) image.getHeight();
        final var ratio   = Math.min(maxWidth / width, maxHeight / height);
        final var scaledW = (int) Math.ceil(width  * ratio);
        final var scaledH = (int) Math.ceil(height * ratio);

        return (scaledW > maxWidth || scaledH > maxWidth)
             ? scaled(scaledW, scaledH)
             : image.getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
    }

    @Override
    public int hashCode()
    {
        return file.hashCode();
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)                       return true;
        if (!(object instanceof Container other)) return false;
        return this.file.equals(other.file);
    }
}
