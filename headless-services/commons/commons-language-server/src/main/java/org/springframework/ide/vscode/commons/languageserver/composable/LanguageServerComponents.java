/*******************************************************************************
 * Copyright (c) 2018 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.commons.languageserver.composable;

import org.springframework.ide.vscode.commons.languageserver.completion.ICompletionEngine;
import org.springframework.ide.vscode.commons.languageserver.hover.HoverInfoProvider;
import org.springframework.ide.vscode.commons.languageserver.reconcile.IReconcileEngine;

public interface LanguageServerComponents {

	IReconcileEngine getReconcileEngine();
	ICompletionEngine getCompletionEngine();
	HoverInfoProvider getHoverProvider();

}
