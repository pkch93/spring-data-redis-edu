package edu.pkch.redis.java.serialize.api;

import edu.pkch.redis.java.serialize.pojo.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/java")
public class JavaSerializingController {
    private static final String JAVA_SERIALIZE_KEY = "java_person";
    private static final Logger logger = LoggerFactory.getLogger(JavaSerializingController.class);

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Person> valueOperations;

    @GetMapping("/serialize")
    public void serialize() {
        Person person = new Person("pkch", 29);
        valueOperations.set(JAVA_SERIALIZE_KEY, person);
    }

    @GetMapping(value = "/deserialize")
    public ResponseEntity<Person> deserialize() {
        Person person = valueOperations.get(JAVA_SERIALIZE_KEY);
        logger.info("deserialize person. name: {}, age: {}", person.getName(), person.getAge());
        return ResponseEntity.ok(person);
    }
}
