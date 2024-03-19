package songdiary.melog.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
public class InputModel {
    private String contents;
    public InputModel(String contents) {
        this.contents = contents;
    }
}
