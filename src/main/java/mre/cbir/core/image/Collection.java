package mre.cbir.core.image;

import mre.cbir.core.util.NaturalOrder;
import mre.cbir.core.util.Nullable;
import mre.cbir.core.util.Precondition;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class Collection
{
    private final File            directory;
    private final List<Container> containers;

    public Collection()
    {
        this.directory  = new File("");
        this.containers = Collections.emptyList();
    }

    public Collection(final File directory)
    {
        final var index = new AtomicInteger(0);
        this.directory  = Precondition.nonNull(directory);
        this.containers = Optional.ofNullable(Nullable.value(() -> directory.listFiles()))
                                  .map(List::of)
                                  .orElse(Collections.emptyList())
                                  .stream()
                                  .filter(Objects::nonNull)
                                  .sorted(new NaturalOrder<>())
                                  .map(file -> Container.create(file, index))
                                  .filter(Objects::nonNull)
                                  .toList();
    }

    public int size()
    {
        return containers.size();
    }

    public boolean contains(final Container container)
    {
        final var index = (container != null) ? container.index() : -1;
        final var found = Nullable.value(() -> containers.get(index));
        return found != null && container != null && Objects.equals(container, found);
    }

    public Collection forEach(final Consumer<Container> action)
    {
        containers.forEach(Precondition.nonNull(action));
        return this;
    }

    @Override
    public int hashCode()
    {
        return directory.hashCode();
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)                        return true;
        if (!(object instanceof Collection other)) return false;
        return Objects.equals(this.directory, other.directory);
    }
}
