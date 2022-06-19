package com.travel.jdbc;

import com.travel.dto.Calculated;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class JdbcClient {

    private final JdbcTemplate jdbcTemplate;

    public JdbcClient(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Calculated> findAllWarrantsAndTheirCostOfTravelASC() {
        final String query = "SELECT warrants.id AS id_warrant, expenses.cost_of_travel FROM warrants JOIN expenses ON warrants.id_expenses = expenses.id ORDER BY cost_of_travel ASC";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) ->
                        new Calculated(
                                rs.getLong("id_warrant"),
                                rs.getBigDecimal("cost_of_travel")
                        ));
    }

    public List<Calculated> findAllWarrantsAndTheirCostOfTravelDESC() {
        final String query = "SELECT warrants.id AS id_warrant, expenses.cost_of_travel FROM warrants JOIN expenses ON warrants.id_expenses = expenses.id ORDER BY cost_of_travel DESC";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) ->
                        new Calculated(
                                rs.getLong("id_warrant"),
                                rs.getBigDecimal("cost_of_travel")
                        ));
    }

}
