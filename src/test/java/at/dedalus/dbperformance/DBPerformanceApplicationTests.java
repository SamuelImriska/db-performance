package at.dedalus.dbperformance;

import at.dedalus.dbperformance.entity.*;
import at.dedalus.dbperformance.utils.UIDUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@SpringBootTest
@Transactional
class DBPerformanceApplicationTests {

	@Autowired
	private SeriesRepository seriesRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private StudyRepository studyRepository;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Test
	void contextLoads() {
	}

	@AfterEach
	public void tearDown() {
		entityManager.flush();
	}


	/**
	 * Have a look how update is performed
	 * Check how @DynamicUpdate can improve it
	 */
	@Test
	void dynamicUpdate() {
		Series series = createSeries();
		long t = System.nanoTime();
		series.setSeriesNumber("1");
		seriesRepository.save(series);
	}


	/**
	 * This task is optional, because not so important any more
	 * Observe generated SQL for the Query. In Hibernate <6 this generate cross join
	 * If you want try to degrade to spring boot 2.7 and observe
	 * This project uses spring boot 3 and hibernate 6.2 and this problem does not occure any more
	 */
	@Test
	void crossJoin() {
		createStudyAndSeries();
		List<Series> series = seriesRepository.searchCRSeriesByStudyID("2");
		Assertions.assertThat(series.size()).isEqualTo(1);
	}

	/**
	 * You are interested only in Study ID and Study Description of specific studies
	 * Write a projection query to retrieve only those. Do NOT use Tuples
	 */
	@Test
	void projections() {

	}

	/**
	 * Observe how deletion of items is executed by Hibernate in M:M Relations
	 * Observe how the behavior changes when using different collection types
	 */
	@Test
	void relationCollection() {
		Study study = createStudyAndSeries();
		Person p1 = new Person("Hugo");
		study.getPersons().add(p1);
		entityManager.persist(p1);
		Person p2 = new Person("John");
		study.getPersons().add(p2);
		entityManager.persist(p2);
		Person p3 = new Person("Alex");
		study.getPersons().add(p3);
		entityManager.persist(p3);
		entityManager.flush();
		study.getPersons().remove(p1);
		entityManager.persist(study);
		entityManager.flush();
		Person p4 = new Person("Einstein");
		study.getPersons().add(0,p4);
		entityManager.persist(p4);
	}


	/**
	 * Observe which statements are issued on inserts.
	 * Modify the mapping to remove the unnecessary update statement
	 */
	@Test
	void relationMapping() {
		Study study = createStudyAndSeries();
		entityManager.flush();
	}






	/**
	 * Modify configuration/code to run the inserts in batches
	 * Measure performance with different batch sizes
	 * Optionally extend to batch updates
	 * Be aware that normal Hibernate Logging DOES NOT SHOW batching
	 * THerefore special proxy is enabled to show it
	 */
	@Test
	@Profile("insert")
	void batching() {
		for (int i=0;i<5;i++) {
			Series s = new Series();
			s.setSeriesIuid(UIDUtils.createUID());
			seriesRepository.save(s);
		}

	}


	private Series createSeries() {
		return transactionTemplate.execute(u->
		{
			Series s = new Series();
			s.setSeriesIuid(UIDUtils.createUID());
			return seriesRepository.save(s);
		});
	}

	private Study createStudyAndSeries() {
		return transactionTemplate.execute(u->
		{
			Study study = new Study();
			study.setStudyIuid(UIDUtils.createUID());
			study.setStudyId("2");
			Series s = new Series();
			s.setModality("CR");
			study = studyRepository.save(study);
			study.addSeries(s);
			s.setSeriesIuid(UIDUtils.createUID());
			seriesRepository.save(s);
			return study;
		});
	}




}
