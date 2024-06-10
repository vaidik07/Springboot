package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");

        //before
//        Doctor doctor = new Doctor();
//        doctor.assist();

ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
//        //after
//        Doctor doctor = context.getBean(Doctor.class);
//        doctor.assist();
//
//        //now calling the nurse class
//        Nurse nurse = context.getBean(Nurse.class);
//        nurse.assist();

        //using staff
        Staff staff = context.getBean(Doctor.class);
        staff.assists();


    }
}