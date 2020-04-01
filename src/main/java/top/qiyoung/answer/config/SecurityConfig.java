package top.qiyoung.answer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import top.qiyoung.answer.service.MyUserDetailsService;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService myUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/toLogin").permitAll()
                .antMatchers("/user/exercise/check").permitAll()
                .antMatchers("/user/exerciseSet/check").permitAll()
                .antMatchers("/toRegister").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/subject/base").permitAll()
                .antMatchers("/subject/subjectByBase").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/user/exercise/uploadFile").hasAuthority("3")
                .antMatchers("/user/exercise/addOrUpdate").hasAuthority("3")
                .antMatchers("/user/exercise/toUpdate").hasAuthority("3")
                .antMatchers("/user/**").hasAnyAuthority("2","3")
                .antMatchers("/manager/**").hasAnyAuthority("0","1")
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/toLogin")//登录页面
                    .loginProcessingUrl("/login")
                    .successForwardUrl("/index")//自定义登录成功的页面地址
                .and()
                    .rememberMe()
                    .tokenValiditySeconds(7 * 24 * 60 * 60)
                    .userDetailsService(myUserDetailsService)
                    .tokenRepository(persistentTokenRepository())
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/toLogin")
                .and()
                    .csrf().disable();
    }

    /* 放行静态资源 */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/fonts/**");
        web.ignoring().antMatchers("/images/**");
        web.ignoring().antMatchers("/img/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/upload/**");

        //解决服务注册url被拦截的问题
    }

    // 设置记住我功能
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource); // 设置数据源
        return tokenRepository;
    }


}

