package br.com.tecnobiz.oauth2.samples.controllers;

import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public abstract class AccessTokenController extends BaseController {

    public AccessTokenController() {
	super();
    }

    protected final void setAccessToken(OAuth2AccessToken accessToken) {
	this.getModelAndView().addObject("accessToken", accessToken);
    }

    protected final void setResourceDetails(
	    ResourceOwnerPasswordResourceDetails resourceDetails) {
	this.getModelAndView().addObject("resourceDetails", resourceDetails);
    }
    
}
