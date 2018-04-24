package org.eclipse.vorto.repository.notification.message;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.repository.account.impl.User;

public class DeleteAccountMessage extends AbstractMessage {

	private TemplateRenderer renderer;
	
	public DeleteAccountMessage(User recipient) {
		super(recipient);
		this.renderer = new TemplateRenderer("delete_account.ftl");
	}

	@Override
	public String getSubject() {
		return "Delete Account Confirmation";
	}

	@Override
	public String getContent() {
		Map<String,Object> ctx = new HashMap<>(1);
		ctx.put("user", recipient);
		try {
			return renderer.render(ctx);
		} catch (Exception e) {
			throw new RuntimeException("Problem rendering delete account email content",e);
			
		}
	}

}
