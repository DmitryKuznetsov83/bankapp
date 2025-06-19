package ru.yandex.practicum.bank.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.yandex.practicum.bank.front.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//
//                // Stateless — не храним сессии
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//
//                // Настраиваем авторизацию
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**").permitAll()     // Разрешить регистрацию, логин
//                        .requestMatchers("/health", "/docs/**").permitAll()
//                        .anyRequest().authenticated()                // Остальное — требует авторизации
//                )
//
//                // Добавим фильтр перед стандартной авторизацией
//                .addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
//
//                // Отключаем стандартную форму входа
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable);
//
//        return http.build();

        //


        http
                .csrf(AbstractHttpConfigurer::disable)


//                // Разрешаем доступ к общедоступным страницам
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/main", "/logout").authenticated()
                        .requestMatchers("/**", "/main", "/logout").permitAll()
//                        .requestMatchers("/signup").anonymous()
                        .anyRequest().authenticated()
                )
//
//                // Используем стандартную форму входа
                .formLogin(form -> form
                        .defaultSuccessUrl("/main") // куда редиректить после успешного входа
                );
//
//                // Поддержка выхода
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                );

//                // Включаем сессионную авторизацию (по умолчанию включено)
//                .sessionManagement(session -> session
//                        .maximumSessions(1)           // только одна сессия на пользователя
//                        .maxSessionsPreventsLogin(false) // если true — блокирует повторный логин
//                );

        return http.build();


    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
