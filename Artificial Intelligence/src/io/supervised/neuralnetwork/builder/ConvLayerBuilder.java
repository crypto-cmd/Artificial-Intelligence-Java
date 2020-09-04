package io.supervised.neuralnetwork.builder;

import io.data_structures.Tuple;
import io.supervised.neuralnetwork.layers.ConvolutionalLayer;

public class ConvLayerBuilder {
  private int num_filters = 8;
  private int filter_size = 3;
  private int filter_depth = 1;
  private int stride = 1;
  private int padding = 0;
  public NetworkBuilder builder;
  private Tuple<Integer> inputSize;

  public ConvLayerBuilder() {
    // TODO Auto-generated constructor stub
  }

  ConvLayerBuilder(Tuple<Integer> input_size) {
    inputSize = input_size;
  }

  public ConvLayerBuilder setPadding(int padding) {
    this.padding = padding;
    return this;
  }

  public ConvLayerBuilder setStride(int stride) {
    this.stride = stride;
    return this;
  }

  public ConvLayerBuilder setFilterSize(int size) {
    this.filter_size = size;
    return this;
  }

  public ConvLayerBuilder setNumFilters(int num) {
    num_filters = num;
    return this;
  }

  public NetworkBuilder compile() {
    builder.layers.add(new ConvolutionalLayer(num_filters,
        filter_size, filter_depth, stride, padding, inputSize));
    return builder;
  }

  public ConvLayerBuilder setFilterDepth(int filter_depth) {
    this.filter_depth = filter_depth;
    return this;
  }
}
