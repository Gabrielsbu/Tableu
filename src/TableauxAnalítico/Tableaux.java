package TableauxAnalítico;
import TableauxAnalítico.No;
import TableauxAnalítico.ArvoreBinaria;
import static TableauxAnalítico.Simbologia.simbologia;
import java.util.Scanner;

public class Tableaux {
   
    
    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        
        String expressao = new String(""); //Inicializa a expressão com vazio;
        
        simbologia(); //Chama o cabeçalho..
        
        while (true) { //Loop infinito
            System.out.println("|--------------------------------------------------------|");
            System.out.println("|Insira abaixo o seu Tableaux:                           |  ");
            System.out.println("|--------------------------------------------------------|");
            expressao = input.nextLine();
            
            if (expressao.equals("") == true){
                break;
            }// se a expressão for nada, ele fecha.
            expressao = expressao.replace(" ", ""); //retira os espaços
            
            long inicio, fim; 
            
            inicio = System.currentTimeMillis(); // Chamada de sistema que inicializa o cronômetro
            ArvoreBinaria tableaux = new ArvoreBinaria(expressao); // instancia a arvore binaria
            tableaux.expandir(); //expande a expressão passada
            tableaux.provar(); // prova a expressão passada
            fim = System.currentTimeMillis(); //depois de provar encerra o cronômetro
            System.out.println("\n|-----------------------------------|");
            System.out.println("|Time: " + (fim - inicio) + " Ms                         |");
            System.out.println("|Quantidade de nós no tableaux: " + tableaux.contarFolhas() + "  |");
            System.out.println("|Quantidade de ramos: " + tableaux.countRamos() + "             |");
            System.out.println("|-----------------------------------|\n");
            if ( tableaux.resultado() ){
                System.out.println("|---------------------------------------------|");
                System.out.println("|Algum ramo ficou aberto. <Não foi Provado>   |" );
                System.out.println("|---------------------------------------------|\n");
            }
            else{
                System.out.println("|---------------------------------------------|");
                System.out.println("|Todos os ramos fecharam. <Provado>           |" );
                System.out.println("|---------------------------------------------|\n");
            }    
            tableaux.mostrar();
        }
    }
}
