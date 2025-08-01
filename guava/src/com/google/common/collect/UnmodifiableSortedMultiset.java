/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import static com.google.common.collect.Sets.unmodifiableNavigableSet;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.collect.Multisets.UnmodifiableMultiset;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Comparator;
import java.util.NavigableSet;
import org.jspecify.annotations.Nullable;

/**
 * Implementation of {@link Multisets#unmodifiableSortedMultiset(SortedMultiset)}, split out into
 * its own file so it can be GWT emulated (to deal with the differing elementSet() types in GWT and
 * non-GWT).
 *
 * @author Louis Wasserman
 */
@GwtCompatible(emulated = true)
final class UnmodifiableSortedMultiset<E extends @Nullable Object> extends UnmodifiableMultiset<E>
    implements SortedMultiset<E> {
  UnmodifiableSortedMultiset(SortedMultiset<E> delegate) {
    super(delegate);
  }

  @Override
  protected SortedMultiset<E> delegate() {
    return (SortedMultiset<E>) super.delegate();
  }

  @Override
  public Comparator<? super E> comparator() {
    return delegate().comparator();
  }

  @Override
  NavigableSet<E> createElementSet() {
    return unmodifiableNavigableSet(delegate().elementSet());
  }

  @Override
  public NavigableSet<E> elementSet() {
    return (NavigableSet<E>) super.elementSet();
  }

  @LazyInit private transient @Nullable UnmodifiableSortedMultiset<E> descendingMultiset;

  // TODO(b/418181860): This method creates retain cycles in J2ObjC. In order to break the cycle,
  // there needs to be separate classes for primary and descending multiset, where the primary one
  // would hold {@code @LazyInit @RetainedWith @Nullable} reference to its descending multiset, and
  // the other {@code final} reference.
  @Override
  public SortedMultiset<E> descendingMultiset() {
    UnmodifiableSortedMultiset<E> result = descendingMultiset;
    if (result == null) {
      result = new UnmodifiableSortedMultiset<>(delegate().descendingMultiset());
      result.descendingMultiset = this;
      return descendingMultiset = result;
    }
    return result;
  }

  @Override
  public @Nullable Entry<E> firstEntry() {
    return delegate().firstEntry();
  }

  @Override
  public @Nullable Entry<E> lastEntry() {
    return delegate().lastEntry();
  }

  @Override
  public @Nullable Entry<E> pollFirstEntry() {
    throw new UnsupportedOperationException();
  }

  @Override
  public @Nullable Entry<E> pollLastEntry() {
    throw new UnsupportedOperationException();
  }

  @Override
  public SortedMultiset<E> headMultiset(@ParametricNullness E upperBound, BoundType boundType) {
    return Multisets.unmodifiableSortedMultiset(delegate().headMultiset(upperBound, boundType));
  }

  @Override
  public SortedMultiset<E> subMultiset(
      @ParametricNullness E lowerBound,
      BoundType lowerBoundType,
      @ParametricNullness E upperBound,
      BoundType upperBoundType) {
    return Multisets.unmodifiableSortedMultiset(
        delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType));
  }

  @Override
  public SortedMultiset<E> tailMultiset(@ParametricNullness E lowerBound, BoundType boundType) {
    return Multisets.unmodifiableSortedMultiset(delegate().tailMultiset(lowerBound, boundType));
  }

  @GwtIncompatible @J2ktIncompatible private static final long serialVersionUID = 0;
}
