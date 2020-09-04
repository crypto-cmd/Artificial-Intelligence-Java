package io.supervised.neuralnetwork.layers;

import io.data_structures.Matrix;
import io.supervised.neuralnetwork.activations.ActivationFunction;
import io.supervised.neuralnetwork.activations.ReLu;

public abstract class Layer {
  public Matrix<Double> activatedOutput, output;
  public ActivationFunction activation = new ReLu();

  public abstract Matrix<Double> pass(Matrix<Double> inputs)
      throws Exception;

  public abstract Matrix<Double> backPropErr(Matrix<Double> myErr,
      Matrix<Double> prevLayerOutput, double alpha) throws Exception;
}
