package io.supervised.neuralnetwork.builder;

import io.data_structures.Matrix;
import io.supervised.neuralnetwork.layers.Layer;

public class Network {
  Layer[] layers;
  double alpha = 0.1d;

  public Network(final Layer[] layers) {
    this.layers = layers;
  }

  public Double[] feedforward(Double... inputArray) throws Exception {
    var output = new Matrix<>(new Double[][][] { { inputArray } });
    output = Matrix.transpose(output); // Vector Transformation
    output = feedforward(output);
    output = Matrix.transpose(output);
    final var a = output.get(0, 0).toArray();
    final var outputArr = new Double[a.length];
    int i = 0;
    for (var obj : a) {
      outputArr[i] = (Double) obj;
      i++;
    }
    return outputArr;
  }

  public Matrix<Double> feedforward(Matrix<Double> inputs)
      throws Exception {
    var output = inputs.clone();
    for (var layers : getLayers()) {
      output = layers.pass(output);
    }
    return output;
  }

  public void train(Double[] targets, Double[] inputs)
      throws Exception {
    var inputVector = Matrix
        .transpose(new Matrix<>(new Double[][][] { { inputs } }));
    var outputVector = Matrix.transpose(
        new Matrix<>(new Double[][][] { { feedforward(inputs) } }));
    var targetVector = Matrix
        .transpose(new Matrix<>(new Double[][][] { { targets } }));
    var outputErr = Matrix.add(targetVector,
        Matrix.multiply(outputVector, -1)); // E = Y - O;

    var i = getLayers().length - 1;
    var prevLayerOutput = i == 0 ? inputVector
        : getLayers()[i - 1].activatedOutput;

    var hiddenErr = getLayers()[getLayers().length - 1].backPropErr(outputErr,
        prevLayerOutput, alpha);
    if (i == 0)
      return;
    i--;
    for (; i >= 0; i--) {
      prevLayerOutput = i == 0 ? inputVector
          : getLayers()[i - 1].activatedOutput;
      hiddenErr = getLayers()[i].backPropErr(hiddenErr, prevLayerOutput,
          alpha);
    }

  }

  public Layer[] getLayers() {
    return layers;
  }
}
