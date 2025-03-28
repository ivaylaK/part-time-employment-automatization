package com.trenkwalder.parttimeemployment.mapper;

public interface Mapper<X, Y> {

    X mapFrom(Y y);
    Y mapTo(X x);
}
