package com.travel.jdbc;

import com.travel.dto.Calculated;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseJdbcClient {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseJdbcClient(JdbcTemplate jdbcTemplate) {
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

    public List<Calculated> findAllWarrantsAndTheirCostOfTravelASCWithFilter(final String costOfTravel, final String mode, final String order){

        String query = null;
        if(order.equals("ASC") || order.equals("DESC")){
            if(mode.equals("higher")){
                query = "SELECT warrants.id AS id_warrant, expenses.cost_of_travel FROM warrants JOIN expenses ON warrants.id_expenses = expenses.id WHERE cost_of_travel > " + costOfTravel + " GROUP BY id_warrant, cost_of_travel ORDER BY cost_of_travel " + order;
            } else if(mode.equals("lower")){
                query = "SELECT warrants.id AS id_warrant, expenses.cost_of_travel FROM warrants JOIN expenses ON warrants.id_expenses = expenses.id WHERE cost_of_travel < " + costOfTravel + " GROUP BY id_warrant, cost_of_travel ORDER BY cost_of_travel " + order;
            } else if(mode.equals("exact")){
                query = "SELECT warrants.id AS id_warrant, expenses.cost_of_travel FROM warrants JOIN expenses ON warrants.id_expenses = expenses.id WHERE cost_of_travel = " + costOfTravel + " GROUP BY id_warrant, cost_of_travel ORDER BY cost_of_travel " + order;
            }
        }

        return this.jdbcTemplate.query(query,
                (rs, rowNum) ->
                        new Calculated(
                                rs.getLong("id_warrant"),
                                rs.getBigDecimal("cost_of_travel")
                        ));
    }

}
