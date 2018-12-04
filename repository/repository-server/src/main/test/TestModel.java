import com.google.common.collect.Sets;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.eclipse.vorto.repository.account.Role.ADMIN;
import static org.eclipse.vorto.repository.account.Role.MODEL_CREATOR;
import static org.eclipse.vorto.repository.account.Role.USER;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestModel {
    String namespace = RandomStringUtils.random(10, true, false).toLowerCase();
    String modelName = RandomStringUtils.random(10, true, false).toUpperCase();
    String description = "InformationModel for " + modelName;
    String version = "1.0.0";
    String prettyName = namespace + ":" + modelName + ":" + version;
    String targetPlatform = "target";
    String json =
        "{"
            + "\"targetPlatformKey\":" + targetPlatform + ","
            + "\"stereotypes\":[],"
            + "\"mappingReference\":null,"
            + "\"id\":{"
                + "\"name\":\"" + modelName
                + "\",\"namespace\":\"" + namespace
                + "\",\"version\":\"1.0.0\","
                + "\"prettyFormat\":\"" + prettyName
            + "\"},"
            + "\"type\":\"InformationModel\","
            + "\"displayName\":\"" + modelName + "\","
            + "\"description\":\"" + description + "\","
            + "\"fileName\":null,"
            + "\"references\":[],"
            + "\"author\":null,"
            + "\"creationDate\":null,"
            + "\"modificationDate\":null,"
            + "\"hasImage\":false,"
            + "\"state\":null,"
            + "\"imported\":false,"
            + "\"referencedBy\":[],"
            + "\"platformMappings\":{},"
            + "\"model\":{"
                + "\"name\":\""+ modelName + "\","
                + "\"namespace\":\""+ namespace + "\","
                + "\"version\":\""+ version +"\","
                + "\"references\":[],"
                + "\"description\":\""+ description + "\","
                + "\"displayname\":\"" + modelName + "\","
                + "\"category\":null,"
                + "\"properties\":[]"
            + "},"
            + "\"targetPlatform\":\"" + targetPlatform + "\","
            + "\"released\":false"
        + "}";

    public TestModel(String namespace, String modelName, String description, String version) {
        this.namespace = namespace;
        this.modelName = modelName;
        this.description = description;
        this.version = version;
    }


    public void createModel(MockMvc mockMvc) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user = user("admin")
            .password("pass")
            .authorities(
                SpringUserUtils
                    .toAuthorityList(Sets.newHashSet(ADMIN, MODEL_CREATOR, USER))
            );
        createModel(mockMvc, user);
    }

    public void createModel(MockMvc mockMvc, SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user1)
        throws Exception {
        mockMvc.perform(
            post("/rest/default/models/" + prettyName + "/InformationModel")
                .with(user1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated());
    }


    public static final class TestModelBuilder {
        String namespace = RandomStringUtils.random(10, true, false).toLowerCase();
        String modelName = RandomStringUtils.random(10, true, false).toUpperCase();
        String description = "InformationModel for " + modelName;
        String version = "1.0.0";

        private TestModelBuilder() {
        }

        public static TestModelBuilder aTestModel() {
            return new TestModelBuilder();
        }

        public TestModelBuilder withNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public TestModelBuilder withModelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public TestModelBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public TestModelBuilder withVersion(String version) {
            this.version = version;
            return this;
        }

        public TestModel build() {
            return new TestModel(namespace, modelName, description, version);
        }
    }
}
