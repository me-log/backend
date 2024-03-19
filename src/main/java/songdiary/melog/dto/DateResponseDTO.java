package songdiary.melog.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class DateResponseDTO {
    LocalDate date;
    String mostEmotion;
}
