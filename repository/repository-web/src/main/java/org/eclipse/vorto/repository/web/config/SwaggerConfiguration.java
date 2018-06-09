package org.eclipse.vorto.repository.web.config;

import static com.google.common.base.Predicates.or;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

	@Bean
	public Docket vortoApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).useDefaultResponseMessages(false)
				.select().paths(paths()).build();

	}

	@SuppressWarnings("unchecked")
	private Predicate<String> paths() {
		return or(	PathSelectors.regex("/rest/secure.*"),
					PathSelectors.regex("/rest/model.*"),
					PathSelectors.regex("/rest/resolver.*"),
					PathSelectors.regex("/rest/generation-router.*"));
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Vorto",
				"Vorto provides tools and services that allow to create and manage technology agnostic, abstract device descriptions, so called information models. <br/>"
						+ "Information models describe the attributes and the capabilities of real world devices. <br/>"
						+ "These information models can be managed and shared within the Vorto Information Model Repository. <br/>"
						+ " Code Generators for Information Models let you integrate devices into different IoT platforms."
						+ "<br/>",
				"1.0.0", "", "Eclipse Vorto Team", "EPL", "https://eclipse.org/org/documents/epl-v10.php");
	}
}
