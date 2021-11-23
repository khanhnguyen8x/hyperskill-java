package com.khanhnv.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

  public static void main(String[] args) {
    var program = new SearchProgram(args);
    program.start();
  }
}

class SearchProgram {

  Map<String, List<Integer>> invertedIndex = new HashMap<>();

  List<String> inputs = new ArrayList<>();

  public SearchProgram(String[] args) {
    if (args.length > 0) {
      var data = args[1];
      this.inputs = readFile(data);
      buildInvertedIndex();
      // invertedIndex.forEach((key, value) -> System.out.println(key + ":" + value));
    }
  }

  private void buildInvertedIndex() {
    for (int i = 0; i < inputs.size(); i++) {
      var item = inputs.get(i);
      String[] strs = item.split("\\s");
      int finalIndex = i;
      Arrays.stream(strs)
          .forEach(
              str -> {
                str = str.toLowerCase();
                if (!invertedIndex.containsKey(str)) {
                  List<Integer> values = new ArrayList<>();
                  values.add(finalIndex);
                  invertedIndex.put(str, values);
                } else {
                  List<Integer> values = invertedIndex.get(str);
                  values.add(finalIndex);
                  invertedIndex.put(str, values);
                }
              });
    }
  }

  private List<String> readFile(String path) {
    List<String> lines = new ArrayList<>();
    var file = new File(path);
    if (file.exists()) {
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
          lines.add(line);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      return lines;
    }
    return new ArrayList<>();
  }

  void start() {
    var scanner = new Scanner(System.in);
    var running = true;
    while (running) {
      System.out.println("");
      System.out.println("=== Menu ===");
      System.out.println("1. Find a person");
      System.out.println("2. Print all people");
      System.out.println("0. Exit");
      var action = 0;
      try {
        action = Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        e.printStackTrace();
        System.out.println("Incorrect option! Try again.");
        continue;
      }
      if (action != 0 && action != 1 && action != 2) {
        System.out.println("Incorrect option! Try again.");
        continue;
      }

      switch (action) {
        case 1:
          search(scanner);
          break;
        case 2:
          printAll();
          break;
        case 0:
          running = false;
          exit();
          break;
        default:
          break;
      }
    }
  }

  private void search(Scanner scanner) {
    System.out.println("");
    System.out.println("Select a matching strategy: ALL, ANY, NONE");
    var option = scanner.nextLine();
    System.out.println("\nEnter a name or email to search all suitable people.");
    var key = scanner.nextLine().toLowerCase().trim();
    SearchFunction function;
    Set<Integer> results = null;
    switch (option) {
      case "ALL":
        function = new SearchAll();
        results = function.search(key, inputs, invertedIndex);
        break;
      case "ANY":
        function = new SearchAny();
        results = function.search(key, inputs, invertedIndex);
        break;
      case "NONE":
        function = new SearchNone();
        results = function.search(key, inputs, invertedIndex);
        break;
      default:
        break;
    }
    if (null != results && !results.isEmpty()) {
      System.out.printf("%d persons found:\n", results.size());
      results.forEach(item -> System.out.println(inputs.get(item)));
    } else {
      System.out.println("No matching people found.");
    }
  }

  private void printAll() {
    System.out.println("");
    System.out.println("=== List of people ===");
    inputs.forEach(System.out::println);
  }

  private void exit() {
    System.out.println("");
    System.out.println("Bye!");
  }
}

interface SearchFunction {
  Set<Integer> search(String search, List<String> inputs, Map<String, List<Integer>> invertedIndex);
}

class SearchAll implements SearchFunction {

  @Override
  public Set<Integer> search(
      String search, List<String> inputs, Map<String, List<Integer>> invertedIndex) {
    Set<Integer> resultIndex = new HashSet<>();
    var temp = new ArrayList<Integer>();
    var keys = search.split("\\s");

    for (int i = 0; i < keys.length; i++) {
      if (i == 0 && invertedIndex.containsKey(keys[i])) {
        temp.addAll(invertedIndex.get(keys[i]));
      }

      List<Integer> checked = invertedIndex.get(keys[i]);
      for (Integer eachTemp : temp) {
        for (Integer eachChecked : checked) {
          if (Objects.equals(eachTemp, eachChecked)) {
            resultIndex.add(eachTemp);
          }
        }
      }
    }
    return resultIndex;
  }
}

class SearchAny implements SearchFunction {

  @Override
  public Set<Integer> search(
      String search, List<String> inputs, Map<String, List<Integer>> invertedIndex) {
    Set<Integer> resultIndex = new HashSet<>();
    var keys = search.split("\\s");
    Arrays.stream(keys)
        .filter(invertedIndex::containsKey)
        .map(invertedIndex::get)
        .forEach(resultIndex::addAll);
    return resultIndex;
  }
}

class SearchNone implements SearchFunction {

  @Override
  public Set<Integer> search(
      String search, List<String> inputs, Map<String, List<Integer>> invertedIndex) {
    var keys = search.split("\\s");
    Set<Integer> resultIndex = new HashSet<>();
    for (String key : invertedIndex.keySet()) {
      resultIndex.addAll(invertedIndex.get(key));
    }
    for (String key : keys) {
      resultIndex.removeAll(invertedIndex.get(key));
    }
    return resultIndex;
  }
}
