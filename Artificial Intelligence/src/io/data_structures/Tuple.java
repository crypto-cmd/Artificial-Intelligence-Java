package io.data_structures;

import java.util.Arrays;

/**
 * Immutable Array-Like structure.
 */
public class Tuple<T> {
  final private T[] values;

  public Tuple(T... ts) {
    values = ts;
  }

  public int size() {
    return values.length;
  }

  public T get(int index) {
    return values[index];
  }

  @Override
  public String toString() {
    return Arrays.deepToString(values).replace("[", "(").replace("]",
        ")");
  }

}
