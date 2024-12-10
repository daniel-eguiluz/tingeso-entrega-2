package com.tutorial.msusuario.controller;

import com.tutorial.msusuario.entity.UsuarioEntity;
import com.tutorial.msusuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> getAll() {
        List<UsuarioEntity> usuarios = usuarioService.getAll();
        if(usuarios.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> getById(@PathVariable("id") int id) {
        UsuarioEntity usuario = usuarioService.getUsuarioById(id);
        if(usuario == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<UsuarioEntity> save(@RequestBody UsuarioEntity usuario) {
        UsuarioEntity usuarioNuevo = usuarioService.save(usuario);
        return ResponseEntity.ok(usuarioNuevo);
    }

    @PutMapping
    public ResponseEntity<UsuarioEntity> update(@RequestBody UsuarioEntity usuario) {
        if(usuario.getId() == 0)
            return ResponseEntity.badRequest().build();
        UsuarioEntity usuarioActualizado = usuarioService.update(usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        boolean deleted = usuarioService.deleteById(id);
        if(!deleted)
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
