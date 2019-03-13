package org.kevin.admin.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTToken implements AuthenticationToken {
	private static final long serialVersionUID = 9217639903967592166L;
	
	private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }


    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public String toString(){
        return token;
    }
}