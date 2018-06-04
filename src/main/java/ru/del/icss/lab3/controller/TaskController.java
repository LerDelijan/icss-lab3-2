package ru.del.icss.lab3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.del.icss.lab3.api.TasksApi;
import ru.del.icss.lab3.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController implements TasksApi {

    private Integer idCounter = 1;
    private List<Task> tasks = new ArrayList<>();

    @Override
    public ResponseEntity<List<Task>> tasksGet() {
        return ResponseEntity.ok(tasks);
    }

    @Override
    public ResponseEntity<Void> tasksIdDelete(@PathVariable("id") Integer id) {
        if (tasks.removeIf(task -> id.equals(task.getId()))) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Task> tasksIdGet(@PathVariable("id") Integer id) {
        return tasks.stream()
                .filter(task -> id.equals(task.getId()))
                .map(ResponseEntity::ok)
                .findFirst()
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Task> tasksIdPatch(@PathVariable("id") Integer id, @RequestBody Task body) {
        Optional<Task> taskOptional = tasks.stream()
                .filter(task -> id.equals(task.getId()))
                .findFirst();
        if (!taskOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Task task = taskOptional.get();
        if (body.getName() != null) {
            task.setName(body.getName());
        }
        if (body.getStatus() != null) {
            task.setStatus(body.getStatus());
        }
        if (body.getDesc() != null) {
            task.setDesc(body.getDesc());
        }
        return ResponseEntity.ok(task);
    }

    @Override
    public ResponseEntity<Task> tasksIdPut(@PathVariable("id") Integer id, @RequestBody Task body) {
        Task task = tasks.stream().filter(m -> id.equals(m.getId())).findFirst().orElse(null);
        boolean created = false;
        if (task == null) {
            task = new Task();
            task.setId(idCounter++);
            created = true;
        }
        task.setName(body.getName());
        task.setStatus(body.getStatus());
        task.setDesc(body.getDesc());
        return new ResponseEntity<>(task, created ? HttpStatus.CREATED : HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Task> tasksPost(@RequestBody Task body) {
        body.setId(idCounter++);
        tasks.add(body);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }
}
