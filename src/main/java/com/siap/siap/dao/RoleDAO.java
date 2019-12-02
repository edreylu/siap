/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siap.siap.dao;

import com.siap.siap.entidad.Role;
import com.siap.siap.rowMapper.RoleRowMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author usuario
 */
@Transactional
@Repository
public class RoleDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    List lista = null;
    String sql;

    public List<Role> traeRegistros() {
        List listaRoles = null;
        String query = null;
        query = "select id, SUBSTRING(role, 6) role \n"
                + "               FROM roles \n"
                + "               order by 1";
        listaRoles = jdbcTemplate.query(query, new BeanPropertyRowMapper(Role.class));
        return listaRoles;
    }

    public int insertaRol(Role ro) {

        sql = "INSERT INTO roles (role) "
                + "VALUES (?)";
        int valor = jdbcTemplate.update(sql, "ROLE_" + ro.getRole());

        return valor;
    }

    public int editaRol(Role ro) {
        sql = "UPDATE roles set role = ? "
                + " where id = ? ";
        int valor = jdbcTemplate.update(sql,
                "ROLE_" + ro.getRole(),
                ro.getId());

        return valor;
    }

    public Role rolEditar(int id) {

        Role ro;
        sql = "select id, SUBSTRING(role, 6) role "
                + "         from roles where id = ?";
        ro = jdbcTemplate.queryForObject(sql, new Object[]{id}, new RoleRowMapper());
        return ro;
    }

    public int eliminaRol(int id) {
        sql = "delete from roles where id = ?";
        int valor = jdbcTemplate.update(sql, id);

        return valor;
    }

}
