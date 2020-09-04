package io.supervised.neuralnetwork.builder;

import java.util.ArrayList;
import java.util.List;

import io.data_structures.Tuple;
import io.supervised.neuralnetwork.layers.ConvolutionalLayer;
import io.supervised.neuralnetwork.layers.DenseLayer;
import io.supervised.neuralnetwork.layers.FlattenLayer;
import io.supervised.neuralnetwork.layers.Layer;

public class NetworkBuilder {
  List<Layer> layers = new ArrayList<>();
  ConvLayerBuilder convBuilder = new ConvLayerBuilder();

  public NetworkBuilder() {
    convBuilder.builder = this;
  }

  public NetworkBuilder addDense(int num_of_nodes) {
    if (layers.isEmpty()) {
      // Input Layer
      layers.add(new DenseLayer(num_of_nodes, num_of_nodes));
    } else {
      final var prevLayer = layers.get(layers.size() - 1);
      if (prevLayer instanceof DenseLayer) {
        layers.add(new DenseLayer(
            ((DenseLayer) prevLayer).num_neurons, num_of_nodes));
      } else if (prevLayer instanceof FlattenLayer) {
        layers.add(new DenseLayer(
            ((FlattenLayer) prevLayer).num_neurons, num_of_nodes));
      } else if (prevLayer instanceof ConvolutionalLayer)
        throw new Error(
            "A FlattenLayer must be present between a Conv and a dense.");
    }
    return this;
  }

  public NetworkBuilder addFlattenLayer() throws Exception {
    if (layers.isEmpty()) {
      throw new Exception("A Conv layer must be present");
    } else {
      final var prevLayer = layers.get(layers.size() - 1);
      if (prevLayer instanceof DenseLayer) {
        throw new Exception("FlattenLayer must preceed a convlayer");
      } else if (prevLayer instanceof FlattenLayer) {
        throw new Exception(
            "Flatten Layer cannot be made after another Flatten Layer");
      } else {
        var size = 1;
        for (var i = 0; i < 3; i++) {
          size += size
              * ((ConvolutionalLayer) prevLayer).dim().get(0);
        }
        layers.add(new FlattenLayer(size));
      }
    }
    return this;
  }

  public ConvLayerBuilder addConvLayer() throws Exception {
    var a = new ConvLayerBuilder();
    if (!layers.isEmpty()) {
      var prevLayer = layers.get(layers.size() - 1);
      if (prevLayer instanceof ConvolutionalLayer) {
        var inputSize = ((ConvolutionalLayer) prevLayer).dim();
        a = new ConvLayerBuilder(inputSize);
      } else {
        throw new Exception(
            "Convolutional Layer must follow a convolutional layer");
      }
    }
    convBuilder = a;
    convBuilder.builder = this;
    return convBuilder;
  }

  public ConvLayerBuilder addConvLayer(Tuple<Integer> inputTuple) {
    convBuilder = new ConvLayerBuilder(inputTuple);
    convBuilder.builder = this;
    return convBuilder;
  }

  public Network compile() {
    Layer[] layerArray;
    if (layers.get(0) instanceof DenseLayer) {
      layerArray = new Layer[layers.size() - 1]; // No input layer
      layers.set(0, null);
    } else {
      layerArray = new Layer[layers.size()];
    }
    var j = 0;
    for (var i = 0; i < layers.size(); i++) {
      if (layers.get(i) == null) {
        continue;
      }
      layerArray[j] = layers.get(i);
      j++;
    }
    return new Network(layerArray);
  }
}
