package edu.pkch.redis.objectmapper;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pkch.redis.serialize.pojo.Person;
import org.junit.jupiter.api.Test;
import org.springframework.core.GenericTypeResolver;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectMapperTest {

    /**
     * ObjectMapper를 기본으로 사용하는 경우 Object 객체에 대해서 LinkedHashMap으로 deserialize 한다.
     *
     * @throws JsonProcessingException
     */
    @Test
    void readValueWithoutActivateDefaultTyping() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        String personJson = "{\"@class\":\"edu.pkch.redis.serialize.pojo.Person\",\"name\":\"pkch\",\"age\":29}";

        // when
        LinkedHashMap<String, Object> actual = (LinkedHashMap<String, Object>) objectMapper.readValue(personJson, Object.class);

        // then
        assertThat(actual.get("@class")).isEqualTo("edu.pkch.redis.serialize.pojo.Person");
        assertThat(actual.get("name")).isEqualTo("pkch");
        assertThat(actual.get("age")).isEqualTo(29);
    }


    /**
     * ObjectMapper에서 activateDefaultTyping를 사용하게되면 @class의 정보를 가지고 객체를 직렬화한다.
     *
     * @throws JsonProcessingException
     */
    @Test
    void readValueWithActivateDefaultTyping() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        String personJson = "{\"@class\":\"edu.pkch.redis.serialize.pojo.Person\",\"name\":\"pkch\",\"age\":29}";

        // when
        Person actual = (Person) objectMapper.readValue(personJson, Object.class);

        // then
        assertThat(actual.getName()).isEqualTo("pkch");
        assertThat(actual.getAge()).isEqualTo(29);
    }

    /**
     * ObjectMapper readValue 시에 타입 토큰 대신 JavaType을 두번째 인자로 전달하면
     * Object를 activeDefaultTyping 등의 설정을 하지 않아도 원하는 객체로 변환이 가능하다.
     * 참고로 AbstractJackson2HttpMessageConverter가 이 방식으로 역직렬화를 한다.
     *
     * @throws JsonProcessingException
     */
    @Test
    void readValueWithJavaType() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        String personJson = "{\"@class\":\"edu.pkch.redis.serialize.pojo.Person\",\"name\":\"pkch\",\"age\":29}";

        JavaType javaType = objectMapper.constructType(GenericTypeResolver.resolveType(Person.class, (Class<?>) null));
        // when
        Person actual = objectMapper.readValue(personJson, javaType);

        // then
        assertThat(actual.getName()).isEqualTo("pkch");
        assertThat(actual.getAge()).isEqualTo(29);
    }
}
