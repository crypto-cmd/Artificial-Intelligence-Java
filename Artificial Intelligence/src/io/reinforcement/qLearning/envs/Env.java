package io.reinforcement.qLearning.envs;

import io.data_structures.Experience;

public interface Env<T> {
  public void reset();

  public Experience<T> step(int action);
}
