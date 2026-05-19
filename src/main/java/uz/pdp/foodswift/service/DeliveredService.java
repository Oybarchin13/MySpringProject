package uz.pdp.foodswift.service;

import uz.pdp.foodswift.criteria.BaseCriteria;
import uz.pdp.foodswift.model.dto.DataList;
import uz.pdp.foodswift.model.dto.DeliveredDto;
import uz.pdp.foodswift.model.dto.DeliveredSaveDto;
import uz.pdp.foodswift.model.dto.DeliveredUpdateDto;
import uz.pdp.foodswift.model.mapper.DeliveredMapper;
import uz.pdp.foodswift.model.validation.DeliveredValidator;
import uz.pdp.foodswift.repository.DeliveredRepository;
import uz.pdp.foodswift.service.base.AbstractService;
import uz.pdp.foodswift.service.base.CrudService;

import java.util.List;

public class DeliveredService extends AbstractService<DeliveredRepository, DeliveredMapper, DeliveredValidator>
    implements CrudService<DeliveredDto, DeliveredUpdateDto,DeliveredSaveDto, BaseCriteria, String>
{
    private DeliveredService(DeliveredRepository repository, DeliveredMapper mapper, DeliveredValidator validator) {
        super(repository, mapper, validator);
    }


    @Override
    public DeliveredDto create(DeliveredSaveDto dto) {
        return null;
    }

    @Override
    public DeliveredDto update(String id, DeliveredUpdateDto dto) {
        return null;
    }

    @Override
    public DeliveredDto get(String id) {
        return null;
    }

    @Override
    public DataList<List<DeliveredDto>> getAll(BaseCriteria criteria) {
        return null;
    }
}
