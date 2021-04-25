package edu.pkch.redis.json.serialize.pojo;

public class Person {

    private String name;
    private int age;

    private Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
