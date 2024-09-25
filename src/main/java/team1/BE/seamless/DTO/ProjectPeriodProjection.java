package team1.BE.seamless.DTO;

import java.time.LocalDateTime;

public interface ProjectPeriodProjection {
    Long getId();
    String getName();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
}
