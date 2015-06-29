package br.com.tecnobiz.oauth2.samples.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController extends BaseController {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(ErrorController.class);

    public ErrorController() {

    }

    @Override
    public String getViewName() {
	return "error";
    }

    @RequestMapping(value = "/error/500")
    public String redirectFromServerError(HttpServletRequest req, Model model) {
	LOGGER.debug("[redirectFromServerError] Server error");
	final Throwable throwable = (Throwable) req
		.getAttribute("javax.servlet.error.exception");

	this.handleError(throwable, model.asMap());

	model.addAttribute("errorCode",
		req.getAttribute("javax.servlet.error.status_code"));

	return this.getViewName();
    }
}
