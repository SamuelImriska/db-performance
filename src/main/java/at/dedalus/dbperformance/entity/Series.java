package at.dedalus.dbperformance.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="series")
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class Series {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator="series_pk_seq" )
    @SequenceGenerator( name="series_pk_seq")
    private Long pk;


    @Column(name = "series_iuid")
    private String seriesIuid;

    @Column(name = "series_no")
    private String seriesNumber;

    @Column(name = "modality")
    private String modality;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "study_fk")
    private Study study;



}
