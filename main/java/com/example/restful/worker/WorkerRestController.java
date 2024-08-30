package com.example.restful.worker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/worker")
public class WorkerRestController {

    private final JdbcWorkerDAO dao;

    public WorkerRestController (JdbcWorkerDAO dao) {
        this.dao = dao;
    }

    @GetMapping("/printAll")
    public ResponseEntity<List<Worker>> findAll(){
        List<Worker> workers = dao.findAll();
        return new ResponseEntity<>(workers, HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<Worker> findById(@RequestParam int id) {
        return dao.findById(id).map(ResponseEntity::ok).orElseThrow(() -> new NoSuchElementException("Worker not found with id: " + id));
    }

    @GetMapping("/findByName")
    public ResponseEntity<Worker> findByName(@RequestParam String last_name) {
        return dao.findByName(last_name).map(ResponseEntity::ok).orElseThrow(() -> new NoSuchElementException("Worker not found with last name: " + last_name));
    }

    @PostMapping("/addEmployee")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public void addEmployee(@RequestBody Worker worker) {
        dao.create(worker);
    }

    @PutMapping("/updateEmployee")
    @Transactional
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateEmployee(@RequestBody Worker worker, @RequestParam int id) {
        dao.update(worker, id);
    }

    @DeleteMapping("/deleteEmployee")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@RequestParam int id){
        dao.delete(id);
    }
}
