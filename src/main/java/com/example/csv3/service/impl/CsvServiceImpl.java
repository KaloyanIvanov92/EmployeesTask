package com.example.csv3.service.impl;

import com.example.csv3.dto.Employee;
import com.example.csv3.exception.CsvException;
import com.example.csv3.service.CsvService;
import com.example.csv3.validation.CsvValidator;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CsvServiceImpl implements CsvService {

    private final CsvValidator csvValidator;

    @Autowired
    public CsvServiceImpl(CsvValidator csvValidator) {
        this.csvValidator = csvValidator;
    }

    @Override
    public List<Long> getDevsForCommonProject(MultipartFile file) throws IOException, CsvException {
        csvValidator.validateFileType(file);
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.US_ASCII));
        String line = br.readLine();
        List<Employee> employees = new ArrayList<>();
        while (line != null)
        {
            String[] employeeData = line.split(",");
            Employee employee = createEmployee(employeeData);
            employees.add(employee);
            line = br.readLine();
        }

        List<Employee> commonProjects = commonProject(employees);
        List<Pair<Employee, Employee>> pairs = toPairs(commonProjects.stream());
        Map<Integer, Long> pairWorkingDays = new HashMap<>();
        for(int i = 0; i < pairs.size(); i++) {
            Employee empl1 = pairs.get(i).getKey();
            Employee empl2 = pairs.get(i).getValue();
            Long res = calculateOverlap(empl1, empl2);
            //i+1 I mean that this is the first pair
            pairWorkingDays.put(i+1, res);
        }

        int pair;
        if(pairWorkingDays.get(1) > pairWorkingDays.get(2)) {
            pair = 1;
        } else {
            pair = 2;
        }

        Pair<Employee, Employee> pair1;

        if(pair == 1) {
            pair1 = pairs.get(0);
        } else {
            pair1 = pairs.get(1);
        }

        long firstEmplDaysOfWork = pair1.getKey().getWorkTo().minusDays(pair1.getKey().getWorkFrom().toEpochDay()).toEpochDay();
        long secondEmplDaysOfWork = pair1.getValue().getWorkTo().minusDays(pair1.getValue().getWorkFrom().toEpochDay()).toEpochDay();

        List<Long> emplIds = new ArrayList<>();

        if(firstEmplDaysOfWork > secondEmplDaysOfWork) {
            emplIds.add(pair1.getKey().getEmployeeId());
            emplIds.add(pair1.getValue().getEmployeeId());
        } else {
            emplIds.add(pair1.getValue().getEmployeeId());
            emplIds.add(pair1.getKey().getEmployeeId());
        }
        emplIds.forEach(System.out::println);
        return emplIds;
    }

    public static <T> List<Pair<T, T>> toPairs(final Stream<T> s) {
        AtomicInteger counter = new AtomicInteger(0);
        return s.collect(
                        Collectors.groupingBy(item -> {
                            int i = counter.getAndIncrement();
                            return (i % 2 == 0) ? i : i - 1;
                        })).values().stream().map(a -> new Pair<T, T>(a.get(0), (a.size() == 2 ? a.get(1) : null)))
                .collect(Collectors.toList());

    }

    private static Employee createEmployee(String[] employeeData) {
        Long employeeId = Long.valueOf(employeeData[0]);
        Integer projectId = Integer.parseInt(employeeData[1]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate workFrom = LocalDate.parse(employeeData[2], formatter);
        LocalDate workToDate;
        String workTo = employeeData[3];
        if(workTo.equals("NULL")) {
            workTo = formatter.format(LocalDate.now());
        }
        workToDate = LocalDate.parse(workTo, formatter);
        return new Employee(employeeId, projectId, workFrom, workToDate);
    }

    private static List<Employee> commonProject(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getProjectId, Collectors.toList()))
                .values()
                .stream()
                .filter(i -> i.size() > 1)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static Long calculateOverlap(Employee firstEmpl, Employee secondEmpl) {
        LocalDate periodStartDate =
                firstEmpl.getWorkFrom().isBefore(secondEmpl.getWorkFrom()) ?
                        secondEmpl.getWorkFrom() : firstEmpl.getWorkFrom();

        LocalDate periodEndDate =
                firstEmpl.getWorkTo().isBefore(secondEmpl.getWorkTo()) ?
                        firstEmpl.getWorkTo() : secondEmpl.getWorkTo();

        return Math.abs(ChronoUnit.DAYS.between(periodStartDate, periodEndDate));
    }

    private static Boolean hasOverlap(Employee firstEmployee, Employee secondEmployee) {
        return (firstEmployee.getWorkFrom().isBefore(secondEmployee.getWorkTo())
                || firstEmployee.getWorkFrom().isEqual(secondEmployee.getWorkTo()))
                && (firstEmployee.getWorkTo().isAfter(secondEmployee.getWorkFrom())
                || firstEmployee.getWorkTo().isEqual(secondEmployee.getWorkFrom()));
    }
}
