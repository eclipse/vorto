package org.eclipse.vorto.service.mapping;

import java.util.Map;

public interface IDataMapper<Input> {

	Map<String,Object> map(Input input);
}
