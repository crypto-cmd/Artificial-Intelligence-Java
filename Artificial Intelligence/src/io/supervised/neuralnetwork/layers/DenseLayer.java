package io.supervised.neuralnetwork.layers;

import io.data_structures.Matrix;

public class DenseLayer extends Layer {

  Matrix<Double> weights, biases, derivative, error;
  public final int num_neurons;

  public DenseLayer(int num_nodes_prev_layer, int num_of_nodes) {
    weights = new Matrix<>(1, num_of_nodes, num_nodes_prev_layer)
        .map((Object[] o) -> Math.random() * 2 - 1);
    biases = new Matrix<>(1, num_of_nodes, 1);
    biases.fill(0d).map((Object[] o) -> Math.random() * 2 - 1);
    num_neurons = num_of_nodes;
  }

  @Override
  public Matrix<Double> pass(Matrix<Double> inputs) throws Exception {
    output = Matrix.add(Matrix.dot(weights, inputs), biases);
    activatedOutput = activation.f(output);
    derivative = activation.fPrime(output);
    return activatedOutput;
  }

  @Override
  public Matrix<Double> backPropErr(Matrix<Double> myErr,
      Matrix<Double> prevLayerOutput, double alpha) throws Exception {
    var deltaW = Matrix.multiply(myErr, derivative);
    deltaW = Matrix.multiply(deltaW, alpha);
    var deltaB = deltaW.clone();
    deltaW = Matrix.dot(deltaW, Matrix.transpose(prevLayerOutput));
    weights = Matrix.add(deltaW, weights);
    biases = Matrix.add(biases, deltaB);
    return Matrix.dot(Matrix.transpose(deltaW), myErr);
  }
}
