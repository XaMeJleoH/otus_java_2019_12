package ru.otus.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue
    @Column(name = "uuid")
    private UUID uuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.SELECT)
    private User user;

    private String number;

    public Phone(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "uuid=" + uuid +
                ", number=" + number +
                '}';
    }
}
