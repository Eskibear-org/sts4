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
package org.springframework.ide.vscode.boot.java.requestmapping;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.SymbolKind;
import org.springframework.ide.vscode.boot.java.handlers.SymbolProvider;
import org.springframework.ide.vscode.commons.util.text.TextDocument;

/**
 * @author Martin Lippert
 */
public class RequestMappingSymbolProvider implements SymbolProvider {

	@Override
	public SymbolInformation getSymbol(Annotation node, TextDocument doc) {
		try {
			SymbolInformation symbol = new SymbolInformation(node.toString(), SymbolKind.Interface,
					new Location(doc.getUri(), doc.toRange(node.getStartPosition(), node.getLength())));
			return symbol;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


}