import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
class User {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String country;
}

public class Main {
    public static void main(String[] args) {
        List<User> users = Arrays.asList(
                new User(1, "Иван", "Иванов", 25, "Россия"),
                new User(2, "Петр", "Петров", 30, "Беларусь"),
                new User(3, "Анна", "Сидорова", 22, "Россия"),
                new User(4, "Мария", "Кузнецова", 18, "Украина"),
                new User(5, "Сергей", "Смирнов", 40, "Казахстан")
        );

        System.out.println("Сортировка по фамилии:");
        printUsersSortedByLastName(users);

        System.out.println("\nСортировка по возрасту:");
        printUsersSortedByAge(users);

        System.out.println("\nВсе ли пользователи старше 7 лет?");
        System.out.println(allUsersOlderThan7(users));

        System.out.println("\nСредний возраст пользователей:");
        System.out.println(calculateAverageAge(users));

        System.out.println("\nКоличество разных стран проживания:");
        System.out.println(countDistinctCountries(users));
    }

    public static void printUsersSortedByLastName(List<User> users) {
        users.stream()
                .sorted(Comparator.comparing(User::getLastName))
                .forEach(user -> System.out.println(user));
    }

    public static void printUsersSortedByAge(List<User> users) {
        users.stream()
                .sorted(Comparator.comparing(User::getAge))
                .forEach(user -> System.out.println(user));
    }

    public static boolean allUsersOlderThan7(List<User> users) {
        return users.stream()
                .allMatch(user -> user.getAge() > 7);
    }

    public static double calculateAverageAge(List<User> users) {
        return users.stream()
                .mapToInt(User::getAge)
                .average()
                .orElse(0.0);
    }

    public static long countDistinctCountries(List<User> users) {
        return users.stream()
                .map(User::getCountry)
                .distinct()
                .count();
    }
}
