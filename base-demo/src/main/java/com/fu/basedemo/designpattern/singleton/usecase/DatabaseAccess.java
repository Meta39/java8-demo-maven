package com.fu.basedemo.designpattern.singleton.usecase;

import com.fu.basedemo.utils.ResultSetMapperUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 2024-07-25
 */
public class DatabaseAccess {

    public <T> List<T> getUsers(Class<T> clazz) {
        List<T> results = new ArrayList<>();

        String query = ResultSetMapperUtils.buildSelectQuery(clazz);

        try (Connection connection = DataSourceSingleton.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                T instance = ResultSetMapperUtils.resultSetToObject(resultSet, clazz);
                results.add(instance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }


}
