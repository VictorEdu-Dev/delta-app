package org.deltacore.delta.core.config.extern_api;

import com.google.api.client.util.Value;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageGoogleConfig {
    @Value("${gcp.project.id}")
    private String projectId;

    @Bean
    public Storage storage() {
        return StorageOptions.newBuilder().setProjectId(projectId).build().getService();
    }
}
