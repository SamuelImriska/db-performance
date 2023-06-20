package at.dedalus.dbperformance.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="study")
@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class Study {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator="study_pk_seq" )
    @SequenceGenerator( name="study_pk_seq")
    private Long pk;

    @Column(name="study_iuid")
    private String studyIuid;

    @Column(name="study_id")
    private String studyId;

    @Column(name="study_desc")
    private String studyDescription;

    @OneToMany
    @JoinColumn(name="study_fk")
    private List<Series> series = new ArrayList<>();

    public void addSeries(Series serie)
    {
        series.add(serie);
        serie.setStudy(this);
    }

    public void removeSeries(Series serie)
    {
        series.remove(serie);
        serie.setStudy(null);
    }

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "study_person",
            joinColumns = { @JoinColumn(name = "study_pk") },
            inverseJoinColumns = { @JoinColumn(name = "person_pk") }
    )
    List<Person> persons = new ArrayList<>();
}
