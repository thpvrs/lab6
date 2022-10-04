package com.example.lab6.repository;

import com.example.lab6.pojo.Wizard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class WizardService {
    @Autowired
    private WizardRepository repository;

    public WizardService(WizardRepository repository) {
        this.repository = repository;
    }

    public Collection<Wizard> retrieveWizards(){
        return repository.findAll();
    }

    public Wizard createWizard(Wizard wizard){
        return repository.save(wizard);
    }

    public Wizard retriveWizardById(String id){
        return repository.findWizardById(id);
    }

    public Wizard updateWizard(Wizard wizard){
        return repository.save(wizard);
    }

    public boolean deleteWizard(Wizard wizard) {
        try { repository.delete(wizard); return true; }
        catch (Exception e){ return false;}
    }
}