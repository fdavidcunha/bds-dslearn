package com.devsuperior.dslearnbds.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

// @EnableResourceServer -> Informa que essa é a classe que representa o ResourceServer do oauth.
// É classe que recebe a requisição de algum recurso + o token e diz se pode ou não entregar o recurso.

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	// Environment -> Objeto especial do ambiente de exceução da aplicação.
	@Autowired private Environment env;
	@Autowired private JwtTokenStore tokenStore;

	// Endpoints liberados para acesso geral, sem login.
	private static final String[] PUBLIC = {"/oauth/token", "/h2-console/**"};
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		// Dessa forma o resourceServer é capaz de decodificar o token e analisar se o token é válido. 
		resources.tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		// env.getActiveProfiles() -> Profiles de execução que estão rodando.
		// Verificando se nos profiles ativos contém o profile "test".
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			// Liberando o banco dedados H2.
			http.headers().frameOptions().disable();
		}
		
		// Definindo quem pode acessar cada endpoints.
		http.authorizeRequests()
			.antMatchers(PUBLIC).permitAll() // Rotas liberadas pra todos.
			.anyRequest().authenticated();   // Qualquer outra rota só é liberada por meio de login.
	}
}