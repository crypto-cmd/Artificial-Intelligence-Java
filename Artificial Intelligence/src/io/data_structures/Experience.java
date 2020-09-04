package io.data_structures;

import io.reinforcement.qLearning.State;

@SuppressWarnings("preview")
public record Experience<T> (State<T> state, State<T> statePrime,
    double reward, int action, boolean isTerminal) {
}