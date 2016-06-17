/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.perspective.view;

import java.io.ByteArrayInputStream;

import org.apache.commons.codec.binary.Base64InputStream;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.ui.handler.CodeGenerationHelper;
import org.eclipse.vorto.core.api.repository.Attachment;
import org.eclipse.vorto.core.api.repository.GeneratorResource;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelRepositoryFactory;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.ui.exception.ExceptionHandlerFactory;

public class GeneratorItem extends Composite {
	
	private final Label lblGenerated = new Label(this, SWT.NONE);

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public GeneratorItem(final Composite parent, int style, final ModelResource model,
			final GeneratorResource codegen) {
		super(parent, SWT.BORDER | SWT.NO_FOCUS);

		setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		setSize(500, 120);

		Label lblIcon = new Label(this, SWT.NONE);
		lblIcon.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		lblIcon.setAlignment(SWT.CENTER);
		lblIcon.setBounds(27, 21, 36, 36);
		ImageDescriptor generatorImage = ImageDescriptor.createFromImageData(new ImageData(new Base64InputStream(new ByteArrayInputStream(codegen.getImage32x32().getBytes()))));
		lblIcon.setImage(generatorImage.createImage());
		Label lblName = new Label(this, SWT.NONE);
		lblName.setAlignment(SWT.CENTER);
		lblName.setBounds(10, 63, 70, 15);
		lblName.setText(codegen.getName());
		lblName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		StyledText txtDescription = new StyledText(this, SWT.READ_ONLY | SWT.WRAP);
		txtDescription.setBounds(96, 10, 390, 75);
		txtDescription.setText(codegen.getDescription());
		txtDescription.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		Label lblCreatedBy = new Label(this, SWT.NONE);
		lblCreatedBy.setBounds(96, 91, 60, 15);
		lblCreatedBy.setText("Created by ");
		lblCreatedBy.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		Label lblAuthor = new Label(this, SWT.NONE);
		lblAuthor.setBounds(157, 91, 131, 15);
		lblAuthor.setText(codegen.getCreator());
		lblAuthor.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		Label lblStarImage = new Label(this, SWT.NONE);
		lblStarImage.setAlignment(SWT.CENTER);
		lblStarImage.setBounds(10, 83, 70, 23);
		lblStarImage.setText("Rating: "+formatRating(codegen.getRating()));
		lblStarImage.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		lblGenerated.setBounds(320, 91, 85, 15);
		lblGenerated.setText("");
		lblGenerated.setAlignment(SWT.RIGHT);
		lblGenerated.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		final Button btnGenerate = new Button(this, SWT.NONE);
		btnGenerate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblGenerated.setText("Generating...");
				
				try {
					final IModelRepository modelRepo = ModelRepositoryFactory.getModelRepository();
					final Attachment attachment = modelRepo.generateCode(model.getId(),codegen.getKey());
					
					CodeGenerationHelper.createEclipseProject(model.getId(), codegen.getKey(), toGenerationResult(attachment));
													
					lblGenerated.setText("Generated.");
					btnGenerate.setEnabled(false);
				} catch (Exception e1) {
					ExceptionHandlerFactory.getHandler().handle(e1);
				}
			}
		});
		btnGenerate.setBounds(411, 86, 75, 25);
		btnGenerate.setText("Generate");
	}
	
	private IGenerationResult toGenerationResult(final Attachment attachment) {
		return new IGenerationResult() {
						
			@Override
			public String getMediatype() {
				return attachment.getType();
			}
			
			@Override
			public String getFileName() {
				return attachment.getFilename();
			}
			
			@Override
			public byte[] getContent() {
				return attachment.getContent();
			}
		};
	}
		
	private String formatRating(String rating) {
		if (rating.equalsIgnoreCase("fair")) {
			return "*";
		} else if (rating.equalsIgnoreCase("good")) {
			return "**";
		} else if (rating.equalsIgnoreCase("very_good")) {
			return "***";
		} else if (rating.equalsIgnoreCase("excellent")) {
			return "****";
		} else {
			return "n/a";
		}
	}
}
