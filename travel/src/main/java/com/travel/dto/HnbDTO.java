package com.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HnbDTO{

    public String brojTečajnice;
    public String datumPrimjene;
    public String država;
    public String šifraValute;
    public String valuta;
    public int jedinica;
    public String kupovniZaDevize;
    public String srednjiZaDevize;
    public String prodajniZaDevize;
}
