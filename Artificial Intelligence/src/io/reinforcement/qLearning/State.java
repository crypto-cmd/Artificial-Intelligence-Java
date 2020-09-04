package io.reinforcement.qLearning;

import io.data_structures.Array;

public class State<T> {
  final private Array<T> properties;

  public State(T... properties) {
    this.properties = Array.from(properties);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof State) {
      var state = (State) obj;
      return state.properties.equals(properties);
    } else
      return false;
  }

  @Override
  public String toString() {
    return properties.toString();
  }
}
