package songdiary.melog.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DiaryRequestDTO {
    private String title;
    private String contents;
    private LocalDate date;
}
