package io.supervised.neuralnetwork.layers;

import java.util.ArrayList;
import java.util.List;

import io.data_structures.Array;
import io.data_structures.Matrix;
import io.data_structures.Tuple;

public class ConvolutionalLayer extends Layer {
  Matrix<Double>[] filters;

  final int stride, filter_size, filter_depth, padding, num_filters;
  final Tuple<Integer> input_size;

  @SuppressWarnings("unchecked")
  public ConvolutionalLayer(int num_filters, int filter_size,
      int filter_depth, int stride, int padding,
      Tuple<Integer> input_Size) {
    output = new Matrix<>(num_filters, 1, 1);
    filters = new Matrix[num_filters];
    this.stride = stride;
    this.filter_size = filter_size;
    this.padding = padding;
    this.num_filters = num_filters;
    this.filter_depth = filter_depth;
    for (var i = 0; i < getFilters().length; i++) {
      getFilters()[i] = new Matrix<Double>(filter_depth, filter_size,
          filter_size).fill(0d)
              .map((Object[] o) -> Math.random() * 2 - 1);
    }
    this.input_size = input_Size;
  }

  public Tuple<Integer> dim() {
    if (input_size != null) {
      final var width = ((input_size.get(2) - filter_size + 2 * 0)
          / stride) + 1;
      final var height = ((input_size.get(1) - filter_size + 2 * 0)
          / stride) + 1;
      final var depth = num_filters;
      return new Tuple<Integer>(depth, height, width);
    }
    var dimensions = output.dim();
    return dimensions;
  }

  @Override
  public Matrix<Double> pass(Matrix<Double> inputs) throws Exception {
    final var width = ((inputs.getCols() - filter_size + 2 * 0)
        / stride) + 1;
    final var height = ((inputs.getRows() - filter_size + 2 * 0)
        / stride) + 1;
    output = new Matrix<>(num_filters, height, width);
    var i = 0;

    for (var filter : getFilters()) {
      Matrix<Double> featureMap = null;
      if (inputs.dim().get(0) > filter_depth) {
        for (var h = 0; h < inputs.getDepth(); h++) {
          var input = inputs.get(h);
          var arr = new Array<Array<Array<Double>>>(1);
          arr.set(0, input);
          var matrix = new Matrix<Double>(arr.length,
              arr.get(0).length, arr.get(0).get(0).length);
          matrix.setValues(arr);
          if (featureMap == null) {
            featureMap = convolve(matrix, filter);
          } else {
            featureMap = Matrix.add(convolve(matrix, filter),
                featureMap);
          }
        }
      } else {
        featureMap = convolve(inputs, filter);
      }
      output.set(i, featureMap.get(0));
      i++;
    }
    activatedOutput = activation.f(output);
    return activatedOutput;
  }

  public Matrix<Double> convolve(Matrix<Double> a, Matrix<Double> b)
      throws Exception {
    // Error checking
    if (a.getDepth() != b.getDepth())
      throw new Exception(
          "Filter and image must have the same depth.");

    // Declare start position (row, column)
    var startRow = -padding;
    var startCol = -padding;

    var endRow = a.getRows() - filter_size + padding;
    var endCol = a.getCols() - filter_size + padding;

    var currentRow = startRow;
    var currentCol = startCol;

    // Matrix<Double> convolvedFeature;
    List<Object[]> total = new ArrayList<>();
    List<Double> list = new ArrayList<>();
    while (currentRow <= endRow) {
      var depthSum = 0d;
      for (var i = 0; i < filter_depth; i++) {
        // Go through each depth.
        var sum = 0d;
        for (var j = 0; j < filter_size; j++) {
          for (var k = 0; k < filter_size; k++) {
            double add1;
            try {
              add1 = a.get(i, currentRow + j, currentCol + k);
            } catch (Exception e) {
              add1 = 0;
            }
            sum += add1 * b.get(i, j, k);
          }
        }
        depthSum += sum;
      }
      list.add(depthSum);
      currentCol += stride;

      if (currentCol > endCol) {
        total.add(list.toArray());
        list.clear();
        currentRow += stride;
        currentCol = startCol;
      }
    }
    var convolvedFeature = new Matrix<Double>(1, total.size(),
        total.get(0).length);
    for (var i = 0; i < total.size(); i++) {
      for (var j = 0; j < total.get(i).length; j++) {
        convolvedFeature.set(0, i, j, (double) total.get(i)[j]);
      }
    }

    return convolvedFeature;
  }

  @Override
  public Matrix<Double> backPropErr(Matrix<Double> myErr,
      Matrix<Double> prevLayerOutput, double alpha) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Matrix<Double>[] getFilters() {
    return filters;
  }

  @Override
  public String toString() {
    var s = "\nConv" + (filter_depth + 1) + "D: { \n\tfilters: "
        + num_filters + " of " + filters[0].dim()
        + ",\n\tActivation: " + activation + " \n}";
    return s;
  }
}
