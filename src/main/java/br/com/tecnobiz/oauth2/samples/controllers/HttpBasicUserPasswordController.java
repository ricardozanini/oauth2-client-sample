package br.com.tecnobiz.oauth2.samples.controllers;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@Scope(WebApplicationContext.SCOPE_SESSION)
@Controller
public class HttpBasicUserPasswordController extends AccessTokenController {

    @Inject
    private OAuth2ClientContext context;

    public HttpBasicUserPasswordController() {
	this.setResourceDetails(new ResourceOwnerPasswordResourceDetails());
    }

    @RequestMapping(value = "/httpbasic-password/", method = RequestMethod.GET)
    public ModelAndView formView() {
	return this.getModelAndView();
    }

    @RequestMapping(value = "/httpbasic-password/", method = RequestMethod.POST)
    public ModelAndView obterAccessToken(
	    ResourceOwnerPasswordResourceDetails resourceDetails) {
	final OAuth2AccessToken accessToken = new OAuth2RestTemplate(
		resourceDetails, context).getAccessToken();

	this.setResourceDetails(resourceDetails);
	this.setAccessToken(accessToken);

	return this.getModelAndView();
    }

    @Override
    public String getViewName() {
	return "httpbasic-password";
    }
}
