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
package org.springframework.ide.vscode.commons.cloudfoundry.client.cftarget;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.ide.vscode.commons.util.StringUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides existing Cloud Foundry client params, like target and credentials,
 * from the CLI config.json in the file system.
 *
 *
 */
public class CfCliParamsProvider implements ClientParamsProvider {

	public static final String NO_CLI_TARGETS_FOUND_MESSAGE = "No Cloud Foundry targets found: Use CF CLI to login";
	public static final String TARGET = "Target";
	public static final String REFRESH_TOKEN = "RefreshToken";
	public static final String ORGANIZATION_FIELDS = "OrganizationFields";
	public static final String SPACE_FIELDS = "SpaceFields";
	public static final String NAME = "Name";
	public static final String SSL_DISABLED = "SSLDisabled";

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.ide.vscode.commons.cloudfoundry.client.cftarget.
	 * ClientParamsProvider#getParams()
	 */
	@Override
	public List<CFClientParams> getParams() throws NoTargetsException, ExecutionException {
		List<CFClientParams> params = new ArrayList<>();

		try {
			File file = getConfigJsonFile();
			if (file != null) {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> userData = mapper.readValue(file, Map.class);
				if (userData != null) {
					String refreshToken = (String) userData.get(REFRESH_TOKEN);
					// Only support connecting to CF via refresh token for now
					if (isRefreshTokenSet(refreshToken)) {
						CFCredentials credentials = CFCredentials.fromRefreshToken(refreshToken);
						boolean sslDisabled = (Boolean) userData.get(SSL_DISABLED);
						String target = (String) userData.get(TARGET);
						Map<String, Object> orgFields = (Map<String, Object>) userData.get(ORGANIZATION_FIELDS);
						Map<String, Object> spaceFields = (Map<String, Object>) userData.get(SPACE_FIELDS);
						if (target != null && orgFields != null && spaceFields != null) {
							String orgName = (String) orgFields.get(NAME);
							String spaceName = (String) spaceFields.get(NAME);
							params.add(new CFClientParams(target, null, credentials, orgName, spaceName, sslDisabled));
						}
					}
				}
			}
		} catch (IOException | InterruptedException e) {
			throw new ExecutionException(e);
		}

		if (params.isEmpty()) {
			throw new NoTargetsException(
					NO_CLI_TARGETS_FOUND_MESSAGE);
		} else {
			return params;
		}
	}

	private boolean isRefreshTokenSet(String token) {
		return StringUtil.hasText(token);
	}

	private File getConfigJsonFile() throws IOException, InterruptedException {
		String homeDir = getHomeDir();
		if (homeDir != null) {
			if (!homeDir.endsWith("/")) {
				homeDir += '/';
			}
			String filePath = homeDir + ".cf/config.json";
			File file = new File(filePath);
			if (file.exists() && file.canRead()) {
				return file;
			}
		}
		return null;
	}

	private String getHomeDir() throws IOException, InterruptedException {
		return System.getProperty("user.home");
	}

}
