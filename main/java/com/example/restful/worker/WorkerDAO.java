package com.example.restful.worker;

import java.util.List;
import java.util.Optional;

public interface WorkerDAO {

    List<Worker> findAll();
    Optional<Worker> findById(int id);
    void create(Worker worker);
    void update(Worker worker, int id);
    void delete(int id);
    Optional<Worker> findByName(String last_name);
}
