package com.example.lab6.view;

import com.example.lab6.pojo.Wizards;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Route(value = "mainPage.it")
public class MainWizardView extends VerticalLayout {
    private int index;
    private Wizards wizardList;

    public MainWizardView(){
        TextField name = new TextField();
        RadioButtonGroup<String> sex = new RadioButtonGroup<>();
        ComboBox position = new ComboBox();
        NumberField money = new NumberField("Dollars");
        ComboBox school = new ComboBox();
        ComboBox house = new ComboBox();
        Button prev = new Button("<<");
        Button create = new Button("Create");
        Button update = new Button("Update");
        Button delete = new Button("Delete");
        Button next = new Button(">>");
        HorizontalLayout hl = new HorizontalLayout();

        name.setPlaceholder("Fullname");
        sex.setLabel("Gender :");
        sex.setItems("Male", "Female");
        position.setPlaceholder("Position");
        position.setItems("student", "teacher");
        money.setPrefixComponent(new Span("$"));
        school.setPlaceholder("School");
        school.setItems("Hogwarts", "Beauxbatons", "Durmstrang");
        house.setPlaceholder("House");
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slyther");
        hl.add(prev, create, update, delete, next);

        add(name, sex, position, money, school, house, hl);

        wizardList = WebClient
                .create()
                .get()
                .uri("http://localhost:8080/wizards")
                .retrieve()
                .bodyToMono(Wizards.class)
                .block();

        index = -1;
        next.addClickListener(e -> {
            if (index + 1 <= wizardList.getModel().size() - 1) {
                index++;
            }
            name.setValue(wizardList.getModel().get(index).getName());
            sex.setValue((wizardList.getModel().get(index).getSex().equals("m")) ? "Male" : "Female");
            position.setValue(wizardList.getModel().get(index).getPosition());
            money.setValue(Double.valueOf(wizardList.getModel().get(index).getMoney()));
            school.setValue(wizardList.getModel().get(index).getSchool());
            house.setValue(wizardList.getModel().get(index).getHouse());

            System.out.println("Index in next button is " + index);
        });

        prev.addClickListener(e -> {
            if (index - 1 >= 0) {
                index--;
            }
            name.setValue(wizardList.getModel().get(index).getName());
            sex.setValue((wizardList.getModel().get(index).getSex().equals("m")) ? "Male" : "Female");
            position.setValue(wizardList.getModel().get(index).getPosition());
            money.setValue(Double.valueOf(wizardList.getModel().get(index).getMoney()));
            school.setValue(wizardList.getModel().get(index).getSchool());
            house.setValue(wizardList.getModel().get(index).getHouse());

            System.out.println("Index in prev button is " + index);
        });

        create.addClickListener(e -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("name", name.getValue());
            formData.add("sex", sex.getValue());
            formData.add("position", position.getValue().toString());
            formData.add("money", money.getValue().toString());
            formData.add("school", (school.getValue() == null ? "" : school.getValue().toString()));
            formData.add("house", (house.getValue() == null ? "" : house.getValue().toString()));
            System.out.println("Sending Data to localhost:8080/addWizard : " + formData);

            wizardList = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Wizards.class)
                    .block();

//            wizardList = WebClient
//                    .create()
//                    .get()
//                    .uri("http://localhost:8080/wizards")
//                    .retrieve()
//                    .bodyToMono(Wizards.class)
//                    .block();

            new Notification("Wizard has been Created", 2000).open();
        });

        update.addClickListener(e -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("name", name.getValue());
            formData.add("sex", sex.getValue());
            formData.add("position", position.getValue().toString());
            formData.add("money", money.getValue().toString());
            formData.add("school", (school.getValue() == null ? "" : school.getValue().toString()));
            formData.add("house", (house.getValue() == null ? "" : house.getValue().toString()));
            formData.add("id", wizardList.getModel().get(index).get_id());
            System.out.println("Sending Data to localhost:8080/updateWizard : " + formData);

            wizardList = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateWizard")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Wizards.class)
                    .block();

//            wizardList = WebClient
//                    .create()
//                    .get()
//                    .uri("http://localhost:8080/wizards")
//                    .retrieve()
//                    .bodyToMono(Wizards.class)
//                    .block();

            new Notification("Wizard has been Updated", 2000).open();
        });

        delete.addClickListener(e -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("id", wizardList.getModel().get(index).get_id());

            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteWizard")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(boolean.class)
                    .block();

            wizardList = WebClient
                    .create()
                    .get()
                    .uri("http://localhost:8080/wizards")
                    .retrieve()
                    .bodyToMono(Wizards.class)
                    .block();

            new Notification("Wizard has been removed", 2000).open();
        });
    }
}