package be.ordina.ordineo.Specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Created by shbe on 24/04/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
