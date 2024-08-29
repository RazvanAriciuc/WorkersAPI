package com.example.restful.worker;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/worker")
public class WorkerRestController {

    private final JdbcWorkerDAO dao;

    public WorkerRestController (JdbcWorkerDAO dao) {
        this.dao = dao;
    }

    @GetMapping("/printAll")
    public List<Worker> findAll(){
        return dao.findAll();
    }

    @GetMapping("/findById")
    public Optional<Worker> findById(@RequestParam int id) {
        return dao.findById(id);
    }

    @GetMapping("/findByName")
    public Optional<Worker> findByName(@RequestParam String last_name) {
        return dao.findByName(last_name);
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


    @GetMapping("/hello")
    public void sayHello() {
        System.out.println("Hello world");
    }
}
