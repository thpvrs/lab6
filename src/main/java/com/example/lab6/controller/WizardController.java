package com.example.lab6.controller;

import com.example.lab6.pojo.Wizard;
import com.example.lab6.pojo.Wizards;
import com.example.lab6.repository.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class WizardController {
    @Autowired
    private WizardService wizardService;
    private Wizards wizardList = new Wizards();

    public WizardController(WizardService service) {
        this.wizardService = service;
    }

    @RequestMapping(value = "/wizards", method = RequestMethod.GET)
    public ResponseEntity<?> getWizards(){
        wizardList.setModel((ArrayList) wizardService.retrieveWizards());

        System.out.println();
        System.out.println("Database NOW!!!");
        for(Wizard wiz : wizardList.getModel()) {
            System.out.print(wiz.getName() + ", ");
        }
        System.out.println();

        return ResponseEntity.ok(wizardList);
    }

    @RequestMapping(value = "/addWizard", method = RequestMethod.POST)
    public ResponseEntity<?> createWizard(@RequestBody MultiValueMap<String, String> data) {
        System.out.println("Data received in createWizard : " + data);
        Wizard w = wizardService.createWizard(new Wizard(null
                , (data.get("sex").get(0).equals("Male") ? "m": "f")
                , data.get("name").get(0), data.get("school").get(0)
                , data.get("house").get(0)
                , (int) Math.round(Double.parseDouble(data.get("money").get(0)))
                , data.get("position").get(0))
        );
        return getWizards();
    }

    @RequestMapping(value = "/updateWizard", method = RequestMethod.POST)
    public ResponseEntity<?> updateWizard(@RequestBody MultiValueMap<String, String> data) {
        Wizard wizardTarget = wizardService.retriveWizardById(data.get("id").get(0));
        if (wizardTarget != null){
            Wizard w = wizardService.updateWizard(new Wizard(wizardTarget.get_id()
                    , (data.get("sex").get(0).equals("Male") ? "m": "f")
                    , data.get("name").get(0)
                    , data.get("school").get(0)
                    , data.get("house").get(0)
                    , (int) Math.round(Double.parseDouble(data.get("money").get(0)))
                    , data.get("position").get(0))
            );
        }
        return getWizards();
    }

    @RequestMapping(value = "/deleteWizard", method = RequestMethod.POST)
    public ResponseEntity<?> deleteWizard(@RequestBody MultiValueMap<String, String> data) {
        Wizard wizardTarget = wizardService.retriveWizardById(data.get("id").get(0));
        boolean status = wizardService.deleteWizard(wizardTarget);
        return ResponseEntity.ok(status);
    }
}
