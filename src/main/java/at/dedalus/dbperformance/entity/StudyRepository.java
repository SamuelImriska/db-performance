package at.dedalus.dbperformance.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudyRepository extends CrudRepository<Study,Long> {



}
