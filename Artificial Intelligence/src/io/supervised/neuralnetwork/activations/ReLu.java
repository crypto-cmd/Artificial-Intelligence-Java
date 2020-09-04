package io.supervised.neuralnetwork.activations;

public class ReLu extends ActivationFunction {

  @Override
  protected Double f(Number x) {
    return Math.max((double) x, 0d);
  }

  @Override
  protected Double fPrime(Number x) {
    return (double) ((double) x >= 0 ? 1 : 0);
  }

}
