/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siap.siap.dao;

import com.siap.siap.entidad.User;
import com.siap.siap.rowMapper.UserRowMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author usuario
 */
@Transactional
@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    List lista = null;
    String sql;

    public List<User> traeRegistros() {
        List listaPersonas = null;
        String query = null;
        query = "select us.id, "
                + "us.username, "
                + "us.password,"
                + "us.enabled, "
                + "us.nombre,"
                + "us.apellido_paterno,"
                + "us.apellido_materno,"
                + "us.correo, "
                + "us.telefono, \n"
                + "ro.id id_role, "
                + "SUBSTRING(ro.role, 6) role"
                + "  FROM users us, user_roles ur, roles ro"
                + "  where us.id = ur.id_user \n"
                + "  and ur.id_role = ro.id \n"
                + "  order by 1";

        listaPersonas = jdbcTemplate.query(query, new UserRowMapper());
        return listaPersonas;
    }

    public int insertaUsuario(User us) {
        int id = nextValueId();

        sql = "INSERT INTO users (id,"
                + "username, "
                + "password, "
                + "nombre,"
                + "apellido_paterno,"
                + "apellido_materno, "
                + "correo, "
                + "telefono) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        int valor = jdbcTemplate.update(sql,
                id,
                us.getUserName(),
                new BCryptPasswordEncoder(4).encode(us.getPassword()),
                us.getNombre(),
                us.getApellidoPaterno(),
                us.getApellidoMaterno(),
                us.getCorreo(),
                us.getTelefono());

        sql = "INSERT INTO user_roles (id_user,id_role) "
                + "VALUES (?,?)";
        jdbcTemplate.update(sql, id, us.getRole().getId());

        return valor;
    }

    public int editaUsuario(User us) {
        sql = "UPDATE users set username = ?, "
                + " nombre = ? ,"
                + "apellido_paterno = ? ,"
                + "apellido_materno = ? , "
                + "correo = ?,"
                + "telefono = ? "
                + " where id = ? ";
        int valor = jdbcTemplate.update(sql,
                us.getUserName(),
                us.getNombre(),
                us.getApellidoPaterno(),
                us.getApellidoMaterno(),
                us.getCorreo(),
                us.getTelefono(),
                us.getId());
        sql = "UPDATE user_roles set id_role = ? "
                + " where id_user = ? ";
        jdbcTemplate.update(sql, us.getRole().getId(), us.getId());

        return valor;
    }

    public User usuarioEditar(int id) {

        User us;
        sql = "select us.id, "
                + "us.username, "
                + "us.password,"
                + "us.enabled, "
                + "us.nombre,"
                + "us.apellido_paterno,"
                + "us.apellido_materno,"
                + "us.correo, "
                + "us.telefono, \n"
                + "ro.id id_role, "
                + "ro.role role"
                + " from users us, "
                + "      user_roles ur, "
                + "      roles ro "
                + " where us.id = ? "
                + "  and us.id = ur.id_user \n"
                + "  and ur.id_role = ro.id \n";
        us = jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
        return us;
    }

    public int eliminaUsuario(int id) {
        sql = "delete from users where id = ?";
        int valor = jdbcTemplate.update(sql, id);

        return valor;
    }

    public int nextValueId() {
        sql = "select max(id)+1 from users";
        Integer valor = jdbcTemplate.queryForObject(sql, Integer.class);
        return valor;
    }

    public String nombreUsuario(String username) {
        sql = "select CONCAT(nombre,' ',apellido_paterno,' ',apellido_materno) "
                + "from users where username = ? ";
        String nombre = jdbcTemplate.queryForObject(sql, new Object[]{username}, String.class);
        return nombre;
    }

}
