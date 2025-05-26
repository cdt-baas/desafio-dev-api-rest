package com.wendersonp.account.infrastructure.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "account")
public class AccountDefaultProperties {

    private String defaultBranch;

    private String withdrawDefaultLimit;
}
