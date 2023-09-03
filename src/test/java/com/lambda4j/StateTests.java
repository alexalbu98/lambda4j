package com.lambda4j;

import com.lambda4j.list.List;
import com.lambda4j.random.JavaRNG;
import com.lambda4j.random.RNG;
import com.lambda4j.state.Nothing;
import com.lambda4j.state.State;
import com.lambda4j.tuple.Tuple;
import org.junit.jupiter.api.Test;

import static com.lambda4j.list.List.list;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateTests {

    RNG rng = new JavaRNG(42);

    @Test
    public void unit_works() {
        State<RNG, Integer> state = State.unit(1);
        RNG rng = new JavaRNG(42);
        Tuple<Integer, RNG> result = state.run.apply(rng);
        assertEquals(1, result.first);
    }

    @Test
    public void map_works() {
        State<RNG, Integer> state = State.unit(1);
        state = state.map(i -> i + 1);
        Tuple<Integer, RNG> result = state.run.apply(rng);
        assertEquals(2, result.first);
    }

    @Test
    public void map2_works() {
        State<RNG, Integer> state1 = State.unit(1);
        State<RNG, Integer> state2 = State.unit(2);
        State<RNG, Integer> state3 = state1.map2(state2, x -> y -> x + y);
        Tuple<Integer, RNG> result = state3.run.apply(rng);
        assertEquals(3, result.first);
    }

    @Test
    public void flat_map_works() {
        State<RNG, Integer> state = State.unit(1);
        state = state.flatMap(i -> State.unit(i + 1));

        Tuple<Integer, RNG> result = state.run.apply(rng);
        assertEquals(2, result.first);
    }

    @Test
    public void sequence_works() {
        State<RNG, Integer> state1 = State.unit(1);
        State<RNG, Integer> state2 = State.unit(2);

        State<RNG, List<Integer>> state = State.sequence(list(state1, state2));
        Tuple<List<Integer>, RNG> tuple = state.run.apply(rng);

        assertEquals(2, tuple.first.lengthMemoized());
        assertEquals(1, tuple.first.head());
    }

    @Test
    public void set_works() {
        State<RNG, Nothing> state = State.set(rng);
        State<RNG, Integer> newState = state.flatMap(n -> State.unit(1));

        assertEquals(1, newState.run.apply(rng).first);
    }
}
