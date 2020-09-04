# Artificial-Intelligence-Java

A collection of algorithms including Multi-Layer Perceptron and Tabular QLearning etc....

### Upcoming Updates

-   Convolutional Layer in NeuralNetwork fix.
-   New Activation Functions like TanH
-   Deep Q Learning
-   New Environments for Reinforcement Learning
-   Genetic Algorthm

### Examples

#### Example of creating a simple NeuralNetwork

```Java
var builder = new NetworkBuiler().addDenseLayer(4).addDenseLayer(5).addDenseLayer(4);
var nn = builder.compile();
```

#### Example of getting output from NeuralNetwork

```Java
var input = {0.2d, 0.1d, 0.4d, 0.2d};
var output = nn.feedforward(input);
```

#### Example of using QMatrix for reinforcement learning

```Java
final var MAX_EPS = 1000;
//Crete environment
final var env = new FrozenLake();
//create a QMatrix with state size and action space
final var table = new QMatrix<Integer>(env.getStateSpace(),env.getActionSpace());

for (var i = 0; i < MAX_EPS; i++) {
    //Reset the environment
    env.reset();
    while (true) {
        //Get current state
        var state = env.getState();

        //Look if the qMatrix has seen this state before
        if (!table.containsState(state)) {
            //If it hasn't add it.
          table.setState(state);
        }

        //Get the action
        final var action = table.getAction(state);
        //To get the best action use: table.getBestActions(state)


        //Do action and get Experience
        final var exp = env.step(action);

        //Update the Q Table using the experience
        table.updateQ(exp.state(), exp.statePrime(), exp.action(),
            exp.reward(), exp.isTerminal());

        //Render the environment if desired
        env.render();

        if (exp.isTerminal()) //Is the episode over?
          break; //Then end episode
      }
      //Decay epsilon at end of episode EPSILON = EPSILON * DECAY
      table.decay(0.9998d);
    }
```

### Useful Data Structures of the Library

-   Matrix\<T> - A 3D data storage of Array\<Array\<Array\<T>>>

    ```Java
    var matrix = new Matrix<String>(depth=1, row, col)
    matrix = matrix.map((Object[] o) -> Math.random() > 0.5 ? "John" : "Bob");
    ```

-   Tuple - Immutable structure of data

    ```Java
    var intTuple = new Tuple<Integer>(1, 2, 3);
    int i = intArray.get(0);
    var stringTuple = new Tuple<String>("John", "Bob", "Angela");
    ```

-   Array - Static 1D data structure, therefore will not change size
    ```Java
    var intArray = new Array<Integer>(length=2);
    var stringArray = intArray.map((Object[] o) -> "John");
    var doubleArray = Array.from(new Double[]{0d, 2.1, 3});
    var doubleArray2 = Array.add(intArray, doubleArray);
    ```
-   Function - Lambda function with unlimited Parameters

### Current Environments

All environments must implement interface Env.

-   FrozenLake
