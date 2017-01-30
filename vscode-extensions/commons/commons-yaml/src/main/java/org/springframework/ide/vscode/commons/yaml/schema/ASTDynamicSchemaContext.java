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
package org.springframework.ide.vscode.commons.yaml.schema;

import java.util.Collections;
import java.util.Set;

import org.springframework.ide.vscode.commons.util.text.IDocument;
import org.springframework.ide.vscode.commons.yaml.ast.NodeUtil;
import org.springframework.ide.vscode.commons.yaml.path.YamlPath;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;

import com.google.common.collect.ImmutableSet;

/**
 * Adapts a SnakeYaml ast node as a {@link DynamicSchemaContext} (so it
 * can be used in YamlSchema based reconciler.
 *
 * @author Kris De Volder
 */
public class ASTDynamicSchemaContext extends CachingSchemaContext {

	private MappingNode mapNode;
	private IDocument doc;
	private YamlPath path;

	public ASTDynamicSchemaContext(IDocument doc, YamlPath path, Node node) {
		this.doc = doc;
		this.path = path;
		this.mapNode = as(MappingNode.class, node);
	}

	@SuppressWarnings("unchecked")
	private <T> T as(Class<T> klass, Node node) {
		if (node!=null && klass.isInstance(node)) {
			return (T) node;
		}
		return null;
	}

	@Override
	protected Set<String> computeDefinedProperties() {
		return NodeUtil.getScalarKeys(mapNode);
	}

	@Override
	public IDocument getDocument() {
		return doc;
	}

	@Override
	public YamlPath getPath() {
		return path;
	}
}
