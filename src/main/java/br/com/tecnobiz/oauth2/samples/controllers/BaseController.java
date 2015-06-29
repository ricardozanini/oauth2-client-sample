package br.com.tecnobiz.oauth2.samples.controllers;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {

    private final ModelAndView modelAndView;

    public BaseController() {
	this.modelAndView = new ModelAndView(this.getViewName());
    }

    public abstract String getViewName();

    protected final ModelAndView getModelAndView() {
	return modelAndView;
    }

    protected final void handleError(Throwable exception) {
	this.clearErrors();
	this.handleError(exception, this.modelAndView.getModelMap());
    }

    protected final void handleError(Throwable exception,
	    Map<String, Object> model) {
	model.put("error", true);
	
	if (exception == null) {
	    // TODO: messages.properties
	    model.put("errorMsg", "Erro desconhecido");
	} else {
	    model.put("errorMsg", exception.getLocalizedMessage());
	}
    }

    protected final void clearErrors() {
	this.modelAndView.addObject("error", false);
	this.modelAndView.addObject("errorMsg", null);
    }
}
