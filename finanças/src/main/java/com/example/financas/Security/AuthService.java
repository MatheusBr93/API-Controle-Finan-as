package com.example.financas.Security;

import com.example.financas.dto.AuthRequest;
import com.example.financas.dto.AuthResponse;
import com.example.financas.model.Usuario;
import com.example.financas.Security.JwtService;
import com.example.financas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest request) {

        try {
            System.out.println("1. Autenticando credenciais do usuário com o Spring Security. Email: " + request.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            System.out.println("2. Usuário autenticado com sucesso!");


            Usuario usuarioAutenticado = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado no sistema após autenticação bem-sucedida."));

            System.out.println("3. Usuário obtido do repositório: " + usuarioAutenticado.getEmail());


            String roleString = usuarioAutenticado.getRole();
            List<String> rolesList = Collections.singletonList(roleString);

            System.out.println("4. Gerando token JWT para o usuário: " + request.getEmail() + " com roles: " + rolesList);
            String jwtToken = jwtService.generateToken(request.getEmail(), rolesList);

            System.out.println("5. Token gerado: " + jwtToken);


            return new AuthResponse(usuarioAutenticado.getId(),usuarioAutenticado.getNome(), request.getEmail(), jwtToken, "Login bem-sucedido.");

        } catch (AuthenticationException e) {
            System.err.println("Erro de autenticação para o email " + request.getEmail() + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {

            System.err.println("Erro inesperado no AuthService.login: " + e.getMessage());
            throw new RuntimeException("Erro inesperado durante o login", e);
        }

    }

    public AuthResponse register(AuthRequest request) {
        try {
            System.out.println("1. Iniciando processo de registro para o email: " + request.getEmail());


            if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
                System.err.println("Usuário com o email " + request.getEmail() + " já existe.");
                throw new RuntimeException("Email já registrado.");
            }

            Usuario novoUsuario = new Usuario();
            novoUsuario.setEmail(request.getEmail());
            novoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));
            String requestedRole = request.getRole();
            if (requestedRole != null && (requestedRole.equals("ADMIN") || requestedRole.equals("USER"))) {
                novoUsuario.setRole(requestedRole);
            } else {
                novoUsuario.setRole("USER");
            }

            System.out.println("2. Salvando novo usuário no banco de dados.");
            Usuario salvoUsuario = usuarioRepository.save(novoUsuario);
            System.out.println("3. Usuário salvo com sucesso. ID: " + salvoUsuario.getId() + ", Email: " + salvoUsuario.getEmail());

            String roleString = salvoUsuario.getRole();
            List<String> rolesList = Collections.singletonList(roleString);

            System.out.println("4. Gerando token JWT para o novo usuário: " + salvoUsuario.getEmail() + " com roles: " + rolesList);
            String jwtToken = jwtService.generateToken(salvoUsuario.getEmail(), rolesList);
            System.out.println("5. Token gerado: " + jwtToken);

            return new AuthResponse(salvoUsuario.getId(), salvoUsuario.getNome(), salvoUsuario.getEmail(), jwtToken, "Registro bem-sucedido.");

        } catch (RuntimeException e) {
            System.err.println("Erro durante o registro para o email " + request.getEmail() + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado no AuthService.register: " + e.getMessage());
            throw new RuntimeException("Erro inesperado durante o registro", e);
        }
    }
}