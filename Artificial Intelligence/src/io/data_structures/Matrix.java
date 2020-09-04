package io.data_structures;

public class Matrix<T> {
  private Array<Array<Array<T>>> values;
  private final int depth;
  private final int rows;
  private final int cols;
  private final Tuple<Integer> dimensions;

  public Matrix(T[][][] arrays) {
    setValues(new Array<>(arrays.length));
    var a = 0;
    for (var j = 0; j < arrays.length; j++) {
      var arr = new Array<Array<T>>(arrays[j].length);
      for (var i = 0; i < arr.length; i++) {
        var b = new Array<T>(arrays[j][i].length);
        var row = arrays[j][i];
        for (var col = 0; col < b.length; col++) {
          b.set(col, row[col]);
        }
        arr.set(i, b);
      }
      getValues().set(a, arr);
      a++;
    }
    depth = getValues().length;
    rows = getValues().get(0).length;
    cols = getValues().get(0).get(0).length;
    dimensions = new Tuple<>(depth, rows, cols);

  }

  public Matrix(int depth, int rows, int cols) {
    setValues(new Array<Array<Array<T>>>(depth).fill(null)
        .map((e) -> new Array<Array<T>>(rows).fill(null)
            .map((r) -> new Array<T>(cols).fill(null))));
    this.depth = depth;
    this.rows = rows;
    this.cols = cols;
    dimensions = new Tuple<>(depth, rows, cols);
  }

  public Matrix(int rows, int cols) {
    this.depth = 1;
    this.rows = rows;
    this.cols = cols;
    setValues(new Array<Array<Array<T>>>(1).fill(null)
        .map((e) -> new Array<Array<T>>(rows).fill(null)
            .map((r) -> new Array<T>(cols).fill(null))));
    dimensions = new Tuple<>(depth, rows, cols);
  }

  public T get(int depth, int row, int col) {
    return getValues().get(depth).get(row).get(col);
  }

  public Array<T> get(int depth, int row) {
    return getValues().get(depth).get(row);
  }

  public Array<Array<T>> get(int depth) {
    return getValues().get(depth);
  }

  public void set(int depth, int row, int col, T a) {
    getValues().get(depth).get(row).set(col, a);
  }

  public void set(int depth, int row, Array<T> a) {
    getValues().get(depth).set(row, a);
  }

  public Tuple<Integer> dim() {
    return dimensions;
  }

  public void set(int depth, Array<Array<T>> a) {
    getValues().set(depth, a);
  }

  public static <A extends Number> Matrix<A> dot(Matrix<A> a,
      Matrix<A> b) throws Exception {
    if (a.getDepth() != b.getDepth())
      throw new Exception(
          "Matrix A must have the same depth as Matrix B");
    if (a.getCols() != b.getRows())
      throw new Exception(
          "Matrix A must have the cols as B has rows");
    var product = new Matrix<A>(a.getDepth(), a.getRows(),
        b.getCols());

    for (var depth = 0; depth < product.getDepth(); depth++) {
      for (var row = 0; row < product.getRows(); row++) {
        for (var col = 0; col < product.getCols(); col++) {
          Double sum = 0d;
          for (var k = 0; k < a.getCols(); k++) {
            sum += a.get(depth, row, k).doubleValue()
                * b.get(depth, k, col).doubleValue();
          }
          product.getValues().get(depth).get(row).set(col, (A) sum);
        }
      }
    }
    return product;
  }

  public static <A, B> Matrix<A> add(Matrix<A> a, Matrix<B> b)
      throws Exception {
    if (a.getDepth() != b.getDepth() || a.getRows() != b.getRows()
        || a.getCols() != b.getCols())
      throw new Exception(
          "Matrix a must have similar dimensions to matrix b");
    var sum = a.clone();
    for (var depth = 0; depth < a.getDepth(); depth++) {
      for (var row = 0; row < b.getRows(); row++) {
        sum.getValues().get(depth).get(row)
            .add(b.getValues().get(depth).get(row));
      }
    }
    return sum;
  }

  public static <T> Matrix<T> transpose(Matrix<T> a) {
    var transposed = new Matrix<T>(a.getDepth(), a.getCols(),
        a.getRows());
    for (var depth = 0; depth < a.getDepth(); depth++) {
      for (var row = 0; row < a.getRows(); row++) {
        for (var col = 0; col < a.getCols(); col++) {
          var element = a.get(depth, row, col);
          transposed.getValues().get(depth).get(col).set(row,
              element);
        }
      }
    }
    return transposed;
  }

  @SuppressWarnings("unchecked")
  public static <A extends Number, B extends Number> Matrix<A> multiply(
      Matrix<A> a, Matrix<B> b) throws Exception {

    if (a.getCols() != b.getCols() || a.getRows() != b.getRows()
        || a.getDepth() != b.getDepth())
      throw new Exception(
          "Matrix A and B must have matching dimensions");
    var product = new Matrix<A>(a.getDepth(), a.getRows(),
        b.getCols());
    for (var depth = 0; depth < product.getDepth(); depth++) {
      for (var row = 0; row < product.getRows(); row++) {
        for (var col = 0; col < product.getCols(); col++) {
          Double sum = a.get(depth, row, col).doubleValue()
              + b.get(depth, row, col).doubleValue();
          product.getValues().get(depth).get(row).set(col, (A) sum);
        }
      }
    }
    return product;

  }

  @SuppressWarnings("unchecked")
  public static <A extends Number> Matrix<A> multiply(
      Matrix<A> matrix, Number factor) {
    return (Matrix<A>) matrix.clone()
        .map((Object[] o) -> ((Number) o[0]).doubleValue()
            * factor.doubleValue());
  }

  public Matrix<T> fill(T element) {
    for (var matrix : getValues()) {
      for (var arr : matrix) {
        arr.fill(element);
      }
    }
    return this;
  }

  @Override
  public String toString() {
    var s = "[";
    for (var matrix : getValues()) {
      s += "\n  [";
      for (var i = 0; i < matrix.length; i++) {
        var arr = matrix.get(i).toString();
        s += arr;
        if (i != matrix.length - 1) {
          s += "\n   ";
        }
      }
      s += "]\n";
    }
    s += "]";
    return s;
  }

  /**
   * 
   * @param <A>
   * @param fn  <{element: T, depth: integer, row: integer, column: integer}, A>
   * @return
   */
  public <A> Matrix<A> map(Function<A> fn) {
    var matrix = new Matrix<A>(getDepth(), getRows(), getCols());

    for (var depth = 0; depth < this.getDepth(); depth++) {
      for (var row = 0; row < this.getRows(); row++) {
        for (var col = 0; col < this.getCols(); col++) {
          final var t = this.getValues().get(depth).get(row).get(col);
          matrix.getValues().get(depth).get(row).set(col,
              fn.apply(t, depth, col, row));
        }
      }
    }
    return matrix;
  }

  @Override
  public Matrix<T> clone() {
    var clone = new Matrix<T>(getDepth(), getRows(), getCols());
    for (var depth = 0; depth < this.getDepth(); depth++) {
      for (var row = 0; row < this.getRows(); row++) {
        for (var col = 0; col < this.getCols(); col++) {
          final var v = this.getValues().get(depth).get(row).get(col);
          clone.getValues().get(depth).get(row).set(col, v);
        }
      }
    }
    return clone;
  }

  public int getDepth() {
    return depth;
  }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }

  public Array<Array<Array<T>>> getValues() {
    return values;
  }

  public void setValues(Array<Array<Array<T>>> values) {
    this.values = values;
  }
}
