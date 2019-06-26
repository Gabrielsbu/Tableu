/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TableauxAnalítico;
import TableauxAnalítico.No;
/**
 *
 * @author gabrielsbu
 */
class ArvoreBinaria {
    public No topo;
    public String expA, expB;
    public int totalFolhas;
    public int totalRamos;
    
    public ArvoreBinaria( String expressao ) {
        
        this.topo = new No(' ', expressao, null, null);
        this.expA = "";
        this.expB = "";
        this.totalFolhas = 0;
        this.totalRamos  = 0;
        
    }
    
    public void expandir() {
        expandir( topo );
    }//função auxiliar para inicializar a principal do topo.
    
    public void expandir(No no ) {
        
        String novaExpressao = "";
        char valoracao = 'T';
        
        for ( int i = 0; i < no.expressao.length(); i++ ) {
            if ( no.expressao.charAt(i) == ',' ) {
                adicionar( topo, valoracao, tratarParenteses(novaExpressao) );
                novaExpressao = ""; 
            }
            else if ( no.expressao.charAt(i) == '|' ) {
                if ( novaExpressao.length() > 0 ) {
                    adicionar( topo, valoracao, tratarParenteses(novaExpressao) );
                }
                
                valoracao = 'F';
                novaExpressao = "";
            }
            else 
                novaExpressao += no.expressao.charAt(i);
        }
        adicionar( topo, valoracao, tratarParenteses(novaExpressao) );
        
        r_exp( topo.esquerda );
        
    }
    
    public void r_exp( No no ) {
        if ( no != null ) {
            total_alfa( no );
            total_beta( no );
            
            r_exp( no.esquerda );
            r_exp( no.direita );
        }
    }
    
    public void total_alfa( No no ) {
        if ( no != null ) {
            expansao_alfa( no );
            
            total_alfa( no.esquerda );
            total_alfa( no.direita );
        }
    }
    
    public void total_beta( No no ) {
        if ( no != null ) {
            if ( !expansao_beta( no ) ) {
                total_beta( no.esquerda );
                total_beta( no.direita );
            }
        }
    }
    
    public boolean expansao_alfa( No no ) {
        if ( no != null && no.estendido == false && verificar_negacao( no, no.expressao, no.valoracao) == false && no.expressao.length() > 1 ) {
            
            char conectivo = conectivo( no.expressao );
            
            expA = tratarParenteses( expA );
            expB = tratarParenteses( expB );
            
            if ( conectivo == '^' && no.valoracao == 'T' ) {
                no.estendido = true;
                adicionar( no, 'T', expA );
                adicionar( no, 'T', expB );
                return true;
            }
            else if ( conectivo == 'v' && no.valoracao == 'F' ) {
                no.estendido = true;
                adicionar( no, 'F', expA );
                adicionar( no, 'F', expB );
                return true;
            }
            else if ( conectivo == '>' && no.valoracao == 'F' ) {
                no.estendido = true;
                adicionar( no, 'T', expA );
                adicionar( no, 'F', expB );
                return true;
            }
            
            return false;
            
        }
        
        return true;
    }
    
    public boolean expansao_beta( No no ) {
        if ( no != null && no.estendido == false && verificar_negacao( no, no.expressao, no.valoracao) == false && no.expressao.length() > 1 ) {
            
            char conectivo = conectivo( no.expressao );
            
            expA = tratarParenteses( expA );
            expB = tratarParenteses( expB ); 
            
            if ( conectivo == '^' && no.valoracao == 'F' ) {
                no.estendido = true;
                adicionar( no, 'F', expA, 'F', expB );
                return true;
            }
            else if ( conectivo == 'v' && no.valoracao == 'T' ) {
                no.estendido = true;
                adicionar( no, 'T', expA, 'T', expB );
                return true;
            }
            else if ( conectivo == '>' && no.valoracao == 'T' ) {
                no.estendido = true;
                adicionar( no, 'F', expA, 'T', expB );
                return true;
            }
            
            return false;
        }
        
        return true;
        
    }
    
    public boolean verificar_negacao( No no, String expressao, char v) {
        
        if ( expressao.length() > 1 && expressao.charAt(0) == '~' && removerNegacao( expressao ) ) {
            
            String novaExp = new String("");
            
            for ( int i = 1; i < expressao.length(); i++ )
                novaExp += expressao.charAt(i);
            
            if ( v == 'T' )
                adicionar( no, 'F', tratarParenteses(novaExp) );
            else if ( v == 'F' )
                adicionar( no, 'T', tratarParenteses(novaExp) );
            
            no.estendido = true;
            
            return true;
        }
        
        return false;
    }
    
    public boolean removerNegacao( String exp ) {
        
        int conectivos, totalParenteses;
        conectivos = totalParenteses = 0;
        boolean parenteses = false;
        
        boolean c, p;
        c = p = false;
        
        for (int i = 0; i < exp.length(); i++) {
            if ( (exp.charAt(i) == '>' || exp.charAt(i) == 'v' || exp.charAt(i) == '^') && p == false )
                c = true;
            else if ( exp.charAt(i) == '(' && c == false )
                p = true;
            
            if ( exp.charAt(i) == '(' )
                totalParenteses++;
            else if ( exp.charAt(i) == ')' )
                totalParenteses--;
                
            if ( totalParenteses == 0 && (exp.charAt(i) == '>' || exp.charAt(i) == 'v' || exp.charAt(i) == '^') )
                return false;
        }
        
        if ( c )
            return false;
        else if ( p )
            return true;
        else 
            return true;
        
    }
    
    public void adicionar(No folha, char v, String exp) {
        
        if ( folha.esquerda == null && folha.direita == null ) {
            folha.esquerda = new No(v, exp, null, null);
            
            if ( exp.length() == 1 )
                folha.esquerda.estendido = true;
            
            totalFolhas += 1; 
        }
        else {
            if ( folha.esquerda != null )
                adicionar( folha.esquerda, v, exp );
            if ( folha.direita != null )
                adicionar( folha.direita, v, exp );
        }
        
    }
    
    public void adicionar(No no, char v1, String exp1, char v2, String exp2) {
        
        if ( no != null ) {
            
            if ( no.esquerda == null && no.direita == null ) {
                no.esquerda = new No( v1, exp1, null, null );
                no.direita  = new No( v2, exp2, null, null );
                
                if ( exp1.length() == 1 )
                    no.esquerda.estendido = true;
                
                if ( exp2.length() == 1 )
                    no.direita.estendido = true;
                
                totalFolhas += 2;
            }
            
            else {
                
                if ( no.esquerda != null )
                    adicionar( no.esquerda, v1, exp1, v2, exp2 );
                if ( no.direita != null )
                    adicionar( no.direita, v1, exp1, v2, exp2 );
                
            }
            
        }
        
    }
    
    public void provar() {
        totalRamos = countRamos(); 
        provar( topo );
    }
    
    public void provar(No folha) {
     //Se um elemento estiver negado, você vai inverter chamando o buscar contradição   
        if ( folha != null ) {
            
            if ( folha.valoracao == 'T' )
                buscarContradicao( folha, folha.expressao, 'F' );
            else if ( folha.valoracao == 'F' )
                buscarContradicao( folha, folha.expressao, 'T' );
            
            if ( folha.esquerda != null )
                provar( folha.esquerda );
            if ( folha.direita != null )
                provar( folha.direita );
            
        }
        
    }
    
    public void buscarContradicao(No folha, String exp, char v) {
        
        if ( folha != null ) {
            
            if ( folha.expressao.equals( exp ) && folha.valoracao == v ) {
                totalRamos -= countRamos( folha );
                
                folha.expressao += "*";
                
                totalFolhas -= (contarFolhas( folha ) - 1) ;
                
                folha.esquerda = null;
                folha.direita  = null;
            }
            
            if ( folha.esquerda != null )
                buscarContradicao( folha.esquerda, exp, v );
            if ( folha.direita != null ) 
                buscarContradicao( folha.direita, exp, v );
            
        }
        
    }
    
    public boolean resultado() {
        if ( totalRamos >= 1 )
            return true;
        else
            return false;
    }
    
    public void mostrar() {
        mostrar(topo.esquerda);
    }
    
    public void mostrar(No no) {
        
        if ( no != null ) {
            System.out.println( no.valoracao + "[ " + no.expressao + " ]" );
            mostrar( no.esquerda );
            mostrar( no.direita );
        }
        else
            System.out.println("\n");
        
    }
    
    public int contarFolhas() {
        return contarFolhas( topo.esquerda );
    }
    
    public int contarFolhas( No no ) {
        if ( no != null )
            return 1 + contarFolhas( no.esquerda ) + contarFolhas( no.direita );
        else
            return 0;
    }
    
    public char conectivo(String exp) { //vai procurr um conectivo.
        
        int  tamanho = exp.length();
        char c = ' ';
        int  parenteses = 0;
        boolean vez = true; 
        expA = expB = "";
        
        for (int i = 0; i < tamanho; i++) {
            
            if ( exp.charAt(i) == '(' )
                parenteses += 1;
            else if ( exp.charAt(i) == ')' )
                parenteses -= 1;
            
            if ( (exp.charAt(i) == '>' || exp.charAt(i) == '^' || exp.charAt(i) == 'v') && parenteses == 0 && vez ) {
                c = exp.charAt(i);
                vez = false;
            }
            else if ( vez )
                expA += exp.charAt(i);
            else
                expB += exp.charAt(i);
            
        }
        
        return c;
    }
    
    public int countRamos() {
        return countRamos(topo);
    } // auxilia minha função principal de contagem, inicializando do topo.
    
    public int countRamos(No folha) {
        
        if ( folha != null ) {
            
            if ( folha.esquerda == null && folha.direita == null )
                return 1;
            
            else 
                return 0 + countRamos( folha.esquerda ) + countRamos( folha.direita );
            
        }
        else
            return 0;
        
    }
    
    public String tratarParenteses(String exp) {
        
        String novaExpressao = "";
        int tamanho, parenteses;
        
        tamanho = exp.length();
        parenteses = 0;
        
        if ( exp.length() > 1 && exp.charAt(0) == '(' && exp.charAt(tamanho - 1) == ')' ) {
            parenteses++;
            
            for ( int i = 1; i < tamanho; i++ ) {
                if ( exp.charAt(i) == '(' )
                    parenteses++;
                if ( exp.charAt(i) == ')' )
                    parenteses--;
                    
                if ( parenteses == 0 && i != (tamanho - 1) )
                    return exp;
                
                if ( i < (tamanho - 1) )
                    novaExpressao += exp.charAt(i);
            }
            
            if ( parenteses == 0 )
                return novaExpressao;
                
        }
        
        return exp;
        
    }
}
