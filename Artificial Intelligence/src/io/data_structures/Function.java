package io.data_structures;

@FunctionalInterface
public interface Function<R> {
  public R apply(Object... objects);
}
