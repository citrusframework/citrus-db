package com.consol.citrus.demo.controller;

import com.consol.citrus.demo.model.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.CallableStatement;
import java.sql.JDBCType;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Christoph Deppisch
 */
@Controller
@RequestMapping(path="/city")
public class CityController {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(CityController.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CityController(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping(path="/add")
    public @ResponseBody
    String addNewProjects (@RequestParam final String names) {
        final List<Object> splitUpNames = Arrays.asList(StringUtils.commaDelimitedListToStringArray(names));

        for(final Object name : splitUpNames) {
            if(log.isDebugEnabled()){
                log.debug(String.format("Inserting city %s", name));
            }
        }

        if (splitUpNames.size() > 1) {
            jdbcTemplate.batchUpdate("INSERT INTO cities(name) VALUES (?)", splitUpNames.stream()
                                                                                                .map(name -> new Object[] { name })
                                                                                                .collect(Collectors.toList()));
        } else {
            jdbcTemplate.update("INSERT INTO cities(name) VALUES (?)", splitUpNames.get(0));
        }
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<City> getAllProjects(@RequestParam(required = false, defaultValue = "") final String name) {
        if (StringUtils.hasText(name)) {
            return jdbcTemplate.query(
                    "SELECT id, name FROM cities WHERE name = ?", new Object[]{ name },
                    (rs, rowNum) -> new City(rs.getLong("id"), rs.getString("name"))
            );
        } else {
            return jdbcTemplate.query(
                    "SELECT id, name FROM cities",
                    (rs, rowNum) -> new City(rs.getLong("id"), rs.getString("name"))
            );
        }
    }

    @GetMapping(path="/findId")
    public @ResponseBody Iterable<City> findCityByName(@RequestParam(defaultValue = "") final String name) {
        if (StringUtils.hasText(name)) {
            final Map<String, Object> call = jdbcTemplate.call(createCallableStatement(name), createParameters());
            final ArrayList<LinkedCaseInsensitiveMap> resultSet = (ArrayList<LinkedCaseInsensitiveMap>) call.get("#result-set-1");
            return resultSet.stream().map(linkedCaseInsensitiveMap ->
                new City(Long.valueOf((Integer) linkedCaseInsensitiveMap.get("id")), (String) linkedCaseInsensitiveMap.get("name"))
            ).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private List<SqlParameter> createParameters() {
        return Collections.singletonList(new SqlParameter("name", Types.VARCHAR));
    }

    private CallableStatementCreator createCallableStatement(final String name) {
        return con ->{
                    final CallableStatement callableStatement = con.prepareCall("CALL findCityByName(?,?)");
                    callableStatement.registerOutParameter(2, JDBCType.INTEGER);
                    callableStatement.setString("name", name);
                    return callableStatement;
                };
    }
}
