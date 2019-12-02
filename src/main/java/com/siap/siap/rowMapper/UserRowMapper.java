/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siap.siap.rowMapper;

import com.siap.siap.entidad.Role;
import com.siap.siap.entidad.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author usuario
 */
public class UserRowMapper implements RowMapper<User> {
//generar un formato especifico del objeto que necesitas para la vista.
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User us = new User();
        Role ro = new Role();
        us.setId(rs.getInt("id"));
        us.setUserName(rs.getString("username"));
        String encoded = new BCryptPasswordEncoder(4).encode(rs.getString("password"));
        us.setPassword(encoded);
        us.setNombre(rs.getString("nombre"));
        us.setApellidoPaterno(rs.getString("apellido_paterno"));
        us.setApellidoMaterno(rs.getString("apellido_materno"));
        us.setCorreo(rs.getString("correo"));
        us.setTelefono(rs.getString("telefono"));
        us.setEnabled(rs.getInt("enabled"));
        ro.setId(rs.getInt("id_role"));
        ro.setRole(rs.getString("role"));
        us.setRole(ro);
        return us;

    }

}
