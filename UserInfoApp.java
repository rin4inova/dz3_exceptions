package DZ3;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/* Задание 1. Проверка корректности даты.
Напишите приложение, которое будет запрашивать у пользователя следующие данные
в произвольном порядке, разделенные пробелом:
Фамилия Имя Отчество дата рождения номер телефона пол
Форматы данных:
фамилия, имя, отчество - строки.
дата рождения - строка формата dd.mm.yyyy
номер телефона - целое беззнаковое число без форматирования
пол - символ латиницей - f или m
Приложение должно проверить введенные данные по количеству.
Если количество не совпадает с требуемым, вернуть код ошибки,
обработать его и показать пользователю сообщение, что он ввел
меньше и больше данных, чем требуется.
Приложение должно попытаться распарсить полученные значения
и выделить из них требуемые параметры. Если форматы данных не совпадают,
нужно бросить исключение, соответствующее типу проблемы. Можно
использовать встроенные типы java и создать свои. Исключение должно
быть корректно обработано,пользователю выведено сообщение с информацией,
что именно неверно. Если все введено и обработано верно, должен создаться
файл с названием, равным фамилии, в него в одну строку должны записаться
полученные данные, вида  <Фамилия><Имя><Отчество><дата рождения><номер телефона><пол>.
Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
Не забудьте закрыть соединение с файлом.
При возникновении проблемы с чтением-записью в файл, исключение должно
быть корректно обработано, пользователь должен увидеть стектрейс ошибки.

 */
public class UserInfoApp {
    public static void main(String[] args) {
        //Создание объекта Scanner для ввода данных от пользователя
        Scanner scanner = new Scanner(System.in);
        // Запрос данных у пользователя
        System.out.println("Enter your data (Surname Name MiddleName DateOfBirth PhoneNumber Gender):");
        String input = scanner.nextLine(); //Чтение строки ввода
        scanner.close(); //Закрытие сканера

        try {
            // Разделение строки ввода по пробелам
            String[] parts = input.split(" ");
            // Проверка на количество введенных данных
            if (parts.length != 6) {
                throw new IllegalArgumentException("Invalid number of arguments. Expected 6.");
            }
            // Извлечение и валидация каждой части.
            String surname = parts[0];
            String name = parts[1];
            String middleName = parts[2];
            //Валидация даты рождения
            LocalDate date = parseDate(parts[3]);

            // Валидация номера телефона
            long phoneNumber = parsePhoneNumber(parts[4]);

            // Валидация пола
            char gender = parseGender(parts[5]);

            // Запись данных в файл

            writeToFile(surname, name, middleName, date, phoneNumber, gender);
        } catch (IllegalArgumentException e) {
            // Обработка исключений для неправильного количества данных или неверного формата
            System.err.println("Здесь ошибка: " + e.getMessage());
        } catch (DateTimeParseException e) {
            // Обработка исключения для неверного формата даты
            System.err.println("Error: Invalid date format. Please" +
                    "use dd.mm.yyyy.");
        } catch (IOException e) {
            // Вывод стектрейса в случае ошибок ввода/вывода
            e.printStackTrace();
        }
    }

    // Метод для разбора и валидации даты

    private static LocalDate parseDate(String dateStr) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(dateStr, formatter);
    }


    // Метод для разбора и валидации номера телефона
    private static long parsePhoneNumber(String phoneNumberStr) {
        try {
            return Long.parseLong(phoneNumberStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
    }

    // Метод для разбора и валидации пола
    private static char parseGender(String genderStr) {
        // Проверка длины строки и допустимых значений
        if (genderStr.length() != 1 || !(genderStr.equalsIgnoreCase("f") || genderStr.equalsIgnoreCase("m"))) {
            throw new IllegalArgumentException("Invalid gender. Expected 'f' or 'm'.");
        }
        return genderStr.toLowerCase().charAt(0);
    }

    // Метод для записи данных в файл
    private static void writeToFile(String surname, String name, String middleName, LocalDate dateOfBirth, long phoneNumber, char gender) throws IOException {
        String filename = surname + ".txt"; //Формирование имени файла
        // Формирование строки для записи
        String line = String.format("%s %s %s %d %c", surname, name, middleName, dateOfBirth, phoneNumber, gender);
        // Запись строки в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(line);
            writer.newLine(); // Переход на новую строку
        }
    }
}


