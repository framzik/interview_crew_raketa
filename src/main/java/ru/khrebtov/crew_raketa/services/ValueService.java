package ru.khrebtov.crew_raketa.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.khrebtov.crew_raketa.dto.ValueDto;
import ru.khrebtov.crew_raketa.entity.SearchCriteria;
import ru.khrebtov.crew_raketa.entity.ValueSpecification;
import ru.khrebtov.crew_raketa.entity.Values;
import ru.khrebtov.crew_raketa.repositories.ValueRepo;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class ValueService {
    private final ValueRepo valueRepo;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private Integer batchSize;

    public ValueService(ValueRepo valueRepo) {
        this.valueRepo = valueRepo;
    }

    @Transactional
    public void createValues(int number) {
        List<Values> values = new LinkedList<>();
        for (int i = 1; i <= number; i++) {
            values.add(new Values("Запись " + i));
            if (values.size() == batchSize) {
                valueRepo.saveAll(values);
                values.clear();
            }
        }
        if (values.size() > 0) valueRepo.saveAll(values);
    }

    @Transactional
    public void removeAll() {
        valueRepo.removeAll();
    }

    public boolean updateValue(long id, ValueDto valueDto) {
        Optional<Values> oValueForUpdate = valueRepo.findById(id);
        if (oValueForUpdate.isPresent()) {
            Values value = oValueForUpdate.get();
            value.setValue(valueDto.getValue());
            value.setDate(LocalDateTime.now());
            valueRepo.save(value);
        }

        return oValueForUpdate.isPresent();
    }

    public List<Values> findAll(Map<String, String> allRequestParams) {
        Specification<Values> finalSpec = new ValueSpecification(null);
        Sort finalSort = null;
        for (Map.Entry<String, String> e : allRequestParams.entrySet()) {
            String[] splitMapKey = e.getKey().split("\\.");

            if (e.getValue().equalsIgnoreCase("desc")) {
                finalSort = updateOrInitFinalSortDesc(finalSort, splitMapKey[1]);
            } else if (e.getValue().equalsIgnoreCase("asc")) {
                finalSort = updateOrInitFinalSortAsc(finalSort, splitMapKey[1]);
            } else {
                finalSpec = finalSpec.and(new ValueSpecification(new SearchCriteria(splitMapKey[1], splitMapKey[2], e.getValue())));
            }
        }

        if (isNull(finalSort)) {
            return valueRepo.findAll(finalSpec);
        }

        return valueRepo.findAll(finalSpec, finalSort);
    }

    private Sort updateOrInitFinalSortAsc(Sort finalSort, String props) {
        if (isNull(finalSort)) {
            finalSort = Sort.by(props).ascending();
        } else
            finalSort = finalSort.and(Sort.by(props).ascending());

        return finalSort;
    }

    private Sort updateOrInitFinalSortDesc(Sort finalSort, String props) {
        if (isNull(finalSort)) {
            finalSort = Sort.by(props).descending();
        } else
            finalSort = finalSort.and(Sort.by(props).descending());

        return finalSort;
    }
}
