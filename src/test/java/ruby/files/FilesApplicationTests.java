package ruby.files;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

@SpringBootTest
class FilesApplicationTests {

    @Test
    void contextLoads() {
        Map<Long, Long> temp = new ConcurrentHashMap<>();

        IntStream.range(0, 10000)
            .parallel()
            .forEach(idx -> {
                LongStream.range(1, 10000)
                    .forEach(num -> {
                        Long orDefault = temp.getOrDefault(num, 0L);
                        temp.put(num, orDefault + num);
                    });
            });

        System.out.println(temp.values().stream().mapToLong(Long::longValue).sum());
    }

    @Test
    void contextLoads2() {
        Map<Long, Long> temp = new ConcurrentHashMap<>();

        Map<Long, Long> collect = IntStream.range(0, 10000)
            .parallel()
            .mapToObj(idx -> {
                List<Long> test = new ArrayList<>();
                for (long i = 1; i <= 10000; i++) {
                    test.add(i);
                }
                return test;
            })
            .flatMap(Collection::parallelStream)
            .collect(groupingBy(
                num -> num,
                summingLong(Long::longValue)
            ));

        System.out.println(collect.values().stream().mapToLong(Long::longValue).sum());
    }

}

// 2147483647 / 1783293664


// 456475581267

// 455122550279

// 445074350545

// 457770416869