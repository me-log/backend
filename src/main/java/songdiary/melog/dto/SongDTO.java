package songdiary.melog.dto;

import lombok.Data;

@Data
public class SongDTO {
    private String title;
    private String artist;
    private Long likes;
}