/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.unisc.dto;

/**
 *
 * @author Douglas Reinicke
 * @author Guilherme Rohr
 */
public class GraphSourceDTO {

    private String name;
    private Double y;

    public GraphSourceDTO() {
        name = "";
        y = 0.0;
    }

    public GraphSourceDTO(String nm, Double vl) {
        name = nm;
        y = vl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

}
