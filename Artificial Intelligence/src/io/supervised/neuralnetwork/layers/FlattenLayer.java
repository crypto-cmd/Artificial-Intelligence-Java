package io.supervised.neuralnetwork.layers;

import io.data_structures.Matrix;

public class FlattenLayer extends Layer {
  final public int num_neurons;

  public FlattenLayer(int num_neurons) {
    this.num_neurons = num_neurons;
  }

  @Override
  public Matrix<Double> pass(Matrix<Double> inputs) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Matrix<Double> backPropErr(Matrix<Double> myErr,
      Matrix<Double> prevLayerOutput, double alpha) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
