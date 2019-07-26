package com.lyz.springboot_neo4j.service;

import com.lyz.springboot_neo4j.config.SqlServerConfig;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhzy on 19-7-19.
 */
@Service
public class SqlServerService {

    private static final Connection conn = SqlServerConfig.getConnection();


    public void find_keywords() throws SQLException {

        String sql = "select keywords_cn from sd_paper";

        PreparedStatement pre = conn.prepareStatement(sql);

        ResultSet rs = pre.executeQuery();

        System.out.println(rs);
    }

}
