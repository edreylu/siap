/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siap.siap.rowMapper;

import com.siap.siap.entidad.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author usuario
 */
public class RoleRowMapper implements RowMapper<Role> {
//generar un formato especifico del objeto que necesitas para la vista.
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role ro = new Role();
        ro.setId(rs.getInt("id"));
        ro.setRole(rs.getString("role"));

        return ro;

    }

}
