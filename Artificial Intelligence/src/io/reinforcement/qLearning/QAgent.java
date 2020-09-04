package io.reinforcement.qLearning;

public interface QAgent {

  public void updateQ(State s, State sPrime, int action,
      double reward, boolean isTerminal);

}
