package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
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
                ", user='" + user + '\'' +
                ", street=" + street +
                '}';
    }
}
