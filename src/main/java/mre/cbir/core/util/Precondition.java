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

    public static void validArg(boolean expression)
    {
        validArg(expression, "invalid arg");
    }

    public static void validArg(boolean expression, final String message)
    {
        Precondition.nonNull(message);
        if (!expression)
            throw new IllegalArgumentException(message);
    }
}
