/*******************************************************************************
 * Copyright (c) 2015, 2016 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.boot.common;

import org.eclipse.lsp4j.CompletionItemKind;
import org.springframework.ide.vscode.commons.languageserver.completion.DocumentEdits;
import org.springframework.ide.vscode.commons.languageserver.completion.ScoreableProposal;
import org.springframework.ide.vscode.commons.languageserver.util.IDocument;
import org.springframework.ide.vscode.commons.util.Renderable;
import org.springframework.ide.vscode.commons.yaml.hover.YPropertyHoverInfo;
import org.springframework.ide.vscode.commons.yaml.schema.YType;
import org.springframework.ide.vscode.commons.yaml.schema.YTypeUtil;

public abstract class AbstractPropertyProposal extends ScoreableProposal {

	@Override
	public String getDetail() {
		return niceTypeName(getType());
	}

	protected final IDocument fDoc;
	private final DocumentEdits proposalApplier;
	private boolean isDeprecated = false;

	public AbstractPropertyProposal(IDocument doc, DocumentEdits applier) {
		this.proposalApplier = applier;
		this.fDoc = doc;
	}

	@Override
	public String getLabel() {
		return getBaseDisplayString();
	}

//	public IRegion getSelection(IDocument document) {
//		try {
//			return proposalApplier.getSelection(document);
//		} catch (Exception e) {
//			Log.log(e);
//			return null;
//		}
//	}


//	public String getDisplayString() {
//		StyledString styledText = getStyledDisplayString();
//		return styledText.getString();
//	}

//	public Image getImage() {
//		return null;
//	}

//	public IContextInformation getContextInformation() {
//		return null;
//	}

//	@Override
//	public StyledString getStyledDisplayString() {
//		StyledString result = new StyledString();
//		result = result.append(super.getStyledDisplayString());
//		YType type = getType();
//		if (type!=null) {
//			String typeStr = niceTypeName(type);
//			result.append(" : "+typeStr, StyledString.DECORATIONS_STYLER);
//		}
//		return result;
//	}

	protected boolean isDeprecated() {
		return isDeprecated;
	}
	public void deprecate() {
		if (!isDeprecated()) {
			deemphasize();
			deemphasize();
			isDeprecated = true;
		}
	}
	protected abstract YType getType();
	protected abstract String getHighlightPattern();
	protected abstract String getBaseDisplayString();
	protected abstract String niceTypeName(YType type);

	@Override
	public CompletionItemKind getKind() {
		return CompletionItemKind.Field;
	}
	
	@Override
	public String toString() {
		return getBaseDisplayString();
	}
	
	@Override
	public final DocumentEdits getTextEdit() {
		return this.proposalApplier;
	}

//	@Override
//	public void apply(IDocument document) {
//		try {
//			proposalApplier.apply(document);
//		} catch (Exception e) {
//			EditorSupportActivator.log(e);
//		}
//	}
}