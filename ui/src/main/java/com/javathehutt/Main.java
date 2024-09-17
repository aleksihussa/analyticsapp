package com.javathehutt;

public class Main {
  public static void main(String[] args) {
    Service.service();
    Data.service();
    System.out.println("This is the whole applciations entrypoint");

    UiModule.main(args);
  }
}
