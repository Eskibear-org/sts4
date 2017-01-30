/*******************************************************************************
 * Copyright (c) 2016, 2017 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.boot.java;

import java.io.IOException;

import org.springframework.ide.vscode.commons.languageserver.LaunguageServerApp;
import org.springframework.ide.vscode.commons.languageserver.java.JavaProjectFinder;
import org.springframework.ide.vscode.commons.languageserver.util.SimpleLanguageServer;

/**
 * Starts up Language Server process
 * 
 * @author Martin Lippert
 */
public class Main {
		
	public static void main(String[] args) throws IOException, InterruptedException {
		LaunguageServerApp.start(() -> {
			JavaProjectFinder javaProjectFinder = BootJavaLanguageServer.DEFAULT_PROJECT_FINDER;
			SimpleLanguageServer server = new BootJavaLanguageServer(javaProjectFinder);
			return server;
		});
	}

}
