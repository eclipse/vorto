package org.eclipse.vorto.repository.core.impl.validation;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.core.AttachmentException;
import org.eclipse.vorto.repository.core.FileContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AttachmentValidator  {

    private static int ONE_KB = 1024;

    @Value("#{'${repo.allowed.extension}'.split(',')}")
    private List<String> allowedExtension;

    @Value("${repo.allowed.fileSize: 2}")
    private int fileSize;

    public void validateAttachment(FileContent file, ModelId modelId) throws AttachmentException {

        if(getFileSizeInMegaBytes(file.getSize()) > fileSize){
            throw new AttachmentException(modelId, "File size exceeded");
        }
        if(!allowedExtension.stream()
                .anyMatch(isExtensionAllowed(file.getFileName()))){
            throw new AttachmentException(modelId,"File type not supported");
        }

    }

    public void validateFileLength(FileContent file, ModelId modelId) throws AttachmentException {
        String fileName;
		try {
			fileName = URLDecoder.decode(file.getFileName(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AttachmentException(modelId,e);
		}
        if (fileName.length() > 100) {
            throw new AttachmentException(modelId,"Name of File exceeds 100 Characters");
        }
    }

    private long getFileSizeInMegaBytes(long size) {
        return size / (ONE_KB * ONE_KB);
    }

    private Predicate<String> isExtensionAllowed(String fileName) {
        return  extension -> FilenameUtils.getExtension(
                fileName).equals(extension.trim());

    }
}