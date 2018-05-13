package ru.kondratj3v.tltsusch.models;


public class Group {
    private String inst;
    private String group;

    public Group() {

    }

    public Group(String inst, String group) {
        this.inst = inst;
        this.group = group;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
