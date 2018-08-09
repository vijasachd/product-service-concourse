package com.apbg.webstore.productservice.config;

import org.springframework.context.annotation.Profile;

import java.util.Properties;

@Profile("cloud")
public interface CloudConfigProperties {
    Properties cloudProperties();
}
