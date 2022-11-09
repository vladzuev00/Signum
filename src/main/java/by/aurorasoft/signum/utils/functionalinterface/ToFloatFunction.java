package by.aurorasoft.signum.utils.functionalinterface;

@FunctionalInterface
public interface ToFloatFunction<T> {
    float apply(T argument);
}
