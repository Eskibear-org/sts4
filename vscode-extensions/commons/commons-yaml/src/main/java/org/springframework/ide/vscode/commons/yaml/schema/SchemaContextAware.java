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
package org.springframework.ide.vscode.commons.yaml.schema;

/**
 * Interface that can be implemented by something producing another
 * component (of some type `T`) where the returned component needs to
 * configured with a {@link DynamicSchemaContext}.
 * 
 * @author Kris De Volder
 */
public interface SchemaContextAware<T> {
	T withContext(DynamicSchemaContext dc);
}
