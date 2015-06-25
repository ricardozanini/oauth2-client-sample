package br.com.tecnobiz.oauth2.samples.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * Arquivo de configuração do cliente do OAuth2.
 * 
 * @author Ricardo Zanini (ricardozanini@gmail.com)
 *
 */
@Configuration
@EnableOAuth2Client
// anotação para adicionar a configuração dos beans de conectividade OAuth2.
public class OAuth2ClientConfig {

    public OAuth2ClientConfig() {

    }

}
