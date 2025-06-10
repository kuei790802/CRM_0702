package service;

import entity.Record;
import org.springframework.stereotype.Service;
import repository.RecordRepository;

import java.util.List;

@Service
public class RecordService {

   private final RecordRepository recordRepository;

   public RecordService(RecordRepository recordRepository) {
       this.recordRepository = recordRepository;
   }

   public List<Record> findAll() {
       return recordRepository.findAll();
   }

   public Record findById(Long id) {
       return recordRepository.findById(id).orElse(null);
   }

   public Record save(Record record) {
       return recordRepository.save(record);
   }

   public void delete(Long id) {
       recordRepository.deleteById(id);
   }


}
