package io.data_structures;

import java.util.Arrays;
import java.util.Iterator;

public class Array<E> implements Iterable<E> {
  public final int length;
  public Object[] values;

  public Array(int length) {
    this.length = length;
    values = new Object[length];
  }

  @SuppressWarnings("unchecked")
  public E get(int index) {
    return (E) values[index];
  }

  public void set(int index, E element) {
    this.values[index] = element;
  }

  public Array<E> fill(E element) {
    for (var i = 0; i < length; i++)
      set(i, element);
    return this;
  }

  /**
   * 
   * @param fn <{element: T, index:integer }, void>
   */
  public void forEach(Function<Void> fn) {
    for (var i = 0; i < length; i++)
      fn.apply(get(i), i);
  }

  /**
   * 
   * @param <T>
   * @param fn  <{element: E, index:integer}, T>
   * @return
   */
  public <T> Array<T> map(Function<T> fn) {
    var arr = new Array<T>(length);
    for (var i = 0; i < length; i++)
      arr.set(i, fn.apply(get(i), i));
    return arr;
  }

  public static <A, B> Array<A> add(Array<A> a, Array<B> b)
      throws Exception {
    var sum = new Array<A>(a.length);
    if (a.getType().getSuperclass().equals(Number.class)
        && b.getType().getSuperclass().equals(Number.class)) {
      sum.values = a.map((Object[] o) -> {
        var element = ((Number) o[0]).doubleValue();
        var index = (int) o[1];
        var el = ((Number) b.get(index)).doubleValue();
        var value = (element + el);
        return value;
      }).values;

    } else if (a.getType().getSuperclass().equals(String.class)
        && a.getType().getSuperclass().equals(String.class)) {
      sum.values = a.map((Object[] o) -> {
        var element = ((String) o[0]);
        var index = (int) o[1];
        var el = ((String) b.get(index));
        return (element + el);
      }).values;

    } else
      throw new Exception("Addition (+) is undefined for classes "
          + a.get(0).getClass() + " and " + b.get(0).getClass());
    sum.cast(a.getType());
    return sum;
  }

  public <A> void add(Array<A> b) throws Exception {
    this.values = Array.add(this, b).values;
  }

  private void cast(Class<?> cls) {
    for (var i = 0; i < length; i++) {
      if (cls.equals(Double.class)) {
        values[i] = ((Number) (values[i])).doubleValue();
      } else if (cls.equals(Integer.class)) {
        values[i] = ((Number) (values[i])).intValue();
      } else if (cls.equals(Long.class)) {
        values[i] = ((Number) (values[i])).longValue();
      } else if (cls.equals(Byte.class)) {
        values[i] = ((Number) (values[i])).byteValue();
      } else if (cls.equals(Float.class)) {
        values[i] = ((Number) (values[i])).floatValue();
      } else {
        values[i] = cls.cast(values[i]);
      }

    }
  }

  @Override
  public String toString() {
    return Arrays.toString(values);
  }

  @SuppressWarnings("unchecked")
  public Class<? extends Object> getType() {
    // System.out.println(values[0]);
    return ((E) values[0]).getClass();
  }

  @Override
  public Array<E> clone() {
    var clone = new Array<E>(length);

    clone = map((Object[] o) -> {
      @SuppressWarnings("unchecked")
      var element = (E) o[0];
      return element;
    });
    return clone;
  }

  @Override
  public Iterator<E> iterator() {
    Iterator<E> it = new Iterator<E>() {

      private int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex < length && values[currentIndex] != null;
      }

      @Override
      public E next() {
        return get(currentIndex++);
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
    return it;
  }

  public Object[] toArray() {
    return values;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Array) {
      var arr = (Array) obj;
      if (arr.length != length)
        return false;
      for (var i = 0; i < arr.length; i++) {
        if (!arr.get(i).equals(get(i)))
          return false;
      }
      return true;
    }
    return false;
  }

  public static <T> Array<T> from(T[] array) {
    var arr = new Array<T>(array.length)
        .map((Object[] o) -> array[(int) o[1]]);
    return arr;
  }

}
