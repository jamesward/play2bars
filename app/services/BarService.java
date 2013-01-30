package services;

import models.Bar;

import java.util.List;

public interface BarService {

    public void addBar(Bar bar);
    public List<Bar> getAllBars();
    
}