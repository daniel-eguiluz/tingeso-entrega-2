package com.tutorial.msusuario.service;

import com.tutorial.msusuario.entity.UsuarioEntity;
import com.tutorial.msusuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public List<UsuarioEntity> getAll() {
        return usuarioRepository.findAll();
    }

    public UsuarioEntity getUsuarioById(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public UsuarioEntity save(UsuarioEntity usuario) {
        return usuarioRepository.save(usuario);
    }

    public UsuarioEntity update(UsuarioEntity usuario) {
        // Se asume que el usuario ya existe, de lo contrario, se agrega lógica adicional.
        return usuarioRepository.save(usuario);
    }

    public boolean deleteById(int id) {
        if(usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
