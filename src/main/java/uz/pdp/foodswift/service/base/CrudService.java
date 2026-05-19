package uz.pdp.foodswift.service.base;

import uz.pdp.foodswift.model.dto.DataList;

import java.util.List;

/**
 *
 * DTO <D>
 * UpdateDTO <UD>
 * CreateDTO <CD>
 * Creteria <C>
 * Id turi Key <K>
 */

public interface CrudService <D, UD, CD, C, K>{

    D create (CD dto);
    D update(K id, UD dto);
    D get(K id);
    DataList<List<D>> getAll(C criteria);
    default void delete(K id){}

}
