package io.reinforcement.qLearning.tabular;

import io.data_structures.Array;
import io.data_structures.Matrix;
import io.reinforcement.qLearning.QAgent;
import io.reinforcement.qLearning.State;

public class QMatrix<T> implements QAgent {
  final Matrix<StateActionPair<T>> qTable;
  private int currentStateCounter = 0;
  private final int action_space;
  private final int state_space;
  private double epsilon = 1d;
  private final double epsilonMin = 0.1d;
  private double alpha = 0.1;
  private double gamma = 0.999;

  public QMatrix(int num_states, int num_actions) {
    state_space = num_states;
    action_space = num_actions;
    qTable = new Matrix<>(num_states, 1);
  }

  public void setState(State<T> s) {
    final var actions = new Array<Double>(action_space).fill(0d);
    qTable.set(0, currentStateCounter, 0,
        new StateActionPair<>(s, actions));
    currentStateCounter++;
  }

  public int getAction(State<T> s) throws Exception {
    var actions = getQ(s);

    if (Math.random() < getEpsilon()) {
      return (int) Math.floor(Math.random() * actions.length);
    }
    return getBestAction(s);

  }

  public int getBestAction(State<T> s) throws Exception {
    var actions = getQ(s);
    var maxIndex = 0;
    for (var i = 0; i < actions.length; i++) {
      maxIndex = actions.get(i) > actions.get(maxIndex) ? i
          : maxIndex;
    }
    return maxIndex;
  }

  public Array<Double> getQ(State<T> s) throws Exception {
    var index = find(s);
    if (index == -1)
      throw new Exception("State " + s + " was not found in QMatrix");
    return qTable.get(0, index, 0).getActions();
  }

  public int find(State<T> s) {
    for (var i = 0; i < currentStateCounter; i++) {
      if (qTable.get(0, i, 0).isState(s))
        return i;
    }
    return -1;

  }

  public boolean containsState(State<T> s) {
    return find(s) != -1;
  }

  @Override
  public String toString() {
    return qTable.toString();
  }

  public void decay(double decay) {
    if (epsilon <= epsilonMin) {
      epsilon = epsilonMin;
      return;
    }
    setEpsilon(getEpsilon() * decay);
  }

  @Override
  public void updateQ(State s, State sPrime, int action,
      double reward, boolean isTerminal) {
    int index = find(s);
    if (isTerminal) {
      qTable.get(0, index, 0).getActions().set(action, reward);
    } else {
      var maxQ = Double.NEGATIVE_INFINITY;
      var sPrimeIndex = find(sPrime);
      if (sPrimeIndex == -1) {
        setState(sPrime);
        sPrimeIndex = find(sPrime);
      }
      var nextQ = qTable.get(0, sPrimeIndex, 0);
      for (var i = 0; i < nextQ.getActions().length; i++) {
        var q = nextQ.getActions().get(i);
        if (q.isNaN())
          continue;
        maxQ = Math.max(maxQ, q);
      }
      final var target = reward + (gamma * maxQ);
      final var currentQ = qTable.get(0, index, 0).getActions()
          .get(action);
      final var q = currentQ + (alpha * (target - currentQ));
      qTable.get(0, index, 0).getActions().set(action, q);
    }
  }

  /**
   * @return the epsilon
   */
  public double getEpsilon() {
    return epsilon;
  }

  /**
   * @param epsilon the epsilon to set
   */
  public void setEpsilon(double epsilon) {
    this.epsilon = epsilon;
  }
}
