/*******************************************************************************
 * Copyright (c) 2009, 2019 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 *******************************************************************************/
package org.jacoco.example.report;

import org.jacoco.core.data.ExecutionDataWriter;
import org.jacoco.core.runtime.RemoteControlReader;
import org.jacoco.core.runtime.RemoteControlWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

@Component
public  class ExecutionDataClient {
	@Value("${execPath}")
	private String execPath;


	@Value("${jacocoipadress}")
	private String address;

	@Value("${jacocoport}")
	private int port;

	/**
	 * Starts the execution data request.
	 * 
	 * @param
	 * @throws IOException
	 */
	public  void exportJacoco() throws IOException {
		final FileOutputStream localFile = new FileOutputStream(execPath+"//"+ DateUtil.format(new Date(),"yyMMdd HHmmss")+"-jacoco.exec");
		final ExecutionDataWriter localWriter = new ExecutionDataWriter(
				localFile);

		// Open a socket to the coverage agent:
		final Socket socket = new Socket(InetAddress.getByName(address), port);
		final RemoteControlWriter writer = new RemoteControlWriter(
				socket.getOutputStream());
		final RemoteControlReader reader = new RemoteControlReader(
				socket.getInputStream());
		reader.setSessionInfoVisitor(localWriter);
		reader.setExecutionDataVisitor(localWriter);

		// Send a dump command and read the response:
		writer.visitDumpCommand(true, false);
		if (!reader.read()) {
			throw new IOException("Socket closed unexpectedly.");
		}

		socket.close();
		localFile.close();
		System.out.println("生成jacocoexe完毕");
	}


}
