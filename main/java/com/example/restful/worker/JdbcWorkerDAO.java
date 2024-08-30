package com.example.restful.worker;

import com.google.common.cache.Cache;
import jakarta.annotation.PostConstruct;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.*;

@Repository
public class JdbcWorkerDAO implements WorkerDAO{

    private static final long counter = 0;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertWorker;
    private final Cache<Integer, Worker> cache;
    private final RowMapper<Worker> rowMapper = (rs, rowNum) -> new Worker(
            rs.getInt("worker_id"),
            rs.getInt("age"),
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getString("workPlace"),
            rs.getString("city"),
            rs.getString("country"),
            rs.getDate("birthDate")
    );

    public JdbcWorkerDAO(JdbcTemplate jdbcTemplate, Cache<Integer, Worker> cache) {
        this.cache = cache;
        this.jdbcTemplate = jdbcTemplate;
        insertWorker = new SimpleJdbcInsert(jdbcTemplate).withTableName("workers").usingGeneratedKeyColumns("worker_id");
    }

    @Override
    public List<Worker> findAll() {
        return cache.asMap().values().stream().toList();
    }

    @Override
    public Optional<Worker> findById(int id) {
        return Optional.ofNullable(cache.getIfPresent(id)).or(() -> {
            try{
                String sql = "SELECT * FROM workers WHERE worker_id = ?";
                Worker worker = jdbcTemplate.queryForObject(sql, rowMapper, id);
                return Optional.ofNullable(worker);
            } catch (EmptyResultDataAccessException e) {
                return Optional.empty();
            }
        });
    }

    @Override
    public Optional<Worker> findByName(String last_name) {

        return cache.asMap().values().stream().filter(worker -> last_name.equals(worker.getLastName())).findAny().or(() -> {
            try{
                String sql = "SELECT * FROM workers WHERE lastName = ?";
                Worker worker = jdbcTemplate.queryForObject(sql, rowMapper, last_name);
                return Optional.ofNullable(worker);
            } catch (EmptyResultDataAccessException e) {
                return Optional.empty();
            }
        });
    }

    //Normally I would return Void here, but I want to Cache the Worker after it's added to the DB, at runtime
    @Override
    public void create(Worker worker) {
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

        //Cache the new entry after it was added to the DB to keep the Cache up to date
        cache.put(workerId, new Worker(
                workerId,
                worker.getAge(),
                worker.getFirstName(),
                worker.getLastName(),
                worker.getWorkPlace(),
                worker.getCity(),
                worker.getCountry(),
                worker.getBirthDate()));
    }

    // Consider a different approach. Maybe a Functional approach with Combinator Pattern.
    @Override
    public void update(Worker worker, int id) {
       String sql = "UPDATE workers SET age = ?, firstName = ?, lastName = ?, workPlace = ?, city = ?, country = ?, birthDate = ? WHERE worker_id = ?";
       Worker updatedWorker = new Worker(id, worker.getAge(), worker.getFirstName(), worker.getLastName(), worker.getWorkPlace(), worker.getCity(), worker.getCountry(), worker.getBirthDate());

       jdbcTemplate.update(sql,
               updatedWorker.getAge(),
               updatedWorker.getFirstName(),
               updatedWorker.getLastName(),
               updatedWorker.getWorkPlace(),
               updatedWorker.getCity(),
               updatedWorker.getCountry(),
               updatedWorker.getBirthDate(),
               id);
       cache.put(id, updatedWorker);
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM workers WHERE worker_id = ?";
        jdbcTemplate.update(sql, id);
        cache.invalidate(id);
    }

    @PostConstruct
    private void populateCacheAtStartTime() {
        List<Worker> workers = jdbcTemplate.query("SELECT * FROM workers", rowMapper);
        workers.forEach(worker -> cache.put(worker.getId(), worker));
    }
}
