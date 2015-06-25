package br.com.tecnobiz.oauth2.samples.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Inicializa a aplicação Web MVC no Spring.
 * 
 * @see <a href="http://www.mkyong.com/spring-security/spring-security-hello-world-annotation-example/">Spring Security Hello World Annotation Example</a>
 * @see <a href="http://www.kubrynski.com/2014/01/understanding-spring-web-initialization.html">Understanding Spring Web Initialization</a>
 * @author Ricardo Zanini (ricardozanini@gmail.com)
 */
public class SpringMvcWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { WebAppConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

}
