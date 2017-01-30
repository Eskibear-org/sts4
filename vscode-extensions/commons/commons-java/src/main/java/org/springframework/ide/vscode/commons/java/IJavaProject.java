/*******************************************************************************
 * Copyright (c) 2017 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.commons.java;

import java.util.function.Predicate;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

public interface IJavaProject extends IJavaElement {
	
	IType findType(String fqName);
	
	Flux<Tuple2<IType, Double>> fuzzySearchTypes(String searchTerm, Predicate<IType> typeFilter);
	
	Flux<Tuple2<String, Double>> fuzzySearchPackages(String searchTerm);
	
	Flux<IType> allSubtypesOf(IType type);
	
	IClasspath getClasspath();
}
