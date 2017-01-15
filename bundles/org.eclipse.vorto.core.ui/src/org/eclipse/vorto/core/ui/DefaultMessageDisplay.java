/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.core.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;

/**
 * 
 * 
 * Used to write to console. Usage: create instance,
 * ConsoleDisplayMgr.getDefault().println("message",
 * ConsoleDisplayMgr.MSG_ERROR)
 * 
 */
public class DefaultMessageDisplay implements IMessageDisplay {
	protected static enum MSG_KIND {
		ERROR, WARNING, INFO, SUCCESS
	}

	private static DefaultMessageDisplay fDefault = null;
	private String fTitle = null;
	private static MessageConsole fMessageConsole = null;
	private static Color warningColor;
	private static Color infoColor;
	private static Color errorColor;
	private static Color successColor;
	private static final String SEPERATOR = "------------------------------------------------------------------------";
	
	private DefaultMessageDisplay(String messageTitle) {
		// fDefault = this;
		fTitle = messageTitle;

		Display display = Display.getDefault();

		infoColor = display.getSystemColor(SWT.COLOR_BLACK);
		errorColor = display.getSystemColor(SWT.COLOR_RED);
		successColor = display.getSystemColor(SWT.COLOR_DARK_GREEN);
		warningColor = display.getSystemColor(SWT.COLOR_BLUE);
	}

	public static DefaultMessageDisplay getDefault() {
		if (fDefault == null) {
			fDefault = new DefaultMessageDisplay(" Vorto ");
		}
		return fDefault;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.vorto.api.console.IConsoleDisplayMgr#println(java.lang.String
	 * , org.eclipse.vorto.api.console.ConsoleDisplayMgr.MSG_KIND)
	 */
	protected void println(final String msg, final MSG_KIND msgKind) {
		// ConsolePlugin.getDefault().getConsoleManager()
		// .showConsoleView(fMessageConsole);
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (msg == null)
					return;
				
				IViewPart consoleViewPart = displayConsoleView();
				if (consoleViewPart == null) {
					MessageDialog.openError(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(), "Error",
							msg);
					return;
				}
				final String msgWithMarker;

				switch (msgKind) {
				case INFO:
					msgWithMarker = "[INFO] " + msg;
					break;
				case ERROR:
					msgWithMarker = "[ERROR] " + msg;
					break;
				case SUCCESS:
					msgWithMarker = "[SUCCESS] " + msg;
					break;
				case WARNING:
					msgWithMarker = "[WARNING] " + msg;
					break;
				default:
					msgWithMarker = msg;
				}

				try {
					getNewMessageConsoleStream(msgKind).write(msgWithMarker);
					getNewMessageConsoleStream(msgKind).write('\n');
					
					if (msgKind == MSG_KIND.ERROR) {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(consoleViewPart);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.vorto.api.console.IConsoleDisplayMgr#printError(java.lang
	 * .String)
	 */
	@Override
	public void displayError(final String msg) {
		this.println(msg, MSG_KIND.ERROR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.vorto.api.console.IConsoleDisplayMgr#printSuccess(java.lang
	 * .String)
	 */
	@Override
	public void displaySuccess(final String msg) {
		this.println(msg, MSG_KIND.SUCCESS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.vorto.api.console.IConsoleDisplayMgr#printWarning(java.lang
	 * .String)
	 */
	@Override
	public void displayWarning(final String msg) {
		this.println(msg, MSG_KIND.WARNING);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.vorto.api.console.IConsoleDisplayMgr#printInfo(java.lang.
	 * String)
	 */
	@Override
	public void display(final String msg) {
		this.println(msg, MSG_KIND.INFO);
	}

	public void clear() {
		IDocument document = getMessageConsole().getDocument();
		if (document != null) {
			document.set("");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.vorto.api.console.IConsoleDisplayMgr#printSeperator()
	 */
	@Override
	public void displayNewLine() {
		println(SEPERATOR, MSG_KIND.INFO);
	}

	public IViewPart displayConsoleView() {
		try {
			IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			if (activeWorkbenchWindow != null) {
				IWorkbenchPage activePage = activeWorkbenchWindow
						.getActivePage();
				if (activePage != null) {
					return activePage.showView(IConsoleConstants.ID_CONSOLE_VIEW,
							null, IWorkbenchPage.VIEW_VISIBLE);
				}
			}

		} catch (PartInitException partEx) {
			throw new RuntimeException("Cannot open console view");
		}
		
		return null;
	}

	public IOConsoleOutputStream getNewMessageConsoleStream(MSG_KIND msgKind) {
		Color color = null;

		switch (msgKind) {
		case INFO:
			color = infoColor;
			break;
		case ERROR:
			color = errorColor;
			break;
		case SUCCESS:
			color = successColor;
			break;
		case WARNING:
			color = warningColor;
			break;
		default:
			color = infoColor;
		}

		IOConsoleOutputStream msgConsoleStream = getMessageConsole()
				.newOutputStream();
		msgConsoleStream.setColor(color);
		return msgConsoleStream;
	}

	private MessageConsole getMessageConsole() {
		if (fMessageConsole == null)
			createMessageConsoleStream(fTitle);

		return fMessageConsole;
	}

	private void createMessageConsoleStream(String title) {
		initalize(title);

		ConsolePlugin.getDefault().getConsoleManager()
				.addConsoles(new IConsole[] { fMessageConsole });
	}

	private static void initalize(String title) {
		fMessageConsole = new MessageConsole(title, null);
	}

	@Override
	public void displayError(Throwable cause) {
		StringWriter sw = new StringWriter();
		cause.printStackTrace(new PrintWriter(sw));
		this.println(sw.toString(), MSG_KIND.ERROR);
	}
}
