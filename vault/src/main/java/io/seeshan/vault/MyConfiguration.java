package io.seeshan.vault;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Vault KV:
 * # vault secrets enable -path=secret/ kv-v2
 * # vault kv put secret/spring-cloud-vault example.username=seeshan example.password=pwd
 * <p>
 * --spring.profiles.active=cloud
 * # vault kv put secret/spring-cloud-vault/cloud example.username=seeshan_cloud example.password=pwd
 */
@ConfigurationProperties("example")
public class MyConfiguration {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
