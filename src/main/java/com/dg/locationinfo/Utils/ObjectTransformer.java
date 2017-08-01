package com.dg.locationinfo.Utils;

import java.util.List;

interface ObjectTransformer<S,T> {
    List<T> transform(List<S> s);
}
