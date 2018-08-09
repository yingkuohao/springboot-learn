package com.kuohao.learn.spring.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/3
 * Time: 下午4:09
 * CopyRight: taobao
 * Descrption:
 */
@Controller
public class AnimalController {
    @Autowired
    AnimalRepository animalRepository;

    //   http://localhost:8080/get-by-name?name=dog
    @RequestMapping("/get-by-name")
    @ResponseBody
    public String getByEmail(String name) {
        String animalId;
        Animal animal = animalRepository.findByName(name);
        if (animal != null) {
            animalId = String.valueOf(animal.getId());
            return "The animal id is: " + animalId;
        }
        return "animal " + name + " is not exist.";
    }

    @RequestMapping("/saveAnimal")
    @ResponseBody
    public String saveAnimal(String name, Integer age) {
        String animalId;
        Animal animal = new Animal();
        animal.setAge(age);
        animal.setName(name);
        animal = animalRepository.save(animal);
        return "animal.id " + animal.getId();
    }
}
