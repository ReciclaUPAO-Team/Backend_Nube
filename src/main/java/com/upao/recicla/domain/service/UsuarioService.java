package com.upao.recicla.domain.service;

import com.upao.recicla.domain.entity.NivelUsuario;
import com.upao.recicla.domain.entity.Rol;
import com.upao.recicla.domain.entity.Usuario;
import com.upao.recicla.infra.repository.UsuarioRepository;
import com.upao.recicla.infra.security.JwtService;
import com.upao.recicla.infra.security.LoginRequest;
import com.upao.recicla.infra.security.TokenResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        Usuario user=usuarioRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user, user);
        return TokenResponse.builder()
                .token(token)
                .build();
    }

    public TokenResponse addUsuario(Usuario usuario) {
        Usuario user = Usuario.builder()
                .username(usuario.getUsername())
                .password(passwordEncoder.encode( usuario.getPassword()))
                .nombre(usuario.getNombre())
                .edad(usuario.getEdad())
                .telefono(usuario.getTelefono())
                .correo(usuario.getCorreo())
                .puntos(0.0)
                .dni(usuario.getDni())
                .rol(Rol.PARTICIPANTE)
                .nivel(NivelUsuario.PLATA)
                .build();

        usuarioRepository.save(user);

        String token = jwtService.getToken(user, user);
        return TokenResponse.builder()
                .token(token)
                .build();
    }

    public Page<Usuario> getAllUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }
    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }
    public Usuario getReferenceById(Long id) {
        return usuarioRepository.getReferenceById(id);
    }

    @Transactional
    public void cambiarRolUsuario(Long usuarioId, Rol nuevoRol) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setRol(nuevoRol);
        usuarioRepository.save(usuario);
    }
    /*public void addUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }*/
    public void updateUsuario(Usuario usuario, Long id) {
        Usuario usuarioExists = usuarioRepository.findById(id)
                .orElse(new Usuario());

        usuarioExists.setRol(usuario.getRol());
        usuarioRepository.save(usuarioExists);
    }
    public void deleteUsuarioById(Long id){
        usuarioRepository.deleteById(id);
    }

}
