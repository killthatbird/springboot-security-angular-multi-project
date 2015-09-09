package demo

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component


/**
 * CORS Negotiation
 * The browser tries to negotiate with our resource server to find out if it is allowed
 * to access it according to the Cross Origin Resource Sharing protocol. It’s not an Angular JS
 * responsibility, so just like the cookie contract it will work like this with all JavaScript
 * in the browser. The two servers do not declare that they have a common origin,
 * so the browser declines to send the request and the UI is broken.
 * 
 * To fix that we need to support the CORS protocol which involves a "pre-flight" OPTIONS
 * request and some headers to list the allowed behaviour of the caller. Spring 4.2 might have
 * some nice fine-grained CORS support, but until that is released we can do an adequate job
 * for the purposes of this application by sending the same CORS responses to all requests
 * using a Filter. We can just create a class in the same directory as the resource server
 * application and make sure it is a @Component (so it gets scanned into the Spring
 * application context)
 * 
 * The Filter is defined with an @Order so that it is definitely applied before the main Spring
 * Security filter. With that change to the resource server, we should be able to re-launch it
 * and get our greeting in the UI.
 *  
 * TODO: Cuando esté lista la version 4.2 de Spring, se puede hacer como indica en esta url:
 * https://dzone.com/articles/cors-support-spring-framework
 * 
 * @author oscar.alvarez
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilter implements Filter {

	void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res
		HttpServletRequest request = (HttpServletRequest) req
		response.setHeader("Access-Control-Allow-Origin", "*")
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE")
		response.setHeader("Access-Control-Max-Age", "3600")
		response.setHeader("Access-Control-Allow-Headers", "x-auth-token, x-requested-with")
		if (request.getMethod()!='OPTIONS') {
			chain.doFilter(req, res)
		} else {
		}
	}

	void init(FilterConfig filterConfig) {}
	void destroy() {}
}
