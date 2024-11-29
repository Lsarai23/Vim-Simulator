package Apl2;

/* 
 * @author Lucas Pires de Camargo Sarai - 10418013
 * @author Vitor Alves Pereira - 10410862
 * 
 * Ciência da Computação
 * 3D
 * EDI
 * 
 * Referências:
 * [1] - https://profkishimoto.github.io/edi03d-2024-1/atividades/n2/EDI-2024.1%20-%20Apl2.pdf
 * [2] - https://profkishimoto.github.io/edi03d-2024-1/conteudo/semana-13/LinkedList2.pdf
 */

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;


public class FileReader {
  private String path;
  private File file;
  private int countLines;
  

  public FileReader(String path) {
    this.path = path;
    file = new File(path);
    countLines = 0;
  }

  // countLines() --> retorna o total de linhas de um arquivo
  public int getCountLines() { return countLines; }
  public void setCountLines(int value) { countLines = value; }

  
  public void readToList(ListaEncadeada list) throws FileNotFoundException {
  
    //Passa o conteúdo do arquivo para a lista
      Scanner scanner = new Scanner(file);
      while(scanner.hasNext()){
        list.append(scanner.nextLine());
        ++countLines;
      }
      scanner.close();
      
  
  }

  public void write(ListaEncadeada list) throws IOException {
    //Escreve no arquivo o conteúdo da lista encadeada
    FileWriter fw = new FileWriter(path);
    PrintWriter writer = new PrintWriter(fw); 
    writer.print(list.toString()); // Limpeza do arquivo e escrita
    writer.close();  
    writer.close(); 
  }
}