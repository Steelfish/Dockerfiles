package odyssion.springboot.admin.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String REDIRECT_PATH = "redirectTo";
    private static final String ASSETS_PATH = "/assets/**";
    // private static final String INSTANCES_URLS_PATH = "/instances/**"
    private static final String INSTANCES_PATH = "/instances/**";
    private static final String LOGIN_PATH = "/login";
    private static final String LOGOUT_PATH = "/logout";
    private final String adminContextPath;

    public SecurityConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler
                = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter(REDIRECT_PATH);

        http.authorizeRequests()
            .antMatchers(adminContextPath + ASSETS_PATH).permitAll()
            .antMatchers(adminContextPath + LOGIN_PATH).permitAll()
            .antMatchers(adminContextPath + INSTANCES_PATH).permitAll()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
            .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage(adminContextPath + LOGIN_PATH)
                .successHandler(successHandler)
            .and()
                .logout()
                .logoutUrl(adminContextPath + LOGOUT_PATH)
            .and()
                .httpBasic()
            .and()
                .csrf()
                .disable();
    }
}