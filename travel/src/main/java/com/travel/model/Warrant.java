package com.travel.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "warrants")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Warrant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String creationDate;

    @Column
    private String warrantStatus;

    @OneToOne
    @JoinColumn(name = "id_expenses", referencedColumnName = "id")
    private Expenses expenses;

    @OneToOne
    @JoinColumn(name = "id_vehicle", referencedColumnName = "id")
    private Vehicle vehicle;


}
