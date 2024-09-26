package team1.BE.seamless.service;

import org.springframework.http.HttpStatus;
import team1.BE.seamless.DTO.TaskDTO;
import team1.BE.seamless.entity.GuestEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.TaskEntity;
import team1.BE.seamless.repository.ProjectRepository;
import team1.BE.seamless.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team1.BE.seamless.util.errorException.BaseHandler;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public TaskEntity createTask(TaskDTO req) {
        ProjectEntity projectEntity = projectRepository.findById(req.getProjectId()).orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트"));

        // Guest 기능 구현 이전 이기 때문에 Mock 데이터로 구현
         GuestEntity guestEntity = new GuestEntity("test@gmail.com", "1", 0, projectEntity);

        TaskEntity taskEntity = new TaskEntity(
            req.getName(),
            req.getRemark(),
            req.getProgress(),
            req.getIsDeleted(),
            projectEntity,
            guestEntity,
            req.getStartDate(),
            req.getEndDate()
        );

        taskRepository.save(taskEntity);
        return taskEntity;
    }

    public void deleteTask(Long taskId) {
        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "존재하지 않는 태스크"));

        taskRepository.delete(taskEntity);
    }
}
