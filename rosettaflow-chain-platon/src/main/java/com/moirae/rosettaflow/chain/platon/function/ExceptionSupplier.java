package com.moirae.rosettaflow.chain.platon.function;

@FunctionalInterface
public interface ExceptionSupplier<T>  {
    T get() throws Exception;
}
