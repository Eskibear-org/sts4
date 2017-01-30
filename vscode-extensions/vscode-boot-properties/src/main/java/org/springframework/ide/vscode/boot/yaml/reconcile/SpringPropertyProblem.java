/*******************************************************************************
 * Copyright (c) 2016-2017 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/

package org.springframework.ide.vscode.boot.yaml.reconcile;

import org.springframework.ide.vscode.boot.metadata.PropertyInfo;
import org.springframework.ide.vscode.commons.languageserver.quickfix.ProblemFixer;
import org.springframework.ide.vscode.commons.languageserver.reconcile.ProblemType;
import org.springframework.ide.vscode.commons.languageserver.reconcile.ReconcileProblemImpl;

public class SpringPropertyProblem extends ReconcileProblemImpl {

	private PropertyInfo property = null;
	private ProblemFixer fixer;
	private String propertyName;

	public SpringPropertyProblem(ProblemType type, String msg, int offset, int len) {
		super(type, msg, offset, len);
	}

	public static SpringPropertyProblem problem(ApplicationYamlProblemType type, String msg, int offset, int len) {
		return new SpringPropertyProblem(type, msg, offset, len);
	}

	public void setMetadata(PropertyInfo property) {
		this.property = property;
	}

	public void setProblemFixer(ProblemFixer fixer) {
		this.fixer = fixer;
	}

	public void setPropertyName(String name) {
		propertyName = name;
	}

}
