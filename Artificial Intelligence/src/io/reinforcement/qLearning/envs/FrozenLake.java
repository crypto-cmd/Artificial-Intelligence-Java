package io.reinforcement.qLearning.envs;

import io.data_structures.Experience;
import io.reinforcement.qLearning.State;

public class FrozenLake implements Env<Integer> {
  private final int action_space = 4;
  private final int state_space;
  private final int rows = 5;
  private final int cols = 5;
  private final double WIN_REWARD = 100;
  private final double LOSE_REWARD = -1;
  private final double SURVIVAL_REWARD = -0.1;
  private final float SLIPPERINESS = 0.001f;
  private State<Integer> state;
  private int x = 0, y = 0;

  public FrozenLake() {
    setState(new State<>(x, y));
    state_space = rows * cols;
  }

  /**
   * 
   * @param action
   * @return Tuple {currentState:State, nextState:State, reward:double,
   *         isTerminal:boolean}
   */
  @Override
  public Experience<Integer> step(int action) {
    if (Math.random() < SLIPPERINESS) {
      if (Math.random() < 0.5) {
        y += Math.floor(Math.signum(Math.random() * 2 - 1));
      } else {
        x += Math.floor(Math.signum(Math.random() * 2 - 1));
      }
    } else {
      switch (action) {
        case 0: // DOWN
          y++;
          break;
        case 1: // UP
          y--;
          break;
        case 2:// RIGHT
          x++;
          break;
        case 3: // LEFT
          y--;
          break;
        default:
          throw new IllegalArgumentException(
              action + " is an invalid action");
      }
    }
    if (x < 0 || x > cols - 1 || y < 0 || y > rows - 1) {
      return new Experience<Integer>(getState(), null, LOSE_REWARD,
          action, true);
    }
    if (x == cols - 1 && y == rows - 1)
      return new Experience<Integer>(getState(), null, WIN_REWARD,
          action, true);
    var nextState = new State<>(x, y);
    var ex = new Experience<Integer>(getState(), nextState,
        SURVIVAL_REWARD, action, false);
    setState(nextState);
    return ex;
  }

  @Override
  public void reset() {
    x = 0;
    y = 0;
  }

  public int getActionSpace() {
    return action_space;
  }

  public int getStateSpace() {
    return state_space;
  }

  public State<Integer> getState() {
    return state;
  }

  public void setState(State<Integer> state) {
    this.state = state;
  }

  public void render() {
    var s = "";
    for (var i = 0; i < rows; i++) {
      s += "   ";
      for (var j = 0; j < cols; j++) {
        s += ((x == j && y == i) ? "*" : ".") + " ";
      }
      s += "\n";
    }
    System.out.println(s);
  }

}
