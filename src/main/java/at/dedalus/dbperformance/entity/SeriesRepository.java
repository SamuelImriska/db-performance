package at.dedalus.dbperformance.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeriesRepository extends CrudRepository<Series,Long> {

    @Query(value = "Select s FROM Series s WHERE s.study.studyId=?1 AND s.modality='CR'")
    List<Series> searchCRSeriesByStudyID(String studyId);
}
