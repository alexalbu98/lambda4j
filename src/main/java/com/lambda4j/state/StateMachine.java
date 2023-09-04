package com.lambda4j.state;

import com.lambda4j.function.Function;
import com.lambda4j.list.List;
import com.lambda4j.result.Result;
import com.lambda4j.tuple.Tuple;

public class StateMachine<A, S> {
    Function<A, State<S, Nothing>> function;

    public StateMachine(List<Tuple<Condition<A, S>, Transition<A, S>>> transitions) {
        function = a -> State.sequence(m ->
                Result.success(new StateTuple<>(a, m))
                        .flatMap((StateTuple<A, S> t) ->
                                transitions.filter((Tuple<Condition<A, S>, Transition<A, S>> x) ->
                                        x.first.apply(t)).headOption().map((Tuple<Condition<A, S>,
                                        Transition<A, S>> y) -> y.second.apply(t))).getOrElse(m));
    }

    public State<S, S> process(List<A> inputs) {
        List<State<S, Nothing>> a = inputs.map(function);
        State<S, List<Nothing>> b = State.compose(a);
        return b.flatMap(x -> State.get());
    }
}
