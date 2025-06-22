package com.example.financas.Security;

import jakarta.servlet.FilterChain;
import com.example.financas.Security.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.financas.model.Usuario;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);

            if (jwt.isEmpty()) {
                System.err.println("Token JWT extraído do cabeçalho está vazio. Pulando validação JWT.");
                filterChain.doFilter(request, response);
                return;
            }
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (JwtException e) {
                System.err.println("Erro ao processar token JWT (malformado ou inválido): " + e.getMessage());



            } catch (Exception e) {
                System.err.println("Erro inesperado ao extrair username do token JWT: " + e.getMessage());
                e.printStackTrace();
            }
        } else {

            System.out.println("Nenhum token JWT 'Bearer' encontrado no cabeçalho de autorização. Requisição continua como não autenticada.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());


                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("Usuário " + username + " autenticado com sucesso.");
            } else {
                System.err.println("Falha na validação do token JWT (username não corresponde ou expirado) para o usuário: " + username);
            }
        }

        filterChain.doFilter(request, response);
    }
}
