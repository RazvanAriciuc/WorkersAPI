package com.example.restful;

import com.example.restful.worker.JdbcWorkerDAO;
import com.example.restful.worker.Worker;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JDBCWorkerDAOTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private SimpleJdbcInsert simpleJdbcInsert;
	@Mock
	private Cache<Integer, Worker> cache;

	@InjectMocks
	private JdbcWorkerDAO dao;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		cache = CacheBuilder.newBuilder().build();
		simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("workers").usingGeneratedKeyColumns("worker_id");
		dao = new JdbcWorkerDAO(jdbcTemplate, cache);
	}

	@Test
	public void testFindAll() {
		Worker worker1 = new Worker(1, 30, "John", "Doe", "Company", "City", "Country", new Date());
		Worker worker2 = new Worker(2, 25, "Jane", "Doe", "Company", "City", "Country", new Date());

		cache.put(worker1.getId(), worker1);
		cache.put(worker2.getId(), worker2);

		List<Worker> workers = dao.findAll();

		assertEquals(2, workers.size());
		assertTrue(workers.contains(worker1));
		assertTrue(workers.contains(worker2));
	}

	@Test
	public void testFindByIdCacheHit() {
		Worker worker1 = new Worker(1, 30, "John", "Doe", "Company", "City", "Country", new Date());

		cache.put(worker1.getId(), worker1);
		Optional<Worker> retrieved = dao.findById(1);

		assertTrue(retrieved.isPresent());
		assertEquals(retrieved.get(), worker1);

	}

	@Test
	public void testFindByIdDBHit() {
		Worker worker = new Worker(1, 30, "John", "Doe", "Company", "City", "Country", new Date());
		String sql = "SELECT * FROM workers WHERE worker_id = ?";

		when(jdbcTemplate.queryForObject(eq(sql), any(RowMapper.class), anyInt())).thenReturn(worker);


		JdbcWorkerDAO spyDao = Mockito.spy(dao);
		Optional<Worker> retrieved = spyDao.findById(1);

		assertTrue(retrieved.isPresent());
		assertEquals(worker, retrieved.get());
	}
}
