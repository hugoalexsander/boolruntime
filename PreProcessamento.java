import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Paths;

public class PreProcessamento {

    public void Processador(){
        try(Scanner scanner = new Scanner(Paths.get("BOOL.txt"))){
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if(line.isEmpty()){
                    continue;
                }

                //line = line.replaceAll("\\s+", " ");
                //System.out.println(line);
                String[] palavras = line.trim().split("\\s+");
                String linha = toString(palavras);
                escreveNoArquivo(linha);
            }
        }catch(Exception e){
            System.out.println("Arquivo inviável");
            e.printStackTrace();
        }
    }

    public String toString(String[] palavras) {
        String linha = "";

        for (int i = 0; i < palavras.length; i++) {
            linha += palavras[i] + " ";
        }

        linha += "\\n ";

        return linha;
    }

    public void escreveNoArquivo(String linha){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) {
            writer.write(linha);
        } catch (IOException e) {
            System.out.println("Erro de compilação!");
        }
    }
}