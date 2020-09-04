package io.supervised.neuralnetwork.activations;

import io.data_structures.Array;
import io.data_structures.Matrix;

public abstract class ActivationFunction {
  protected abstract Double f(Number x);

  protected abstract Double fPrime(Number x);

  public Matrix<Double> f(Matrix<? extends Number> x) {
    return x.map((Object[] o) -> f((Number) o[0]));
  }

  public Matrix<Double> fPrime(Matrix<? extends Number> x) {
    return x.map((Object[] o) -> fPrime((Number) o[0]));
  }

  public Array<Double> f(Array<? extends Number> x) {
    return x.map((Object[] o) -> f((Number) o[0]));
  }

  public Array<Double> fPrime(Array<? extends Number> x) {
    return x.map((Object[] o) -> f((Number) o[0]));
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
