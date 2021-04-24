package edu.pkch.redis.serialize.api;

import edu.pkch.redis.serialize.pojo.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/json")
public class JsonSerializingController {
    private static final String JSON_SERIALIZE_KEY = "json_person";
    private static final Logger logger = LoggerFactory.getLogger(JsonSerializingController.class);

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Person> valueOperations;

    @GetMapping("/serialize")
    public void serialize() {
        Person person = new Person("pkch", 29);
        valueOperations.set(JSON_SERIALIZE_KEY, person);
    }

    @GetMapping(value = "/deserialize")
    public ResponseEntity<Person> deserialize() {
        Person person = valueOperations.get(JSON_SERIALIZE_KEY);
        logger.info("deserialize person. name: {}, age: {}", person.getName(), person.getAge());
        return ResponseEntity.ok(person);
    }
}
