package com.example.restful.worker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.*;

@Repository
public class JdbcWorkerDAO implements WorkerDAO{

    private static final long counter = 0;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertWorker;

    public JdbcWorkerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertWorker = new SimpleJdbcInsert(jdbcTemplate).withTableName("workers").usingGeneratedKeyColumns("worker_id");
    }

    RowMapper<Worker> rowMapper = (rs, rowNum) -> new Worker(
            rs.getInt("worker_id"),
            rs.getInt("age"),
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getString("workPlace"),
            rs.getString("city"),
            rs.getString("country"),
            rs.getDate("birthDate")
    );


    @Override
    public List<Worker> findAll() {
        String sql = "SELECT * FROM workers";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Worker> findById(int worker_id) {
        String sql = "SELECT * FROM workers WHERE worker_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, worker_id));
    }

    @Override
    public Optional<Worker> findByName(String last_name) {
        String sql = "SELECT * FROM workers WHERE lastName = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, last_name));
    }

    //Normally I would return Void here, but I want to Cache the Worker after it's added to the DB, at runtime
    @Override
    public Worker create(Worker worker) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("age", worker.getAge());
        parameters.put("firstName", worker.getFirstName());
        parameters.put("lastName", worker.getLastName());
        parameters.put("workPlace", worker.getWorkPlace());
        parameters.put("city", worker.getCity());
        parameters.put("country", worker.getCountry());
        parameters.put("birthDate", worker.getBirthDate());

        Number id_worker = insertWorker.executeAndReturnKey(parameters);
        // Convert BigInteger to Integer
        int workerId = (id_worker instanceof BigInteger) ? ((BigInteger) id_worker).intValue() : (Integer) id_worker;

        return new Worker(
                workerId,
                worker.getAge(),
                worker.getFirstName(),
                worker.getLastName(),
                worker.getWorkPlace(),
                worker.getCity(),
                worker.getCountry(),
                worker.getBirthDate());
    }

    @Override
    public void update(Worker worker, int id) {
        // tbd
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM workers WHERE worker_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
