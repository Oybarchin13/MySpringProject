package uz.pdp.foodswift.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseCriteria {
    private String search;
    private Integer size;
    private Integer page;
}
