package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.attachment.Attachment;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ModelRepositoryAttachmentTest extends AbstractIntegrationTest {
	
	@Test
	public void testAttachFile() {
		IUserContext erle = UserContext.user("erle");
		importModel("Color.type", erle);
		
		try {
			boolean result = modelRepository.attachFile(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"), 
					new FileContent("backup1.xml", IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream())), erle);
			
			assertTrue(result);
			
			List<Attachment> attachments = modelRepository.getAttachments(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"));
			
			assertEquals(1, attachments.size());
			
			assertEquals("backup1.xml",attachments.get(0).getFilename());
			assertEquals(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"),attachments.get(0).getModelId());
			assertEquals(0,attachments.get(0).getTags().size());
			attachments.forEach(attachment -> System.out.println(attachment));
			
		} catch (IOException | FatalModelRepositoryException e) {
			e.printStackTrace();
			fail("Cannot load sample file");
		}
	}
	
	@Test
	public void testAttachFileWithTag() {
		IUserContext erle = UserContext.user("erle");
		importModel("Color.type", erle);
		
		try {
			modelRepository.attachFile(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"), 
					new FileContent("backup1.xml", IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream())), erle,Attachment.TAG_DOCUMENTATION);
						
			List<Attachment> attachments = modelRepository.getAttachments(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"));
			
			assertEquals(1, attachments.get(0).getTags().size());
			assertEquals(Attachment.TAG_DOCUMENTATION, attachments.get(0).getTags().get(0));
			
			attachments.forEach(name -> System.out.println(name));
			
		} catch (IOException | FatalModelRepositoryException e) {
			e.printStackTrace();
			fail("Cannot load sample file");
		}
	}
	
	@Test
	public void testAttachFileWithMultipleTags() {
		IUserContext erle = UserContext.user("erle");
		importModel("Color.type", erle);
		
		try {
			modelRepository.attachFile(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"), 
					new FileContent("backup1.xml", IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream())), erle,Attachment.TAG_DOCUMENTATION,Attachment.TAG_IMAGE);
						
			List<Attachment> attachments = modelRepository.getAttachmentsByTag(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"),Attachment.TAG_IMAGE);
			
			assertEquals(2, attachments.get(0).getTags().size());
			
			attachments.forEach(name -> System.out.println(name));
			
		} catch (IOException | FatalModelRepositoryException e) {
			e.printStackTrace();
			fail("Cannot load sample file");
		}
	}
	
	@Test
	public void testGetAttachmentsWithTagExisting() {
		IUserContext erle = UserContext.user("erle");
		importModel("Color.type", erle);
		
		try {
			modelRepository.attachFile(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"), 
					new FileContent("backup1.xml", IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream())), erle,Attachment.TAG_DOCUMENTATION);
						
			List<Attachment> attachments = modelRepository.getAttachmentsByTag(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"),Attachment.TAG_DOCUMENTATION);
			
			assertEquals(1,attachments.size());
			assertEquals(Attachment.TAG_DOCUMENTATION, attachments.get(0).getTags().get(0));
						
		} catch (IOException | FatalModelRepositoryException e) {
			e.printStackTrace();
			fail("Cannot load sample file");
		}
	}
	
	@Test
	public void testGetAttachmentsWithTagNotExisting() {
		IUserContext erle = UserContext.user("erle");
		importModel("Color.type", erle);
		
		try {
			modelRepository.attachFile(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"), 
					new FileContent("backup1.xml", IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream())), erle,Attachment.TAG_DOCUMENTATION);
						
			List<Attachment> attachments = modelRepository.getAttachmentsByTag(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"),Attachment.TAG_IMAGE);
			
			assertEquals(0,attachments.size());
						
		} catch (IOException | FatalModelRepositoryException e) {
			e.printStackTrace();
			fail("Cannot load sample file");
		}
	}
	
	@Test
	public void testAttachMultipleFiles() {
		IUserContext erle = UserContext.user("erle");
		
		testAttachFile();
		
		try {
			boolean result = modelRepository.attachFile(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"), 
					new FileContent("Color.xmi", IOUtils.toByteArray(new ClassPathResource("sample_models/Color.xmi").getInputStream())), erle);
			
			assertTrue(result);
			
			List<Attachment> attachments = modelRepository.getAttachments(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"));
			
			assertEquals(2, attachments.size());
			
			attachments.forEach(name -> System.out.println(name));
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("Cannot load sample file");
		}
	}
	
	@Test
	public void testExtractAttachedFileContent() {
		
		testAttachFile();
		
		try {
			Optional<FileContent> colorXmi = modelRepository.getAttachmentContent(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"), "Color.xmi");
			
			assertFalse(colorXmi.isPresent());
			
			Optional<FileContent> backup1 = modelRepository.getAttachmentContent(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"), "backup1.xml");
			
			assertTrue(backup1.isPresent());
			
			byte[] backup1Array = IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream());
			
			assertTrue(Arrays.equals(backup1Array, backup1.get().getContent()));
			
		} catch (IOException | FatalModelRepositoryException e) {
			e.printStackTrace();
			fail("Cannot load sample file");
		}
	}
	
	@Test
	public void testWrongModelIdIsNotExceptional() {
		IUserContext erle = UserContext.user("erle");
		importModel("Color.type", erle);
		
		try {
			boolean result = modelRepository.attachFile(new ModelId("org.eclipse.vorto.examples.type", "Color", "1.0.0"), 
					new FileContent("backup1.xml", IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream())), erle);
			
			assertFalse(result);
			
			List<Attachment> attachments = modelRepository.getAttachments(new ModelId("org.eclipse.vorto.examples.type", "Color", "1.0.0"));
			
			assertEquals(0, attachments.size());
			
			Optional<FileContent> backup1 = modelRepository.getAttachmentContent(new ModelId("org.eclipse.vorto.examples.type", "Color", "1.0.0"), "backup1.xml");
			
			assertFalse(backup1.isPresent());
			
		} catch (IOException | FatalModelRepositoryException e) {
			e.printStackTrace();
			fail("Cannot load sample file");
		}
	}	
	
	@Test
	public void testDeleteAttachments() {
		IUserContext erle = UserContext.user("erle");
		importModel("Color.type", erle);
		
		try {
			ModelId modelId = new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0");
			
			boolean result = modelRepository.attachFile(modelId, 
					new FileContent("backup1.xml", IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream())), erle);
			
			assertTrue(result);
			
			boolean deleteResult = modelRepository.deleteAttachment(modelId, "backup1.xml");
			
			assertTrue(deleteResult);
			
			// You cannot delete it twice
			boolean deleteResult1 = modelRepository.deleteAttachment(modelId, "backup1.xml");
			
			assertFalse(deleteResult1);
			
			boolean deleteResult2 = modelRepository.deleteAttachment(modelId, "backup2.xml");
			
			assertFalse(deleteResult2);
			
		} catch (IOException | FatalModelRepositoryException e) {
			e.printStackTrace();
			fail("Cannot load sample file");
		}
	}
}
