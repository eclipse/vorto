package org.eclipse.vorto.service.mapping.spec;

import java.io.InputStream;

public interface IMappingSpecificationReader {

	IMappingSpecification read(InputStream input) throws MappingSpecReadProblem;
	
	public class MappingSpecReadProblem extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MappingSpecReadProblem(Throwable t) {
			super(t);
		}
	}
}
