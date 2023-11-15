/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.borrador.appservicios.enumeradores;

/**
 *
 * @author facun
 */
public enum Categoria {
    SALUD("Salud"),
    PLOMERIA("Plomeria"),
    ELECTRICIDAD("Electricidad"),
    LIMPIEZA("Limpieza"),
    JARDINERIA("Jardineria"),
    VARIOS("Varios");
    
   
    private final String nombre;

    Categoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    
}
