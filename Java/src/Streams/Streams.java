package Streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Stream{
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
    }

    public static void main(String[] args){
        //find the string the largest length
        List<String> stringList = Arrays.asList("Apple", "Banana", "Cherry","Date", "grapefruit");
        Optional<String> largest = stringList.stream().max(Comparator.comparingInt(String::length));
        System.out.println(largest);
        //find average
        List<Person> personList = Arrays.asList(new Person("Bhavana", 24),new Person("Priya",26) );
       double avg = personList.stream().mapToInt(Person::getAge).average().orElse(0);

       //merge two sorted lists into a single sorted list
        List<Integer> list1 = Arrays.asList(1, 3, 5, 7, 9);
        List<Integer> list2 = Arrays.asList(2, 4, 6, 8, 10);

        List<Integer> mergedSortedList = Streams.concat(list2.stream())



    }
}