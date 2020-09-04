package io.supervised.neuralnetwork.activations;

public class Sigmoid extends ActivationFunction {

  @Override
  protected Double f(Number x) {
    return 1d / (1 + Math.exp(-(double) x));
  }

  @Override
  protected Double fPrime(Number x) {
    return f(x) * (1 - f(x));
  }
}
