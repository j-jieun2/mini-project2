package com.eomcs.pms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.eomcs.util.Prompt;

public class ClientApp {

  static String host;
  static int port;

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("프로그램 사용법:");
      System.out.println("  java -cp ... ClientApp 서버주소 포트번호");
      System.exit(0);
    }

    System.out.println("                                     방탈출 게임에 오신 것을 환영합니다.");
    System.out.println("--------------------------------------------------------------------------------------------------------------");
    System.out.println(" _______     _______.  ______     ___      .______    _______    .______        ______     ______   .___  ___. ");
    System.out.println("|   ____|   /       | /      |   /   \\     |   _  \\  |   ____|   |   _  \\      /  __  \\   /  __  \\  |   \\/   | ");
    System.out.println("|  |__     |   (----`|  ,----'  /  ^  \\    |  |_)  | |  |__      |  |_)  |    |  |  |  | |  |  |  | |  \\  /  |");
    System.out.println("|   __|     \\   \\    |  |      /  /_\\  \\   |   ___/  |   __|     |      /     |  |  |  | |  |  |  | |  |\\/|  | ");
    System.out.println("|  |____.----)   |   |  `----./  _____  \\  |  |      |  |____    |  |\\  \\----.|  `--'  | |  `--'  | |  |  |  | ");
    System.out.println("|_______|_______/     \\______/__/     \\__\\ | _|      |_______|   | _| `._____| \\______/   \\______/  |__|  |__| ");

    System.out.println("--------------------------------------------------------------------------------------------------------------");
    System.out.println("[필독]");
    System.out.println("룰 및 유의할 점");
    System.out.println();
    System.out.println("명령어 목록을 보고 싶다면 '/rull' 명령어를 입력하세요.");
    System.out.println();
    System.out.println("만약 예기치 못한 문제가 발생했다면 'quit' 명령어를 입력하셔서 "
        + "처음부터 다시 시작하실 수 있습니다.");
    System.out.println();
    System.out.println("게임을 시작하려면 '/start' 명령어를 입력하세요.");
    System.out.println("--------------------------------------------------------------------------------------------------------------");


    host = args[0];
    port = Integer.parseInt(args[1]);

    while (true) {
      String input = Prompt.inputString("명령> ");
      if (input.equalsIgnoreCase("quit"))
        break;

      request(input);

      if (input.equalsIgnoreCase("stop"))
        break;
    }

    System.out.println("                                          방탈출 게임을 종료합니다.");
    System.out.println("--------------------------------------------------------------------------------------------------------------");
    System.out.println(" _______     _______.  ______     ___      .______    _______    .______        ______     ______   .___  ___. ");
    System.out.println("|   ____|   /       | /      |   /   \\     |   _  \\  |   ____|   |   _  \\      /  __  \\   /  __  \\  |   \\/   | ");
    System.out.println("|  |__     |   (----`|  ,----'  /  ^  \\    |  |_)  | |  |__      |  |_)  |    |  |  |  | |  |  |  | |  \\  /  |");
    System.out.println("|   __|     \\   \\    |  |      /  /_\\  \\   |   ___/  |   __|     |      /     |  |  |  | |  |  |  | |  |\\/|  | ");
    System.out.println("|  |____.----)   |   |  `----./  _____  \\  |  |      |  |____    |  |\\  \\----.|  `--'  | |  `--'  | |  |  |  | ");
    System.out.println("|_______|_______/     \\______/__/     \\__\\ | _|      |_______|   | _| `._____| \\______/   \\______/  |__|  |__| ");
    System.out.println("--------------------------------------------------------------------------------------------------------------");
  }

  private static void request(String message) {
    // 클라이언트가 서버에 stop 명령을 보내면 다음 변수를 true로 변경한다.
    boolean stop = false;

    try (Socket socket = new Socket(host, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

      out.println(message);
      out.flush();

      receiveResponse(out, in);

      if (message.equalsIgnoreCase("stop")) {
        stop = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (stop) {
      // 서버를 멈추기 위해 그냥 접속했다가 끊는다.
      try (Socket socket = new Socket(host, port)) {
        // 아무것도 안한다.
        // 서버가 stop 할 기회를 주기 위함이다.
      } catch (Exception e) {
        // 아무것도 안한다.
      }
    }
  }

  private static void receiveResponse(PrintWriter out, BufferedReader in) throws Exception {
    while (true) {
      String response = in.readLine();
      if (response.length() == 0) {
        break;
      } else if (response.equals("!{}!")) {
        // 사용자로부터 값을 입력을 받아서 서버에 보낸다.
        out.println(Prompt.inputString(""));
        out.flush(); // 주의! 출력하면 버퍼에 쌓인다. 서버로 보내고 싶다면 flush()를 호출하라!
      } else {
        System.out.println(response);
      }
    }
  }
}
