package team1.BE.seamless.service;

import team1.BE.seamless.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public void deleteTask(Long project_id, Long task_id) {
//        taskRepository.deleteById(project_id, task_id);
    }

}
