package com.example.odataapp01.odata;

import javax.ws.rs.core.MultivaluedMap;

import org.odata4j.consumer.ODataClientRequest;
import org.odata4j.jersey.consumer.behaviors.JerseyClientBehavior;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.Filterable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SAPCSRFBehavior implements JerseyClientBehavior {  
    private String xcsrfToken = "";  
    //private List<String> setCookie = Collections.singletonList("");
    private ClientResponse savedResponse;
    private String cookieString;

    @Override  
    public ODataClientRequest transform(ODataClientRequest request) {  
        if(request.getMethod().equals("GET")){  
            request = request
                        .header("X-CSRF-Token", "Fetch")
            		    .header("X-Requested-With", "XMLHttpRequest");
            return request;
        }else{

            List<String> headers = savedResponse.getHeaders().get("Set-Cookie");
            for (String temp : headers) {
                if (cookieString != null){
                    cookieString = cookieString + temp + "; ";
                }
                else {
                    cookieString = temp + "; ";
                }

            }
            cookieString = cookieString.substring(0, cookieString.length() - 2);

            request = request
                        .header("X-CSRF-Token", this.xcsrfToken)
            		    .header("X-Requested-With", "XMLHttpRequest")
                        .header("Cookie", cookieString);

            return request;

        }  
          
    }  
    @Override  
    public void modify(ClientConfig arg0) {  
        // TODO Auto-generated method stub  
          
    }  
    @Override  
    public void modifyClientFilters(Filterable client) {  
        client.addFilter(new ClientFilter(){  
            @Override  
            public ClientResponse handle(ClientRequest clientRequest)  
                    throws ClientHandlerException {  
                ClientResponse response = this.getNext().handle(clientRequest);
                savedResponse = response;
                MultivaluedMap<String, String> headers = response.getHeaders();  
                xcsrfToken = headers.getFirst("X-CSRF-Token");
                System.out.println("Token: " + xcsrfToken);
                //setCookie = headers.getFirst("Set-Cookie");
                //setCookie = headers.get("Set-Cookie");
                return response;
            }  
         });  
    }  
    @Override  
    public void modifyWebResourceFilters(Filterable arg0) {  
        // TODO Auto-generated method stub  
          
    }
}
