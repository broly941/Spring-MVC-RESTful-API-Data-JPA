package com.intexsoft.devi.funcInterface;

/**
 * @author DEVIAPHAN on 16.01.2019
 * @project university
 * Its implementation of the functional interface
 */
@FunctionalInterface
public interface TriFunction<A, B, C, R> {
    R apply(A a, B b, C c);
}
