/**
 * Licensed to Odiago, Inc. under one or more contributor license
 * agreements.  See the NOTICE.txt file distributed with this work for
 * additional information regarding copyright ownership.  Odiago, Inc.
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.odiago.flumebase.exec.builtins;

import java.util.Collections;
import java.util.List;

import com.odiago.flumebase.exec.Bucket;

import com.odiago.flumebase.lang.AggregateFunc;
import com.odiago.flumebase.lang.Type;
import com.odiago.flumebase.lang.UniversalType;

/**
 * Return the max non-null value in a column.
 */
@SuppressWarnings("rawtypes")
public class max extends AggregateFunc<Comparable> {
  private UniversalType mArgType;

  public max() {
    // Argument may have any input type.
    mArgType = new UniversalType("'a");
    mArgType.addConstraint(Type.getNullable(Type.TypeName.TYPECLASS_COMPARABLE));
  }

  @Override
  public void addToBucket(Object arg, Bucket<Comparable> bucket, Type type) {
    assert type.isComparable();

    Comparable comparableArg = (Comparable) arg;
    if (null != comparableArg) {
      Object curMax = bucket.getState();
      if (curMax == null || comparableArg.compareTo(curMax) > 0) {
        // No currently defined max, or we just exceeded the observed max. Use this.
        bucket.setState(comparableArg);
      }
    }
  }

  @Override
  public Object finishWindow(Iterable<Bucket<Comparable>> buckets, Type type) {
    Comparable curMax = null;
    for (Bucket<Comparable> bucket : buckets) {
      Comparable state = bucket.getState();
      if (curMax == null || (null != state && state.compareTo(curMax) > 0)) {
        curMax = state;
      }
    }

    return curMax;
  }

  @Override
  public Type getReturnType() {
    return mArgType; // Return type is the same as our argument.
  }

  @Override
  public List<Type> getArgumentTypes() {
    return Collections.singletonList((Type) mArgType);
  }
}
