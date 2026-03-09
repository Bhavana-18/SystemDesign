package Streams;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Streams{
    static class Person{
       String name;
       int age;
       Person(String name, int age){
           this.name = name;
           this.age = age;
       }

        public int getAge() {
            return age;
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] args){
        //find the string the largest length
        List<String> stringList = Arrays.asList("Apple", "Banana", "Cherry","Date", "grapefruit");
        Optional<String> largest = stringList.stream().max(Comparator.comparingInt(String::length));
        System.out.println(largest);
        //find average
        List<Person> personList = Arrays.asList(new Person("Bhavana", 24),new Person("Priya",26) );
       double avg = personList.stream().mapToInt(Person::getAge).average().orElse(0);

       //Group by person name and average age
        Map<String , Integer> avgAgeByPerson = personList.stream().collect(Collectors.groupingBy(Person::getName, Collectors.summingInt(Person::getAge)));

       //merge two sorted lists into a single sorted list
        List<Integer> list1 = Arrays.asList(1, 3, 5, 7, 9);
        List<Integer> list2 = Arrays.asList(2, 4, 6, 8, 10);

        List<Integer> mergedSortedList = Stream.concat(list2.stream(), list1.stream()).sorted().toList();

        //intersection of two arrays

        List<Integer>  list3 = list1.stream().filter(list2::contains).toList();
        // find the frequency of each word using Java
        List<String> words = Arrays.asList("apple", "banana", "apple", "cherry",
                "banana", "apple");
        Map<String, Long> wordFrequency = words
                .stream()
                .collect(Collectors
                        .groupingBy(Function.identity(), Collectors.counting())
                );

    }
}