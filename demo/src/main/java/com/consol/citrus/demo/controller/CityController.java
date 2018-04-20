package com.consol.citrus.demo.controller;

import com.consol.citrus.demo.model.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christoph Deppisch
 */
@Controller
@RequestMapping(path="/city")
public class CityController {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(CityController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping(path="/add")
    public @ResponseBody
    String addNewProjects (@RequestParam String names) {
        List<Object> splitUpNames = Arrays.asList(StringUtils.commaDelimitedListToStringArray(names));

        for(Object name : splitUpNames) {
            log.info(String.format("Inserting city %s", name));
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
    public @ResponseBody Iterable<City> getAllProjects(@RequestParam(required = false, defaultValue = "") String name) {
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
}
