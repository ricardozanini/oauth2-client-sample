package br.com.tecnobiz.oauth2.samples.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.inject.Inject;
import javax.net.ssl.SSLContext;

import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Scope(WebApplicationContext.SCOPE_SESSION)
@Controller
public class CertificateUserPasswordController {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(CertificateUserPasswordController.class);

    @Inject
    private OAuth2ClientContext context;

    private final ModelAndView modelAndView;

    public CertificateUserPasswordController() {
	this.modelAndView = new ModelAndView("cert-password");
	this.setResourceDetails(new ResourceOwnerPasswordResourceDetails());
    }

    protected final void setResourceDetails(
	    ResourceOwnerPasswordResourceDetails resourceDetails) {
	this.modelAndView.addObject("resourceDetails", resourceDetails);
    }

    protected final void setAccessToken(OAuth2AccessToken accessToken) {
	this.modelAndView.addObject("accessToken", accessToken);
    }

    @RequestMapping(value = "/cert-password/", method = RequestMethod.GET)
    public ModelAndView formView() {
	return this.modelAndView;
    }

    @RequestMapping(value = "/cert-password/", method = RequestMethod.POST)
    public ModelAndView obterAccessToken(
	    ResourceOwnerPasswordResourceDetails resourceDetails,
	    @RequestParam("keystore") MultipartFile ks) throws IOException {
	final OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(
		resourceDetails, context);
	final ResourceOwnerPasswordAccessTokenProvider tokenProvider = new ResourceOwnerPasswordAccessTokenProvider();

	try {
	    final ClientHttpRequestFactory requestFactory = this
		    .createClientCertificateAuthRequestFactory(
			    ks.getInputStream(),
			    resourceDetails.getClientSecret());
	    
	    resourceDetails.setClientId(ks.getOriginalFilename());
	    // request factory para as operacoes OAuth2
	    restTemplate.setRequestFactory(requestFactory);
	    // request factory do provider (recuperação do token)
	    tokenProvider.setRequestFactory(requestFactory);
	    // associa o provider ao restTemplate das operações OAuth2
	    restTemplate.setAccessTokenProvider(tokenProvider);
	} catch (IOException e) {
	    LOGGER.error("[obterAccessToken] Impossible to load keystore file", e);
	    // TODO: our annotated exception would be nice
	    throw e;
	}

	// the authentication happens on SSLContext.
	resourceDetails
		.setClientAuthenticationScheme(AuthenticationScheme.none);

	final OAuth2AccessToken accessToken = restTemplate.getAccessToken();

	this.setResourceDetails(resourceDetails);
	this.setAccessToken(accessToken);

	HttpClients.custom().setSSLSocketFactory(null);

	return this.modelAndView;
    }

    private ClientHttpRequestFactory createClientCertificateAuthRequestFactory(
	    InputStream ks, String ksPass) throws IOException {

	final CloseableHttpClient httpClient = HttpClients
		.custom()
		.setSSLSocketFactory(
			this.createSSLConnectionSocketFactory(ks, ksPass))
		.build();

	final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
		httpClient);

	return requestFactory;
    }

    private LayeredConnectionSocketFactory createSSLConnectionSocketFactory(
	    InputStream ks, String ksPass) throws IOException {
	SSLContext sslContext = null;

	try {
	    final KeyStore keystore = KeyStore.getInstance("PKCS12",
		    BouncyCastleProvider.PROVIDER_NAME);
	    keystore.load(ks, ksPass.toCharArray());
	    sslContext = SSLContexts.custom()
		    .loadKeyMaterial(keystore, ksPass.toCharArray()).build();
	} catch (GeneralSecurityException e) {
	    throw new IllegalArgumentException("Cannot load the keystore", e);
	} finally {
	    ks.close();
	}

	final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
		sslContext, new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" }, null,
		SSLConnectionSocketFactory.getDefaultHostnameVerifier());

	return sslsf;
    }

}
