package mre.cbir.core.util;

public class Precondition
{
    private Precondition()
    {
    }

    public static <T> T nonNull(final T value)
    {
        if (value == null)
            throw new NullPointerException();
        return value;
    }
}
