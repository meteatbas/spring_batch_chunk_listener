package br.com.dcc.springbatchexamples.domain.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.dcc.springbatchexamples.domain.Customer;

public class CustomerRowMapper implements RowMapper<Customer> {

	@Override
	public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
		Customer customer=new Customer();
		customer.setId(rs.getLong("id"));
		customer.setEmail(rs.getString("email"));
		customer.setFirstName(rs.getString("firstName"));
		customer.setLastName(rs.getString("lastName"));
		return customer;
	}

}
