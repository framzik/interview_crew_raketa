package ru.khrebtov.crew_raketa.services;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.khrebtov.crew_raketa.dto.ValueDto;
import ru.khrebtov.crew_raketa.entity.SearchCriteria;
import ru.khrebtov.crew_raketa.entity.Value;
import ru.khrebtov.crew_raketa.entity.ValueSpecification;
import ru.khrebtov.crew_raketa.repositories.ValueRepo;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ValueService {
    private final ValueRepo valueRepo;
    private final Integer BATCH_SIZE = 75;

    public ValueService(ValueRepo valueRepo) {
        this.valueRepo = valueRepo;
    }

    @Transactional
    public void createValues(int number) {
        List<Value> values = new LinkedList<>();
//        int countFullBatch = number / BATCH_SIZE;
//        int remainder = number - (countFullBatch * BATCH_SIZE);
//        int countWrittenBatch = 0;
//
        for (int i = 1; i <= number; i++) {
            values.add(new Value("Запись " + i));
//            if (values.size() == BATCH_SIZE) {
//                valueRepo.saveAll(values);
//                values.clear();
//                countWrittenBatch++;
//            }
//
//            if (countFullBatch == countWrittenBatch && values.size() == remainder) {
//                valueRepo.saveAll(values);
//                values.clear();
//            }
        }
        valueRepo.saveAll(values);
    }

    @Transactional
    public void removeAll() {
        valueRepo.removeAll();
    }

    public boolean updateValue(long id, ValueDto valueDto) {
        Optional<Value> oValueForUpdate = valueRepo.findById(id);
        if (oValueForUpdate.isPresent()) {
            Value value = oValueForUpdate.get();
            value.setValue(valueDto.getValue());
            value.setDate(LocalDateTime.now());
            valueRepo.save(value);
        }

        return oValueForUpdate.isPresent();
    }

    public List<Value> findAll(Map<String, String> allRequestParams) {
        Specification<Value> finalSpec = new ValueSpecification(null);
        for (Map.Entry<String, String> e : allRequestParams.entrySet()) {
            finalSpec = finalSpec.and(initValueSpec(e));
        }

        return valueRepo.findAll(finalSpec);
    }

    private ValueSpecification initValueSpec(Map.Entry<String, String> e) {
        String key = e.getKey();
        String[] splitKey = key.split("\\.");
        return new ValueSpecification(new SearchCriteria(splitKey[1], splitKey[2], e.getValue()));
    }
}
