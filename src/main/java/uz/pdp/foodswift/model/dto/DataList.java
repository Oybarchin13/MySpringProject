package uz.pdp.foodswift.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataList<T> {
    private T data;
    private Long allElements;
    private Long totalPages;

    public DataList(T data, Long allElements, Long totalPages) {
        this.data = data;
        this.allElements = allElements;
        this.totalPages = totalPages;
    }
}
