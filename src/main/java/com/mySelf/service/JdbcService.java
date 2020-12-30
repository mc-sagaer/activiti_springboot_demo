package com.mySelf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Component 这个注解加的并不规范，本来是做成静态工具类，但jdbcTemplate是null
 * 为了注入，加了 @PostConstruct，配合把实例对象的值注入静态jdbcTemplate
 * 仅作演示吧
 */
@Component
public class JdbcService {

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplate jdbcTemplate2;

    @PostConstruct
    public void  isGetInstance(){
        if(jdbcTemplate == null){
            jdbcTemplate = jdbcTemplate2;
        }

    }


    public static List find(String sql, Object... obj) {
        return jdbcTemplate.queryForList(sql, obj);
    }

    public static boolean update(String sql, Object... obj) {
        return jdbcTemplate.update(sql, obj) > 0;
    }





}
