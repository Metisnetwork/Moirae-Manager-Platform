package com.datum.platform.chain.platon.function;

@FunctionalInterface
public interface ExceptionSupplier<T>  {
    T get() throws Exception;
}
