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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) throws FileNotFoundException {

    Scanner scanner = new Scanner(System.in);
    String input;

    String command[]; // Comando (opcao + argumentos)
    String option; // opcao (:e,:a,etc.)
    String com_args[]; // Argumentos do comando

    ListaEncadeada mainList = new ListaEncadeada();
    ListaEncadeada transferList = new ListaEncadeada();

    Node markedNode = new Node();

    String filepath = "";
    FileReader file = new FileReader(filepath); // Arquivo a ser lido
    char saveOrNot;
    boolean isSaved = true;

    int start_line = 0;
    int end_line = 0;
    int idx_line = 0;
    int ini_count;
    int final_count;

    System.out.println("===================================");
    System.out.println("Bem-vindo ao Gerenciador de Arquivos!\n Lucas P.C.Sarai e Vitor A. Pereira");
    System.out.println("===================================\n");

    while (true) {

      // Leitura do comando
      System.out.print(">> ");
      input = scanner.nextLine();
      if (input.equals(""))
        continue; // Quando apenas passa uma linha em branco com [ENTER],exibe outro prompt

      // Leitura do comando inserido, separando-os cada comando por espaços
      command = input.split(" ");

      // Leitura da opção escolhida no modelo ":option"
      option = command[0];

      // Validando comandos ':<comando>' inseridos incorretamente
      if (option.toCharArray()[0] != ':' || option.length() == 1 || option.toCharArray()[1] == ' ') {
        System.out.println("Erro: Comando inválido!\n");
        continue;
      }

      // Comando de Encerramento do Programa
      if (option.equals(":q!")) {
        if (!isSaved) { // Salvar caso ainda não esteja salvo
          try {
            // Perguntar se o usuário deseja salvar
            System.out.print("Deseja salvar o arquivo antes de sair? (s/n): ");
            saveOrNot = scanner.nextLine().charAt(0);
            // Validando entrada
            while (saveOrNot != 's' && saveOrNot != 'S' && saveOrNot != 'n' && saveOrNot != 'N') {
              System.out.print(
                  "\nOpção inválida! Somente 's' ou 'n' são aceitos.\nDeseja salvar o arquivo antes de sair (s/n): ");
              saveOrNot = scanner.nextLine().charAt(0);
            }
            // Se resposta afirmativa, salvar
            if (saveOrNot == 's' || saveOrNot == 'S') {
              System.out.print("Digite o endereço do arquivo: ");
              filepath = scanner.nextLine();
              file = new FileReader(filepath);
              file.write(mainList);
              System.out.println("Arquivo salvo com sucesso!\n");
            }
          } catch (IOException e) { // Tratamento de exceção do método file.write()
            System.out.println("Erro ao escrever no arquivo: Arquivo não encontrado!\n");
          }

        } else { // Se já estiver salvo
          // Exibe o arquivo onde já está salvo
          if (!mainList.isEmpty())
            System.out.println("\nConteúdo já salvo em " + filepath.replaceAll("//", "/") + "\n");
        }

        scanner.close();
        System.out.println("Editor encerrado.");
        break;
      }

      // Leitura dos argumentos do comando
      com_args = new String[3];

      // Validação do comando para ver se foi escrito corretamente
      if (command.length > 4 || (option.length() != 2 && !(option.equals(":xG")
                                                          ||option.equals(":XG")
                                                          ||option.equals(":help")))) {
        System.out.println("Erro: Comando inválido!\n");
        continue;
      }
      // Receber e contar os argumentos
      for (int args_idx = 1; args_idx < command.length; args_idx++)
        com_args[args_idx - 1] = command[args_idx];
      int com_args_count = command.length - 1; // Quantidade de argumentos

      // Tratando a entrada para que o endereço do arquivo seja lido corretamente
      if (com_args_count > 0 && (option.equals(":w") || option.equals(":e"))) {
        filepath = command[1].replaceAll("/", "//").replaceAll("\"", "");

      }
      
      // Rodar a ação de acordo com o comando
      switch (option.toCharArray()[1]) {
          
        // :e NomeArq.ext --> insere cada linha do arquivo como um nó na lista encadeada
        case 'e' -> {
            // Validando quantidade de argumentos inseridos
            if (com_args_count != 1)
                System.out.println("Erro: Uso incorreto do comando!\n");
            else {
                try {
                    mainList.clear(); // Limpa conteúdo anterior salvo
                    
                    // Recebe o arquivo do parâmetro
                    file = new FileReader(filepath);
                    file.readToList(mainList);
                    isSaved = false;
                    file.setCountLines(mainList.count());
                    System.out.println("Arquivo lido com sucesso!\n");
                } catch (FileNotFoundException nfe) {
                    // Caso arquivo não seja encontrado
                    System.out.println("Erro ao ler do arquivo: Arquivo não encontrado!\n");
                }
            } 
        }
        // :w & :w NomeArq.ext --> Escrever a lista em um arquivo
        case 'w' -> {
            if (mainList.isEmpty()) {
                System.out.println("Erro: Não há conteúdo salvo!\n");
                break;
            }

            try {
                // Analisando quantidade de argumentos inseridos
                if (com_args_count > 1) {
                    System.out.println("Erro: Uso incorreto do comando!\n");
                } else {
                    file = new FileReader(filepath);
                    file.write(mainList);
                    
                    isSaved = true; // Tratando como salva a lista principal
                    System.out.println("Arquivo salvo com sucesso!\n");
                }
            } catch (IOException e) {
                // Tratando exceção do método file.write()
                System.out.println("Erro ao escrever no arquivo: Ocorreu um erro de E/S!\n");
            } 
        }
        // :v LinIni LinFim --> marca um texto da lista em um intervalo determinado
        case 'v' -> {
            // Tratando quantidade de argumentos inseridos
            if (com_args_count != 2)
                System.out.println("Erro: Uso incorreto do comando!\n");
            else {
                try {
                    // Recebe o intervalo de linhas
                    start_line = Integer.parseInt(com_args[0]);
                    end_line = Integer.parseInt(com_args[1]);
                    idx_line = 1;
                    
                    // Percorrendo a lista até a primeira linha (nó) do intervalo
                    // Caso o intervalo seja válido
                    Node aux = mainList.getHead();
                    if ((start_line >= 1 && end_line <= mainList.count()) && (start_line <= end_line)) {
                        transferList.clear(); // Limpando conteúdo anterior da área de transferência
                        while (idx_line < start_line) {
                            aux = aux.getNext();
                            ++idx_line;
                        }
                        // Marcando a primeira linha do intervalo
                        markedNode = aux;
                        
                        System.out.println(
                                "Intervalo de linhas " + start_line + "-" + end_line + " marcado para cópia ou recorte.\n");
                        
                    } else {
                        System.out.println("Erro: Intervalo inválido!\n");
                    }
                } catch (NumberFormatException nfe) {
                    // Caso o intervalo não seja numérico
                    System.out.println("Erro: Somente são aceitos os índices inteiros das linhas!\n");
                }
            } 
        }
        // :y --> copia o conteúdo marcado para uma lista utilizada como área de
        // transferência
        case 'y' -> {
            // Tratando quantidade de argumentos
            if (com_args_count != 0)
                System.out.println("Erro: Uso incorreto do comando!\n");
            // Tratando se algum texto foi marcado
            else if (start_line == 0 || end_line == 0) {
                System.out.println("Erro: Nenhum texto foi marcado para a área de transferência!\n");
            } else {
                // Adicionando na área de transferência o conteúdo marcado
                while (idx_line <= end_line) {
                    transferList.append(markedNode.getData());
                    markedNode = markedNode.getNext();
                    ++idx_line;
                }
                System.out.println("Conteúdo copiado para área de transferência:\n" + transferList.toString() + "\n\n");
                // Desmarcando as linhas
                start_line = end_line = 0;
            } }
        
        // :c --> recorta o texto marcado para a lista de área de transferência
        case 'c' -> {
            // Tratando quantidade de argumentos inseridos
            if (com_args_count != 0)
                System.out.println("Erro: Uso incorreto do comando!\n");
            // Tratando se algum texto foi marcado
            else if (start_line == 0 || end_line == 0)
                System.out.println("Erro: Nenhum texto foi marcado para a área de transferência!\n");
            else {
                Node removedNode;
                // Removendo da lista principal e passando para a área de transferência o
                // conteúdo marcado
                while (idx_line <= end_line) {
                    removedNode = markedNode;
                    markedNode = markedNode.getNext();
                    removedNode = mainList.removeNode(removedNode.getData());
                    transferList.append(removedNode.getData());
                    ++idx_line;
                }
                // Desmarcando as linhas
                start_line = end_line = 0;
                System.out.println("Conteúdo copiado para área de transferência:\n" + transferList.toString() + "\n\n");
            } 
        }
        
         // p LinIniColar --> cola o conteúdo da área de transferência na lista, a partir
        // de LinIniColar (linha presente na mainList)
        case 'p' -> {
            // Tratando quantidade de argumentos
            if (com_args_count != 1)
                System.out.println("Erro: Uso incorreto do comando!\n");
            else {
                try {
                    // Tratando se há conteúdo na área de transferência
                    if (transferList.isEmpty()) {
                        System.out.println("Erro: Não há conteúdo na Área de transferência!\n");
                        break;
                    }
                    // Recebe o índice da linha
                    start_line = Integer.parseInt(com_args[0]);
                    
                    // Insere as linhas na lista principal, caso a linha inicial seja válida
                    if (start_line >= 1 && start_line <= mainList.count()) {
                        idx_line = 1;
                        Node reference = mainList.getHead();
                        // Posicionando o nó na linha desejada
                        while (idx_line < start_line-1) {
                            reference = reference.getNext();
                            ++idx_line;
                        }
                        idx_line = 1;
                        
                        // Percorrendo a área de transferência e inserindo cada linha na lista principal
                        Node transf_line = transferList.getHead();
                        while (idx_line <= transferList.count()) {
                            mainList.insertAfter(transf_line.getData(), reference);
                            transf_line = transf_line.getNext();
                            reference = reference.getNext();
                            ++idx_line;
                        }
                        System.out.println(transferList.count() + " linha(s) inserida(s) após linha " + start_line + "\n");
                        transferList.clear(); // Limpando a área de transferência
                    } else {
                        System.out.println("Erro: Linha inválida!\n");
                    }
                } catch (NumberFormatException nfe) {
                    // Caso a linha não seja numérica
                    System.out.println("Erro: Somente são aceitos os índices inteiros das linhas!\n");
                }
                
            } 
        }
         // :s ou :s LinIni LinFim
        case 's' -> {
            String show;
            // Acessando o comando de acordo com a quantidade de argumentos
            
            // :s -> mostra o conteúdo completo da lista principal
            if (com_args_count == 0) {
                start_line = 1;
                end_line = mainList.count();
                show = mainList.showList(start_line, end_line);
                // Tratando se a lista está vazia
                if (show == null) {
                    System.out.println("Erro: Não há conteúdo na lista!\n");
                    break;
                }
                // Percorrendo a lista, de 20 em 20 linhas, e exibindo
                while (start_line <= end_line) {
                    System.out.println(show + "\n\n");
                    if (end_line - start_line < 20) // Se a lista tiver menos de 20 linhas
                        break; // Exibe até o final e não solicita mais linhas
                    //Solicitando novas linhas, após 20 linhas
                    System.out.print("Pressione [ENTER] para continuar: ");
                    scanner.nextLine();
                    start_line+=20; // Avança para as próximas 20 linhas
                    show = mainList.showList(start_line, end_line);
                    
                }
                // Tratando a quantidade de argumentos inseridos
            } else if (com_args_count != 2)
                System.out.println("Erro: Uso incorreto do comando!\n");
            // :s LinIni LinFim -> mostra o conteúdo de LinIni a LinFim
            else {
                try {
                    //Recebendo o intervalo de linhas
                    start_line = Integer.parseInt(com_args[0]);
                    end_line = Integer.parseInt(com_args[1]);
                    
                    show = mainList.showList(start_line, end_line);
                    //Tratando se há conteúdo na lista
                    if (show == null) {
                        System.out.println("Erro: Não há conteúdo na lista!\n");
                        break;
                    }
                    
                    //Exibe o conteúdo desejado, se o intervalo for válido
                    if ((start_line < 0 || end_line > mainList.count()) || (start_line > end_line))
                        System.out.println("Erro: intervalo inválido!\n");
                    else {
                        //Percorrendo o intervalo de linhas e exibindo, de 20 em 20 linhas
                        while (start_line <= end_line) {
                            System.out.println(show + "\n\n");
                            
                            if (end_line - start_line < 20) //Caso a quantidade solicitada seja menor que 20
                                break; //Exibe de uma vez e não solicita mais linhas
                            
                            //Solicitando novas linhas, após 20 linhas
                            System.out.print("Pressione [ENTER] para continuar: ");
                            scanner.nextLine();
                            start_line+=20;
                            show = mainList.showList(start_line, end_line);
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Caso o intervalo não seja numérico
                    System.out.println("Erro: Somente são aceitos os índices inteiros das linhas!\n");
                }
                
            } 
        }
        
        // :x posLin ou xG posLin
        case 'x' -> {
            // Acessando o comando de acordo com a quantidade de argumentos
            // Validando quantidade de argumentos
            if (com_args_count != 1)
                System.out.println("Erro: Uso incorreto do comando!\n");
            
            // :xG posLin -> exclui a partir da linha posLin, todas as linhas seguintes
            else if (option.equals(":xG")) {
                try {
                    // Recebendo a linha, caso seja válida
                    start_line = Integer.parseInt(com_args[0]);
                    if (start_line >= 1 && start_line <= mainList.count()) {
                        idx_line = 1;
                        ini_count = file.getCountLines();
                        // Se quiser apagar desde o início, então já chama o método de limpeza completa
                        if (start_line == idx_line) {
                            mainList.clear();
                            break;
                        }
                        // Encontra a linha inicial a remover
                        Node aux = mainList.getHead();
                        while (idx_line < start_line) {
                            idx_line++;
                            aux = aux.getNext();
                        }
                        Node removed = aux;
                        boolean isRep = false;
                        // Remove as outras linhas
                        while (!isRep) {
                            aux = aux.getNext();
                            mainList.removeNode(removed.getData());
                            idx_line++;
                            removed = aux;
                            if (aux == mainList.getHead()) {
                                isRep = true;
                            }
                        }
                        file.setCountLines(mainList.count());
                        final_count = file.getCountLines();
                        System.out.println(ini_count - final_count + " linha(s) apagada(s)!\n");
                    } else {
                        System.out.println("Erro: Linha inválida!\n");
                    }
                } catch (NumberFormatException nfe) {
                    // Caso a linha não seja numérica
                    System.out.println("Erro: Somente são aceitos os índices inteiros das linhas!\n");
                }
                
            }
            
            // :x posLin -> exclui a linha de posLin
            else if (option.equals(":x")) {
                try {
                    // Recebendo o índice desejado
                    start_line = Integer.parseInt(com_args[0]);
                    // Percorrendo a lista e excluindo a linha desejada, caso seja válida
                    if (start_line >= 1 && start_line <= mainList.count()) {
                        idx_line = 1;
                        
                        Node nodeToRemove = mainList.getHead();
                        while (idx_line < start_line) {
                            nodeToRemove = nodeToRemove.getNext();
                            ++idx_line;
                        }
                        
                        System.out.println("Linha " + idx_line + " removida!\n");
                        mainList.removeNode(nodeToRemove.getData());
                        System.out.println(idx_line + "     " + nodeToRemove.getData() + "\n\n");
                        
                    } else {
                        System.out.println("Erro: Linha inválida!\n");
                    }
                } catch (NumberFormatException nfe) {
                    // Caso a linha não seja numérica
                    System.out.println("Erro: Somente são aceitos os índices inteiros das linhas!\n");
                }
                
            } else
                System.out.println("Erro: Uso incorreto do comando!\n");
        }
        
        // :XG posLin -> Remove toda as linhas até parar na linha posLin
        case 'X' -> {
            // Valindando quantidade de argumentos
            if (com_args_count != 1)
                System.out.println("Erro: Uso incorreto do comando!\n");
            else {
                try {
                    // Recebendo índice da linha de parada
                    end_line = Integer.parseInt(com_args[0]);
                    // Removendo elementos até a linha de parada, caso seja válida
                    if (end_line >= 1 && end_line <= mainList.count()) {
                        idx_line = 1;
                        ini_count = file.getCountLines();
                        while (idx_line <= end_line) {
                            mainList.removeHead();
                            idx_line++;
                        }
                        file.setCountLines(mainList.count());
                        final_count = file.getCountLines();
                        System.out.println(ini_count - final_count + " linha(s) apagada(s)!\n");
                    } else {
                        System.out.println("Erro: Linha inválida!\n");
                    }
                } catch (NumberFormatException nfe) {
                    // Caso a linha não seja numérica
                    System.out.println("Erro: Somente são aceitos os índices inteiros das linhas!\n");
                }
            } 
        }

        
        case '/' -> {
            
            
            // Acessando o comando de acordo com a quantidade de argumentos
            
            // :/ Elem -> Procura e exibe linhas com o elemento procurado
            if (com_args_count == 1) {
                //Tratando se a lista estiver vazia
                if (mainList.isEmpty())
                    System.out.println("Erro: Não há conteúdo salvo!\n");
                else {
                    //Recebendo o elemento procurado
                    String element_ref = com_args[0];
                    Node aux = mainList.getHead();
                    boolean isRep = false, isFound = false;
                    idx_line = 1;
                    //Percorrendo a lista e procurando o elemento
                    //Caso encontre, exibe a linha
                    while (!isRep) {
                        if (aux.getData().contains(element_ref)) {
                            System.out.println(idx_line + "               " + aux.getData());
                            isFound = true;
                        }
                        
                        idx_line++;
                        aux = aux.getNext();
                        if (aux == mainList.getHead())
                            isRep = true;
                        
                    }
                    if (!isFound)
                        System.out.println("Erro: Elemento não encontrado!\n");
                }
                
                //:/ Elem ElemTroca-> Troca o elemento procurado pelo elemento de reposição
            } else if (com_args_count == 2)
                //Tratando se há conteúdo salvo na lista
                if (mainList.isEmpty())
                    System.out.println("Erro: Não há conteúdo salvo!\n");
                else {
                    //Recebendo os elementos
                    String element_ref = com_args[0];
                    String element_to_replace = com_args[1];
                    Node aux = mainList.getHead();
                    boolean isRep = false, isFound = false;
                    idx_line = 1;
                    //Percorrendo a lisra e trocando os elementos
                    while (!isRep) {
                        if (aux.getData().contains(element_ref)) {
                            System.out.println("Linha " + idx_line + ":");
                            System.out.println("Antes: " + aux.getData());
                            aux.setData(aux.getData().replaceAll(element_ref, element_to_replace));
                            System.out.println("Depois: " + aux.getData() + "\n");
                            isFound = true;
                        }
                        
                        idx_line++;
                        aux = aux.getNext();
                        if (aux == mainList.getHead())
                            isRep = true;
                    }
                    if (!isFound)
                        System.out.println("Erro: Elemento não encontrado!\n");
                }
            //:/ Elem ElemTroca Linha -> Troca o elemento procurado pelo elemento de reposição na linha específica
            else if (com_args_count == 3)
                try {
                    //Tratando se há conteúdo salvo
                    if (mainList.isEmpty())
                        System.out.println("Erro: Não há conteúdo salvo!\n");
                    else {
                        //Recebendo os argumentos
                        start_line = Integer.parseInt(com_args[2]);
                        //Validando a linha
                        if (start_line < 1 || start_line > mainList.count()) {
                            System.out.println("Erro: Linha inválida!\n");
                            break;
                        }
                        String element_ref = com_args[0];
                        String element_to_replace = com_args[1];
                        Node aux = mainList.getHead();
                        boolean isFound = false;
                        idx_line = 1;
                        //Posicionando o Nó na linha desejada
                        while (idx_line < start_line) {
                            idx_line++;
                            aux = aux.getNext();
                        }
                        //Realizando a troca
                        if (aux.getData().contains(element_ref)) {
                            System.out.println("Linha " + idx_line + ":\n");
                            System.out.println("Antes: " + aux.getData());
                            aux.setData(aux.getData().replaceAll(element_ref, element_to_replace));
                            System.out.println("Depois: " + aux.getData() + "\n");
                            isFound = true;
                        }
                        if (!isFound)
                            System.out.println("Erro: Elemento não encontrado!\n");
                    }
                } catch (NumberFormatException nfe) {
                    //Caso a linha não seja numérica
                    System.out.println("Erro: Somente são aceitos os índices inteiros das linhas!\n");
                }
            
            //Quantidade de argumentos inválida
            else
                System.out.println("Erro: Uso incorreto do comando!\n");
            }
        
        // :a --> Permitir a inserção de uma ou mais linhas e inserir na lista depois da posição PosLin
        case 'a' -> {
            //Validando a quantidade de argumentos
            if (com_args_count != 1)
                System.out.println("Erro: Uso incorreto do comando!\n");
            else {
                try {
                    //Validando se não há conteúdo salvo
                    if (mainList.isEmpty()) {
                        System.out.println("Erro: Não há conteúdo salvo!\n");
                        break;
                    }
                    //Recebendo o índice de inserção
                    start_line = Integer.parseInt(com_args[0]);
                    
                    // Validando o índice
                    if (start_line < 1 || start_line > mainList.count()) {
                        System.out.println("Erro: Linha inválida!\n");
                        break;
                    }
                    
                    //Encontrando a linha de referência
                    Node aux = mainList.getHead();
                    idx_line = 1;
                    while (idx_line < start_line) {
                        aux = aux.getNext();
                        idx_line++;
                    }
                    //Recebendo o conteúdo a ser inserido
                    Node ref = aux;
                    int count_lines_in = 0;
                    System.out.println("Insira o conteúdo nas linhas abaixo:");
                    String new_line = scanner.nextLine();
                    //Inserindo as linhas
                    //Para na linha de valor :a
                    while (!new_line.equals(":a")) {
                        mainList.insertAfter(new_line, ref);
                        new_line = scanner.nextLine();
                        ref = ref.getNext();
                        count_lines_in++;
                    }
                    System.out.println(count_lines_in + " linha(s) inserida(s) após a linha " + idx_line + "!\n");
                } catch (NumberFormatException nfe) {
                    //Caso o índice não seja numérico
                    System.out.println("Erro: Somente são aceitos os índices inteiros das linhas!\n");
                }
            } 
        }
        
        // :i --> Permitir a inserção de uma ou mais linhas e inserir na lista antes da posição PosLin
        case 'i' -> {
            //Validando quantidade de argumentos
            if (com_args_count != 1)
                System.out.println("Erro: Uso incorreto do comando!\n");
            else {
                try {
                    //Se não houver conteúdo salvo
                    if (mainList.isEmpty()) {
                        System.out.println("Erro: Não há conteúdo salvo!\n");
                        break;
                    }
                    //Recebendo o índice de inserção
                    start_line = Integer.parseInt(com_args[0]);
                    
                    //Validando índice recebido
                    if (start_line < 1 || start_line > mainList.count()) {
                        System.out.println("Erro: Linha inválida!\n");
                        break;
                    }
                    
                    //Percorrendo a lista até a linha anterior à posLin
                    Node aux = mainList.getHead();
                    idx_line = 1;
                    while (idx_line < start_line - 1) {
                        aux = aux.getNext();
                        idx_line++;
                    }
                    Node ref = aux;
                    int count_lines_in = 0;
                    //Recebendo o conteúdo a ser inserido
                    System.out.println("Insira o conteúdo nas linhas abaixo:");
                    String new_line = scanner.nextLine();
                    
                    //Inserindo as linhas após a linha de referência (anterior à posLin), até que seja digitado :i
                    while (!new_line.equals(":i")) {
                        mainList.insertAfter(new_line, ref);
                        new_line = scanner.nextLine();
                        ref = ref.getNext();
                        // System.out.println("Referência: "+ ref.getData());
                        count_lines_in++;
                    }
                    System.out.println(count_lines_in + " linhas inseridas antes da linha " + idx_line + "!\n");
                } catch (NumberFormatException nfe) {
                    //Caso o índice não seja numérico
                    System.out.println("Erro: Somente são aceitos os índices inteiros das linhas!\n");
                }
            } 
        }
        
        // :help --> Exibir informações sobre as operações possíveis
        case 'h' -> {
            //Validando quantidade de argumentos, pois não deve possuir argumentos
            if (com_args_count != 0)
                System.out.println("Erro: Uso incorreto do comando!\n");
            //Caso não seja escrito de forma correta
            else if (!option.equals(":help"))
                System.out.println("Erro: Comando inválido!\n");
            
            //Exibir informações de ajuda
            else {
                System.out.println(
                        "Operações      ->      Ação resultante\n\n:e      ->      NomeArq.ext Abrir o arquivo de nome NomeArq.ext e armazenar cada linha em um nó da lista.\n:w      ->      Salvar a lista no arquivo atualmente aberto.\n:w NomeArq.ext      ->      Salvar a lista no arquivo de nome NomeArq.ext.\n:q!      ->       Encerrar o editor. Caso existam modificações não salvas na lista, o programa deve solicitar confirmação se a pessoa usuária do editor deseja salvar as alterações em arquivo antes de encerrar o editor.\n:v LinIni LinFim      ->      Marcar um texto da lista (para cópia ou recorte – “área de transferência”) da LinIni até LinFim. Deve ser verificado se o intervalo [LinIni, LinFim] é válido.\n:y      ->      Copiar o texto marcado (ver comando anterior) para uma lista usada como área de transferência.\n:c      ->      Recortar o texto marcado para a lista de área de transferência.\n:p LinIniColar      ->      Colar o conteúdo da área de transferência na lista, a partir da linha indicada em LinIniColar. Deve ser verificado se LinIniColar é válido.\n:s      ->       Exibir em tela o conteúdo completo do código-fonte que consta na lista, de 20 em 20 linhas.\n:s LinIni LinFim      ->      Exibir na tela o conteúdo do código-fonte que consta nalista, dalinha inicial LinIni até a linha final LinFim, de 20 em 20 linhas.\n:x Lin      ->      Apagar a linha de posição Lin da lista.\n:xG Lin      ->      Apagar o conteúdo a partir da linha Lin até o final da lista.\n:XG Lin      ->      Apagar o conteúdo da linha Lin até o início da lista.\n:/ Elemento      ->      Percorrer a lista, localizar as linhas que contém Elemento e exibir o conteúdo das linhas por completo.\n:/ Elem ElemTroca      ->      Percorrer a lista e realizar a troca de Elem por ElemTroca em todas as linhas do código-fonte.\n:/ Elem ElemTroca Linha      ->      Realizar a troca de Elem por ElemTroca na linha Linha do código fonte.\n:a PosLin      ->      Permitir a inserção de uma ou mais linhas e inserir na lista depoisda posição PosLin. O término da entrada do novo conteúdo é dado por um :a em uma linha vazia.\n:i PosLin      ->      Permitir a inserção de uma ou mais linhas e inserir na lista antesda posição PosLin. O término da entrada do novo conteúdo é dado por um :i em uma linha vazia.\n:help      ->      Apresentar na tela todas as operações permitidas no editor.\n\n");
                
            } 
        }
        //Caso não seja nenhum dos comandos acima
        default -> System.out.println("Erro: Comando inválido!");
      }
    }
  }
}