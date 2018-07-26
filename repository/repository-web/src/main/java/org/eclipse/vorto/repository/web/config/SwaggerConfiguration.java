package org.eclipse.vorto.repository.web.config;

import static com.google.common.base.Predicates.or;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Predicate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

	@Bean
	@Profile({"local"})
	public Docket vortoApiLocal() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).useDefaultResponseMessages(false)
				.select().paths(paths()).build();

	}
	
	@Bean
	@Profile({"eclipse"})
	public Docket vortoApiCloud() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).useDefaultResponseMessages(false)
				.select().paths(paths()).build().pathProvider(new BasePathAwareRelativePathProvider(""));

	}

	@SuppressWarnings("unchecked")
	private Predicate<String> paths() {
		return or(PathSelectors.regex("/api/v1/models.*"),
				  PathSelectors.regex("/api/v1/search.*"),
				  PathSelectors.regex("/api/v1/generators.*"),
				  PathSelectors.regex("/api/v1/attachments.*"));
	}

	@SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
		return new ApiInfo("Vorto",
				"Vorto provides tools and services that allow to create and manage technology agnostic, abstract device descriptions, so called information models. <br/>"
						+ "Information models describe the attributes and the capabilities of real world devices. <br/>"
						+ "These information models can be managed and shared within the Vorto Information Model Repository. <br/>"
						+ " Code Generators for Information Models let you integrate devices into different IoT platforms."
						+ "<br/>",
				"1.0.0", "", "Eclipse Vorto Team", "EPL", "https://eclipse.org/org/documents/epl-v10.php");
	}
	
	class BasePathAwareRelativePathProvider extends AbstractPathProvider {
        private String basePath;

        public BasePathAwareRelativePathProvider(String basePath) {
            this.basePath = basePath;
        }

        @Override
        protected String applicationPath() {
            return basePath;
        }

        @Override
        protected String getDocumentationPath() {
            return "/";
        }

        @Override
        public String getOperationPath(String operationPath) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("");
            return Paths.removeAdjacentForwardSlashes(
                    uriComponentsBuilder.path(operationPath.replaceFirst(basePath, "")).build().toString());
        }
    }
}
