package com.devsuperior.dslearnbds.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.devsuperior.dslearnbds.entities.User;
import com.devsuperior.dslearnbds.repositories.UserRepository;

@Component
public class JWTTokenEnhancer implements TokenEnhancer {

	@Autowired private UserRepository userRepository;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		// authentication.getName() -> Retorna o nome do usuário, que neste caso é o e-mail.
		User user = userRepository.findByEmail(authentication.getName());
		
		// Adicionando informações extras do usuário no token.
		Map<String, Object> map = new HashMap<>();
		map.put("userID", user.getId());
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		token.setAdditionalInformation(map);
		
		return token;
	}
}