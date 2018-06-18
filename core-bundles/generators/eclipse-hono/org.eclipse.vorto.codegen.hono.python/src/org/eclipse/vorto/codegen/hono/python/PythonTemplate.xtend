package org.eclipse.vorto.codegen.hono.python
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.model.Model

abstract class PythonTemplate <T extends Model> implements IFileTemplate<T> {
    public String rootPath;
}