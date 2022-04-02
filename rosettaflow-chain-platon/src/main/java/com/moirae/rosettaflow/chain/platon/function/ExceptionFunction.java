package com.moirae.rosettaflow.chain.platon.function;

@FunctionalInterface
public interface ExceptionFunction<T, R>  {

    R apply(T t) throws Exception;
}
