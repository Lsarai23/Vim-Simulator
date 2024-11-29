package Apl2;

/* 
 * @author Lucas Pires de Camargo Sarai - 10418013
 * @author Vitor Alves Pereira - 10410862
 * 
 * Ciência da Computação
 * 3Da
 * EDI
 * 
 * Referências:
  *[1] - https://profkishimoto.github.io/edi03d-2024-1/atividades/n2/EDI-2024.1%20-%20Apl2.pdf
 * [2] - https://profkishimoto.github.io/edi03d-2024-1/conteudo/semana-13/LinkedList2.pdf
 */

public class ListaEncadeada {

  private Node head;
  private int count;

  public ListaEncadeada() {
    head = null;
    count = 0;
  }
  
  // insertAfter(String value, Nó ref) --> Insere um novo nó após o nó que
  // referência
  public boolean insertAfter(String value, Node ref) {

    // O nó com valor referência não existe se aux for null.
    if (getNode(ref.getData()) == null) {
      return false;
    }

    // Se a referência for o tail, insere ao final
    if (ref == getTail()) {
      append(value);
      return true;
    }

    //Conectando o novo nó à lista
    Node n = new Node(value, ref.getNext());
    ref.setNext(n);

    ++count;

    return true;
  }

  

  // isDup(String value) --> Retorna true se existirem, pelo menos, dois nós com o
  // mesmo valor
  public boolean isDup(String value) {
    Node ref = getNode(value), aux;
    //Se a referência estiver na lista, procura duplicatas
    if (ref != null) {
        
      //Procurando duplicatas
      aux = ref.getNext();
      while (aux != null) {
        if (aux.getData().equals(value))
          return true; // Caso encontre
        aux = aux.getNext();
      }
    }

    //Caso não encontre
    return false;

  }


  // insert(String value) --> insere um novo elemento no início da lista
  public void insert(String value) {
    //Posiciona o nó antes de head
    Node n = new Node(value, head);
    ++count;
    if (count == 1) { // head = tail
      n.setNext(n);
    } else { // O tail precisa referenciar o novo head
      Node tail = getTail();
      tail.setNext(n);
    }
    
    //Atualizando head
    head = n;
  }

  // append(String value) --> insere um novo elemento no final da lista
  public void append(String value) {
    Node n = new Node(value);

    if (isEmpty()) {
      head = n; // Lista vazia -> passa a ter o primeiro elemento
      head.setNext(head);
    } else { // Lista não vazia -> percorre até o último elemento
      Node tail = getTail();
      
      //Insere ao final e referencia o head
      tail.setNext(n);
      tail = n;
      tail.setNext(head);
    }

    ++count;
  }

  // removeHead() --> remove o primeiro elemento da lista
  public Node removeHead() {
    if (head == null) { // Lista vazia.
      return null;
    }

    Node removed = head;
    Node newHead = head.getNext();
    if (newHead != head) { // count > 1
      if (newHead.getNext() == removed) // count = 2
        newHead.setNext(newHead); // o novo head se referencia -> count = 1
      else { //count > 2, procura o tail e referencia o novo head
        Node tail = getTail();
        tail.setNext(newHead);
      }
    } else { // count = 1 -> lista fica vazia
      newHead = null;
    }
    head = newHead;
    // Romper a conexão com o nó removido.
    removed.setNext(null);

    --count;
    return removed;
  }

  // removeTail() --> remove o ultimo elemento da lista
  public Node removeTail() {

    if (isEmpty())
      return null; // Lista vaiza

    if (head.getNext() == head)
      return removeHead(); // Head = Tail

    // Procurar um antes do tail
    Node aux = head;
    while (aux.getNext().getNext() != head)
      aux = aux.getNext();

    // Cortar conexões com o Nó removido
    Node removed = aux.getNext();
    aux.setNext(head);
    removed.setNext(null);
    --count;

    return removed;

  }

  // removeNode(String value) --> remover nó específico, usando o valor como
  // referência
  public Node removeNode(String value) {

    if (isEmpty()) { // Lista vazia
      return null;
    }

    // Head é a referência a remover
    if (head != null && head.getData().equals(value)) {
      return removeHead();
    }

    // Tail é a referência a remover
    Node tail = getTail();
    if (tail != null && tail.getData().equals(value) && !isDup(tail.getData())) {
      return removeTail();
    }

    Node aux = head;
    Node ref = getNode(value); // Procura o Nó a remover

    if (ref == null)
      return null; // Se não encontrar o Nó de referência

    // Encontra o Nó anterior ao de referência
    while (aux.getNext() != tail && aux.getNext() != ref) {
      aux = aux.getNext();
    }

    // Romper as conexões
    Node removed = aux.getNext();
    aux.setNext(removed.getNext());
    removed.setNext(null);
    --count;

    return removed;
  }

  // getHead() --> retorna o primeiro elemento da lista
  public Node getHead() {
    return head;
  }

  // getTail() --> retorna o ultimo elemento da lista
  public Node getTail() {
    if (isEmpty()) { // Lista vazia
      return null;
    }
    // Se houver apenas um elemento na lista, retorna-o
    if (head.getNext() == head)
      return head;

    // Procurar o último elemento e retornar
    Node tail = head;
    while (tail.getNext() != head) {
      tail = tail.getNext();
    }
    return tail;
  }

  // getNode(String value) --> Busca e retorna o Nó específico valor procurado
  public Node getNode(String value) {
    Node aux = head;
    boolean isRep = false;
    
    //Percorre a lista procurando um nó de mesmo valor
    while (!isRep && !aux.getData().equals(value)) {
      aux = aux.getNext();
      if (aux == head)
        isRep = true;
    }
    
    if(isRep) return null; //Caso não encontre
    return aux; // Caso encontre
  }

  // count() --> retorna a quantidade de elementos da lista
  public int count() {
    return count;
  }

  // isEmpty() --> retorna um valor booleano para a verificação do conteúdo da
  // lista
  public boolean isEmpty() {
    return count == 0;
  }

  // clear() --> Limpa a lista, removendo todas as conexões
  public void clear() {
    while (head != null)
      removeHead();
  }

 

  
  // showList() --> retorna uma representação da lista
  public String showList(int start_line, int end_line) {
    if (isEmpty())
      return null;
    
    int idx_line = 1;
    int countReadLines = 1;
    Node aux = head;
    
    // Procura o nó da primeira linha passada
    while(idx_line < start_line) {
      aux = aux.getNext();
      idx_line++;
    }
    
    StringBuilder sb = new StringBuilder();
    boolean isRep = false;
    
    // Concatena todos as linhas (nós) dentro do intervalo, junto de seu índice
    while (countReadLines <= 20 && idx_line <= end_line && !isRep) {
      sb.append(idx_line).append("             ").append(aux.getData()).append("\n");
      idx_line++;
      countReadLines++;
      aux = aux.getNext();
      if (aux == head)
        isRep = true;
    }
    return sb.toString(); // Converte para String
  }

// writeList() --> adiciona elementos em uma lista
  @Override
  public String toString() {
     //Lista vazia
    if (isEmpty())
      return null;
    Node aux = head;
    StringBuilder sb = new StringBuilder();
    boolean isRep = false;
   
    //Concatena todos as linhas (nós), separadas por '\n'
    while(!isRep){
      sb.append(aux.getData()).append("\n");
      aux = aux.getNext();
      if(aux == head) isRep = true;
    }
    
    return sb.toString(); // Converte para String
  }

}

