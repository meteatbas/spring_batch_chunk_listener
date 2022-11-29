package br.com.dcc.springbatchexamples.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;

import org.springframework.batch.item.database.support.SqlServerPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.dcc.springbatchexamples.domain.Customer;
import br.com.dcc.springbatchexamples.domain.mapper.CustomerRowMapper;
import br.com.dcc.springbatchexamples.listener.SimpleChunkListener;
import br.com.dcc.springbatchexamples.processor.CustomerFilteringItemProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ReadFromDbFilteringConfiguration {

	@Bean
	public JdbcPagingItemReader<Customer> readFromDbFilteringReader(DataSource dataSource) {
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(2);
		reader.setRowMapper(new CustomerRowMapper());

		SqlServerPagingQueryProvider queryProvider = new SqlServerPagingQueryProvider();
		queryProvider.setSelectClause("id, email, firstName, lastName");
		queryProvider.setFromClause("from customer");
		Map<String, Order> sortKeys = new HashMap<>(2);
		sortKeys.put("id", Order.ASCENDING);
		queryProvider.setSortKeys(sortKeys);
		reader.setQueryProvider(queryProvider);

		return reader;
	}


	@Bean
	public ItemWriter<Customer> readFromDbFilteringWriter() {
		return items -> {
			for (Customer item : items) {
				System.out.println(item.toString());
//				log.info("Writing item {}", item.toString());
			}
		};
	}

	@Bean
	public Step readFromDbFilteringStep1(StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
		return stepBuilderFactory.get("ReadFromDbFilteringStep1")
				.<Customer, Customer>chunk(1)
				.faultTolerant()
				.listener(new SimpleChunkListener())
				.reader(readFromDbFilteringReader(dataSource))
				.processor(new CustomerFilteringItemProcessor())
				.writer(readFromDbFilteringWriter())
				.build();
	}

	@Bean
	public Job readFromDbFilteringJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
		return jobBuilderFactory.get("ReadFromDbFilteringJob")
				.start(readFromDbFilteringStep1(stepBuilderFactory, dataSource))
				.build();

	}

}
