package com.intexsoft.devi.service;

/**
 * @author DEVIAPHAN on 08.01.2019
 * @project university
 */
@FunctionalInterface
public interface ThreePridicate<T, U, R> {
    boolean test(T t, U u, R r);
}
