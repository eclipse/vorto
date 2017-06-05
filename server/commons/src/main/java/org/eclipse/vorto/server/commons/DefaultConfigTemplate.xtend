package org.eclipse.vorto.server.commons

import java.util.Collections
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo
import org.eclipse.vorto.server.commons.IGeneratorConfigUITemplate

class DefaultConfigTemplate implements IGeneratorConfigUITemplate {
	
	override getContent(GeneratorServiceInfo info) {
		'''
		<div class="box">
			<div><img src="data:image/png;base64,«info.image144x144»" width="144" height="144"></div>
				<div href="#" class="col-md-10">
					<h4>«info.name»</h4>
					<ul>
						<li>«info.description»</li>
					    <li class="author"><i class="fa fa-fw fa-user"> </i>«info.creator»</li>
					    <li class="doc"><a ng-href="«info.documentationUrl»"> <i class="fa fa-fw fa-book"></i>Documentation</a></li>
					</ul>
				</div>
			</div>
		</div>
		'''
	}
	
	override getKeys() {
		return Collections.emptySet
	}
	
}
