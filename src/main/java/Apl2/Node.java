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

public class Node{

    private String data;
    private Node next;

    public Node() { this("", null); }
    public Node(String data) { this(data, null);	}
    public Node(String data, Node next) {
      this.data = data;
      this.next = next;
    }

    public String getData() { return data; }
    public void setData(String data) { this.data = data;	}

    public Node getNext() { return next; }
    public void setNext(Node next) { this.next = next; }

    @Override
    public String toString() {
      return "data: " + data + ", next: " + (next != null ? next.getData() : "null");
    }

  }