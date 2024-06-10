package org.example;

public class Doctor implements Staff {\

    private Nurse nurse;

    public void assists() {
        System.out.println("Doctor is assisting");
    }

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }
}
