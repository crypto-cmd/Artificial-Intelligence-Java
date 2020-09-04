package io.reinforcement.qLearning.tabular;

import io.data_structures.Array;
import io.reinforcement.qLearning.State;

/**
 * Holds a state and an custom-Array<Double> of the rward for each action
 * 
 * @author Orville Daley
 * @param <S>
 */
public class StateActionPair<S> {
  private State<S> state;
  private Array<Double> actions;

  public StateActionPair(State<S> state, Double[] actions) {
    this.state = state;
    this.actions = new Array<Double>(actions.length);
    this.actions.values = actions.clone();
  }

  public StateActionPair(State<S> state, Array<Double> actions) {
    this.state = state;
    this.actions = actions.clone();

  }

  public boolean isState(State<S> s) {
    return s.equals(getState());
  }

  @Override
  public String toString() {
    return "{State: " + getState().toString() + ", Actions: "
        + getActions().toString() + "}";
  }

  public Array<Double> getActions() {
    return actions;
  }

  public State<S> getState() {
    return state;
  }

}
