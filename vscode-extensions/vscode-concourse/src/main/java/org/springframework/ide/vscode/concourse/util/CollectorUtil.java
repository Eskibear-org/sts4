/*******************************************************************************
 * Copyright (c) 2016 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.concourse.util;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;

/**
 * Stuff missing from {@link Collectors} that we implement ourself.
 */
public class CollectorUtil {
	
	/**
	 * Collects elements into a ImmutableMutiset (the set is converted to an immutable one
	 * at the end. Accumulating / combining is done with a mutable Multiset because that involves
	 * less copying.)
	 */
	public static <T> Collector<T, HashMultiset<T>, ImmutableMultiset<T>> toMultiset() {
		return new Collector<T, HashMultiset<T>, ImmutableMultiset<T>>() {

			@Override
			public Supplier<HashMultiset<T>> supplier() {
				return HashMultiset::create;
			}

			@Override
			public BiConsumer<HashMultiset<T>, T> accumulator() {
				return (a, e) -> a.add(e);
			}

			@Override
			public BinaryOperator<HashMultiset<T>> combiner() {
				return (a1, a2) -> {
					a1.addAll(a2);
					return a1;
				};
			}
			@Override
			public Function<HashMultiset<T>, ImmutableMultiset<T>> finisher() {
				return ImmutableMultiset::copyOf;
			}

			@Override
			public Set<Collector.Characteristics> characteristics() {
				return ImmutableSet.of(Collector.Characteristics.UNORDERED);
			}
		};
	}

}
