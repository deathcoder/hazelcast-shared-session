/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.session;

import javax.servlet.http.HttpSession;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class HelloRestController {
    private static Logger logger = LoggerFactory.getLogger( HelloRestController.class );

    @GetMapping("/")
	String uid(HttpSession session) {
		Integer counter = (Integer) session.getAttribute("gateway-counter");
		if (counter == null) {
			counter = 0;
		}
		session.setAttribute("gateway-counter", counter + 1);
		return session.getId();
	}

	@GetMapping("/counter")
	String counter(HttpSession session) {
		Integer counter = (Integer) session.getAttribute("gateway-counter");
		if (counter == null) {
			counter = 0;
		}
		return counter.toString();
	}

	@GetMapping("/service-1")
	String service1( HttpSession session, @RequestHeader("Cookie") String cookie ) {
		// create an http request to another service propagating session id
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        logger.info( "adding cookie [{}] to service-1 call", cookie );
        headers.set("Cookie", cookie);
        URI uri = URI.create( "http://localhost:8082/" );
        RequestEntity<Object> request = new RequestEntity<>( headers, HttpMethod.GET, uri );
        String result = restTemplate.exchange( request, String.class ).getBody();
        if( result.contains( "login" )) {
            logger.info( "service-1 sent the login page" );
            return "please login";
        }
        logger.info( "service-1 sent the session id" );
        assert result.equals( session.getId() );
        logger.info( "service-1 successfully shared session with the api-gtw" );
        return result;
    }

    @GetMapping("/service-1/counter")
    String service1Counter(HttpSession session, @RequestHeader("Cookie") String cookie ) {
        // create an http request to another service propagating session id
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        logger.info( "adding cookie [{}] to service-1 call", cookie );
        headers.set("Cookie", cookie);
        URI uri = URI.create( "http://localhost:8082/counter" );
        RequestEntity<Object> request = new RequestEntity<>( headers, HttpMethod.GET, uri );
        String result = restTemplate.exchange( request, String.class ).getBody();
        if( result.contains( "login" )) {
            logger.info( "service-1 sent the login page" );
            return "please login";
        }
        logger.info( "service-1 sent the shared counter" );
        assert result.equals( session.getAttribute( "gateway-counter" ) );
        logger.info( "service-1 successfully shared session with the api-gtw" );
        return result;

    }

    @GetMapping("/service-1/custom-object")
    String service1CustomObject(HttpSession session, @RequestHeader("Cookie") String cookie ) {
        // create an http request to another service propagating session id
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        logger.info( "adding cookie [{}] to service-1 call", cookie );
        headers.set("Cookie", cookie);
        URI uri = URI.create( "http://localhost:8082/custom-object" );
        RequestEntity<Object> request = new RequestEntity<>( headers, HttpMethod.GET, uri );
        String result = restTemplate.exchange( request, String.class ).getBody();
        if( result.contains( "login" )) {
            logger.info( "service-1 sent the login page" );
            return "please login";
        }
        logger.info( "service-1 sent the custom object and we didn't blow up! yay!" );
        return result;

    }

    @GetMapping("/service-1/expire")
    String service1Logout(HttpSession session, @RequestHeader("Cookie") String cookie ) {
        // create an http request to another service propagating session id
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        logger.info( "adding cookie [{}] to service-1 call", cookie );
        headers.set("Cookie", cookie);
        URI uri = URI.create( "http://localhost:8082/expire" );
        RequestEntity<Object> request = new RequestEntity<>( headers, HttpMethod.GET, uri );
        String result = restTemplate.exchange( request, String.class ).getBody();
        if( result.contains( "login" )) {
            logger.info( "service-1 sent the login page" );
            return result;
        }
        logger.info( "service-1 logged out" );
        return result;

    }

}
