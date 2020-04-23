package ru.otus.hw.db.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @OneToOne(mappedBy = "address", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private User user;

    private String street;

    public Address(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street=" + street +
                '}';
    }
}
