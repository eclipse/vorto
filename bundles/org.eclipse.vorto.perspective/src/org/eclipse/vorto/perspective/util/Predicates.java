package org.eclipse.vorto.perspective.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;

import com.google.common.base.Predicate;

public class Predicates {

	public static Predicate<IResource> isVortoModel = new Predicate<IResource>() {
		public boolean apply(IResource res) {
			return (res instanceof IFile) && isVortoModel(res);
		}
	};

	public static Predicate<IMarkerDelta> isVortoModelWithMarkerError = new Predicate<IMarkerDelta>() {
		public boolean apply(IMarkerDelta delta) {
			return isVortoModel.apply(delta.getResource())
					&& delta.getAttribute("severity").equals(IMarker.SEVERITY_ERROR);
		}
	};
	
	public static Predicate<IResourceDelta> isVortoModelResourceRemoved = new Predicate<IResourceDelta>() {
		public boolean apply(IResourceDelta delta) {
			return delta.getKind() == IResourceDelta.REMOVED && isVortoModel.apply(delta.getResource());
		}
	};

	private static boolean isVortoModel(IResource res) {
		return res.getFileExtension().equals("type") || res.getFileExtension().equals("fbmodel")
				|| res.getFileExtension().equals("infomodel");
	}
}
