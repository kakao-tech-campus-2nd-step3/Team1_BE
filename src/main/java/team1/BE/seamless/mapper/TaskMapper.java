package team1.BE.seamless.mapper;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.TaskEntity;

@Component
public class TaskMapper {

    public TaskEntity toEntity(String name, String remark, ProjectEntity projectEntity,
        MemberEntity memberEntity, LocalDateTime startDate, LocalDateTime endDate) {
        return new TaskEntity(name, remark, projectEntity, memberEntity, startDate, endDate);
    }
}
