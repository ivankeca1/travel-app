package com.travel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
    private long id;

    @Column
    private String creationDate;

    @Column
    private String warrantStatus;

    @OneToOne
    @JoinColumn(name = "id_expenses", referencedColumnName = "id")
    private Expenses expenses;


}
