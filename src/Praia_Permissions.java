import java.io.*;
import java.util.Scanner;

// user_id login/registo/logout praia(A, B ou C) data  horário número_de_pessoas
public class Praia_Permissions {
    // Mapa para armazenar reservas
    //private static HashMap<String, String> reservas = new HashMap<>();
    // Mapa para armazenar usuários autenticados
    // private static HashMap<String, Str> usuariosRegistados = new HashMap<>();HN
    private static String filePath = "output.txt";
    // Cria um arquivo temporário para armazenar as alterações
    private static File tempFile = new File("temp.txt");
    public static void main(String[] args) {
        int count = 1;
        System.out.println("A - login id senha\n" +
                "B - registo id senha\n" +
                "C - logout id senha\n" +
                "D - reserva user_id senha praia(A, B ou C) data  horário número_de_pessoas"
        );
        //System.out.println("reserva user_id senha praia(A, B ou C) data  horário número_de_pessoas ");
        while (count == 1) {
            Scanner scanner = new Scanner(System.in);
            String linhaCompleta = scanner.nextLine();
            String[] palavras = linhaCompleta.split(" ");

            if (palavras.length == 3) {

                if (palavras[0].equals("A")) {
                    fazerLogin(palavras[1], palavras[2]);
                }
                if (palavras[0].equals("B")) {
                    fazerRegisto(palavras[1], palavras[2]);
                }
                if (palavras[0].equals("C")) {
                    fazerLogout(palavras[1], palavras[2]);
                }
                if (palavras[0].equals("D")) {
                    fazerReserva(palavras[1], palavras[2], palavras[3], palavras[4], palavras[5], palavras[6]);
                }

            } else if (palavras.length == 7) {

                if (palavras[0].equals("D")) {
                    fazerReserva(palavras[1], palavras[2], palavras[3], palavras[4], palavras[5], palavras[6]);
                }

            } else {

                System.out.println("Verifique os parametros introduzidos");
                count = 0;

            }
        }
        //tenho de ter algo que controle os ID's para nao serem maiores ou menores que o suposto
        //tenho de ter ago que mostre as infos no escrâ
        //criar as praias e estabelecer logo as quantidades de tudo - construtor está acima
        //Realizar login
    }

    private static boolean credentialsExist(String user_id) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");

                if (line[0].equals(user_id)) {
                    // User ID already exists in the file
                    scanner.close();

                    return true;
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            // Handle file not found exception
            e.printStackTrace();
        }

        // User ID does not exist in the file
        return false;
    }

    public static void fazerRegisto(String user_id, String senha) {
        if (user_id.isEmpty() || senha.isEmpty()) {
            System.out.println("credenciais erradas");
        } else {
            if (credentialsExist(user_id)) {
                System.out.println("User with the same ID already exists.");
                return;
            }

            String userInfo = user_id + " " + senha + " " + "false";
            try {
                FileWriter fileWriter = new FileWriter(filePath);
                PrintWriter printWriter = new PrintWriter(fileWriter);

                printWriter.println(userInfo);

                printWriter.close();
                fileWriter.close();

                //"Information written to the file successfully."
            } catch (IOException e) {
                // Handle IO exception
                e.printStackTrace();
            }
        }
    }



    public static boolean fazerLogin(String user_id, String senha) {
        int changed = 0;
        try {
            // Cria um BufferedReader para ler o arquivo original e um para escrever no arquivo temporário
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));

            //Lê cada linha do arquivo
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                //Divide a linha usando espaços como delimitador
                String[] tokens = line.split(" ");

                //Verifica se o user_id e a senha correspondem
                if (tokens.length >= 2) {
                    if (tokens[0].equals(user_id) && tokens[1].equals(senha)) {
                        //Se correspondem, atualiza a terceira informação para "true"
                        tokens[2] = "true";
                        changed = 1;
                    } if (!tokens[0].equals(user_id) && tokens[1].equals(senha)) {
                        System.out.println("User id incorreto");
                    } if (tokens[0].equals(user_id) && !tokens[1].equals(senha)) {
                        System.out.println("Senha incorreta");
                    } if (!tokens[0].equals(user_id) && !tokens[1].equals(senha)) {
                        System.out.println("Credenciais inexistentes. Registe-se.");
                    }
                }

                //Escreve a linha atualizada no arquivo temporário
                bufferedWriter.write(String.join(" ", tokens));
                bufferedWriter.newLine();
            }

            // Fecha os recursos
            bufferedReader.close();
            bufferedWriter.close();

            if (changed != 0) {
                //ocorreu mudança == user existe
                // Read from the temporary file
                FileReader tempFileReader = new FileReader(tempFile);
                bufferedReader = new BufferedReader(tempFileReader);

                // Open the existing file (output.txt) in append mode, we'll re-write the whole file
                FileWriter existingFileWriter = new FileWriter(filePath, false);
                PrintWriter printWriter = new PrintWriter(existingFileWriter);

                // Read each line from the temporary file and append it to the existing file
                while ((line = bufferedReader.readLine()) != null) {
                    printWriter.println(line);
                }

                // Close resources
                bufferedReader.close();
                printWriter.close();
                tempFileReader.close();
                existingFileWriter.close();
                System.out.println("Login efetuado com sucesso");
                return true;
            }

        } catch (IOException e) {
            // Trata a exceção de IO
            e.printStackTrace();
        }
        return false;
    }
    public static void fazerLogout(String user_id, String senha) {
        int changed = 0;
        try {
            // Cria um BufferedReader para ler o arquivo original e um para escrever no arquivo temporário
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));

            //Lê cada linha do arquivo
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                //Divide a linha usando espaços como delimitador
                String[] tokens = line.split(" ");

                //Verifica se o user_id e a senha correspondem
                if (tokens.length >= 2 && tokens[0].equals(user_id) && tokens[1].equals(senha)) {
                    //Se correspondem, atualiza a terceira informação para "true"
                    tokens[2] = "false";
                    changed = 1;
                }

                //Escreve a linha atualizada no arquivo temporário
                bufferedWriter.write(String.join(" ", tokens));
                bufferedWriter.newLine();
            }

            // Fecha os recursos
            bufferedReader.close();
            bufferedWriter.close();

            if (changed != 0) {
                //ocorreu mudança == user existe
                // Read from the temporary file
                FileReader tempFileReader = new FileReader(tempFile);
                bufferedReader = new BufferedReader(tempFileReader);

                // Open the existing file (output.txt) in append mode, we'll re-write the whole file
                FileWriter existingFileWriter = new FileWriter(filePath, false);
                PrintWriter printWriter = new PrintWriter(existingFileWriter);

                // Read each line from the temporary file and append it to the existing file
                while ((line = bufferedReader.readLine()) != null) {
                    printWriter.println(line);
                }

                // Close resources
                bufferedReader.close();
                printWriter.close();
                tempFileReader.close();
                existingFileWriter.close();
                System.out.println("Logout efetuado com sucesso");
            }

        } catch (IOException e) {
            // Trata a exceção de IO
            e.printStackTrace();
        }
    }
    public static void fazerReserva(String user_id, String senha,
                                    String praia, String data, String hora, String qnt_people) {
        //primeiro verificar se o user efetuou login
        //verificar se a praia ainda tem objetos disponiveis
        //verificar se a quantidade de pessoas é a correta para a praia escolhida
    }
}