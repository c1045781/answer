package top.qiyoung.answer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

   /* @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
        manager.createUser(User.withUsername("lisi").password("456").authorities("p2").build());
        return manager;
    }*/

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/manager/**").hasAnyAuthority("0","1")//所有/r/**的请求必须认证通过
                .antMatchers("/user/exercise/uploadFile").hasAuthority("3")
                .antMatchers("/user/exercise/addOrUpdate").hasAuthority("3")
                .antMatchers("/user/exercise/toUpdate").hasAuthority("3")
                .antMatchers("/user/**").hasAnyAuthority("2","3")//所有/r/**的请求必须认证通过
                .antMatchers("/**").authenticated()
                .anyRequest().permitAll()//除了/r/**，其它的请求可以访问
                .and()
                .formLogin()//允许表单登录
                .loginPage("/toLogin")//登录页面
                .loginProcessingUrl("/login")
                .successForwardUrl("/index")//自定义登录成功的页面地址
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/toLogin");
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
        web.ignoring().antMatchers("/uploa  d/**");
        web.ignoring().antMatchers("/toLogin");
        web.ignoring().antMatchers("/user/exercise/check");
        web.ignoring().antMatchers("/user/exerciseSet/check");
        web.ignoring().antMatchers("/toRegister");
        web.ignoring().antMatchers("/register");
        web.ignoring().antMatchers("/subject/base");
        web.ignoring().antMatchers("/");
        web.ignoring().antMatchers("/subject/subjectByBase");

        //解决服务注册url被拦截的问题
    }


}

