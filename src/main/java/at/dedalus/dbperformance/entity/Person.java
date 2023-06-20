package at.dedalus.dbperformance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name="person")
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class Person {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator="person_pk_seq" )
    @SequenceGenerator( name="person_pk_seq")
    private Long pk;
    @Column( name = "name")
    private String name;

    public Person(String name)
    {
        this.name = name;
    }
}
